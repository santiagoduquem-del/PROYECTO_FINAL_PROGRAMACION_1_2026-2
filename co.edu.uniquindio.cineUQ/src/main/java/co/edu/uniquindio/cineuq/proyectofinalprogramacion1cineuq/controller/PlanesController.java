package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Membresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

/**
 * Controller de la pantalla Planes (paso 2 del registro de un cliente).
 */
public class PlanesController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public PlanesController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    /** Nombre del cliente que se está registrando (para saludarlo en la pantalla). */
    public String obtenerNombreCliente() {
        Cliente cliente = modelFactory.getClienteEnRegistro();
        return cliente == null ? "" : cliente.getNombre();
    }

    /** Valores vigentes de un plan (los que puso el administrador), para mostrarlos en las tarjetas. */
    public Membresia obtenerMembresia(PlanMembresia plan) {
        return cine.buscarMembresia(plan);
    }

    /** Le asigna el plan al cliente y lo registra en el cine. */
    public void registrarConPlan(PlanMembresia plan) throws CinemaException {
        Cliente cliente = modelFactory.getClienteEnRegistro();
        if (cliente == null) {
            throw new DatoInvalidoException("Primero debe llenar sus datos en la pantalla de registro.");
        }
        if (plan == null) {
            throw new DatoInvalidoException("Seleccione uno de los planes.");
        }
        cliente.setPlan(plan);
        cine.registrarCliente(cliente);
        ModelFactoryController.getInstance().guardar();   // persistencia
        modelFactory.setClienteEnRegistro(null);
    }

    public void cancelarRegistro() {
        modelFactory.setClienteEnRegistro(null);
    }
}
