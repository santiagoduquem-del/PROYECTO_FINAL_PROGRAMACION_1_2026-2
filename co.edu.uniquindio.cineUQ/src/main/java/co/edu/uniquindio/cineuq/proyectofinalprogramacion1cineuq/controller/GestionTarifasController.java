package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Tarifa;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoEntrada;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.util.List;

/**
 * Controller de la pantalla Gestionar tarifas (administrador).
 */
public class GestionTarifasController {

    private final Cine cine;

    public GestionTarifasController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public List<Tarifa> obtenerTarifas() {
        return cine.consultarTarifas();
    }

    public void modificarTarifa(TipoEntrada tipoEntrada, String valorTexto) throws CinemaException {
        double valor = Conversor.aDecimal(valorTexto, "valor");
        cine.modificarTarifa(tipoEntrada, valor);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }
}
