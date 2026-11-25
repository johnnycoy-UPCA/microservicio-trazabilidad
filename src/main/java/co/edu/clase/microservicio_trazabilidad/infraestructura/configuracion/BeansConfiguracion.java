package co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion;

import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarEficienciaEmpleadosServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTiemposRestauranteServicioPuerto;
// import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarTrazabilidadPedidoHandler;
// import co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador.ConsultarTrazabilidadDtoMapeador;
import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTrazabilidadServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.api.RegistrarEventoServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.casouso.ConsultarEficienciaEmpleadosCasoUso;
import co.edu.clase.microservicio_trazabilidad.dominio.casouso.ConsultarTiemposRestauranteCasoUso;
import co.edu.clase.microservicio_trazabilidad.dominio.casouso.ConsultarTrazabilidadCasoUso;
import co.edu.clase.microservicio_trazabilidad.dominio.casouso.RegistrarEventoCasoUso;
import co.edu.clase.microservicio_trazabilidad.dominio.servicio.CalculadoraTiempos;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.ConsultarTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventoTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventosRestaurantePersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.RestauranteAccesoPuerto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Clock;

@Configuration
public class BeansConfiguracion {

    @Bean
    public Clock relojAplicacion() {
        return Clock.systemUTC();
    }

    @Bean
    public RegistrarEventoServicioPuerto
            registrarEventoServicioPuerto(
                    EventoTrazabilidadPersistenciaPuerto
                            eventoPersistenciaPuerto,
                    Clock relojAplicacion) {

        return new RegistrarEventoCasoUso(
                eventoPersistenciaPuerto,
                relojAplicacion
        );
    }

	@Bean
	public ConsultarTrazabilidadServicioPuerto
			consultarTrazabilidadServicioPuerto(
					ConsultarTrazabilidadPersistenciaPuerto persistenciaPuerto) {

		return new ConsultarTrazabilidadCasoUso(
				persistenciaPuerto
		);
	}

	// @Bean
	// public ConsultarTrazabilidadPedidoHandler
	// 		consultarTrazabilidadPedidoHandler(
	// 				ConsultarTrazabilidadServicioPuerto servicioPuerto,
	// 				ConsultarTrazabilidadDtoMapeador mapeador) {

	// 	return new ConsultarTrazabilidadPedidoHandler(
	// 			servicioPuerto,
	// 			mapeador
	// 	);
	// }

	// @Bean
	// public ConsultarTrazabilidadDtoMapeador
	// 		consultarTrazabilidadDtoMapeador() {

	// 	return new ConsultarTrazabilidadDtoMapeador();
	// }

    @Bean("clientePlazoleta")
    public RestClient clientePlazoleta(
            PlazoletaServicioPropiedades propiedades) {

        return RestClient.builder()
                .baseUrl(propiedades.getUrlBase())
                .build();
    }

    @Bean
    public CalculadoraTiempos calculadoraTiempos() {
        return new CalculadoraTiempos();
    }

    @Bean
    public ConsultarTiemposRestauranteServicioPuerto
            consultarTiemposRestauranteServicioPuerto(
                    RestauranteAccesoPuerto accesoPuerto,
                    EventosRestaurantePersistenciaPuerto
                            eventosPuerto,
                    CalculadoraTiempos calculadora) {

        return new ConsultarTiemposRestauranteCasoUso(
                accesoPuerto,
                eventosPuerto,
                calculadora
        );
    }

    @Bean
    public ConsultarEficienciaEmpleadosServicioPuerto
            consultarEficienciaEmpleadosServicioPuerto(
                    RestauranteAccesoPuerto accesoPuerto,
                    EventosRestaurantePersistenciaPuerto
                            eventosPuerto,
                    CalculadoraTiempos calculadora) {

        return new ConsultarEficienciaEmpleadosCasoUso(
                accesoPuerto,
                eventosPuerto,
                calculadora
        );
    }
}