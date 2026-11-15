package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Tipos de entrada. Cada uno tiene un valor por defecto (valores ilustrativos del enunciado).
 * El valor vigente se guarda en la clase Tarifa, que el administrador puede modificar.
 */
public enum TipoEntrada {
    GENERAL("General", 15000),
    ESTUDIANTE("Estudiante", 12000),
    NINO("Niño", 10000),
    ADULTO_MAYOR("Adulto mayor", 11000);

    private final String descripcion;
    private final double valorPorDefecto;

    TipoEntrada(String descripcion, double valorPorDefecto) {
        this.descripcion = descripcion;
        this.valorPorDefecto = valorPorDefecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getValorPorDefecto() {
        return valorPorDefecto;
    }

    // Así se muestra en los ComboBox de la interfaz
    @Override
    public String toString() {
        return descripcion;
    }
}
