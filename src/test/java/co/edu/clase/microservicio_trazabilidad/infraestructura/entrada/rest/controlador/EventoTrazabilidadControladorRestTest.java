package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.controlador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EventoTrazabilidadRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.solicitud.RegistrarEventoSolicitudDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.RegistrarEventoHandler;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.manejador.ManejadorGlobalExcepciones;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventoTrazabilidadControladorRestTest {

    @Mock
    private RegistrarEventoHandler
            registrarEventoHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void configurar() {
        EventoTrazabilidadControladorRest controlador =
                new EventoTrazabilidadControladorRest(
                        registrarEventoHandler
                );

        LocalValidatorFactoryBean validador =
                new LocalValidatorFactoryBean();

        validador.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(controlador)
                .setControllerAdvice(
                        new ManejadorGlobalExcepciones()
                )
                .setValidator(validador)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void debeRegistrarEventoYResponderCreado()
            throws Exception {

        RegistrarEventoSolicitudDto solicitud =
                crearSolicitud();

        when(registrarEventoHandler.ejecutar(
                any(RegistrarEventoSolicitudDto.class)
        )).thenReturn(crearRespuesta());

        mockMvc.perform(post(ConstantesApi.API_EVENTOS)
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(
                                objectMapper
                                        .writeValueAsString(
                                                solicitud
                                        )
                        ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id")
                        .value("evento-1"))
                .andExpect(jsonPath("$.pedidoId")
                        .value(100))
                .andExpect(jsonPath("$.estadoNuevo")
                        .value("PENDIENTE"));

        verify(registrarEventoHandler).ejecutar(
                any(RegistrarEventoSolicitudDto.class)
        );
    }

    @Test
    void debeRechazarPedidoIdNulo()
            throws Exception {

        RegistrarEventoSolicitudDto solicitud =
                crearSolicitud();

        solicitud.setPedidoId(null);

        mockMvc.perform(post(ConstantesApi.API_EVENTOS)
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content(
                                objectMapper
                                        .writeValueAsString(
                                                solicitud
                                        )
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo")
                        .value("VAL-001"))
                .andExpect(jsonPath("$.detalles.pedidoId")
                        .exists());
    }

    private RegistrarEventoSolicitudDto
            crearSolicitud() {

        RegistrarEventoSolicitudDto solicitud =
                new RegistrarEventoSolicitudDto();

        solicitud.setPedidoId(100L);
        solicitud.setClienteId(20L);
        solicitud.setRestauranteId(1L);
        solicitud.setEstadoAnterior(null);
        solicitud.setEstadoNuevo(
                EstadoPedido.PENDIENTE
        );

        return solicitud;
    }

    private EventoTrazabilidadRespuestaDto
            crearRespuesta() {

        EventoTrazabilidadRespuestaDto respuesta =
                new EventoTrazabilidadRespuestaDto();

        respuesta.setId("evento-1");
        respuesta.setPedidoId(100L);
        respuesta.setClienteId(20L);
        respuesta.setRestauranteId(1L);
        respuesta.setEstadoAnterior(null);
        respuesta.setEstadoNuevo(
                EstadoPedido.PENDIENTE
        );
        respuesta.setFechaEvento(
                Instant.parse(
                        "2026-11-20T19:30:00Z"
                )
        );

        return respuesta;
    }
}