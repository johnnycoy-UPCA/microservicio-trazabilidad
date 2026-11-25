package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

public record EficienciaEmpleadoRespuestaDto(
        int posicion,
        Long empleadoId,
        int pedidosGestionados,
        Long promedioPreparacionSegundos
) {
}