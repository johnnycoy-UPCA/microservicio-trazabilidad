package co.edu.clase.microservicio_trazabilidad.util.constantes;

public final class CodigosError {

    public static final String PEDIDO_OBLIGATORIO = "TRZ-001";
    public static final String CLIENTE_OBLIGATORIO = "TRZ-002";
    public static final String RESTAURANTE_OBLIGATORIO = "TRZ-003";
    public static final String ESTADO_NUEVO_OBLIGATORIO = "TRZ-004";
    public static final String TRANSICION_INVALIDA = "TRZ-005";
    public static final String EVENTO_DUPLICADO = "TRZ-006";
    public static final String PEDIDO_NO_ENCONTRADO = "TRZ-007";
    public static final String RESTAURANTE_NO_PERTENECE_PROPIETARIO = "TRZ-008";
    public static final String PLAZOLETA_NO_DISPONIBLE = "TRZ-009";
    public static final String RESTAURANTE_NO_ENCONTRADO = "TRZ-010";

    public static final String SOLICITUD_INVALIDA = "VAL-001";
    public static final String CUERPO_SOLICITUD_INVALIDO = "VAL-002";

    public static final String ERROR_INTERNO = "SYS-001";

    public static final String CREDENCIAL_INTERNA_REQUERIDA = "SEG-001";
    public static final String CREDENCIAL_INTERNA_INVALIDA = "SEG-002";
    public static final String TOKEN_INVALIDO = "SEG-003";
    public static final String TOKEN_EXPIRADO = "SEG-004";
    public static final String AUTENTICACION_REQUERIDA = "SEG-005";
    public static final String ACCESO_DENEGADO = "SEG-006";

    private CodigosError() {
        throw new IllegalStateException(
            "No se permite crear instancias "
            + "de una clase de constantes"
        );
    }
}