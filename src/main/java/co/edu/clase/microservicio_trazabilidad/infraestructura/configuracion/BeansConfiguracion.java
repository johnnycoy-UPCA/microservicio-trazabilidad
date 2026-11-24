package co.edu.clase.microservicio_trazabilidad.infraestructura.configuracion;

// import co.edu.clase.microservicio_trazabilidad.aplicacion.handler.ConsultarTrazabilidadPedidoHandler;
// import co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador.ConsultarTrazabilidadDtoMapeador;
import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTrazabilidadServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.api.RegistrarEventoServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.casouso.ConsultarTrazabilidadCasoUso;
import co.edu.clase.microservicio_trazabilidad.dominio.casouso.RegistrarEventoCasoUso;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.ConsultarTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.EventoTrazabilidadPersistenciaPuerto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}