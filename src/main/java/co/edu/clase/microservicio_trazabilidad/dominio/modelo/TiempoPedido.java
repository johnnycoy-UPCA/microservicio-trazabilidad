package co.edu.clase.microservicio_trazabilidad.dominio.modelo;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

public record TiempoPedido(
        Long pedidoId,
        Long clienteId,
        Long empleadoId,
        EstadoPedido estadoActual,
        Long esperaSegundos,
        Long preparacionSegundos,
        Long entregaSegundos,
        Long totalSegundos,
        boolean superaPromedio
) {

    public TiempoPedido conSuperaPromedio(
            boolean superaPromedio) {

        return new TiempoPedido(
                pedidoId,
                clienteId,
                empleadoId,
                estadoActual,
                esperaSegundos,
                preparacionSegundos,
                entregaSegundos,
                totalSegundos,
                superaPromedio
        );
    }
}