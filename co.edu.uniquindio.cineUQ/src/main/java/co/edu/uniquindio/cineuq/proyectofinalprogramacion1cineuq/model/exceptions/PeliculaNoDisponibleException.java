package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta programar una función con una película deshabilitada.
 */
public class PeliculaNoDisponibleException extends CinemaException {

    public PeliculaNoDisponibleException(String codigoPelicula) {
        super("La película " + codigoPelicula + " no está disponible.");
    }
}
