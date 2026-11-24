package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.PerfilUsuario;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FiltroAutenticacionJwtTest {

    @Mock
    private TokenJwtServicio tokenJwtServicio;

    @Mock
    private FilterChain cadenaFiltros;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void debeAutenticarSolicitudConTokenValido()
            throws Exception {

        String token = "token-valido";

        when(tokenJwtServicio.validar(token))
                .thenReturn(EstadoToken.VALIDO);
        when(tokenJwtServicio.extraerCorreo(token))
                .thenReturn("cliente@correo.com");
        when(tokenJwtServicio.extraerUsuarioId(token))
                .thenReturn(20L);
        when(tokenJwtServicio.extraerPerfil(token))
                .thenReturn(PerfilUsuario.CLIENTE);

        MockHttpServletRequest solicitud =
                crearSolicitud(token);
        MockHttpServletResponse respuesta =
                new MockHttpServletResponse();

        new FiltroAutenticacionJwt(tokenJwtServicio)
                .doFilter(
                        solicitud,
                        respuesta,
                        cadenaFiltros
                );

        assertTrue(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .isAuthenticated()
        );
        assertEquals(
                "ROLE_CLIENTE",
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .iterator()
                        .next()
                        .getAuthority()
        );

        verify(cadenaFiltros).doFilter(solicitud, respuesta);
    }

    @Test
    void debeRegistrarTokenInvalidoSinAutenticar()
            throws Exception {

        String token = "token-invalido";

        when(tokenJwtServicio.validar(token))
                .thenReturn(EstadoToken.INVALIDO);

        MockHttpServletRequest solicitud =
                crearSolicitud(token);

        new FiltroAutenticacionJwt(tokenJwtServicio)
                .doFilter(
                        solicitud,
                        new MockHttpServletResponse(),
                        cadenaFiltros
                );

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        );
        assertEquals(
                CodigosError.TOKEN_INVALIDO,
                solicitud.getAttribute(
                        ConstantesSeguridad
                                .ATRIBUTO_CODIGO_ERROR
                )
        );
    }

    @Test
    void debeRegistrarTokenExpiradoSinAutenticar()
            throws Exception {

        String token = "token-expirado";

        when(tokenJwtServicio.validar(token))
                .thenReturn(EstadoToken.EXPIRADO);

        MockHttpServletRequest solicitud =
                crearSolicitud(token);

        new FiltroAutenticacionJwt(tokenJwtServicio)
                .doFilter(
                        solicitud,
                        new MockHttpServletResponse(),
                        cadenaFiltros
                );

        assertNull(
                SecurityContextHolder.getContext()
                        .getAuthentication()
        );
        assertEquals(
                CodigosError.TOKEN_EXPIRADO,
                solicitud.getAttribute(
                        ConstantesSeguridad
                                .ATRIBUTO_CODIGO_ERROR
                )
        );
    }

    private MockHttpServletRequest crearSolicitud(
            String token) {

        MockHttpServletRequest solicitud =
                new MockHttpServletRequest();

        solicitud.addHeader(
                ConstantesSeguridad.ENCABEZADO_AUTORIZACION,
                ConstantesSeguridad.PREFIJO_TOKEN + token
        );

        return solicitud;
    }
}