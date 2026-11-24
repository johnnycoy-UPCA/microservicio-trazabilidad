package co.edu.clase.microservicio_trazabilidad.dominio.api;

// import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TrazabilidadPedido;

// import java.util.List;

public interface ConsultarTrazabilidadServicioPuerto {

    TrazabilidadPedido consultarPorPedido(
            Long pedidoId,
            Long clienteId
    );
}