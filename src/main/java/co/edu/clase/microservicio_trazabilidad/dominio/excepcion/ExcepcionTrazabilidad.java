package co.edu.clase.microservicio_trazabilidad.dominio.excepcion;

import co.edu.clase.microservicio_trazabilidad.util.enumeraciones.CategoriaError;

public class ExcepcionTrazabilidad
        extends RuntimeException {

    private final String codigo;
    private final CategoriaError categoria;

    public ExcepcionTrazabilidad(
            String codigo,
            String mensaje,
            CategoriaError categoria) {

        super(mensaje);
        this.codigo = codigo;
        this.categoria = categoria;
    }

    public ExcepcionTrazabilidad(
            String codigo,
            String mensaje,
            CategoriaError categoria,
            Throwable causa) {

        super(mensaje, causa);
        this.codigo = codigo;
        this.categoria = categoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public CategoriaError getCategoria() {
        return categoria;
    }
}