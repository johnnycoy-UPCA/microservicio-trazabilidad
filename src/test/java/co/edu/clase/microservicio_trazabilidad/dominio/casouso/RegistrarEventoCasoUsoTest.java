package co.edu.clase.microservicio_trazabilidad.dominio.casouso;

import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventoTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarEventoCasoUsoTest {

    private static final Instant FECHA_FIJA =
            Instant.parse("2026-11-20T19:30:00Z");

    @Mock
    private EventoTrazabilidadPersistenciaPuerto
            eventoPersistenciaPuerto;

    private RegistrarEventoCasoUso casoUso;

    @BeforeEach
    void configurar() {
        Clock reloj = Clock.fixed(
                FECHA_FIJA,
                ZoneOffset.UTC
        );

        casoUso = new RegistrarEventoCasoUso(
                eventoPersistenciaPuerto,
                reloj
        );
    }

    @Test
    void debeRegistrarEventoInicial() {
        EventoTrazabilidad evento =
                crearEventoInicial();

        when(eventoPersistenciaPuerto
                .existeTransicion(
                        100L,
                        null,
                        EstadoPedido.PENDIENTE
                ))
                .thenReturn(false);

        when(eventoPersistenciaPuerto.guardar(
                any(EventoTrazabilidad.class)
        )).thenAnswer(invocacion -> {
            EventoTrazabilidad guardado =
                    invocacion.getArgument(0);

            guardado.setId("evento-1");
            return guardado;
        });

        EventoTrazabilidad resultado =
                casoUso.registrar(evento);

        assertEquals("evento-1", resultado.getId());
        assertEquals(100L, resultado.getPedidoId());
        assertNull(resultado.getEstadoAnterior());
        assertEquals(
                EstadoPedido.PENDIENTE,
                resultado.getEstadoNuevo()
        );
        assertEquals(
                FECHA_FIJA,
                resultado.getFechaEvento()
        );

        verify(eventoPersistenciaPuerto)
                .guardar(evento);
    }

    @Test
    void debeRegistrarTransicionAPreparacion() {
        EventoTrazabilidad evento =
                crearEventoInicial();

        evento.setEstadoAnterior(
                EstadoPedido.PENDIENTE
        );
        evento.setEstadoNuevo(
                EstadoPedido.EN_PREPARACION
        );
        evento.setEmpleadoId(30L);

        when(eventoPersistenciaPuerto
                .existeTransicion(
                        100L,
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PREPARACION
                ))
                .thenReturn(false);

        when(eventoPersistenciaPuerto.guardar(any()))
                .thenAnswer(invocacion ->
                        invocacion.getArgument(0)
                );

        EventoTrazabilidad resultado =
                casoUso.registrar(evento);

        assertEquals(
                EstadoPedido.EN_PREPARACION,
                resultado.getEstadoNuevo()
        );
        assertEquals(30L, resultado.getEmpleadoId());
        assertNotNull(resultado.getFechaEvento());
    }

    @Test
    void debeRechazarTransicionInvalida() {
        EventoTrazabilidad evento =
                crearEventoInicial();

        evento.setEstadoAnterior(
                EstadoPedido.PENDIENTE
        );
        evento.setEstadoNuevo(
                EstadoPedido.ENTREGADO
        );

        ExcepcionTrazabilidad excepcion =
                assertThrows(
                        ExcepcionTrazabilidad.class,
                        () -> casoUso.registrar(evento)
                );

        assertEquals(
                CodigosError.TRANSICION_INVALIDA,
                excepcion.getCodigo()
        );

        verify(eventoPersistenciaPuerto, never())
                .guardar(any());
    }

    @Test
    void debeRechazarEventoDuplicado() {
        EventoTrazabilidad evento =
                crearEventoInicial();

        when(eventoPersistenciaPuerto
                .existeTransicion(
                        100L,
                        null,
                        EstadoPedido.PENDIENTE
                ))
                .thenReturn(true);

        ExcepcionTrazabilidad excepcion =
                assertThrows(
                        ExcepcionTrazabilidad.class,
                        () -> casoUso.registrar(evento)
                );

        assertEquals(
                CodigosError.EVENTO_DUPLICADO,
                excepcion.getCodigo()
        );

        verify(eventoPersistenciaPuerto, never())
                .guardar(any());
    }

    private EventoTrazabilidad crearEventoInicial() {
        return new EventoTrazabilidad(
                null,
                100L,
                20L,
                1L,
                null,
                null,
                EstadoPedido.PENDIENTE,
                null
        );
    }
}