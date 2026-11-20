package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.solicitud;

import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RegistrarEventoSolicitudDto {

    @NotNull(
            message = MensajesError.PEDIDO_OBLIGATORIO
    )
    @Positive(
            message = MensajesError.PEDIDO_OBLIGATORIO
    )
    private Long pedidoId;

    @NotNull(
            message = MensajesError.CLIENTE_OBLIGATORIO
    )
    @Positive(
            message = MensajesError.CLIENTE_OBLIGATORIO
    )
    private Long clienteId;

    @NotNull(
            message = MensajesError.RESTAURANTE_OBLIGATORIO
    )
    @Positive(
            message = MensajesError.RESTAURANTE_OBLIGATORIO
    )
    private Long restauranteId;

    @Positive(
            message =
                    "El identificador del empleado debe ser positivo"
    )
    private Long empleadoId;

    private EstadoPedido estadoAnterior;

    @NotNull(
            message = MensajesError
                    .ESTADO_NUEVO_OBLIGATORIO
    )
    private EstadoPedido estadoNuevo;

    public RegistrarEventoSolicitudDto() {
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(
            Long restauranteId) {

        this.restauranteId = restauranteId;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(Long empleadoId) {
        this.empleadoId = empleadoId;
    }

    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(
            EstadoPedido estadoAnterior) {

        this.estadoAnterior = estadoAnterior;
    }

    public EstadoPedido getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(
            EstadoPedido estadoNuevo) {

        this.estadoNuevo = estadoNuevo;
    }
}