package co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.FiltroApiKeyInterna;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

    @Bean
    public SecurityFilterChain filtroSeguridadDesarrollo(
            HttpSecurity seguridadHttp,
			FiltroApiKeyInterna filtroApiKeyInterna)
			throws Exception {

        seguridadHttp
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(
					// "/api/v1/trazabilidad/eventos"
					ConstantesApi.API_EVENTOS
				)
			)
			.sessionManagement(sesion -> sesion
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(autorizacion ->
				autorizacion
					.requestMatchers(
							"/swagger-ui.html",
							"/swagger-ui/**",
							"/v3/api-docs/**"
							// "/api/v1/trazabilidad/eventos"
					)
					.permitAll()
					.requestMatchers(
						HttpMethod.POST,
						ConstantesApi.API_EVENTOS
					)
					.hasRole("INTERNAL")
					.anyRequest()
					.authenticated()
			)
			.addFilterBefore(
				filtroApiKeyInterna,
				UsernamePasswordAuthenticationFilter.class
			);

        return seguridadHttp.build();
    }
}