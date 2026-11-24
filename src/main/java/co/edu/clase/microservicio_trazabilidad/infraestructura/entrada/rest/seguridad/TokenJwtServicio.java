package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion.JwtPropiedades;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoToken;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.PerfilUsuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class TokenJwtServicio {

    private final JwtPropiedades jwtPropiedades;

    public TokenJwtServicio(
            JwtPropiedades jwtPropiedades) {

        this.jwtPropiedades = jwtPropiedades;
    }

    public EstadoToken validar(String token) {
        try {
            Claims reclamos = extraerReclamos(token);

            if (!ConstantesSeguridad.EMISOR_TOKEN.equals(
                    reclamos.getIssuer()
            )) {
                return EstadoToken.INVALIDO;
            }

            return EstadoToken.VALIDO;
        } catch (ExpiredJwtException excepcion) {
            return EstadoToken.EXPIRADO;
        } catch (JwtException
                | IllegalArgumentException excepcion) {

            return EstadoToken.INVALIDO;
        }
    }

    public String extraerCorreo(String token) {
        return extraerReclamos(token).getSubject();
    }

    public Long extraerUsuarioId(String token) {
        Number identificador = extraerReclamos(token).get(
                ConstantesSeguridad.RECLAMO_USUARIO_ID,
                Number.class
        );

        return identificador.longValue();
    }

    public PerfilUsuario extraerPerfil(String token) {
        String perfil = extraerReclamos(token).get(
                ConstantesSeguridad.RECLAMO_PERFIL,
                String.class
        );

        return PerfilUsuario.valueOf(perfil);
    }

    private Claims extraerReclamos(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerClaveFirma() {
        byte[] secreto = Decoders.BASE64.decode(
                jwtPropiedades.getSecreto()
        );

        return Keys.hmacShaKeyFor(secreto);
    }
}