package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Asiento;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ConflictoHorarioException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.EliminacionNoPermitidaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionDuplicadaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoDisponibleException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoEncontradaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.PeliculaNoDisponibleException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.PeliculaNoEncontradaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.SalaNoDisponibleException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.SalaNoEncontradaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Operaciones de programación y consulta de funciones.
 */
public interface GestionFunciones {

    Funcion programarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha,
                             LocalTime hora, double valorBase)
            throws FuncionDuplicadaException, PeliculaNoEncontradaException, PeliculaNoDisponibleException,
                   SalaNoEncontradaException, SalaNoDisponibleException, ConflictoHorarioException,
                   DatoInvalidoException;

    Funcion buscarFuncion(String codigo) throws FuncionNoEncontradaException;

    List<Funcion> consultarFunciones();

    /** Solo las funciones en estado PROGRAMADA. */
    List<Funcion> consultarFuncionesProgramadas();

    /** Funciones PROGRAMADAS de una película. */
    List<Funcion> consultarFuncionesProgramadas(String codigoPelicula);

    /** Todas las funciones de una fecha, sin importar su estado. */
    List<Funcion> consultarFuncionesPorFecha(LocalDate fecha);

    List<Asiento> consultarAsientos(String codigoFuncion) throws FuncionNoEncontradaException;

    /** Cambia el valor base (recargo opcional por estreno o formato especial). */
    void modificarValorBase(String codigoFuncion, double nuevoValor)
            throws FuncionNoEncontradaException, DatoInvalidoException;

    /** Modifica película, sala, fecha, hora y valor base, solo si la función no tiene ventas. */
    Funcion modificarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha,
                             LocalTime hora, double valorBase) throws CinemaException;

    /** Elimina la función solo si no tiene ventas. */
    void eliminarFuncion(String codigo) throws FuncionNoEncontradaException, EliminacionNoPermitidaException;

    /** Cancela la función y todas sus ventas activas. */
    void cancelarFuncion(String codigo) throws FuncionNoEncontradaException, FuncionNoDisponibleException;

}
