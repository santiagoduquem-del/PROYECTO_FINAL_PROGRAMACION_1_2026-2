package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta registrar un cliente con una identificación ya existente.
 */
public class ClienteDuplicadoException extends CinemaException {

    public ClienteDuplicadoException(String identificacion) {
        super("Ya existe un cliente con la identificación " + identificacion + ".");
    }
}
