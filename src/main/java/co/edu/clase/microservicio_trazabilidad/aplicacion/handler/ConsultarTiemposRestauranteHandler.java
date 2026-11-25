package co.edu.clase.microservicio_trazabilidad.aplicacion.handler;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.ReporteTiemposRestauranteRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador.TiemposDtoMapeador;
import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTiemposRestauranteServicioPuerto;
import org.springframework.stereotype.Component;

@Component
public class ConsultarTiemposRestauranteHandler {

    private final ConsultarTiemposRestauranteServicioPuerto
            servicioPuerto;

    private final TiemposDtoMapeador mapeador;

    public ConsultarTiemposRestauranteHandler(
            ConsultarTiemposRestauranteServicioPuerto
                    servicioPuerto,
            TiemposDtoMapeador mapeador) {

        this.servicioPuerto = servicioPuerto;
        this.mapeador = mapeador;
    }

    public ReporteTiemposRestauranteRespuestaDto ejecutar(
            Long restauranteId,
            String token) {

        return mapeador.aReporteRespuesta(
                servicioPuerto.consultar(
                        restauranteId,
                        token
                )
        );
    }
}