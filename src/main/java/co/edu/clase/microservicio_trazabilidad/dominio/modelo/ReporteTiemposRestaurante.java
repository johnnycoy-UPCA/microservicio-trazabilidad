package co.edu.clase.microservicio_trazabilidad.dominio.modelo;

import java.util.List;

public record ReporteTiemposRestaurante(
        Long restauranteId,
        Long promedioTotalSegundos,
        int pedidosEntregados,
        List<TiempoPedido> pedidos
) {
}