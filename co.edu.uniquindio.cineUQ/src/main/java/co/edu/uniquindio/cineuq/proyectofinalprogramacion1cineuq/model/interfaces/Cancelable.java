package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

/**
 * Entidad cuyo estado puede pasar a CANCELADA (Funcion, Venta).
 * Cada clase que la implementa declara una excepción más específica
 * (por ejemplo, Venta lanza VentaYaCanceladaException).
 */
public interface Cancelable {

    void cancelar() throws CinemaException;

    boolean estaCancelada();

}
