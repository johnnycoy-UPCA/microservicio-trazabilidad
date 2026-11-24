package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TramoEstado;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TrazabilidadPedido;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.ConsultarTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarTrazabilidadCasoUsoTest {

    private static final Long PEDIDO_ID = 100L;
    private static final Long CLIENTE_ID = 20L;
    private static final Long EMPLEADO_ID = 30L;

    private static final Instant T0 =
            Instant.parse("2026-11-24T15:00:00Z");

    @Mock
    private ConsultarTrazabilidadPersistenciaPuerto
            persistenciaPuerto;

    private ConsultarTrazabilidadCasoUso casoUso;

    @BeforeEach
    void configurar() {
        casoUso = new ConsultarTrazabilidadCasoUso(
                persistenciaPuerto
        );
    }

    @Test
    void debeCalcularTiempoPorEstadoDePedidoEntregado() {
        when(persistenciaPuerto.consultarPorPedidoYCliente(
                PEDIDO_ID,
                CLIENTE_ID
        )).thenReturn(List.of(
                evento(null, EstadoPedido.PENDIENTE, 0, null),
                evento(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION,
                        120,
                        EMPLEADO_ID
                ),
                evento(
                        EstadoPedido.EN_PREPARACION,
                        EstadoPedido.LISTO,
                        720,
                        EMPLEADO_ID
                ),
                evento(
                        EstadoPedido.LISTO,
                        EstadoPedido.ENTREGADO,
                        780,
                        EMPLEADO_ID
                )
        ));

        TrazabilidadPedido resultado =
                casoUso.consultarPorPedido(
                        PEDIDO_ID,
                        CLIENTE_ID
                );

        assertEquals(
                EstadoPedido.ENTREGADO,
                resultado.getEstadoActual()
        );
        assertEquals(780L, resultado.getTiempoTotalSegundos());
        assertEquals(4, resultado.getTramos().size());

        TramoEstado pendiente = resultado.getTramos().get(0);
        TramoEstado preparacion = resultado.getTramos().get(1);
        TramoEstado listo = resultado.getTramos().get(2);
        TramoEstado entregado = resultado.getTramos().get(3);

        assertEquals(120L, pendiente.getDuracionSegundos());
        assertNull(pendiente.getEmpleadoId());

        assertEquals(600L, preparacion.getDuracionSegundos());
        assertEquals(EMPLEADO_ID, preparacion.getEmpleadoId());

        assertEquals(60L, listo.getDuracionSegundos());

        assertNull(entregado.getDuracionSegundos());
        assertNull(entregado.getHasta());
        assertFalse(entregado.isEnCurso());
    }

    @Test
    void debeMarcarUltimoTramoEnCursoSiPedidoNoTermino() {
        when(persistenciaPuerto.consultarPorPedidoYCliente(
                PEDIDO_ID,
                CLIENTE_ID
        )).thenReturn(List.of(
                evento(null, EstadoPedido.PENDIENTE, 0, null),
                evento(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION,
                        90,
                        EMPLEADO_ID
                )
        ));

        TrazabilidadPedido resultado =
                casoUso.consultarPorPedido(
                        PEDIDO_ID,
                        CLIENTE_ID
                );

        TramoEstado ultimo = resultado.getTramos().get(1);

        assertEquals(
                EstadoPedido.EN_PREPARACION,
                resultado.getEstadoActual()
        );
        assertNull(resultado.getTiempoTotalSegundos());
        assertTrue(ultimo.isEnCurso());
        assertNull(ultimo.getDuracionSegundos());
        assertEquals(
                90L,
                resultado.getTramos().get(0)
                        .getDuracionSegundos()
        );
    }

    @Test
    void debeCalcularTiempoTotalDePedidoCancelado() {
        when(persistenciaPuerto.consultarPorPedidoYCliente(
                PEDIDO_ID,
                CLIENTE_ID
        )).thenReturn(List.of(
                evento(null, EstadoPedido.PENDIENTE, 0, null),
                evento(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.CANCELADO,
                        45,
                        null
                )
        ));

        TrazabilidadPedido resultado =
                casoUso.consultarPorPedido(
                        PEDIDO_ID,
                        CLIENTE_ID
                );

        assertEquals(
                EstadoPedido.CANCELADO,
                resultado.getEstadoActual()
        );
        assertEquals(45L, resultado.getTiempoTotalSegundos());
        assertFalse(resultado.getTramos().get(1).isEnCurso());
    }

    @Test
    void debeRechazarPedidoSinTrazabilidad() {
        when(persistenciaPuerto.consultarPorPedidoYCliente(
                PEDIDO_ID,
                CLIENTE_ID
        )).thenReturn(List.of());

        ExcepcionTrazabilidad excepcion = assertThrows(
                ExcepcionTrazabilidad.class,
                () -> casoUso.consultarPorPedido(
                        PEDIDO_ID,
                        CLIENTE_ID
                )
        );

        assertEquals(
                CodigosError.PEDIDO_NO_ENCONTRADO,
                excepcion.getCodigo()
        );
    }

    private EventoTrazabilidad evento(
            EstadoPedido anterior,
            EstadoPedido nuevo,
            long segundosDesdeInicio,
            Long empleadoId) {

        return new EventoTrazabilidad(
                "evento-" + nuevo.name(),
                PEDIDO_ID,
                CLIENTE_ID,
                1L,
                empleadoId,
                anterior,
                nuevo,
                T0.plusSeconds(segundosDesdeInicio)
        );
    }
}