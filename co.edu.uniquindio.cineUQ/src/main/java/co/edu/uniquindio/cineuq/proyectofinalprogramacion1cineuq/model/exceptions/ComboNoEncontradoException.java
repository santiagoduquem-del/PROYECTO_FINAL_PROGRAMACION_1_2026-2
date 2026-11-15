package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No existe un combo con el código indicado.
 */
public class ComboNoEncontradoException extends CinemaException {

    public ComboNoEncontradoException(String codigoCombo) {
        super("No se encontró el combo " + codigoCombo + ".");
    }
}
