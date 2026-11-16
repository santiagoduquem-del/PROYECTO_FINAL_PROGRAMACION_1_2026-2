package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Géneros cinematográficos.
 * Cada género tiene el nombre bonito que se muestra en pantalla ("Ciencia ficción").
 * IMPORTANTE: no cambiar los nombres en mayúsculas (CIENCIA_FICCION...): las películas
 * guardadas en el archivo recuerdan su género por ese nombre.
 */
public enum Genero {
    ACCION("Acción"),
    AVENTURA("Aventura"),
    ANIMACION("Animación"),
    CIENCIA_FICCION("Ciencia ficción"),
    COMEDIA("Comedia"),
    DOCUMENTAL("Documental"),
    DRAMA("Drama"),
    ROMANCE("Romance"),
    TERROR("Terror");

    private final String nombre;

    Genero(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    // Así se muestra en los ComboBox, tablas, tarjetas y en el detalle
    @Override
    public String toString() {
        return nombre;
    }
}
