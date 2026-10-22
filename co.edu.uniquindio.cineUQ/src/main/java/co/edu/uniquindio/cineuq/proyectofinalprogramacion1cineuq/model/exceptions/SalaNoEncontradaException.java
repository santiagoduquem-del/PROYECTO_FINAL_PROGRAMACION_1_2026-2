package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No existe una sala con el código indicado.
 */
public class SalaNoEncontradaException extends CinemaException {

    public SalaNoEncontradaException(String codigoSala) {
        super("No se encontró la sala " + codigoSala + ".");
    }
}
