package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.PerfilUsuario;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroAutenticacionJwt
        extends OncePerRequestFilter {

    private final TokenJwtServicio tokenJwtServicio;

    public FiltroAutenticacionJwt(
            TokenJwtServicio tokenJwtServicio) {

        this.tokenJwtServicio = tokenJwtServicio;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            FilterChain cadenaFiltros)
            throws ServletException, IOException {

        String encabezado = solicitud.getHeader(
                ConstantesSeguridad.ENCABEZADO_AUTORIZACION
        );

        if (encabezado == null
                || !encabezado.startsWith(
                ConstantesSeguridad.PREFIJO_TOKEN
        )) {
            cadenaFiltros.doFilter(solicitud, respuesta);
            return;
        }

        String token = encabezado.substring(
                ConstantesSeguridad.PREFIJO_TOKEN.length()
        );

        EstadoToken estado = tokenJwtServicio.validar(token);

        if (!EstadoToken.VALIDO.equals(estado)) {
            registrarError(solicitud, estado);
            cadenaFiltros.doFilter(solicitud, respuesta);
            return;
        }

        establecerAutenticacion(token);
        cadenaFiltros.doFilter(solicitud, respuesta);
    }

    private void registrarError(
            HttpServletRequest solicitud,
            EstadoToken estado) {

        boolean expirado =
                EstadoToken.EXPIRADO.equals(estado);

        solicitud.setAttribute(
                ConstantesSeguridad.ATRIBUTO_CODIGO_ERROR,
                expirado
                        ? CodigosError.TOKEN_EXPIRADO
                        : CodigosError.TOKEN_INVALIDO
        );

        solicitud.setAttribute(
                ConstantesSeguridad.ATRIBUTO_MENSAJE_ERROR,
                expirado
                        ? MensajesError.TOKEN_EXPIRADO
                        : MensajesError.TOKEN_INVALIDO
        );
    }

    private void establecerAutenticacion(String token) {
        PerfilUsuario perfil =
                tokenJwtServicio.extraerPerfil(token);

        UsuarioAutenticado principal =
                new UsuarioAutenticado(
                        tokenJwtServicio.extraerUsuarioId(token),
                        tokenJwtServicio.extraerCorreo(token),
                        perfil,
                        token
                );

        SimpleGrantedAuthority autoridad =
                new SimpleGrantedAuthority(
                        ConstantesSeguridad.PREFIJO_AUTORIDAD
                                + perfil.name()
                );

        UsernamePasswordAuthenticationToken autenticacion =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        List.of(autoridad)
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(autenticacion);
    }
}