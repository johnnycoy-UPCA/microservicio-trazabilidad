package co.edu.clase.microservicio_trazabilidad.dominio.spi;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

public interface EventoTrazabilidadPersistenciaPuerto {

    boolean existeTransicion(
            Long pedidoId,
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo
    );

    EventoTrazabilidad guardar(
            EventoTrazabilidad evento
    );
}