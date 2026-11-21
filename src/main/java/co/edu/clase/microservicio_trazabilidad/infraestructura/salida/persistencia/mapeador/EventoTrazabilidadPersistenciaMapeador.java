package co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.mapeador;

import co.edu.clase.microservicio_trazabilidad.dominio.modelo.EventoTrazabilidad;
import co.edu.clase.microservicio_trazabilidad.infraestructura.salida.persistencia.documento.EventoTrazabilidadDocumento;
import org.springframework.stereotype.Component;

@Component
public class EventoTrazabilidadPersistenciaMapeador {

    public EventoTrazabilidadDocumento aDocumento(
            EventoTrazabilidad modelo) {

        if (modelo == null) {
            return null;
        }

        EventoTrazabilidadDocumento documento =
                new EventoTrazabilidadDocumento();

        documento.setId(modelo.getId());
        documento.setPedidoId(modelo.getPedidoId());
        documento.setClienteId(modelo.getClienteId());
        documento.setRestauranteId(
                modelo.getRestauranteId()
        );
        documento.setEmpleadoId(
                modelo.getEmpleadoId()
        );
        documento.setEstadoAnterior(
                modelo.getEstadoAnterior()
        );
        documento.setEstadoNuevo(
                modelo.getEstadoNuevo()
        );
        documento.setFechaEvento(
                modelo.getFechaEvento()
        );

        return documento;
    }

    public EventoTrazabilidad aModelo(
            EventoTrazabilidadDocumento documento) {

        if (documento == null) {
            return null;
        }

        return new EventoTrazabilidad(
                documento.getId(),
                documento.getPedidoId(),
                documento.getClienteId(),
                documento.getRestauranteId(),
                documento.getEmpleadoId(),
                documento.getEstadoAnterior(),
                documento.getEstadoNuevo(),
                documento.getFechaEvento()
        );
    }
}