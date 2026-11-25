package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TramoEstadoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TrazabilidadPedidoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarTrazabilidadPedidoHandler;
import co.edu.clase.microservicio_trazabilidad.dominio.excepcion.ExcepcionTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.UsuarioAutenticado;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.PerfilUsuario;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ConsultarTrazabilidadControladorRestTest {

    private static final Long PEDIDO_ID = 100L;
    private static final Long CLIENTE_ID = 20L;

    @Mock
    private ConsultarTrazabilidadPedidoHandler handler;

    private MockMvc mockMvc;

    @BeforeEach
    void configurar() {
        ConsultarTrazabilidadControladorRest controlador =
                new ConsultarTrazabilidadControladorRest(
                        handler
                );

        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setControllerAdvice(
                        new ManejadorGlobalExcepciones()
                )
                .setCustomArgumentResolvers(
                        new AuthenticationPrincipalArgumentResolver()
                )
                .build();
    }

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeConsultarTrazabilidadDelClienteAutenticado()
            throws Exception {

        when(handler.ejecutar(
                eq(PEDIDO_ID),
                eq(CLIENTE_ID)
        )).thenReturn(crearRespuesta());

        mockMvc.perform(
                        get(
                                ConstantesApi
                                        .API_CONSULTA_TRAZABILIDAD
                                        + "/100"
                        ).with(autenticacionCliente())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pedidoId").value(100))
                .andExpect(jsonPath("$.estadoActual")
                        .value("ENTREGADO"))
                .andExpect(jsonPath("$.tiempoTotalSegundos")
                        .value(780))
                .andExpect(jsonPath("$.tramos.length()")
                        .value(2))
                .andExpect(jsonPath("$.tramos[0].estado")
                        .value("PENDIENTE"))
                .andExpect(jsonPath("$.tramos[0].duracionSegundos")
                        .value(120))
                .andExpect(jsonPath("$.tramos[1].enCurso")
                        .value(false))
                .andExpect(jsonPath("$.clienteId")
                        .doesNotExist());

        verify(handler).ejecutar(
                eq(PEDIDO_ID),
                eq(CLIENTE_ID)
        );
    }

    @Test
    void debeResponderNoEncontradoSiNoHayTrazabilidad()
            throws Exception {

        when(handler.ejecutar(
                eq(PEDIDO_ID),
                eq(CLIENTE_ID)
        )).thenThrow(new ExcepcionTrazabilidad(
                CodigosError.PEDIDO_NO_ENCONTRADO,
                MensajesError.PEDIDO_NO_ENCONTRADO,
                CategoriaError.NO_ENCONTRADO
        ));

        mockMvc.perform(
                        get(
                                ConstantesApi
                                        .API_CONSULTA_TRAZABILIDAD
                                        + "/100"
                        ).with(autenticacionCliente())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo")
                        .value("TRZ-007"));
    }

    private RequestPostProcessor autenticacionCliente() {
        return solicitud -> {
            UsuarioAutenticado principal =
                    new UsuarioAutenticado(
                            CLIENTE_ID,
                            "cliente@correo.com",
                            PerfilUsuario.CLIENTE,
                            "token-cliente"
                    );

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            List.of(
                                    new SimpleGrantedAuthority(
                                            "ROLE_CLIENTE"
                                    )
                            )
                    );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(autenticacion);

            return solicitud;
        };
    }

    private TrazabilidadPedidoRespuestaDto crearRespuesta() {
        Instant inicio =
                Instant.parse("2026-11-24T15:00:00Z");

        return new TrazabilidadPedidoRespuestaDto(
                PEDIDO_ID,
                EstadoPedido.ENTREGADO,
                780L,
                List.of(
                        new TramoEstadoRespuestaDto(
                                EstadoPedido.PENDIENTE,
                                inicio,
                                inicio.plusSeconds(120),
                                120L,
                                null,
                                false
                        ),
                        new TramoEstadoRespuestaDto(
                                EstadoPedido.ENTREGADO,
                                inicio.plusSeconds(780),
                                null,
                                null,
                                30L,
                                false
                        )
                )
        );
    }
}