package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.util.List;

public class TrazabilidadPedidoRespuestaDto {

    private Long pedidoId;
    private EstadoPedido estadoActual;
    private Long tiempoTotalSegundos;
    private List<TramoEstadoRespuestaDto> tramos;

    public TrazabilidadPedidoRespuestaDto() {
    }

    public TrazabilidadPedidoRespuestaDto(
            Long pedidoId,
            EstadoPedido estadoActual,
            Long tiempoTotalSegundos,
            List<TramoEstadoRespuestaDto> tramos) {

        this.pedidoId = pedidoId;
        this.estadoActual = estadoActual;
        this.tiempoTotalSegundos = tiempoTotalSegundos;
        this.tramos = tramos;
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

    public List<TramoEstadoRespuestaDto> getTramos() {
        return tramos;
    }
}