package co.edu.clase.microservicio_trazabilidad.dominio.api;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.ReporteTiemposRestaurante;

public interface ConsultarTiemposRestauranteServicioPuerto {

    ReporteTiemposRestaurante consultar(
            Long restauranteId,
            String token
    );
}