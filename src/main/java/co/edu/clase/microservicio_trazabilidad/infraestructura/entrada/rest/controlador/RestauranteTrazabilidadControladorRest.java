package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EficienciaEmpleadoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.ReporteTiemposRestauranteRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarEficienciaEmpleadosHandler;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarTiemposRestauranteHandler;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Tiempos y eficiencia",
        description =
                "Consulta de tiempos de pedidos y eficiencia "
                + "de empleados de un restaurante"
)
@RestController
@RequestMapping(
        ConstantesApi.API_RESTAURANTES_TRAZABILIDAD
                    + "/{restauranteId}"
)
public class RestauranteTrazabilidadControladorRest {

    private final ConsultarTiemposRestauranteHandler tiemposHandler;
    private final ConsultarEficienciaEmpleadosHandler eficienciaHandler;

    public RestauranteTrazabilidadControladorRest(
            ConsultarTiemposRestauranteHandler
                    tiemposHandler,
            ConsultarEficienciaEmpleadosHandler
                    eficienciaHandler) {

        this.tiemposHandler = tiemposHandler;
        this.eficienciaHandler = eficienciaHandler;
    }

    @Operation(
            summary = "Consultar tiempos de pedidos",
            description =
                    "Devuelve la duración por etapa y total de "
                    + "los pedidos del restaurante, el promedio "
                    + "de pedidos entregados y marca los que "
                    + "superan ese promedio"
    )
    @SecurityRequirement(name = "autenticacionBearer")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Tiempos consultados correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token ausente, inválido o expirado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Perfil sin autorización o restaurante ajeno"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante no encontrado"
        ),
        @ApiResponse(
            responseCode = "502",
            description = "Microservicio Plazoleta no disponible"
        )
    })
    @GetMapping("/pedidos/tiempos")
    public ResponseEntity<ReporteTiemposRestauranteRespuestaDto>
            consultarTiempos(
                    @PathVariable Long restauranteId,
                    @AuthenticationPrincipal
                    UsuarioAutenticado usuarioAutenticado) {

        return ResponseEntity.ok(
                tiemposHandler.ejecutar(
                        restauranteId,
                        usuarioAutenticado.getToken()
                )
        );
    }

    @Operation(
            summary = "Consultar eficiencia de empleados",
            description =
                    "Ranking ascendente de empleados según el "
                    + "promedio de preparación "
                    + "(EN_PREPARACION a LISTO)"
    )
    @SecurityRequirement(name = "autenticacionBearer")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Ranking consultado correctamente"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Token ausente, inválido o expirado"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Perfil sin autorización o restaurante ajeno"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante no encontrado"
        ),
        @ApiResponse(
            responseCode = "502",
            description = "Microservicio Plazoleta no disponible"
        )
    })
    @GetMapping("/empleados/eficiencia")
    public ResponseEntity<List<EficienciaEmpleadoRespuestaDto>>
            consultarEficiencia(
                    @PathVariable Long restauranteId,
                    @AuthenticationPrincipal
                    UsuarioAutenticado usuarioAutenticado) {

        return ResponseEntity.ok(
                eficienciaHandler.ejecutar(
                        restauranteId,
                        usuarioAutenticado.getToken()
                )
        );
    }
}