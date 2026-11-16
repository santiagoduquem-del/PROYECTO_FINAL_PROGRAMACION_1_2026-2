package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * No se puede cancelar algo porque todavía tiene elementos activos
 * (ej.: una función con entradas vendidas activas).
 */
public class CancelacionNoPermitidaException extends CinemaException {

    public CancelacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
