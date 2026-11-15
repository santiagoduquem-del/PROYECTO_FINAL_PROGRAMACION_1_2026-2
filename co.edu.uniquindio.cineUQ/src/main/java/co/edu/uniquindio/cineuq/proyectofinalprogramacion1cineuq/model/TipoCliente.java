package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Tipos de cliente del cinema.
 * (Se usa NINO sin Ñ en el código para evitar problemas de codificación entre equipos.)
 */
public enum TipoCliente {
    GENERAL("General"),
    ESTUDIANTE("Estudiante"),
    NINO("Niño"),
    ADULTO_MAYOR("Adulto mayor");

    private final String descripcion;

    TipoCliente(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Así se muestra en los ComboBox de la interfaz
    @Override
    public String toString() {
        return descripcion;
    }
}
