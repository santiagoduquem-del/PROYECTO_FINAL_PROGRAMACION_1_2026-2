package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoCliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.util.List;

/**
 * Controller de la pantalla Registrar clientes (operador).
 */
public class RegistrarClienteController {

    private final Cine cine;

    public RegistrarClienteController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public void registrarCliente(String identificacion, String nombre, String telefono, String correo,
                                 TipoCliente tipoCliente, PlanMembresia plan) throws CinemaException {
        Cliente cliente = new Cliente(identificacion.trim(), nombre.trim(), telefono.trim(), correo.trim(),
                tipoCliente, plan);
        cine.registrarCliente(cliente);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Cambia los datos del cliente (la identificación no cambia). */
    public void modificarCliente(String identificacion, String nombre, String telefono, String correo,
                                 TipoCliente tipoCliente, PlanMembresia plan) throws CinemaException {
        cine.modificarCliente(identificacion.trim(), nombre, telefono, correo, tipoCliente, plan);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Elimina el cliente (solo si no tiene compras). */
    public void eliminarCliente(String identificacion) throws CinemaException {
        cine.eliminarCliente(identificacion.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Cliente> obtenerClientes() {
        return cine.consultarClientes();
    }
}
