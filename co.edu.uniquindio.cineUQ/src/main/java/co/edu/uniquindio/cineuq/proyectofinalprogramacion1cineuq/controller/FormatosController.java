package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;

/**
 * Controller de la pantalla "Más formatos".
 */
public class FormatosController {

    private final ModelFactoryController modelFactory;

    public FormatosController() {
        modelFactory = ModelFactoryController.getInstance();
    }

    /**
     * Guarda el formato elegido para que su pantalla muestre las películas y funciones.
     * Recibe el nombre del formato tal como está en el userData de la tarjeta (ej.: "TRES_D").
     */
    public Formato seleccionarFormato(String nombreFormato) {
        Formato formato = Formato.valueOf(nombreFormato);
        modelFactory.setFormatoSeleccionado(formato);
        return formato;
    }
}
