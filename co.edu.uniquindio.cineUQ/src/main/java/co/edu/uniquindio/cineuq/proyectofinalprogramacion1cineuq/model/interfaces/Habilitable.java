package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

/**
 * Entidad que puede habilitarse o deshabilitarse (Pelicula, Sala).
 */
public interface Habilitable {

    boolean estaDisponible();

    void habilitar();

    void deshabilitar();

}
