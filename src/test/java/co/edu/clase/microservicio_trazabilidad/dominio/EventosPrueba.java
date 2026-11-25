package co.edu.clase.microservicio_trazabilidad.dominio;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class EventosPrueba {

	private static final Instant T0 =
		Instant.parse("2026-11-24T10:00:00Z");

	private EventosPrueba() {
		throw new IllegalStateException(
			"No se permite crear instancias de una clase de utilidad"
		);
	}

	public static List<EventoTrazabilidad> pedidoEnPreparacion(
			Long pedidoId,
			Long empleadoId,
			long esperaSegundos) {

		List<EventoTrazabilidad> eventos = new ArrayList<>();

		eventos.add(evento(
			pedidoId, null,
			null, EstadoPedido.PENDIENTE,
			0
		));
		eventos.add(evento(
			pedidoId, empleadoId,
			EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION,
			esperaSegundos
		));

		return eventos;
	}

	public static List<EventoTrazabilidad> pedidoHastaListo(
			Long pedidoId,
			Long empleadoId,
			long esperaSegundos,
			long preparacionSegundos) {

		List<EventoTrazabilidad> eventos =
			pedidoEnPreparacion(
				pedidoId,
				empleadoId,
				esperaSegundos
			);

		eventos.add(evento(
			pedidoId, empleadoId,
			EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO,
			esperaSegundos + preparacionSegundos
		));

		return eventos;
	}

	public static List<EventoTrazabilidad> pedidoEntregado(
			Long pedidoId,
			Long empleadoId,
			long esperaSegundos,
			long preparacionSegundos,
			long entregaSegundos) {

		List<EventoTrazabilidad> eventos =
			pedidoHastaListo(
				pedidoId,
				empleadoId,
				esperaSegundos,
				preparacionSegundos
			);

		eventos.add(evento(
			pedidoId, empleadoId,
			EstadoPedido.LISTO, EstadoPedido.ENTREGADO,
			esperaSegundos
				+ preparacionSegundos
				+ entregaSegundos
		));

		return eventos;
	}

	public static List<EventoTrazabilidad> pedidoCancelado(
			Long pedidoId,
			long segundos) {

		List<EventoTrazabilidad> eventos = new ArrayList<>();

		eventos.add(evento(
			pedidoId, null,
			null, EstadoPedido.PENDIENTE,
			0
		));
		eventos.add(evento(
			pedidoId, null,
			EstadoPedido.PENDIENTE, EstadoPedido.CANCELADO,
			segundos
		));

		return eventos;
	}

	@SafeVarargs
	public static List<EventoTrazabilidad> unir(
			List<EventoTrazabilidad>... grupos) {

		List<EventoTrazabilidad> resultado = new ArrayList<>();

		for (List<EventoTrazabilidad> grupo : grupos) {
			resultado.addAll(grupo);
		}

		return resultado;
	}

	private static EventoTrazabilidad evento(
			Long pedidoId,
			Long empleadoId,
			EstadoPedido anterior,
			EstadoPedido nuevo,
			long segundosDesdeInicio) {

		return new EventoTrazabilidad(
			null,
			pedidoId,
			20L,
			1L,
			empleadoId,
			anterior,
			nuevo,
			T0.plusSeconds(segundosDesdeInicio)
		);
	}
}