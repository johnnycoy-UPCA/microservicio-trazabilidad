package co.edu.clase.microservicio_trazabilidad.dominio.modelo;

public record EficienciaEmpleado(
        int posicion,
        Long empleadoId,
        int pedidosGestionados,
        Long promedioPreparacionSegundos
) {
}