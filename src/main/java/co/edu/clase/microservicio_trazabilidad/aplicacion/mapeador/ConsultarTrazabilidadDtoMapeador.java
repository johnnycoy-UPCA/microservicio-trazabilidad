package co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.ConsultarTrazabilidadRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EventoTrazabilidadDetalleDto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;

import java.util.List;

public class ConsultarTrazabilidadDtoMapeador {

    public ConsultarTrazabilidadRespuestaDto aRespuestaDto(
				Long pedidoId,
				List<EventoTrazabilidad> eventos) {

        ConsultarTrazabilidadRespuestaDto respuesta =
                new ConsultarTrazabilidadRespuestaDto();

        respuesta.setPedidoId(pedidoId);

        respuesta.setEventos(
                eventos.stream()
                        .map(this::aDetalleDto)
                        .toList()
        );

        return respuesta;
    }

    private EventoTrazabilidadDetalleDto
    aDetalleDto(EventoTrazabilidad evento) {

        EventoTrazabilidadDetalleDto dto =
                new EventoTrazabilidadDetalleDto();

        dto.setEstadoAnterior(
                evento.getEstadoAnterior() == null
                        ? null
                        : evento.getEstadoAnterior().name()
        );

        dto.setEstadoNuevo(
                evento.getEstadoNuevo() == null
                        ? null
                        : evento.getEstadoNuevo().name()
        );

        dto.setFechaEvento(
                evento.getFechaEvento()
        );

        dto.setEmpleadoId(
                evento.getEmpleadoId()
        );

        return dto;
    }
}