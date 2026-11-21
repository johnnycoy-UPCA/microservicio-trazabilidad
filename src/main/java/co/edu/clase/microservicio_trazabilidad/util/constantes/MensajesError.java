package co.edu.clase.microservicio_trazabilidad.util.constantes;

public final class MensajesError {

    public static final String PEDIDO_OBLIGATORIO =
            "El identificador del pedido es obligatorio";

    public static final String CLIENTE_OBLIGATORIO =
            "El identificador del cliente es obligatorio";

    public static final String RESTAURANTE_OBLIGATORIO =
            "El identificador del restaurante es obligatorio";

    public static final String ESTADO_NUEVO_OBLIGATORIO =
            "El estado nuevo del pedido es obligatorio";

    public static final String TRANSICION_INVALIDA =
            "La transición de estado del pedido no es válida";

    public static final String EVENTO_DUPLICADO =
            "La transición ya fue registrada";

	public static final String SOLICITUD_INVALIDA =
			"La solicitud contiene datos inválidos";

	public static final String CUERPO_SOLICITUD_INVALIDO =
			"El cuerpo de la solicitud no es válido";

	public static final String ERROR_INTERNO =
			"Ocurrió un error interno en el servicio";

	public static final String CREDENCIAL_INTERNA_REQUERIDA =
			"La solicitud requiere una credencial interna";

	public static final String CREDENCIAL_INTERNA_INVALIDA =
			"La credencial interna no es válida";

    private MensajesError() {
        throw new IllegalStateException(
            "No se permite crear instancias "
            + "de una clase de constantes"
        );
    }
}