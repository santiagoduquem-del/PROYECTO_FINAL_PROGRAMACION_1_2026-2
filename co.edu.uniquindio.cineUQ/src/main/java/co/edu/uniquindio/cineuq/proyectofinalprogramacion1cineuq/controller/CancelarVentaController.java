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

    /** Cancela la venta y devuelve el mensaje del reembolso para mostrarlo. */
    public String cancelarVenta(String codigoVenta) throws CinemaException {
        if (codigoVenta == null || codigoVenta.trim().isEmpty()) {
            throw new DatoInvalidoException("Escriba el código de la venta o selecciónela en la tabla.");
        }
        String codigo = codigoVenta.trim().toUpperCase();
        cine.cancelarVenta(codigo);
        ModelFactoryController.getInstance().guardar();   // persistencia

        Venta venta = cine.buscarVenta(codigo);
        String mensaje = "La venta se canceló y el asiento quedó disponible.";
        if (venta.getPago() != null) {
            mensaje += "\nSe reembolsaron " + Conversor.aPesos(venta.getValorPagado())
                    + " a la tarjeta " + venta.getPago().describir() + ".";
        }
        return mensaje;
    }

    public List<Venta> obtenerVentas() {
        return cine.consultarVentas();
    }
}
