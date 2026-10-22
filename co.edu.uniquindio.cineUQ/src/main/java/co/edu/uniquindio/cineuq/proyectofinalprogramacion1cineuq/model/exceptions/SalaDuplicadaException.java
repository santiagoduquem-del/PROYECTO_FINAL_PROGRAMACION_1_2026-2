package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta registrar una sala con un código ya existente.
 */
public class SalaDuplicadaException extends CinemaException {

    public SalaDuplicadaException(String codigoSala) {
        super("Ya existe una sala con el código " + codigoSala + ".");
    }
}
