package co.edu.clase.microservicio_trazabilidad.dominio.modelo;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.util.List;

public class TrazabilidadPedido {

    private final Long pedidoId;
    private final EstadoPedido estadoActual;
    private final Long tiempoTotalSegundos;
    private final List<TramoEstado> tramos;

    public TrazabilidadPedido(
            Long pedidoId,
            EstadoPedido estadoActual,
            Long tiempoTotalSegundos,
            List<TramoEstado> tramos) {

        this.pedidoId = pedidoId;
        this.estadoActual = estadoActual;
        this.tiempoTotalSegundos = tiempoTotalSegundos;
        this.tramos = List.copyOf(tramos);
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public EstadoPedido getEstadoActual() {
        return estadoActual;
    }

    public Long getTiempoTotalSegundos() {
        return tiempoTotalSegundos;
    }

    public List<TramoEstado> getTramos() {
        return tramos;
    }
}