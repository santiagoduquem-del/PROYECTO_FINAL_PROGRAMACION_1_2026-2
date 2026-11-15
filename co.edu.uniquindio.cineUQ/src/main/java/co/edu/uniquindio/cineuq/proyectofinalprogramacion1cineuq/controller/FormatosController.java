package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller de la pantalla "Más formatos": muestra los formatos que no tienen
 * botón propio en la barra superior (dolby e IMAX sí lo tienen).
 */
public class FormatosController {

    private final ModelFactoryController modelFactory;

    public FormatosController() {
        modelFactory = ModelFactoryController.getInstance();
    }

    /** Todos los formatos menos los que ya están en la barra (Dolby Cinema e IMAX). */
    public List<Formato> obtenerFormatosRestantes() {
        List<Formato> restantes = new ArrayList<>();
        for (Formato formato : Formato.values()) {
            if (formato != Formato.IMAX && formato != Formato.DOLBY_CINEMA) {
                restantes.add(formato);
            }
        }
        return restantes;
    }

    /** Guarda el formato elegido para que la pantalla DetalleFormato muestre su información. */
    public void seleccionarFormato(Formato formato) {
        modelFactory.setFormatoSeleccionado(formato);
    }
}
