package co.edu.clase.microservicio_trazabilidad.util.constantes;

public final class ConstantesSeguridad {

    public static final String ENCABEZADO_API_KEY =
            "X-Internal-Api-Key";

    public static final String AUTORIDAD_INTERNA =
            "ROLE_INTERNAL";

    public static final String PRINCIPAL_INTERNO =
            "microservicio-plazoleta";

    private ConstantesSeguridad() {
        throw new IllegalStateException(
                "No se permite crear instancias "
                        + "de una clase de constantes"
        );
    }
}