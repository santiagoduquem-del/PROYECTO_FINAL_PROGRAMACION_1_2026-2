package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No existe un empleado (operador o administrador) con la identificación indicada.
 */
public class EmpleadoNoEncontradoException extends CinemaException {

    public EmpleadoNoEncontradoException(String identificacion) {
        super("No se encontró el empleado con identificación " + identificacion + ".");
    }
}
