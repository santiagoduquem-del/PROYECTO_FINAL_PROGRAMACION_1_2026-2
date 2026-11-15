package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Membresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.util.List;

/**
 * Controller de la pantalla Gestionar membresías (administrador).
 */
public class GestionMembresiasController {

    private final Cine cine;

    public GestionMembresiasController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public List<Membresia> obtenerMembresias() {
        return cine.consultarMembresias();
    }

    /**
     * Los descuentos se escriben en la pantalla como porcentaje (ej.: 25)
     * y aquí se convierten a decimal (0.25) para el modelo.
     */
    public void modificarMembresia(PlanMembresia plan, String precioTexto, String descuentoEntradasTexto,
                                   String descuentoCombosTexto, String beneficioExtra) throws CinemaException {
        double precio = Conversor.aDecimal(precioTexto, "precio mensual");
        double descuentoEntradas = Conversor.aDecimal(descuentoEntradasTexto, "descuento en entradas") / 100;
        double descuentoCombos = Conversor.aDecimal(descuentoCombosTexto, "descuento en combos") / 100;
        cine.modificarMembresia(plan, precio, descuentoEntradas, descuentoCombos, beneficioExtra);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Descuento como texto de porcentaje, ej.: 0.25 -> "25 %". */
    public String aPorcentaje(double descuento) {
        return Math.round(descuento * 100) + " %";
    }
}
