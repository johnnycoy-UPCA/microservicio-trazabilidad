package co.edu.clase.microservicio_trazabilidad.aplicacion.handler;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EficienciaEmpleadoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador.TiemposDtoMapeador;
import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarEficienciaEmpleadosServicioPuerto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarEficienciaEmpleadosHandler {

    private final ConsultarEficienciaEmpleadosServicioPuerto
            servicioPuerto;

    private final TiemposDtoMapeador mapeador;

    public ConsultarEficienciaEmpleadosHandler(
            ConsultarEficienciaEmpleadosServicioPuerto
                    servicioPuerto,
            TiemposDtoMapeador mapeador) {

        this.servicioPuerto = servicioPuerto;
        this.mapeador = mapeador;
    }

    public List<EficienciaEmpleadoRespuestaDto> ejecutar(
            Long restauranteId,
            String token) {

        return mapeador.aRankingRespuesta(
                servicioPuerto.consultar(
                        restauranteId,
                        token
                )
        );
    }
}