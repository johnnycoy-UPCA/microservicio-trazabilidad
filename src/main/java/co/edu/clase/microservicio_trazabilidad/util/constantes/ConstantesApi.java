package co.edu.clase.microservicio_trazabilidad.util.constantes;

public final class ConstantesApi {

    public static final String API_EVENTOS = "/api/v1/trazabilidad/eventos";
    public static final String API_CONSULTA_TRAZABILIDAD = "/api/v1/trazabilidad/pedidos";
    public static final String VALIDACION_RESTAURANTE_PROPIETARIO = "/api/v1/restaurantes/{restauranteId}/validacion-propietario";
    public static final String API_RESTAURANTES_TRAZABILIDAD = "/api/v1/trazabilidad/restaurantes";

    private ConstantesApi() {
        throw new IllegalStateException(
                "No se permite crear instancias "
                + "de una clase de constantes"
        );
    }
}