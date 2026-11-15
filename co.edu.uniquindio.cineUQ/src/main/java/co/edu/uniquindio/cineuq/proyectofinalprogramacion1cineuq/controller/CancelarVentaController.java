package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Venta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.util.List;

/**
 * Controller de la pantalla Cancelar venta (operador).
 */
public class CancelarVentaController {

    private final Cine cine;

    public CancelarVentaController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public void cancelarVenta(String codigoVenta) throws CinemaException {
        if (codigoVenta == null || codigoVenta.trim().isEmpty()) {
            throw new DatoInvalidoException("Escriba el código de la venta o selecciónela en la tabla.");
        }
        cine.cancelarVenta(codigoVenta.trim().toUpperCase());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Venta> obtenerVentas() {
        return cine.consultarVentas();
    }
}
