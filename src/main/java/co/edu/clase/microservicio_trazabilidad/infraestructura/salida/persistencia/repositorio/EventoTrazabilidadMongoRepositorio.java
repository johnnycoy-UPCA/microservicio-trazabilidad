package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.repositorio;

import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.documento.EventoTrazabilidadDocumento;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventoTrazabilidadMongoRepositorio
        extends MongoRepository<
        EventoTrazabilidadDocumento,
        String> {

    boolean existsByPedidoIdAndEstadoAnteriorAndEstadoNuevo(
            Long pedidoId,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo
    );
}