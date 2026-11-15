package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta eliminar algo que tiene historial (por ejemplo, una película con funciones).
 * Borrarlo dejaría incompletos las ventas y los reportes; el mensaje sugiere qué hacer en su lugar.
 */
public class EliminacionNoPermitidaException extends CinemaException {

    public EliminacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
