package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No existe un cliente con la identificación indicada.
 */
public class ClienteNoEncontradoException extends CinemaException {

    public ClienteNoEncontradoException(String identificacion) {
        super("No se encontró el cliente con identificación " + identificacion + ".");
    }
}
