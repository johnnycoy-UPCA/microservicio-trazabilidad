package co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TramoEstadoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TrazabilidadPedidoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TramoEstado;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TrazabilidadPedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrazabilidadPedidoDtoMapeador {

    public TrazabilidadPedidoRespuestaDto aRespuesta(
            TrazabilidadPedido trazabilidad) {

        List<TramoEstadoRespuestaDto> tramos =
                trazabilidad.getTramos()
                        .stream()
                        .map(this::aTramoRespuesta)
                        .toList();

        return new TrazabilidadPedidoRespuestaDto(
                trazabilidad.getPedidoId(),
                trazabilidad.getEstadoActual(),
                trazabilidad.getTiempoTotalSegundos(),
                tramos
        );
    }

    private TramoEstadoRespuestaDto aTramoRespuesta(
            TramoEstado tramo) {

        return new TramoEstadoRespuestaDto(
                tramo.getEstado(),
                tramo.getDesde(),
                tramo.getHasta(),
                tramo.getDuracionSegundos(),
                tramo.getEmpleadoId(),
                tramo.isEnCurso()
        );
    }
}