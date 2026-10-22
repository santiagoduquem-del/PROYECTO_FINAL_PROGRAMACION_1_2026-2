package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta registrar una película con un código ya existente.
 */
public class PeliculaDuplicadaException extends CinemaException {

    public PeliculaDuplicadaException(String codigoPelicula) {
        super("Ya existe una película con el código " + codigoPelicula + ".");
    }
}
