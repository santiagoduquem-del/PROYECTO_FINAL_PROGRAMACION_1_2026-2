package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Operaciones de programación y consulta de funciones.
 */
public interface GestionFunciones {

    Funcion programarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha,
                             LocalTime hora, double valorBase)
            throws PeliculaNoEncontradaException, SalaNoEncontradaException, SalaNoDisponibleException;

    Funcion buscarFuncion(String codigo) throws FuncionNoEncontradaException;

    List<Funcion> consultarFunciones();

    List<Funcion> consultarFuncionesPorFecha(LocalDate fecha);

    List<Asiento> consultarAsientos(String codigoFuncion) throws FuncionNoEncontradaException;

}
