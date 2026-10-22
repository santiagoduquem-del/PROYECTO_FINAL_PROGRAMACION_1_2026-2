package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;

/**
 * Operaciones de venta y cancelación de entradas.
 */
public interface GestionVentas {

    Venta venderEntrada(String codigoFuncion, String codigoAsiento, Cliente cliente,
                        TipoEntrada tipoEntrada, Operador operador)
            throws FuncionNoEncontradaException, AsientoNoEncontradoException,
                   AsientoOcupadoException, SinAsientosDisponiblesException;

    void cancelarVenta(String codigoVenta) throws VentaNoEncontradaException, VentaYaCanceladaException;

    Venta buscarVenta(String codigoVenta) throws VentaNoEncontradaException;

}
