package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.api.RegistrarEventoServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventoTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Clock;

public class RegistrarEventoCasoUso
        implements RegistrarEventoServicioPuerto {

    private final EventoTrazabilidadPersistenciaPuerto
            eventoPersistenciaPuerto;

    private final Clock reloj;

    public RegistrarEventoCasoUso(
            EventoTrazabilidadPersistenciaPuerto
                    eventoPersistenciaPuerto,
            Clock reloj) {

        this.eventoPersistenciaPuerto =
                eventoPersistenciaPuerto;

        this.reloj = reloj;
    }

    @Override
    public EventoTrazabilidad registrar(
            EventoTrazabilidad evento) {

        validarDatosObligatorios(evento);
        validarTransicion(
                evento.getEstadoAnterior(),
                evento.getEstadoNuevo()
        );
        validarDuplicado(evento);

        evento.setId(null);
        evento.setFechaEvento(reloj.instant());

        return eventoPersistenciaPuerto.guardar(
                evento
        );
    }

    private void validarDatosObligatorios(
            EventoTrazabilidad evento) {

        if (evento.getPedidoId() == null) {
            throw crearExcepcionValidacion(
                    CodigosError.PEDIDO_OBLIGATORIO,
                    MensajesError.PEDIDO_OBLIGATORIO
            );
        }

        if (evento.getClienteId() == null) {
            throw crearExcepcionValidacion(
                    CodigosError.CLIENTE_OBLIGATORIO,
                    MensajesError.CLIENTE_OBLIGATORIO
            );
        }

        if (evento.getRestauranteId() == null) {
            throw crearExcepcionValidacion(
                    CodigosError.RESTAURANTE_OBLIGATORIO,
                    MensajesError.RESTAURANTE_OBLIGATORIO
            );
        }

        if (evento.getEstadoNuevo() == null) {
            throw crearExcepcionValidacion(
                    CodigosError
                            .ESTADO_NUEVO_OBLIGATORIO,
                    MensajesError
                            .ESTADO_NUEVO_OBLIGATORIO
            );
        }
    }

    private void validarTransicion(
            EstadoPedido estadoAnterior,
            EstadoPedido estadoNuevo) {

        boolean creacionValida =
                estadoAnterior == null
                        && EstadoPedido.PENDIENTE
                        .equals(estadoNuevo);

        boolean preparacionValida =
                EstadoPedido.PENDIENTE
                        .equals(estadoAnterior)
                        && EstadoPedido.EN_PREPARACION
                        .equals(estadoNuevo);

        boolean listoValido =
                EstadoPedido.EN_PREPARACION
                        .equals(estadoAnterior)
                        && EstadoPedido.LISTO
                        .equals(estadoNuevo);

        boolean entregaValida =
                EstadoPedido.LISTO
                        .equals(estadoAnterior)
                        && EstadoPedido.ENTREGADO
                        .equals(estadoNuevo);

        boolean cancelacionValida =
                EstadoPedido.PENDIENTE
                        .equals(estadoAnterior)
                        && EstadoPedido.CANCELADO
                        .equals(estadoNuevo);

        boolean transicionValida =
                creacionValida
                        || preparacionValida
                        || listoValido
                        || entregaValida
                        || cancelacionValida;

        if (!transicionValida) {
            throw crearExcepcionValidacion(
                    CodigosError.TRANSICION_INVALIDA,
                    MensajesError.TRANSICION_INVALIDA
            );
        }
    }

    private void validarDuplicado(
            EventoTrazabilidad evento) {

        boolean existe =
                eventoPersistenciaPuerto
                        .existeTransicion(
                                evento.getPedidoId(),
                                evento.getEstadoAnterior(),
                                evento.getEstadoNuevo()
                        );

        if (existe) {
            throw new ExcepcionTrazabilidad(
                    CodigosError.EVENTO_DUPLICADO,
                    MensajesError.EVENTO_DUPLICADO,
                    CategoriaError.CONFLICTO
            );
        }
    }

    private ExcepcionTrazabilidad
            crearExcepcionValidacion(
                    String codigo,
                    String mensaje) {

        return new ExcepcionTrazabilidad(
                codigo,
                mensaje,
                CategoriaError.VALIDACION
        );
    }
}