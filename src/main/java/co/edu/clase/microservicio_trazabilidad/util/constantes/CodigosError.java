package co.edu.clase.microservicio_trazabilidad.util.constantes;

public final class CodigosError {

    public static final String PEDIDO_OBLIGATORIO = "TRZ-001";
    public static final String CLIENTE_OBLIGATORIO = "TRZ-002";
    public static final String RESTAURANTE_OBLIGATORIO = "TRZ-003";
    public static final String ESTADO_NUEVO_OBLIGATORIO = "TRZ-004";
    public static final String TRANSICION_INVALIDA = "TRZ-005";
    public static final String EVENTO_DUPLICADO = "TRZ-006";

    public static final String SOLICITUD_INVALIDA = "VAL-001";
    public static final String CUERPO_SOLICITUD_INVALIDO = "VAL-002";

    public static final String ERROR_INTERNO = "SYS-001";

    private CodigosError() {
        throw new IllegalStateException(
            "No se permite crear instancias "
            + "de una clase de constantes"
        );
    }
}