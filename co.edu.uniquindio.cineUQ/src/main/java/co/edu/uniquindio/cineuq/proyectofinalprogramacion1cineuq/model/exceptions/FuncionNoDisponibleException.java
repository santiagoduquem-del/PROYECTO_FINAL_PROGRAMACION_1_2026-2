package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/**
 * Se intenta vender o cancelar sobre una función que no está PROGRAMADA
 * (ya fue cancelada o finalizada).
 */
public class FuncionNoDisponibleException extends CinemaException {

    public FuncionNoDisponibleException(String codigoFuncion) {
        super("La función " + codigoFuncion + " no está programada (está cancelada o finalizada).");
    }
}
