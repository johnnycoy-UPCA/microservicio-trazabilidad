package co.edu.clase.microservicio_trazabilidad.dominio.spi;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;

import java.util.List;

public interface EventosRestaurantePersistenciaPuerto {

    List<EventoTrazabilidad> consultarPorRestaurante(
            Long restauranteId
    );
}