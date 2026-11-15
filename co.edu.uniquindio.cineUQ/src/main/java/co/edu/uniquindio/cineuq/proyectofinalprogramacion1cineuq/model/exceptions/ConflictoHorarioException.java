package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta programar una función que se cruza en horario con otra de la misma sala.
 */
public class ConflictoHorarioException extends CinemaException {

    public ConflictoHorarioException(String codigoSala, String codigoFuncionExistente) {
        super("La sala " + codigoSala + " ya está ocupada a esa hora por la función "
                + codigoFuncionExistente + ".");
    }
}
