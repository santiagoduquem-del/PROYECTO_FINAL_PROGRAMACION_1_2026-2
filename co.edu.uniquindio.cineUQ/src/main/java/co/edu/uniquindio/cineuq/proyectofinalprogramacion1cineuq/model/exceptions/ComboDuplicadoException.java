package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta registrar un combo con un código ya existente.
 */
public class ComboDuplicadoException extends CinemaException {

    public ComboDuplicadoException(String codigoCombo) {
        super("Ya existe un combo con el código " + codigoCombo + ".");
    }
}
