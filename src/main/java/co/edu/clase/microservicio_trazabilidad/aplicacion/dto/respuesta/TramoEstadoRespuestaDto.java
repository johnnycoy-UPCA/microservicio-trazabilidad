package co.edu.clase.microservicio_trazabilidad.aplicacion.dto.respuesta;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.EstadoPedido;

import java.time.Instant;

public class TramoEstadoRespuestaDto {

    private EstadoPedido estado;
    private Instant desde;
    private Instant hasta;
    private Long duracionSegundos;
    private Long empleadoId;
    private boolean enCurso;

    public TramoEstadoRespuestaDto() {
    }

    public TramoEstadoRespuestaDto(
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