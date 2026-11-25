package co.edu.clase.microservicio_trazabilidad.dominio.servicio;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TiempoPedido;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CalculadoraTiemposTest {

    private final CalculadoraTiempos calculadora =
            new CalculadoraTiempos();

    @Test
    void debeCalcularEtapasYTotalDePedidoEntregado() {
        List<EventoTrazabilidad> eventos = List.of(
                evento(1L, null, null,
                        EstadoPedido.PENDIENTE,
                        "2026-11-24T10:00:00Z"),
                evento(1L, 30L,
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION,
                        "2026-11-24T10:05:00Z"),
                evento(1L, 30L,
                        EstadoPedido.EN_PREPARACION,
                        EstadoPedido.LISTO,
                        "2026-11-24T10:20:00Z"),
                evento(1L, 30L,
                        EstadoPedido.LISTO,
                        EstadoPedido.ENTREGADO,
                        "2026-11-24T10:25:00Z")
        );

        List<TiempoPedido> resultado =
                calculadora.calcularPorPedido(eventos);

        assertEquals(1, resultado.size());

        TiempoPedido tiempo = resultado.get(0);

        assertEquals(30L, tiempo.empleadoId());
        assertEquals(EstadoPedido.ENTREGADO,
                tiempo.estadoActual());
        assertEquals(300L, tiempo.esperaSegundos());
        assertEquals(900L, tiempo.preparacionSegundos());
        assertEquals(300L, tiempo.entregaSegundos());
        assertEquals(1500L, tiempo.totalSegundos());
    }

    @Test
    void debeExcluirPedidosCancelados() {
        List<EventoTrazabilidad> eventos = List.of(
                evento(2L, null, null,
                        EstadoPedido.PENDIENTE,
                        "2026-11-24T11:00:00Z"),
                evento(2L, null,
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.CANCELADO,
                        "2026-11-24T11:02:00Z")
        );

        assertEquals(
                0,
                calculadora.calcularPorPedido(eventos).size()
        );
    }

    @Test
    void debeDejarNulosEnPedidoIncompleto() {
        List<EventoTrazabilidad> eventos = List.of(
                evento(3L, null, null,
                        EstadoPedido.PENDIENTE,
                        "2026-11-24T12:00:00Z"),
                evento(3L, 30L,
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION,
                        "2026-11-24T12:03:00Z")
        );

        TiempoPedido tiempo =
                calculadora.calcularPorPedido(eventos).get(0);

        assertEquals(EstadoPedido.EN_PREPARACION,
                tiempo.estadoActual());
        assertEquals(180L, tiempo.esperaSegundos());
        assertNull(tiempo.preparacionSegundos());
        assertNull(tiempo.entregaSegundos());
        assertNull(tiempo.totalSegundos());
    }

    private EventoTrazabilidad evento(
            Long pedidoId,
            Long empleadoId,
            EstadoPedido anterior,
            EstadoPedido nuevo,
            String fecha) {

        return new EventoTrazabilidad(
                null,
                pedidoId,
                20L,
                1L,
                empleadoId,
                anterior,
                nuevo,
                Instant.parse(fecha)
        );
    }
}