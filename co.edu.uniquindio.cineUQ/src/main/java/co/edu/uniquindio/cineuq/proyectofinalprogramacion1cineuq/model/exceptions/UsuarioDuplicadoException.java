package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta registrar un empleado con un nombre de usuario o identificación ya existentes.
 */
public class UsuarioDuplicadoException extends CinemaException {

    public UsuarioDuplicadoException(String dato) {
        super("Ya existe un empleado registrado con " + dato + ".");
    }
}
