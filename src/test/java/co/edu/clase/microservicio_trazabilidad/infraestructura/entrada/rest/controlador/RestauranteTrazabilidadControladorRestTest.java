package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EficienciaEmpleadoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.ReporteTiemposRestauranteRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TiempoPedidoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarEficienciaEmpleadosHandler;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarTiemposRestauranteHandler;
import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestauranteTrazabilidadControladorRestTest {

	private static final Long RESTAURANTE_ID = 1L;
	private static final String TOKEN = "token-propietario";

	private static final String RUTA_TIEMPOS =
		ConstantesApi.API_RESTAURANTES_TRAZABILIDAD
			+ "/1/pedidos/tiempos";

	private static final String RUTA_EFICIENCIA =
		ConstantesApi.API_RESTAURANTES_TRAZABILIDAD
			+ "/1/empleados/eficiencia";

	@Mock
	private ConsultarTiemposRestauranteHandler tiemposHandler;

	@Mock
	private ConsultarEficienciaEmpleadosHandler eficienciaHandler;

	private MockMvc mockMvc;

	@BeforeEach
	void configurar() {
		RestauranteTrazabilidadControladorRest controlador =
			new RestauranteTrazabilidadControladorRest(
				tiemposHandler,
				eficienciaHandler
			);

		mockMvc = MockMvcBuilders
			.standaloneSetup(controlador)
			.setControllerAdvice(
				new ManejadorGlobalExcepciones()
			)
			.setCustomArgumentResolvers(
				new AuthenticationPrincipalArgumentResolver()
			)
			.build();
	}

	@AfterEach
	void limpiarContexto() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void debeConsultarTiemposDelRestaurante() throws Exception {
		when(tiemposHandler.ejecutar(
			eq(RESTAURANTE_ID),
			eq(TOKEN)
		)).thenReturn(crearReporte());

		mockMvc.perform(
				get(RUTA_TIEMPOS)
					.with(autenticacionPropietario())
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.restauranteId").value(1))
			.andExpect(jsonPath("$.promedioTotalSegundos")
				.value(1200))
			.andExpect(jsonPath("$.pedidosEntregados").value(3))
			.andExpect(jsonPath("$.pedidos.length()").value(2))
			.andExpect(jsonPath("$.pedidos[0].pedidoId")
				.value(3))
			.andExpect(jsonPath("$.pedidos[0].superaPromedio")
				.value(true))
			.andExpect(jsonPath("$.pedidos[1].superaPromedio")
				.value(false))
			.andExpect(jsonPath("$.pedidos[0].clienteId")
				.doesNotExist());

		verify(tiemposHandler).ejecutar(
			eq(RESTAURANTE_ID),
			eq(TOKEN)
		);
	}

	@Test
	void debeConsultarEficienciaDeEmpleados() throws Exception {
		when(eficienciaHandler.ejecutar(
			eq(RESTAURANTE_ID),
			eq(TOKEN)
		)).thenReturn(List.of(
			new EficienciaEmpleadoRespuestaDto(1, 32L, 1, 100L),
			new EficienciaEmpleadoRespuestaDto(2, 30L, 2, 600L)
		));

		mockMvc.perform(
				get(RUTA_EFICIENCIA)
					.with(autenticacionPropietario())
			)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(2))
			.andExpect(jsonPath("$[0].posicion").value(1))
			.andExpect(jsonPath("$[0].empleadoId").value(32))
			.andExpect(jsonPath("$[0].promedioPreparacionSegundos")
				.value(100))
			.andExpect(jsonPath("$[1].pedidosGestionados")
				.value(2));

		verify(eficienciaHandler).ejecutar(
			eq(RESTAURANTE_ID),
			eq(TOKEN)
		);
	}

	@Test
	void debeResponderProhibidoSiRestauranteEsAjeno()
			throws Exception {

		when(tiemposHandler.ejecutar(
			eq(RESTAURANTE_ID),
			eq(TOKEN)
		)).thenThrow(new ExcepcionTrazabilidad(
			CodigosError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
			MensajesError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
			CategoriaError.ACCESO_DENEGADO
		));

		mockMvc.perform(
				get(RUTA_TIEMPOS)
					.with(autenticacionPropietario())
			)
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.codigo").value("TRZ-008"));
	}

	@Test
	void debeResponderBadGatewaySiPlazoletaNoEstaDisponible()
			throws Exception {

		when(eficienciaHandler.ejecutar(
			eq(RESTAURANTE_ID),
			eq(TOKEN)
		)).thenThrow(new ExcepcionTrazabilidad(
			CodigosError.PLAZOLETA_NO_DISPONIBLE,
			MensajesError.PLAZOLETA_NO_DISPONIBLE,
			CategoriaError.DEPENDENCIA_EXTERNA
		));

		mockMvc.perform(
				get(RUTA_EFICIENCIA)
					.with(autenticacionPropietario())
			)
			.andExpect(status().isBadGateway())
			.andExpect(jsonPath("$.codigo").value("TRZ-009"));
	}

	private RequestPostProcessor autenticacionPropietario() {
		return solicitud -> {
			UsuarioAutenticado principal =
				new UsuarioAutenticado(
					10L,
					"propietario@correo.com",
					PerfilUsuario.PROPIETARIO,
					TOKEN
				);

			UsernamePasswordAuthenticationToken autenticacion =
				new UsernamePasswordAuthenticationToken(
					principal,
					null,
					List.of(
						new SimpleGrantedAuthority(
							"ROLE_PROPIETARIO"
						)
					)
				);

			SecurityContextHolder
				.getContext()
				.setAuthentication(autenticacion);

			return solicitud;
		};
	}

	private ReporteTiemposRestauranteRespuestaDto crearReporte() {
		return new ReporteTiemposRestauranteRespuestaDto(
			RESTAURANTE_ID,
			1200L,
			3,
			List.of(
				new TiempoPedidoRespuestaDto(
					3L, 30L, EstadoPedido.ENTREGADO,
					180L, 900L, 720L, 1800L, true
				),
				new TiempoPedidoRespuestaDto(
					1L, 30L, EstadoPedido.ENTREGADO,
					60L, 300L, 240L, 600L, false
				)
			)
		);
	}
}