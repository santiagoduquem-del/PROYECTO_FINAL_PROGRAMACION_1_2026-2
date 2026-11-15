package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Administrador;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Empleado;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;

/**
 * Controller de la barra superior que comparten las pantallas
 * (volver atrás, dolby, Home, más formatos e IMAX).
 */
public class BarraSuperiorController {

    private final ModelFactoryController modelFactory;

    public BarraSuperiorController() {
        modelFactory = ModelFactoryController.getInstance();
    }

    /** El menú principal depende de quién inició sesión. */
    public String obtenerMenuPrincipal() {
        Empleado empleado = modelFactory.getEmpleadoActual();
        if (empleado == null) {
            return "Login.fxml";
        }
        if (empleado instanceof Administrador) {
            return "MenuAdministrador.fxml";
        }
        return "MenuOperador.fxml";
    }

    /** Guarda el formato que se quiere ver (lo lee la pantalla DetalleFormato). */
    public void seleccionarFormato(Formato formato) {
        modelFactory.setFormatoSeleccionado(formato);
    }
}
