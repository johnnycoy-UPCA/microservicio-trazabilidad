package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTrazabilidadServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TramoEstado;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TrazabilidadPedido;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.ConsultarTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ConsultarTrazabilidadCasoUso
        implements ConsultarTrazabilidadServicioPuerto {

    private final ConsultarTrazabilidadPersistenciaPuerto
            persistenciaPuerto;

    public ConsultarTrazabilidadCasoUso(
            ConsultarTrazabilidadPersistenciaPuerto
                    persistenciaPuerto) {

        this.persistenciaPuerto = persistenciaPuerto;
    }

    @Override
    public TrazabilidadPedido consultarPorPedido(
            Long pedidoId,
            Long clienteId) {

        List<EventoTrazabilidad> eventos =
                persistenciaPuerto
                        .consultarPorPedidoYCliente(
                                pedidoId,
                                clienteId
                        );

        if (eventos == null || eventos.isEmpty()) {
            throw new ExcepcionTrazabilidad(
                    CodigosError.PEDIDO_NO_ENCONTRADO,
                    MensajesError.PEDIDO_NO_ENCONTRADO,
                    CategoriaError.NO_ENCONTRADO
            );
        }

        List<TramoEstado> tramos =
                construirTramos(eventos);

        TramoEstado primero = tramos.get(0);
        TramoEstado ultimo =
                tramos.get(tramos.size() - 1);

        Long tiempoTotal = esTerminal(ultimo.getEstado())
                ? Duration.between(
                        primero.getDesde(),
                        ultimo.getDesde()
                ).getSeconds()
                : null;

        return new TrazabilidadPedido(
                pedidoId,
                ultimo.getEstado(),
                tiempoTotal,
                tramos
        );
    }

    private List<TramoEstado> construirTramos(
            List<EventoTrazabilidad> eventos) {

        List<TramoEstado> tramos = new ArrayList<>();
        int ultimoIndice = eventos.size() - 1;

        for (int i = 0; i <= ultimoIndice; i++) {
            EventoTrazabilidad actual = eventos.get(i);
            boolean esUltimo = i == ultimoIndice;

            if (esUltimo) {
                tramos.add(new TramoEstado(
                        actual.getEstadoNuevo(),
                        actual.getFechaEvento(),
                        null,
                        null,
                        actual.getEmpleadoId(),
                        !esTerminal(
                                actual.getEstadoNuevo()
                        )
                ));
                continue;
            }

            EventoTrazabilidad siguiente =
                    eventos.get(i + 1);

            tramos.add(new TramoEstado(
                    actual.getEstadoNuevo(),
                    actual.getFechaEvento(),
                    siguiente.getFechaEvento(),
                    Duration.between(
                            actual.getFechaEvento(),
                            siguiente.getFechaEvento()
                    ).getSeconds(),
                    actual.getEmpleadoId(),
                    false
            ));
        }

        return tramos;
    }

    private boolean esTerminal(EstadoPedido estado) {
        return EstadoPedido.ENTREGADO.equals(estado)
                || EstadoPedido.CANCELADO.equals(estado);
    }
}