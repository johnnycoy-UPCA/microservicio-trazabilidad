package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad;

import co.edu.clase.microservicio_trazabilidad.util.constantes.CodigosError;
import co.edu.clase.microservicio_trazabilidad.util.constantes.MensajesError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
public class ManejadorAccesoDenegado
        implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public ManejadorAccesoDenegado(
            ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest solicitud,
            HttpServletResponse respuesta,
            AccessDeniedException excepcion)
            throws IOException {

        respuesta.setStatus(
                HttpServletResponse.SC_FORBIDDEN
        );
        respuesta.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );
        respuesta.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(
                respuesta.getOutputStream(),
                Map.of(
                        "fechaHora", Instant.now().toString(),
                        "estado",
                        HttpServletResponse.SC_FORBIDDEN,
                        "codigo", CodigosError.ACCESO_DENEGADO,
                        "mensaje", MensajesError.ACCESO_DENEGADO,
                        "ruta", solicitud.getRequestURI(),
                        "detalles", Map.of()
                )
        );
    }
}