package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Instant;

public class EventoTrazabilidadRespuestaDto {

    private String id;
    private Long pedidoId;
    private Long clienteId;
    private Long restauranteId;
    private Long empleadoId;
    private EstadoPedido estadoAnterior;
    private EstadoPedido estadoNuevo;
    private Instant fechaEvento;

    public EventoTrazabilidadRespuestaDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setRestauranteId(Long restauranteId) {
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

    public Instant getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(
            Instant fechaEvento) {

        this.fechaEvento = fechaEvento;
    }
}