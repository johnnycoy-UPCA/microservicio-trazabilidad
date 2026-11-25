package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.ReporteTiemposRestaurante;
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
import static co.edu.clase.microservicio_trazabilidad.dominio.EventosPrueba.unir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarTiemposRestauranteCasoUsoTest {

	private static final Long RESTAURANTE_ID = 1L;
	private static final String TOKEN = "token-propietario";

	@Mock
	private RestauranteAccesoPuerto restauranteAccesoPuerto;

	@Mock
	private EventosRestaurantePersistenciaPuerto eventosPuerto;

	private ConsultarTiemposRestauranteCasoUso casoUso;

	@BeforeEach
	void configurar() {
		casoUso = new ConsultarTiemposRestauranteCasoUso(
			restauranteAccesoPuerto,
			eventosPuerto,
			new CalculadoraTiempos()
		);
	}

	@Test
	void debeCalcularPromedioYMarcarPedidosQueLoSuperan() {
		when(eventosPuerto.consultarPorRestaurante(
			RESTAURANTE_ID
		)).thenReturn(unir(
			pedidoEntregado(1L, 30L, 60, 300, 240),
			pedidoEntregado(2L, 31L, 120, 600, 480),
			pedidoEntregado(3L, 30L, 180, 900, 720),
			pedidoCancelado(4L, 45),
			pedidoEnPreparacion(5L, 31L, 60)
		));

		ReporteTiemposRestaurante reporte =
			casoUso.consultar(RESTAURANTE_ID, TOKEN);

		assertEquals(RESTAURANTE_ID, reporte.restauranteId());
		assertEquals(1200L, reporte.promedioTotalSegundos());
		assertEquals(3, reporte.pedidosEntregados());
		assertEquals(4, reporte.pedidos().size());

		assertEquals(3L, reporte.pedidos().get(0).pedidoId());
		assertEquals(2L, reporte.pedidos().get(1).pedidoId());
		assertEquals(1L, reporte.pedidos().get(2).pedidoId());
		assertEquals(5L, reporte.pedidos().get(3).pedidoId());

		assertTrue(reporte.pedidos().get(0).superaPromedio());
		assertFalse(reporte.pedidos().get(1).superaPromedio());
		assertFalse(reporte.pedidos().get(2).superaPromedio());
		assertFalse(reporte.pedidos().get(3).superaPromedio());

		assertNull(reporte.pedidos().get(3).totalSegundos());

		verify(restauranteAccesoPuerto)
			.validarPropiedad(RESTAURANTE_ID, TOKEN);
	}

	@Test
	void debeResponderVacioSiNoHayPedidosEntregados() {
		when(eventosPuerto.consultarPorRestaurante(
			RESTAURANTE_ID
		)).thenReturn(unir(
			pedidoCancelado(4L, 45)
		));

		ReporteTiemposRestaurante reporte =
			casoUso.consultar(RESTAURANTE_ID, TOKEN);

		assertNull(reporte.promedioTotalSegundos());
		assertEquals(0, reporte.pedidosEntregados());
		assertEquals(List.of(), reporte.pedidos());
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