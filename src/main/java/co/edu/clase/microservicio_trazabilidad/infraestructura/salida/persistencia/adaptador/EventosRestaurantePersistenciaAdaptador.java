package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventosRestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.mapeador.EventoTrazabilidadPersistenciaMapeador;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.repositorio.EventoTrazabilidadMongoRepositorio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventosRestaurantePersistenciaAdaptador
        implements EventosRestaurantePersistenciaPuerto {

    private final EventoTrazabilidadMongoRepositorio
            repositorio;

    private final EventoTrazabilidadPersistenciaMapeador
            mapeador;

    public EventosRestaurantePersistenciaAdaptador(
            EventoTrazabilidadMongoRepositorio repositorio,
            EventoTrazabilidadPersistenciaMapeador mapeador) {

        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public List<EventoTrazabilidad> consultarPorRestaurante(
            Long restauranteId) {

        return repositorio
                .findByRestauranteIdOrderByFechaEventoAsc(
                        restauranteId
                )
                .stream()
                .map(documento ->
                        mapeador.aModelo(documento)
                )
                .toList();
    }
}