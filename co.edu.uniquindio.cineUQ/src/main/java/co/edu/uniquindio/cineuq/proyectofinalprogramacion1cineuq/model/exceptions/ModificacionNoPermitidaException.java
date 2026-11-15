package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta modificar algo que ya no se puede cambiar (por ejemplo, una función que ya tiene ventas).
 * El mensaje sugiere qué hacer en su lugar.
 */
public class ModificacionNoPermitidaException extends CinemaException {

    public ModificacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
