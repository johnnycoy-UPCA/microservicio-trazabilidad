package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

import java.util.List;

public record ReporteTiemposRestauranteRespuestaDto(
        Long restauranteId,
        Long promedioTotalSegundos,
        int pedidosEntregados,
        List<TiempoPedidoRespuestaDto> pedidos
) {
}