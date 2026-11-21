package co.edu.clase.microservicio_trazabilidad.dominio.api;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;

public interface RegistrarEventoServicioPuerto {

    EventoTrazabilidad registrar(
            EventoTrazabilidad evento
    );
}