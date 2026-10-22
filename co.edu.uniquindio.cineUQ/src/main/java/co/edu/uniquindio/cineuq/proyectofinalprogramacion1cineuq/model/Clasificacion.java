package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Clasificación por edades de una película.
 */
public enum Clasificacion {
    TODOS("Todo público"),
    MAYORES_7("Mayores de 7 años"),
    MAYORES_12("Mayores de 12 años"),
    MAYORES_15("Mayores de 15 años"),
    MAYORES_18("Mayores de 18 años");

    private final String descripcion;

    Clasificacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
