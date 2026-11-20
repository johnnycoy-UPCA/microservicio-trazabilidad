package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.manejador;

import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    @ExceptionHandler(ExcepcionTrazabilidad.class)
    public ResponseEntity<ErrorRespuesta>
            manejarExcepcionTrazabilidad(
                    ExcepcionTrazabilidad excepcion,
                    HttpServletRequest solicitud) {

        HttpStatus estado =
                obtenerEstado(excepcion.getCategoria());

        return ResponseEntity.status(estado).body(
                construirRespuesta(
                        estado,
                        excepcion.getCodigo(),
                        excepcion.getMessage(),
                        solicitud.getRequestURI(),
                        Map.of()
                )
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<ErrorRespuesta>
            manejarValidacion(
                    MethodArgumentNotValidException
                            excepcion,
                    HttpServletRequest solicitud) {

        Map<String, Object> detalles =
                new LinkedHashMap<>();

        excepcion.getBindingResult()
                .getFieldErrors()
                .forEach(error -> detalles.putIfAbsent(
                        error.getField(),
                        error.getDefaultMessage()
                ));

        return ResponseEntity.badRequest().body(
                construirRespuesta(
                        HttpStatus.BAD_REQUEST,
                        CodigosError.SOLICITUD_INVALIDA,
                        MensajesError.SOLICITUD_INVALIDA,
                        solicitud.getRequestURI(),
                        detalles
                )
        );
    }

    @ExceptionHandler(
            HttpMessageNotReadableException.class
    )
    public ResponseEntity<ErrorRespuesta>
            manejarCuerpoInvalido(
                    HttpMessageNotReadableException
                            excepcion,
                    HttpServletRequest solicitud) {

        return ResponseEntity.badRequest().body(
                construirRespuesta(
                        HttpStatus.BAD_REQUEST,
                        CodigosError
                                .CUERPO_SOLICITUD_INVALIDO,
                        MensajesError
                                .CUERPO_SOLICITUD_INVALIDO,
                        solicitud.getRequestURI(),
                        Map.of()
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta>
            manejarErrorInesperado(
                    Exception excepcion,
                    HttpServletRequest solicitud) {

        return ResponseEntity
                .status(
                        HttpStatus
                                .INTERNAL_SERVER_ERROR
                )
                .body(construirRespuesta(
                        HttpStatus
                                .INTERNAL_SERVER_ERROR,
                        CodigosError.ERROR_INTERNO,
                        MensajesError.ERROR_INTERNO,
                        solicitud.getRequestURI(),
                        Map.of()
                ));
    }

    private ErrorRespuesta construirRespuesta(
            HttpStatus estado,
            String codigo,
            String mensaje,
            String ruta,
            Map<String, Object> detalles) {

        return new ErrorRespuesta(
                Instant.now(),
                estado.value(),
                codigo,
                mensaje,
                ruta,
                detalles
        );
    }

    private HttpStatus obtenerEstado(
            CategoriaError categoria) {

        return switch (categoria) {
            case VALIDACION -> HttpStatus.BAD_REQUEST;
            case CONFLICTO -> HttpStatus.CONFLICT;
            case NO_ENCONTRADO -> HttpStatus.NOT_FOUND;
            case NO_AUTORIZADO ->
                    HttpStatus.UNAUTHORIZED;
            case ACCESO_DENEGADO ->
                    HttpStatus.FORBIDDEN;
            case ERROR_INTERNO ->
                    HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}