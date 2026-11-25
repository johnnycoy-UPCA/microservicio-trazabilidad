package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EficienciaEmpleado;
import co.edu.clase.microservicio_trazabilidad.dominio.servicio.CalculadoraTiempos;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventosRestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.RestauranteAccesoPuerto;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static co.edu.clase.microservicio_trazabilidad.dominio.EventosPrueba.pedidoCancelado;
import static co.edu.clase.microservicio_trazabilidad.dominio.EventosPrueba.pedidoEnPreparacion;
import static co.edu.clase.microservicio_trazabilidad.dominio.EventosPrueba.pedidoEntregado;
import static co.edu.clase.microservicio_trazabilidad.dominio.EventosPrueba.pedidoHastaListo;
import static co.edu.clase.microservicio_trazabilidad.dominio.EventosPrueba.unir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarEficienciaEmpleadosCasoUsoTest {

	private static final Long RESTAURANTE_ID = 1L;
	private static final String TOKEN = "token-propietario";

	@Mock
	private RestauranteAccesoPuerto restauranteAccesoPuerto;

	@Mock
	private EventosRestaurantePersistenciaPuerto eventosPuerto;

	private ConsultarEficienciaEmpleadosCasoUso casoUso;

	@BeforeEach
	void configurar() {
		casoUso = new ConsultarEficienciaEmpleadosCasoUso(
			restauranteAccesoPuerto,
			eventosPuerto,
			new CalculadoraTiempos()
		);
	}

	@Test
	void debeOrdenarAscendenteYDesempatarPorPedidosGestionados() {
		when(eventosPuerto.consultarPorRestaurante(
			RESTAURANTE_ID
		)).thenReturn(unir(
			pedidoEntregado(1L, 30L, 60, 300, 240),
			pedidoHastaListo(2L, 31L, 120, 600),
			pedidoEntregado(3L, 30L, 180, 900, 720),
			pedidoCancelado(4L, 45),
			pedidoEntregado(6L, 32L, 30, 100, 50),
			pedidoEnPreparacion(7L, 33L, 60)
		));

		List<EficienciaEmpleado> ranking =
			casoUso.consultar(RESTAURANTE_ID, TOKEN);

		assertEquals(3, ranking.size());

		assertEquals(1, ranking.get(0).posicion());
		assertEquals(32L, ranking.get(0).empleadoId());
		assertEquals(1, ranking.get(0).pedidosGestionados());
		assertEquals(
			100L,
			ranking.get(0).promedioPreparacionSegundos()
		);

		assertEquals(2, ranking.get(1).posicion());
		assertEquals(30L, ranking.get(1).empleadoId());
		assertEquals(2, ranking.get(1).pedidosGestionados());
		assertEquals(
			600L,
			ranking.get(1).promedioPreparacionSegundos()
		);

		assertEquals(3, ranking.get(2).posicion());
		assertEquals(31L, ranking.get(2).empleadoId());
		assertEquals(1, ranking.get(2).pedidosGestionados());
		assertEquals(
			600L,
			ranking.get(2).promedioPreparacionSegundos()
		);

		verify(restauranteAccesoPuerto)
			.validarPropiedad(RESTAURANTE_ID, TOKEN);
	}

	@Test
	void debeResponderListaVaciaSinPedidosPreparados() {
		when(eventosPuerto.consultarPorRestaurante(
			RESTAURANTE_ID
		)).thenReturn(unir(
			pedidoCancelado(4L, 45),
			pedidoEnPreparacion(7L, 33L, 60)
		));

		assertTrue(
			casoUso.consultar(RESTAURANTE_ID, TOKEN).isEmpty()
		);
	}

	@Test
	void debeRechazarRestauranteAjenoSinConsultarEventos() {
		doThrow(new ExcepcionTrazabilidad(
			CodigosError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
			MensajesError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
			CategoriaError.ACCESO_DENEGADO
		)).when(restauranteAccesoPuerto)
			.validarPropiedad(RESTAURANTE_ID, TOKEN);

		ExcepcionTrazabilidad excepcion = assertThrows(
			ExcepcionTrazabilidad.class,
			() -> casoUso.consultar(RESTAURANTE_ID, TOKEN)
		);

		assertEquals(
			CodigosError.RESTAURANTE_NO_PERTENECE_PROPIETARIO,
			excepcion.getCodigo()
		);

		verifyNoInteractions(eventosPuerto);
	}
}