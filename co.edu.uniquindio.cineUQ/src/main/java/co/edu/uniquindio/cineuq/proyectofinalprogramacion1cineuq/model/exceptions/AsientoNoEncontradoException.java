package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No existe un asiento con el código indicado en la función.
 */
public class AsientoNoEncontradoException extends CinemaException {

    public AsientoNoEncontradoException(String codigoAsiento) {
        super("No se encontró el asiento " + codigoAsiento + ".");
    }
}
