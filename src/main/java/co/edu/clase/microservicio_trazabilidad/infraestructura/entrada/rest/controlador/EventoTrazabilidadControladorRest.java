package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EventoTrazabilidadRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.solicitud.RegistrarEventoSolicitudDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.RegistrarEventoHandler;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Trazabilidad",
        description =
                "Operaciones internas de trazabilidad de pedidos"
)
@RestController
@RequestMapping(ConstantesApi.API_EVENTOS)
public class EventoTrazabilidadControladorRest {

    private final RegistrarEventoHandler
            registrarEventoHandler;

    public EventoTrazabilidadControladorRest(
            RegistrarEventoHandler
                    registrarEventoHandler) {

        this.registrarEventoHandler =
                registrarEventoHandler;
    }

    @Operation(
            summary = "Registrar transición",
            description =
                    "Registra una transición válida "
                            + "del estado de un pedido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description =
                            "Transición registrada correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description =
                            "Datos o transición inválida"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description =
                            "La transición ya fue registrada"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description =
                            "Error interno del microservicio"
            )
    })
    @PostMapping
    public ResponseEntity<
            EventoTrazabilidadRespuestaDto>
            registrarEvento(
                    @Valid @RequestBody
                    RegistrarEventoSolicitudDto solicitud) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        registrarEventoHandler
                                .ejecutar(solicitud)
                );
    }
}