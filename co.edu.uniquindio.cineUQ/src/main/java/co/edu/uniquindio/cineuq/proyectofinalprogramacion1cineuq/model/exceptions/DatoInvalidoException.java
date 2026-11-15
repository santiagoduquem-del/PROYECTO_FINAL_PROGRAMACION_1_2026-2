package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Un dato ingresado no es válido (campo vacío, número negativo, etc.).
 * El mensaje explica cuál es el problema.
 */
public class DatoInvalidoException extends CinemaException {

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
