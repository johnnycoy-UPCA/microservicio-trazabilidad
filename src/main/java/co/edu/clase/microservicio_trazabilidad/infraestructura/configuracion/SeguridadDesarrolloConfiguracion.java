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
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.FiltroAutenticacionJwt;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.ManejadorAccesoDenegado;
import co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.seguridad.PuntoEntradaNoAutorizado;
import co.edu.clase.microservicio_trazabilidad.util.constantes.ConstantesApi;
import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.PerfilUsuario;

@Configuration
@Profile("desarrollo")
public class SeguridadDesarrolloConfiguracion {

    @Bean
    public SecurityFilterChain filtroSeguridadDesarrollo(
            HttpSecurity seguridadHttp,
			FiltroApiKeyInterna filtroApiKeyInterna,
			FiltroAutenticacionJwt filtroAutenticacionJwt,
            PuntoEntradaNoAutorizado puntoEntradaNoAutorizado,
            ManejadorAccesoDenegado manejadorAccesoDenegado)
			throws Exception {

        seguridadHttp
			.csrf(csrf -> csrf
				.ignoringRequestMatchers(
					// "/api/v1/trazabilidad/eventos"
					ConstantesApi.API_EVENTOS
				)
			)
			.sessionManagement(sesion -> sesion
				.sessionCreationPolicy(
					SessionCreationPolicy.STATELESS
				)
			)
			.exceptionHandling(excepciones -> excepciones
				.authenticationEntryPoint(
					puntoEntradaNoAutorizado
				)
				.accessDeniedHandler(
					manejadorAccesoDenegado
				)
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
					.requestMatchers(
						HttpMethod.GET,
						ConstantesApi
							.API_CONSULTA_TRAZABILIDAD
							+ "/*"
					)
					.hasRole(PerfilUsuario.CLIENTE.name())
					.anyRequest()
					.authenticated()
			)
			.addFilterBefore(
				filtroApiKeyInterna,
				UsernamePasswordAuthenticationFilter.class
			)
			.addFilterBefore(
				filtroAutenticacionJwt,
				UsernamePasswordAuthenticationFilter.class
			);

        return seguridadHttp.build();
    }
}