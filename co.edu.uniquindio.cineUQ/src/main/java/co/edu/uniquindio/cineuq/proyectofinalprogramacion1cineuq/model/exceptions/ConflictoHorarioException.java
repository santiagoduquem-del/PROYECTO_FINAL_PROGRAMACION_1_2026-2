package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;

/**
 * Se intenta programar una función que se cruza en horario con otra de la misma sala.
 */
public class ConflictoHorarioException extends CinemaException {

    /** Mensaje con el horario que ocupa la otra función y desde cuándo queda libre la sala. */
    public ConflictoHorarioException(String codigoSala, Funcion existente) {
        super("La sala " + codigoSala + " ya está ocupada por la función " + existente.getCodigo()
                + " (" + existente.getPelicula().getNombre() + "): de " + existente.getInicio().toLocalTime()
                + " a " + existente.getFin().toLocalTime() + ", más " + Funcion.MINUTOS_LIMPIEZA
                + " min de limpieza. La sala queda libre el " + existente.getSalaLibreDesde().toLocalDate()
                + " a las " + existente.getSalaLibreDesde().toLocalTime() + ".");
    }

    public ConflictoHorarioException(String codigoSala, String codigoFuncionExistente) {
        super("La sala " + codigoSala + " ya está ocupada a esa hora por la función "
                + codigoFuncionExistente + ".");
    }
}
