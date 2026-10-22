package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;

/**
 * Entidad que puede ocuparse y liberarse (Asiento).
 */
public interface Ocupable {

    boolean estaDisponible();

    void ocupar() throws AsientoOcupadoException;

    void liberar();

}
