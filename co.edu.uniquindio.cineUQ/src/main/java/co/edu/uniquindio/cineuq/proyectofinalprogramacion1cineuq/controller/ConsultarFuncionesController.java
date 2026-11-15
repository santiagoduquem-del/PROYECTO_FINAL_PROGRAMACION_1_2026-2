package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;

import java.util.List;

/**
 * Controller de la pantalla Consultar funciones (operador).
 */
public class ConsultarFuncionesController {

    private final Cine cine;

    public ConsultarFuncionesController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public List<Funcion> obtenerFuncionesProgramadas() {
        return cine.consultarFuncionesProgramadas();
    }
}
