package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;

/**
 * Entidad cuyo estado puede pasar a CANCELADA (Funcion, Venta).
 */
public interface Cancelable {

    void cancelar() throws CinemaException;

    boolean estaCancelada();

}
