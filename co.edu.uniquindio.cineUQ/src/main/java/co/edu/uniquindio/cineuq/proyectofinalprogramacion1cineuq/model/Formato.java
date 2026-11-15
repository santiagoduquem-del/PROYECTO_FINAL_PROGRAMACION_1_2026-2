package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Formatos en los que se puede proyectar una película.
 * La información de cada formato (descripción, "ideal para", características e imagen)
 * está escrita en su propio FXML, para editarla en Scene Builder.
 * Aquí solo se guarda el nombre que se muestra y el FXML que se abre.
 *
 * IMPORTANTE: no cambiar los nombres en mayúsculas (DOS_D, IMAX...): las películas
 * guardadas en el archivo recuerdan su formato por ese nombre.
 */
public enum Formato {
    DOS_D("2D", "Formato2D.fxml"),
    TRES_D("3D", "Formato3D.fxml"),
    IMAX("IMAX", "FormatoIMAX.fxml"),
    DOLBY_CINEMA("Dolby Cinema", "FormatoDolbyCinema.fxml"),
    CUATRO_DX("4DX", "Formato4DX.fxml"),
    VIP_BISTRO("VIP Bistró", "FormatoVipBistro.fxml");

    private final String nombre;
    private final String vista;

    Formato(String nombre, String vista) {
        this.nombre = nombre;
        this.vista = vista;
    }

    public String getNombre() {
        return nombre;
    }

    /** Nombre del FXML con la información del formato. */
    public String getVista() {
        return vista;
    }

    // Así se muestra en los ComboBox y tablas
    @Override
    public String toString() {
        return nombre;
    }
}
