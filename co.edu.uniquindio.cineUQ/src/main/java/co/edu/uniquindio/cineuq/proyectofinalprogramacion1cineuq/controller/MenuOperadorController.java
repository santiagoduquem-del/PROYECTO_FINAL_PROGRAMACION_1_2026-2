package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

/**
 * Controller del menú del operador.
 */
public class MenuOperadorController {

    private final ModelFactoryController modelFactory;

    public MenuOperadorController() {
        modelFactory = ModelFactoryController.getInstance();
    }

    public String obtenerNombreEmpleado() {
        return modelFactory.getEmpleadoActual() == null ? "" : modelFactory.getEmpleadoActual().getNombre();
    }

    /** "Consultar funciones" desde el menú: se muestran los horarios de TODAS las películas. */
    public void prepararConsultaFunciones() {
        modelFactory.setPeliculaHorarios(null);
    }

    public void cerrarSesion() {
        modelFactory.setEmpleadoActual(null);
    }
}
