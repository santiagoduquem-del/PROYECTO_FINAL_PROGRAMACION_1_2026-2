package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No existe una función con el código indicado.
 */
public class FuncionNoEncontradaException extends CinemaException {

    public FuncionNoEncontradaException(String codigoFuncion) {
        super("No se encontró la función " + codigoFuncion + ".");
    }
}
