package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class PuntoEntradaNoAutorizado
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public PuntoEntradaNoAutorizado(
            ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            AuthenticationException excepcion)
            throws IOException {

        String codigo = obtenerAtributo(
                solicitud,
                ConstantesSeguridad.ATRIBUTO_CODIGO_ERROR,
                CodigosError.AUTENTICACION_REQUERIDA
        );

        String mensaje = obtenerAtributo(
                solicitud,
                ConstantesSeguridad.ATRIBUTO_MENSAJE_ERROR,
                MensajesError.AUTENTICACION_REQUERIDA
        );

        respuesta.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );
        respuesta.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );
        respuesta.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                respuesta.getOutputStream(),
                Map.of(
                        "fechaHora", Instant.now().toString(),
                        "estado",
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "codigo", codigo,
                        "mensaje", mensaje,
                        "ruta", solicitud.getRequestURI(),
                        "detalles", Map.of()
                )
        );
    }

    private String obtenerAtributo(
            HttpServletRequest solicitud,
            String nombre,
            String valorPredeterminado) {

        Object valor = solicitud.getAttribute(nombre);

        if (valor instanceof String texto
                && !texto.isBlank()) {

            return texto;
        }

        return valorPredeterminado;
    }
}