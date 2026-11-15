package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Administrador;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Empleado;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CredencialesInvalidasException;

/**
 * Controller de la pantalla Login.
 */
public class LoginController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public LoginController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    /** Valida las credenciales y deja guardado quién inició sesión. */
    public Empleado iniciarSesion(String usuario, String contrasena) throws CredencialesInvalidasException {
        Empleado empleado = cine.autenticarEmpleado(usuario.trim(), contrasena);
        modelFactory.setEmpleadoActual(empleado);
        return empleado;
    }

    public boolean esAdministrador(Empleado empleado) {
        return empleado instanceof Administrador;
    }
}
