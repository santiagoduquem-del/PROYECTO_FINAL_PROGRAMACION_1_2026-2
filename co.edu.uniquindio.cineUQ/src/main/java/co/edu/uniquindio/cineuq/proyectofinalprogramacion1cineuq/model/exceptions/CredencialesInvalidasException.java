package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Usuario o contraseña incorrectos al autenticar a un empleado.
 */
public class CredencialesInvalidasException extends CinemaException {

    public CredencialesInvalidasException(String usuario) {
        super("Credenciales inválidas para el usuario " + usuario + ".");
    }
}
