package co.edu.clase.microservicio_trazabilidad.aplicacion.handler;

import co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta.TrazabilidadPedidoRespuestaDto;
import co.edu.clase.microservicio_trazabilidad.aplicacion.mapeador.TrazabilidadPedidoDtoMapeador;
import co.edu.clase.microservicio_trazabilidad.dominio.api.ConsultarTrazabilidadServicioPuerto;
import co.edu.clase.microservicio_trazabilidad.dominio.modelo.TrazabilidadPedido;
import org.springframework.stereotype.Component;

@Component
public class ConsultarTrazabilidadPedidoHandler {

    private final ConsultarTrazabilidadServicioPuerto
            consultarTrazabilidadServicioPuerto;

    private final TrazabilidadPedidoDtoMapeador
            trazabilidadPedidoDtoMapeador;

    public ConsultarTrazabilidadPedidoHandler(
            ConsultarTrazabilidadServicioPuerto
                    consultarTrazabilidadServicioPuerto,
            TrazabilidadPedidoDtoMapeador
                    trazabilidadPedidoDtoMapeador) {

        this.consultarTrazabilidadServicioPuerto =
                consultarTrazabilidadServicioPuerto;

        this.trazabilidadPedidoDtoMapeador =
                trazabilidadPedidoDtoMapeador;
    }

    public TrazabilidadPedidoRespuestaDto ejecutar(
            Long pedidoId,
            Long clienteId) {

        TrazabilidadPedido trazabilidad =
                consultarTrazabilidadServicioPuerto
                        .consultarPorPedido(
                                pedidoId,
                                clienteId
                        );

        return trazabilidadPedidoDtoMapeador
                .aRespuesta(trazabilidad);
    }
}