package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Membresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Tarifa;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoEntrada;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.util.List;

/**
 * Operaciones de gestión de tarifas (valor de cada tipo de entrada)
 * y de membresías (precio y descuentos de cada plan).
 */
public interface GestionTarifas {

    List<Tarifa> consultarTarifas();

    Tarifa buscarTarifa(TipoEntrada tipoEntrada);

    void modificarTarifa(TipoEntrada tipoEntrada, double nuevoValor) throws DatoInvalidoException;

    /** Valor que paga el cliente según el tipo de entrada. */
    double calcularValorEntrada(TipoEntrada tipoEntrada);

    List<Membresia> consultarMembresias();

    Membresia buscarMembresia(PlanMembresia plan);

    void modificarMembresia(PlanMembresia plan, double precioMensual, double descuentoEntradas,
                            double descuentoCombos, String beneficioExtra) throws DatoInvalidoException;

    /** Valor de una entrada: (tarifa + valor base de la función) con el descuento del plan del cliente. */
    double calcularValorEntrada(TipoEntrada tipoEntrada, PlanMembresia plan, double valorBaseFuncion);

}
