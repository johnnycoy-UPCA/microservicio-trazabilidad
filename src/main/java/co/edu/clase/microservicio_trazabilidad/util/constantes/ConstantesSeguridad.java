package co.edu.clase.microservicio_trazabilidad.util.constantes;

public final class ConstantesSeguridad {

    public static final String ENCABEZADO_API_KEY = "X-Internal-Api-Key";
    public static final String AUTORIDAD_INTERNA = "ROLE_INTERNAL";
    public static final String PRINCIPAL_INTERNO = "microservicio-plazoleta";
    public static final String ENCABEZADO_AUTORIZACION = "Authorization";
    public static final String PREFIJO_TOKEN = "Bearer ";
    public static final String PREFIJO_AUTORIDAD = "ROLE_";
    public static final String RECLAMO_USUARIO_ID = "usuarioId";
    public static final String RECLAMO_PERFIL = "perfil";
    public static final String EMISOR_TOKEN = "microservicio-usuarios";
    public static final String ATRIBUTO_CODIGO_ERROR = "codigoErrorAutenticacion";
    public static final String ATRIBUTO_MENSAJE_ERROR = "mensajeErrorAutenticacion";

    private ConstantesSeguridad() {
        throw new IllegalStateException(
			"No se permite crear instancias "
			+ "de una clase de constantes"
        );
    }
}