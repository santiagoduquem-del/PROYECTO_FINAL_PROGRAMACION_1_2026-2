package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Operador;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoEntrada;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Venta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoOcupadoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ClienteNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoDisponibleException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoEncontradaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.SinAsientosDisponiblesException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.VentaNoEncontradaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.VentaYaCanceladaException;

import java.util.List;

/**
 * Operaciones de venta y cancelación de entradas.
 */
public interface GestionVentas {

    Venta venderEntrada(String codigoFuncion, String codigoAsiento, String identificacionCliente,
                        TipoEntrada tipoEntrada, List<Combo> combos, Operador operador)
            throws FuncionNoEncontradaException, FuncionNoDisponibleException, ClienteNoEncontradoException,
                   SinAsientosDisponiblesException, AsientoNoEncontradoException, AsientoOcupadoException,
                   DatoInvalidoException;

    /** Valor de los combos con el descuento de combos del plan. */
    double calcularValorCombos(List<Combo> combos, PlanMembresia plan);

    /** Cancela la venta y libera el asiento. */
    void cancelarVenta(String codigoVenta) throws VentaNoEncontradaException, VentaYaCanceladaException;

    Venta buscarVenta(String codigoVenta) throws VentaNoEncontradaException;

    List<Venta> consultarVentas();

}
