package co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.EficienciaEmpleadoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.ReporteTiemposRestauranteRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TiempoPedidoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EficienciaEmpleado;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.ReporteTiemposRestaurante;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TiempoPedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TiemposDtoMapeador {

    public ReporteTiemposRestauranteRespuestaDto
            aReporteRespuesta(
                    ReporteTiemposRestaurante reporte) {

        List<TiempoPedidoRespuestaDto> pedidos =
                reporte.pedidos().stream()
                        .map(pedido ->
                                aTiempoRespuesta(pedido)
                        )
                        .toList();

        return new ReporteTiemposRestauranteRespuestaDto(
                reporte.restauranteId(),
                reporte.promedioTotalSegundos(),
                reporte.pedidosEntregados(),
                pedidos
        );
    }

    public List<EficienciaEmpleadoRespuestaDto>
            aRankingRespuesta(
                    List<EficienciaEmpleado> ranking) {

        return ranking.stream()
                .map(empleado ->
                        aEficienciaRespuesta(empleado)
                )
                .toList();
    }

    private TiempoPedidoRespuestaDto aTiempoRespuesta(
            TiempoPedido tiempo) {

        return new TiempoPedidoRespuestaDto(
                tiempo.pedidoId(),
                tiempo.empleadoId(),
                tiempo.estadoActual(),
                tiempo.esperaSegundos(),
                tiempo.preparacionSegundos(),
                tiempo.entregaSegundos(),
                tiempo.totalSegundos(),
                tiempo.superaPromedio()
        );
    }

    private EficienciaEmpleadoRespuestaDto
            aEficienciaRespuesta(
                    EficienciaEmpleado empleado) {

        return new EficienciaEmpleadoRespuestaDto(
                empleado.posicion(),
                empleado.empleadoId(),
                empleado.pedidosGestionados(),
                empleado.promedioPreparacionSegundos()
        );
    }
}