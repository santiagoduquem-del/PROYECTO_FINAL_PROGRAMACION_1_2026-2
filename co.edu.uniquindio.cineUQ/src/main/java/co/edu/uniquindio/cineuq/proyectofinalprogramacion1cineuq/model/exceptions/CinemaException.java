package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Excepción base del dominio CINEMAUQ. Todas las excepciones propias extienden de ella,
 * lo que permite capturarlas de forma general cuando sea conveniente (por ejemplo en la GUI).
 */
public class CinemaException extends Exception {

    public CinemaException(String mensaje) {
        super(mensaje);
    }

    public CinemaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
