package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion.SeguridadInternaPropiedades;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class FiltroApiKeyInternaTest {

    private static final String API_KEY =
            "clave-interna-prueba-2026";

    private FiltroApiKeyInterna filtro;

    @BeforeEach
    void configurar() {
        SeguridadInternaPropiedades propiedades =
                new SeguridadInternaPropiedades();

        propiedades.setApiKey(API_KEY);

        filtro = new FiltroApiKeyInterna(
                propiedades
        );
    }

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeAutenticarApiKeyValida()
            throws Exception {

        MockHttpServletRequest solicitud =
                crearSolicitud();

        solicitud.addHeader(
                ConstantesSeguridad
                        .ENCABEZADO_API_KEY,
                API_KEY
        );

        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        FilterChain cadena = mock(FilterChain.class);

        filtro.doFilter(
                solicitud,
                respuesta,
                cadena
        );

        verify(cadena).doFilter(
                solicitud,
                respuesta
        );

        assertNotNull(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );
    }

    @Test
    void debeRechazarApiKeyAusente()
            throws Exception {

        MockHttpServletRequest solicitud =
                crearSolicitud();

        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        FilterChain cadena = mock(FilterChain.class);

        filtro.doFilter(
                solicitud,
                respuesta,
                cadena
        );

        assertEquals(
                401,
                respuesta.getStatus()
        );

        assertEquals(
                true,
                respuesta.getContentAsString()
                        .contains("SEG-001")
        );

        verify(cadena, never()).doFilter(
                solicitud,
                respuesta
        );
    }

    @Test
    void debeRechazarApiKeyInvalida()
            throws Exception {

        MockHttpServletRequest solicitud =
                crearSolicitud();

        solicitud.addHeader(
                ConstantesSeguridad
                        .ENCABEZADO_API_KEY,
                "clave-incorrecta"
        );

        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        FilterChain cadena = mock(FilterChain.class);

        filtro.doFilter(
                solicitud,
                respuesta,
                cadena
        );

        assertEquals(
                401,
                respuesta.getStatus()
        );

        assertEquals(
                true,
                respuesta.getContentAsString()
                        .contains("SEG-002")
        );

        verify(cadena, never()).doFilter(
                solicitud,
                respuesta
        );
    }

    private MockHttpServletRequest crearSolicitud() {
        MockHttpServletRequest solicitud =
                new MockHttpServletRequest();

        solicitud.setMethod("POST");
        solicitud.setRequestURI(
                ConstantesApi.API_EVENTOS
        );

        return solicitud;
    }
}