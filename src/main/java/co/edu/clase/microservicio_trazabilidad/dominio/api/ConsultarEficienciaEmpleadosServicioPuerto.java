package co.edu.clase.microservicio_trazabilidad.dominio.api;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EficienciaEmpleado;

import java.util.List;

public interface ConsultarEficienciaEmpleadosServicioPuerto {

    List<EficienciaEmpleado> consultar(
            Long restauranteId,
            String token
    );
}