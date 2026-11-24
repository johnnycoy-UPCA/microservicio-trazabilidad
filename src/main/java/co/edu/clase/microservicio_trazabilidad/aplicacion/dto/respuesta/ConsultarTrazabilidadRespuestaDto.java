package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

import java.util.List;

public class ConsultarTrazabilidadRespuestaDto {

    private Long pedidoId;
    private List<EventoTrazabilidadDetalleDto> eventos;

    public ConsultarTrazabilidadRespuestaDto() {
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public List<EventoTrazabilidadDetalleDto> getEventos() {
        return eventos;
    }

    public void setEventos(
            List<EventoTrazabilidadDetalleDto> eventos) {
        this.eventos = eventos;
    }
}