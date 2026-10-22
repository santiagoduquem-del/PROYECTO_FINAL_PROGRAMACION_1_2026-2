package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;


import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Reportes mínimos solicitados en el enunciado.
 */
public interface Reportes {

    int calcularTotalEntradasVendidas();

    double calcularTotalIngresos();

    /** Devuelve la película con más entradas vendidas, o null si aún no hay ventas. */
    Pelicula obtenerPeliculaMasVendida();

    double obtenerOcupacionFuncion(String codigoFuncion) throws FuncionNoEncontradaException;

    List<Funcion> obtenerFuncionesProgramadas(LocalDate fecha);

}
