package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.adaptador;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.dominio.spi.ConsultarTrazabilidadPersistenciaPuerto;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.documento.EventoTrazabilidadDocumento;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.mapeador.EventoTrazabilidadPersistenciaMapeador;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.repositorio.EventoTrazabilidadMongoRepositorio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarTrazabilidadPersistenciaAdaptador
        implements ConsultarTrazabilidadPersistenciaPuerto {

    private final EventoTrazabilidadMongoRepositorio repositorio;
    private final EventoTrazabilidadPersistenciaMapeador mapeador;

    public ConsultarTrazabilidadPersistenciaAdaptador(
            EventoTrazabilidadMongoRepositorio repositorio,
            EventoTrazabilidadPersistenciaMapeador mapeador) {
        this.repositorio = repositorio;
        this.mapeador = mapeador;
    }

    @Override
    public List<EventoTrazabilidad> consultarPorPedidoYCliente(
            Long pedidoId,
            Long clienteId) {

        List<EventoTrazabilidadDocumento> documentos =
                repositorio
                        .findByPedidoIdAndClienteIdOrderByFechaEventoAsc(
                                pedidoId,
                                clienteId
                        );

        return documentos.stream()
                .map(mapeador::aModelo)
                .toList();
    }
}