package co.edu.clase.microservicio_trazabilidad.dominio.modelo;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Instant;

public class TramoEstado {

    private final EstadoPedido estado;
    private final Instant desde;
    private final Instant hasta;
    private final Long duracionSegundos;
    private final Long empleadoId;
    private final boolean enCurso;

    public TramoEstado(
            EstadoPedido estado,
            Instant desde,
            Instant hasta,
            Long duracionSegundos,
            Long empleadoId,
            boolean enCurso) {

        this.estado = estado;
        this.desde = desde;
        this.hasta = hasta;
        this.duracionSegundos = duracionSegundos;
        this.empleadoId = empleadoId;
        this.enCurso = enCurso;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Instant getDesde() {
        return desde;
    }

    public Instant getHasta() {
        return hasta;
    }

    public Long getDuracionSegundos() {
        return duracionSegundos;
    }

    public Long getEmpleadoId() {
        return empleadoId;
    }

    public boolean isEnCurso() {
        return enCurso;
    }
}