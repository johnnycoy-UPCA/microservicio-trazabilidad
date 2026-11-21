package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion.SeguridadInternaPropiedades;
import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.List;

@Component
public class FiltroApiKeyInterna
        extends OncePerRequestFilter {

    private final SeguridadInternaPropiedades
            seguridadInternaPropiedades;

    public FiltroApiKeyInterna(
            SeguridadInternaPropiedades
                    seguridadInternaPropiedades) {

        this.seguridadInternaPropiedades =
                seguridadInternaPropiedades;
    }

    @Override
    protected boolean shouldNotFilter(
            HttpServletRequest solicitud) {

        boolean esRegistroEvento =
                HttpMethod.POST.matches(
                        solicitud.getMethod()
                )
                        && ConstantesApi.API_EVENTOS
                        .equals(
                                solicitud.getRequestURI()
                        );

        return !esRegistroEvento;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            FilterChain cadenaFiltros)
            throws ServletException, IOException {

        String apiKeyRecibida = solicitud.getHeader(
                ConstantesSeguridad.ENCABEZADO_API_KEY
        );

        if (apiKeyRecibida == null
                || apiKeyRecibida.isBlank()) {

            escribirError(
                    respuesta,
                    solicitud,
                    CodigosError
                            .CREDENCIAL_INTERNA_REQUERIDA,
                    MensajesError
                            .CREDENCIAL_INTERNA_REQUERIDA
            );
            return;
        }

        if (!esClaveValida(apiKeyRecibida)) {
            escribirError(
                    respuesta,
                    solicitud,
                    CodigosError
                            .CREDENCIAL_INTERNA_INVALIDA,
                    MensajesError
                            .CREDENCIAL_INTERNA_INVALIDA
            );
            return;
        }

        establecerAutenticacion();
        cadenaFiltros.doFilter(solicitud, respuesta);
    }

    private boolean esClaveValida(String apiKeyRecibida) {
        String apiKeyConfigurada =
                seguridadInternaPropiedades.getApiKey();

        if (apiKeyConfigurada == null
                || apiKeyConfigurada.isBlank()) {

            return false;
        }

        byte[] recibida = apiKeyRecibida.getBytes(
                StandardCharsets.UTF_8
        );

        byte[] configurada = apiKeyConfigurada.getBytes(
                StandardCharsets.UTF_8
        );

        return MessageDigest.isEqual(
                recibida,
                configurada
        );
    }

    private void establecerAutenticacion() {
        UsernamePasswordAuthenticationToken autenticacion =
                new UsernamePasswordAuthenticationToken(
                        ConstantesSeguridad
                                .PRINCIPAL_INTERNO,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        ConstantesSeguridad
                                                .AUTORIDAD_INTERNA
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(autenticacion);
    }

    private void escribirError(
            HttpServletResponse respuesta,
            HttpServletRequest solicitud,
            String codigo,
            String mensaje)
            throws IOException {

        respuesta.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );
        respuesta.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );
        respuesta.setCharacterEncoding("UTF-8");

        String cuerpo = """
                {
                  "fechaHora": "%s",
                  "estado": 401,
                  "codigo": "%s",
                  "mensaje": "%s",
                  "ruta": "%s",
                  "detalles": {}
                }
                """.formatted(
                Instant.now(),
                codigo,
                mensaje,
                solicitud.getRequestURI()
        );

        respuesta.getWriter().write(cuerpo);
    }
}