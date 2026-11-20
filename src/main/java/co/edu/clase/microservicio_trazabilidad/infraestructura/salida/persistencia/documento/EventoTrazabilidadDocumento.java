package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.documento;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "trazabilidad_pedidos")
@CompoundIndexes({
        @CompoundIndex(
                name = "idx_pedido_fecha",
                def = "{'pedido_id': 1, 'fecha_evento': 1}"
        ),
        @CompoundIndex(
                name = "idx_cliente_fecha",
                def = "{'cliente_id': 1, 'fecha_evento': -1}"
        ),
        @CompoundIndex(
                name = "idx_restaurante_fecha",
                def = "{'restaurante_id': 1, 'fecha_evento': -1}"
        ),
        @CompoundIndex(
                name = "idx_restaurante_empleado_fecha",
                def = "{'restaurante_id': 1, "
                        + "'empleado_id': 1, "
                        + "'fecha_evento': 1}"
        ),
        @CompoundIndex(
                name = "uk_pedido_transicion",
                def = "{'pedido_id': 1, "
                        + "'estado_anterior': 1, "
                        + "'estado_nuevo': 1}",
                unique = true
        )
})
public class EventoTrazabilidadDocumento {

    @Id
    private String id;

    @Field("pedido_id")
    private Long pedidoId;

    @Field("cliente_id")
    private Long clienteId;

    @Field("restaurante_id")
    private Long restauranteId;

    @Field("empleado_id")
    private Long empleadoId;

    @Field("estado_anterior")
    private EstadoPedido estadoAnterior;

    @Field("estado_nuevo")
    private EstadoPedido estadoNuevo;

    @Field("fecha_evento")
    private Instant fechaEvento;

    public EventoTrazabilidadDocumento() {
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