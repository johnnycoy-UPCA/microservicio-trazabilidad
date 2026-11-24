package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.controlador;

// import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.ConsultarTrazabilidadRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TrazabilidadPedidoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarTrazabilidadPedidoHandler;
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
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Consulta Trazabilidad",
        description =
                "Consulta la trazabilidad de los pedidos "
				+ "del cliente autenticado"
)
@RestController
@RequestMapping(
        ConstantesApi.API_CONSULTA_TRAZABILIDAD
)
public class ConsultarTrazabilidadControladorRest {

    private final ConsultarTrazabilidadPedidoHandler consultarTrazabilidadPedidoHandler;

    public ConsultarTrazabilidadControladorRest(
            ConsultarTrazabilidadPedidoHandler consultarTrazabilidadPedidoHandler) {

        this.consultarTrazabilidadPedidoHandler = consultarTrazabilidadPedidoHandler;
    }

	@Operation(
			summary = "Consultar trazabilidad de pedido",
			description =
					"Devuelve los estados del pedido en orden "
					+ "cronológico y el tiempo (en segundos) "
					+ "que permaneció en cada uno"
	)
	@SecurityRequirement(name = "autenticacionBearer")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "Trazabilidad consultada correctamente"
		),
		@ApiResponse(
			responseCode = "401",
			description = "Token ausente, inválido o expirado"
		),
		@ApiResponse(
			responseCode = "403",
			description = "El usuario no tiene perfil CLIENTE"
		),
		@ApiResponse(
			responseCode = "404",
			description = "No existe trazabilidad del pedido "
							+ "para el cliente autenticado"
		),
		@ApiResponse(
			responseCode = "500",
			description = "Error interno del microservicio"
		)
	})
	@GetMapping("/{pedidoId}")
	public ResponseEntity<TrazabilidadPedidoRespuestaDto>
		consultarTrazabilidad(
			@PathVariable Long pedidoId,
			@AuthenticationPrincipal UsuarioAutenticado usuarioAutenticado) {

		return ResponseEntity.ok(
			consultarTrazabilidadPedidoHandler.ejecutar(
				pedidoId,
				usuarioAutenticado.getUsuarioId()
			)
		);
    }
}