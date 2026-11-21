package co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesSeguridad;

@Configuration
public class OpenApiConfiguracion {

	private static final String ESQUEMA_SEGURIDAD_INTERNA = "apiKeyInterna";

    @Bean
    public OpenAPI documentacionApi() {
        return new OpenAPI()
			.info(new Info()
					.title(
						"Microservicio Trazabilidad"
					)
					.description(
						"API para registrar y consultar "
						+ "la trazabilidad de pedidos"
					)
					.version("1.0.0")
					.contact(new Contact()
						.name("Johnny Coy Herrera")
					)
			)
            .components(new Components()
				.addSecuritySchemes(
					ESQUEMA_SEGURIDAD_INTERNA,
					new SecurityScheme()
							.type(
								SecurityScheme.Type.APIKEY
							)
							.in(
								SecurityScheme.In.HEADER
							)
							.name(
								ConstantesSeguridad
										.ENCABEZADO_API_KEY
							)
				)
            );
	}
}