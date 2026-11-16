package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Idioma en que se proyecta una función: doblada al español o en idioma original con subtítulos.
 */
public enum Idioma {
    DOBLADA("Doblada"),
    SUBTITULADA("Subtitulada");

    private final String nombre;

    Idioma(String nombre) {
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
