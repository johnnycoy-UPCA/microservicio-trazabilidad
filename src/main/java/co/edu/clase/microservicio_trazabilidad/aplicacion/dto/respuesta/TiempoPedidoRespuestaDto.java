package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

public record TiempoPedidoRespuestaDto(
        Long pedidoId,
        Long empleadoId,
        EstadoPedido estadoActual,
        Long esperaSegundos,
        Long preparacionSegundos,
        Long entregaSegundos,
        Long totalSegundos,
        boolean superaPromedio
) {
}