package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Formatos en los que se puede proyectar una película.
 * La información de cada formato (descripción, "ideal para", características e imagen)
 * está escrita en su propio FXML, para editarla en Scene Builder.
 * Aquí solo se guarda el nombre que se muestra, el FXML que se abre y el archivo de su logo.
 *
 * IMPORTANTE: no cambiar los nombres en mayúsculas (DOS_D, IMAX...): las películas
 * guardadas en el archivo recuerdan su formato por ese nombre.
 */
public enum Formato {
    // Antes era "2D". El nombre DOS_D se deja igual para que las películas guardadas se sigan leyendo.
    DOS_D("Signature", "FormatoSignature.fxml", "logo_signature.png"),
    TRES_D("3D", "Formato3D.fxml", "logo_3d.png"),
    IMAX("IMAX", "FormatoIMAX.fxml", "logo_imax.png"),
    DOLBY_CINEMA("Dolby Cinema", "FormatoDolbyCinema.fxml", "logo_dolby.png"),
    CUATRO_DX("4DX", "Formato4DX.fxml", "logo_4dx.png"),
    VIP_BISTRO("VIP Bistró", "FormatoVipBistro.fxml", "logo_vip.png"),
    INFINITY_VISION("Infinity Vision", "FormatoInfinityVision.fxml", "logo_infinity.png"),
    IMAX_70MM("IMAX 70 mm", "FormatoImax70mm.fxml", "logo_imax70.png"),
    SCREENX("ScreenX", "FormatoScreenX.fxml", "logo_screenx.png"),
    PRIME("Prime", "FormatoPrime.fxml", "logo_prime.png"),
    XD("XD", "FormatoXD.fxml", "logo_xd.png");

    private final String nombre;
    private final String vista;
    private final String logo;    // imagen del logo en recursosGraficos

    Formato(String nombre, String vista, String logo) {
        this.nombre = nombre;
        this.vista = vista;
        this.logo = logo;
    }

    /** Nombre del archivo del logo dentro de recursosGraficos (ej.: "logo_imax.png"). */
    public String getLogo() {
        return logo;
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
