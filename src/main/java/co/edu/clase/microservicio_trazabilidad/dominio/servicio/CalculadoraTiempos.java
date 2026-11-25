package co.edu.clase.microservicio_trazabilidad.dominio.servicio;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TiempoPedido;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CalculadoraTiempos {

    public List<TiempoPedido> calcularPorPedido(
            List<EventoTrazabilidad> eventos) {

        Map<Long, List<EventoTrazabilidad>> porPedido =
                eventos.stream()
                        .collect(Collectors.groupingBy(
                                // EventoTrazabilidad::getPedidoId,
                                evento -> evento.getPedidoId(),
                                LinkedHashMap::new,
                                Collectors.toList()
                        ));

        List<TiempoPedido> resultado = new ArrayList<>();

        for (List<EventoTrazabilidad> eventosPedido
                : porPedido.values()) {

            construir(eventosPedido)
                    .ifPresent(resultado::add);
        }

        return resultado;
    }

    private Optional<TiempoPedido> construir(
            List<EventoTrazabilidad> eventos) {

        Map<EstadoPedido, EventoTrazabilidad> porEstado =
                new EnumMap<>(EstadoPedido.class);

        for (EventoTrazabilidad evento : eventos) {
            porEstado.put(
                    evento.getEstadoNuevo(),
                    evento
            );
        }

        EventoTrazabilidad pendiente =
                porEstado.get(EstadoPedido.PENDIENTE);

        boolean cancelado =
                porEstado.containsKey(
                        EstadoPedido.CANCELADO
                );

        if (pendiente == null || cancelado) {
            return Optional.empty();
        }

        EventoTrazabilidad preparacion =
                porEstado.get(EstadoPedido.EN_PREPARACION);
        EventoTrazabilidad listo =
                porEstado.get(EstadoPedido.LISTO);
        EventoTrazabilidad entregado =
                porEstado.get(EstadoPedido.ENTREGADO);

        Instant fechaPendiente =
                pendiente.getFechaEvento();
        Instant fechaPreparacion =
                fecha(preparacion);
        Instant fechaListo = fecha(listo);
        Instant fechaEntregado = fecha(entregado);

        return Optional.of(new TiempoPedido(
                pendiente.getPedidoId(),
                pendiente.getClienteId(),
                preparacion == null
                        ? null
                        : preparacion.getEmpleadoId(),
                determinarEstado(
                        preparacion,
                        listo,
                        entregado
                ),
                segundos(
                        fechaPendiente,
                        fechaPreparacion
                ),
                segundos(
                        fechaPreparacion,
                        fechaListo
                ),
                segundos(
                        fechaListo,
                        fechaEntregado
                ),
                segundos(
                        fechaPendiente,
                        fechaEntregado
                ),
                false
        ));
    }

    private EstadoPedido determinarEstado(
            EventoTrazabilidad preparacion,
            EventoTrazabilidad listo,
            EventoTrazabilidad entregado) {

        if (entregado != null) {
            return EstadoPedido.ENTREGADO;
        }

        if (listo != null) {
            return EstadoPedido.LISTO;
        }

        if (preparacion != null) {
            return EstadoPedido.EN_PREPARACION;
        }

        return EstadoPedido.PENDIENTE;
    }

    private Instant fecha(EventoTrazabilidad evento) {
        return evento == null
                ? null
                : evento.getFechaEvento();
    }

    private Long segundos(Instant inicio, Instant fin) {
        if (inicio == null || fin == null) {
            return null;
        }

        return Duration.between(inicio, fin)
                .getSeconds();
    }
}