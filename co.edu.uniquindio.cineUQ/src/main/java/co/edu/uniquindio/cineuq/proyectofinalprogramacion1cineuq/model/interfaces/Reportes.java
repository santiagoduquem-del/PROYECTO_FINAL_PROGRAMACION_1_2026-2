package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoEncontradaException;

import java.time.LocalDate;
import java.util.List;

/**
 * Reportes mínimos solicitados en el enunciado.
 * Las ventas canceladas NO se tienen en cuenta.
 */
public interface Reportes {

    int calcularTotalEntradasVendidas();

    double calcularTotalIngresos();

    /** Devuelve la película con más entradas vendidas, o null si aún no hay ventas. */
    Pelicula obtenerPeliculaMasVendida();

    /** Porcentaje de ocupación de la función, de 0 a 100. */
    double obtenerOcupacionFuncion(String codigoFuncion) throws FuncionNoEncontradaException;

    /** Solo las funciones en estado PROGRAMADA de esa fecha. */
    List<Funcion> obtenerFuncionesProgramadas(LocalDate fecha);

}
