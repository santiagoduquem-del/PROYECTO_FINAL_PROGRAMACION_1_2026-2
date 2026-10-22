package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import java.util.List;

/**
 * Operaciones de administración de salas.
 */
public interface GestionSalas {

    void registrarSala(Sala sala) throws SalaDuplicadaException;

    Sala buscarSala(String codigo) throws SalaNoEncontradaException;

    List<Sala> consultarSalas();

    void modificarSala(String codigo, String nombre, int capacidad) throws SalaNoEncontradaException;

    void deshabilitarSala(String codigo) throws SalaNoEncontradaException;

}
