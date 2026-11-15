package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

/**
 * Controller del menú del administrador.
 */
public class MenuAdministradorController {

    private final ModelFactoryController modelFactory;

    public MenuAdministradorController() {
        modelFactory = ModelFactoryController.getInstance();
    }

    public String obtenerNombreEmpleado() {
        return modelFactory.getEmpleadoActual() == null ? "" : modelFactory.getEmpleadoActual().getNombre();
    }

    public void cerrarSesion() {
        modelFactory.setEmpleadoActual(null);
    }
}
