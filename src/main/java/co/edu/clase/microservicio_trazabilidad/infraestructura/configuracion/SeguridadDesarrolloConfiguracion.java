package co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

    @Bean
    public SecurityFilterChain filtroSeguridadDesarrollo(
            HttpSecurity seguridadHttp) throws Exception {

        seguridadHttp
                .authorizeHttpRequests(autorizacion ->
                        autorizacion
                                .requestMatchers(
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/api/v1/trazabilidad/eventos"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(
								"/api/v1/trazabilidad/eventos"
						)
				);

        return seguridadHttp.build();
    }
}