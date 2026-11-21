package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventoTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.documento.EventoTrazabilidadDocumento;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.mapeador.EventoTrazabilidadPersistenciaMapeador;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.repositorio.EventoTrazabilidadMongoRepositorio;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import org.springframework.stereotype.Component;

@Component
public class EventoTrazabilidadPersistenciaAdaptador
        implements EventoTrazabilidadPersistenciaPuerto {

    private final EventoTrazabilidadMongoRepositorio
            eventoMongoRepositorio;

    private final EventoTrazabilidadPersistenciaMapeador
            eventoPersistenciaMapeador;

    public EventoTrazabilidadPersistenciaAdaptador(
            EventoTrazabilidadMongoRepositorio
                    eventoMongoRepositorio,
            EventoTrazabilidadPersistenciaMapeador
                    eventoPersistenciaMapeador) {

        this.eventoMongoRepositorio =
                eventoMongoRepositorio;

        this.eventoPersistenciaMapeador =
                eventoPersistenciaMapeador;
    }

    @Override
    public boolean existeTransicion(
            Long pedidoId,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo) {

        return eventoMongoRepositorio
                .existsByPedidoIdAndEstadoAnteriorAndEstadoNuevo(
                        pedidoId,
                        estadoAnterior,
                        estadoNuevo
                );
    }

    @Override
    public EventoTrazabilidad guardar(
            EventoTrazabilidad evento) {

        EventoTrazabilidadDocumento documento =
                eventoPersistenciaMapeador
                        .aDocumento(evento);

        EventoTrazabilidadDocumento guardado =
                eventoMongoRepositorio.save(documento);

        return eventoPersistenciaMapeador
                .aModelo(guardado);
    }
}