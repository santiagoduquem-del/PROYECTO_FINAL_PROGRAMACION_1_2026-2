package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ComboDuplicadoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ComboNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.EliminacionNoPermitidaException;

import java.util.List;

/**
 * Operaciones del administrador sobre los combos (CRUD).
 */
public interface GestionCombos {

    void registrarCombo(Combo combo) throws ComboDuplicadoException, DatoInvalidoException;

    Combo buscarCombo(String codigo) throws ComboNoEncontradoException;

    List<Combo> consultarCombos();

    List<Combo> consultarCombosDisponibles();

    void modificarCombo(String codigo, String nombre, String descripcion, double precio, boolean exclusivoVipBistro)
            throws ComboNoEncontradoException, DatoInvalidoException;

    void habilitarCombo(String codigo) throws ComboNoEncontradoException;

    void deshabilitarCombo(String codigo) throws ComboNoEncontradoException;

    void eliminarCombo(String codigo) throws ComboNoEncontradoException, EliminacionNoPermitidaException;
}
