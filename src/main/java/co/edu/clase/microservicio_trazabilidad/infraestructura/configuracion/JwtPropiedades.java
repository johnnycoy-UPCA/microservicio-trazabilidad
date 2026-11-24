package co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Base64;

@Component
@Validated
@ConfigurationProperties(prefix = "seguridad.jwt")
public class JwtPropiedades {

    private static final int MINIMO_BYTES_SECRETO = 32;
    private static final String VALOR_EJEMPLO =
            "TU_SECRETO_BASE64";

    @NotBlank(message = "JWT_SECRETO es obligatorio")
    private String secreto;

    @PostConstruct
    void validarConfiguracion() {
        if (VALOR_EJEMPLO.equals(secreto)) {
            throw new IllegalStateException(
                    "JWT_SECRETO contiene un valor de ejemplo"
            );
        }

        byte[] decodificado;

        try {
            decodificado =
                    Base64.getDecoder().decode(secreto);
        } catch (IllegalArgumentException excepcion) {
            throw new IllegalStateException(
                    "JWT_SECRETO debe ser Base64 válido",
                    excepcion
            );
        }

        if (decodificado.length < MINIMO_BYTES_SECRETO) {
            throw new IllegalStateException(
                    "JWT_SECRETO debe representar "
                            + "al menos 32 bytes"
            );
        }
    }

    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }
}