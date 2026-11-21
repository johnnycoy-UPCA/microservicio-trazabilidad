package co.edu.clase.microservicio_trazabilidad.aplicacion.handler;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EventoTrazabilidadRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.solicitud.RegistrarEventoSolicitudDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador.EventoTrazabilidadDtoMapeador;
import co.edu.clase.microservicio_trazabilidad.dominio.api.RegistrarEventoServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import org.springframework.stereotype.Component;

@Component
public class RegistrarEventoHandler {

    private final RegistrarEventoServicioPuerto
            registrarEventoServicioPuerto;

    private final EventoTrazabilidadDtoMapeador
            eventoDtoMapeador;

    public RegistrarEventoHandler(
            RegistrarEventoServicioPuerto
                    registrarEventoServicioPuerto,
            EventoTrazabilidadDtoMapeador
                    eventoDtoMapeador) {

        this.registrarEventoServicioPuerto =
                registrarEventoServicioPuerto;

        this.eventoDtoMapeador = eventoDtoMapeador;
    }

    public EventoTrazabilidadRespuestaDto ejecutar(
            RegistrarEventoSolicitudDto solicitud) {

        EventoTrazabilidad evento =
                eventoDtoMapeador.aModelo(solicitud);

        EventoTrazabilidad registrado =
                registrarEventoServicioPuerto
                        .registrar(evento);

        return eventoDtoMapeador.aRespuesta(
                registrado
        );
    }
}