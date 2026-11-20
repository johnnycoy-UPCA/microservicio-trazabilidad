package co.edu.clase.microservicio_trazabilidad.infraestructura.entrada.rest.manejador;

import java.time.Instant;
import java.util.Map;

public class ErrorRespuesta {

    private Instant fechaHora;
    private int estado;
    private String codigo;
    private String mensaje;
    private String ruta;
    private Map<String, Object> detalles;

    public ErrorRespuesta() {
    }

    public ErrorRespuesta(
            Instant fechaHora,
            int estado,
            String codigo,
            String mensaje,
            String ruta,
            Map<String, Object> detalles) {

        this.fechaHora = fechaHora;
        this.estado = estado;
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.ruta = ruta;
        this.detalles = detalles;
    }

    public Instant getFechaHora() {
        return fechaHora;
    }

    public int getEstado() {
        return estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getRuta() {
        return ruta;
    }

    public Map<String, Object> getDetalles() {
        return detalles;
    }
}