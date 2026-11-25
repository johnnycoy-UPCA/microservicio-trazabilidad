package co.edu.clase.microservicio_trazabilidad.dominio.spi;

public interface RestauranteAccesoPuerto {

    void validarPropiedad(
            Long restauranteId,
            String token
    );
}