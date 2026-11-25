package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.repositorio;

import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.documento.EventoTrazabilidadDocumento;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.util.List;

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

	List<EventoTrazabilidadDocumento>
	findByPedidoIdAndClienteIdOrderByFechaEventoAsc(
			Long pedidoId,
			Long clienteId
	);

    List<EventoTrazabilidadDocumento>
    findByRestauranteIdOrderByFechaEventoAsc(
            Long restauranteId
    );
}