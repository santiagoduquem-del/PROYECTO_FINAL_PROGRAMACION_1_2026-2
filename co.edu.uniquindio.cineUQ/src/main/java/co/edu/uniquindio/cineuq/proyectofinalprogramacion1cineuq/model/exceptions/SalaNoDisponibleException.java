package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta programar una función en una sala fuera de servicio.
 */
public class SalaNoDisponibleException extends CinemaException {

    public SalaNoDisponibleException(String codigoSala) {
        super("La sala " + codigoSala + " se encuentra fuera de servicio.");
    }
}
