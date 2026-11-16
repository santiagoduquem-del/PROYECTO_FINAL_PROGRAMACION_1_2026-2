package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/** Tipo de tarjeta con la que se paga la compra. */
public enum TipoTarjeta {
    DEBITO("Débito"),
    CREDITO("Crédito");

    private final String nombre;

    TipoTarjeta(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
