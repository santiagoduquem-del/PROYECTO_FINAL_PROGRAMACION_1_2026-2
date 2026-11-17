package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;

import java.util.List;

/**
 * Controller de la ventana Combos. Los combos elegidos se guardan en ModelFactoryController
 * para que la pantalla de venta los pueda leer cuando se cierre esta ventana.
 */
public class CombosController {

    private final List<Combo> combosSeleccionados;

    public CombosController() {
        combosSeleccionados = ModelFactoryController.getInstance().getCombosSeleccionados();
    }

    /** Combos que se pueden vender en la función elegida (los de VIP Bistró solo en esa sala). */
    public List<Combo> obtenerCombosDisponibles() {
        ModelFactoryController modelFactory = ModelFactoryController.getInstance();
        return modelFactory.getCine().consultarCombosParaFuncion(modelFactory.getFuncionEnCombos());
    }

    public void agregarCombo(Combo combo) {
        combosSeleccionados.add(combo);
    }

    public void quitarCombo(Combo combo) {
        combosSeleccionados.remove(combo);   // quita una sola unidad
    }

    public void quitarTodos() {
        combosSeleccionados.clear();
    }

    public int contarCombo(Combo combo) {
        int cantidad = 0;
        for (Combo c : combosSeleccionados) {
            if (c == combo) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /** Precio de los combos elegidos, sin descuentos (el descuento del plan se aplica al vender). */
    public double calcularSubtotal() {
        double total = 0;
        for (Combo combo : combosSeleccionados) {
            total += combo.getPrecio();
        }
        return total;
    }
}
