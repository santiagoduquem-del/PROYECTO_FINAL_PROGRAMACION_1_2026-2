package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Asiento;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Idioma;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CancelacionNoPermitidaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ConflictoHorarioException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
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
                             LocalTime hora, double valorBase, Formato formato, Idioma idioma)
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

    /** Modifica película, sala, fecha, hora, valor base, formato e idioma, solo si la función no tiene ventas. */
    Funcion modificarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha,
                             LocalTime hora, double valorBase, Formato formato, Idioma idioma) throws CinemaException;


    /** Cancela la función (queda en el historial). Solo si no tiene entradas activas. */
    void cancelarFuncion(String codigo)
            throws FuncionNoEncontradaException, FuncionNoDisponibleException, CancelacionNoPermitidaException;

}
