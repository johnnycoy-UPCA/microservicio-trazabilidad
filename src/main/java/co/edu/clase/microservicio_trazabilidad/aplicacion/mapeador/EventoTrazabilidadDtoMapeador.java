package co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EventoTrazabilidadRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.solicitud.RegistrarEventoSolicitudDto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import org.springframework.stereotype.Component;

@Component
public class EventoTrazabilidadDtoMapeador {

    public EventoTrazabilidad aModelo(
            RegistrarEventoSolicitudDto solicitud) {

        EventoTrazabilidad evento =
                new EventoTrazabilidad();

        evento.setPedidoId(solicitud.getPedidoId());
        evento.setClienteId(solicitud.getClienteId());
        evento.setRestauranteId(
                solicitud.getRestauranteId()
        );
        evento.setEmpleadoId(
                solicitud.getEmpleadoId()
        );
        evento.setEstadoAnterior(
                solicitud.getEstadoAnterior()
        );
        evento.setEstadoNuevo(
                solicitud.getEstadoNuevo()
        );

        return evento;
    }

    public EventoTrazabilidadRespuestaDto aRespuesta(
            EventoTrazabilidad evento) {

        EventoTrazabilidadRespuestaDto respuesta =
                new EventoTrazabilidadRespuestaDto();

        respuesta.setId(evento.getId());
        respuesta.setPedidoId(evento.getPedidoId());
        respuesta.setClienteId(evento.getClienteId());
        respuesta.setRestauranteId(
                evento.getRestauranteId()
        );
        respuesta.setEmpleadoId(
                evento.getEmpleadoId()
        );
        respuesta.setEstadoAnterior(
                evento.getEstadoAnterior()
        );
        respuesta.setEstadoNuevo(
                evento.getEstadoNuevo()
        );
        respuesta.setFechaEvento(
                evento.getFechaEvento()
        );

        return respuesta;
    }
}