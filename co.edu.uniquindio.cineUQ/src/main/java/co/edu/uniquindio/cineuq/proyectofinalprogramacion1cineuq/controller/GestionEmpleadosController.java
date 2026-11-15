package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Administrador;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Empleado;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Operador;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.util.List;

/**
 * Controller de la pantalla Gestionar empleados (administrador).
 * CRUD de operadores y administradores.
 */
public class GestionEmpleadosController {

    public static final String ROL_OPERADOR = "Operador";
    public static final String ROL_ADMINISTRADOR = "Administrador";

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public GestionEmpleadosController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    public List<Empleado> obtenerEmpleados() {
        return cine.consultarEmpleados();
    }

    public String obtenerRol(Empleado empleado) {
        return (empleado instanceof Administrador) ? ROL_ADMINISTRADOR : ROL_OPERADOR;
    }

    /** Crea un operador o un administrador, según el rol elegido. */
    public void registrarEmpleado(String identificacion, String nombre, String usuario, String contrasena,
                                  String rol) throws CinemaException {
        if (rol == null) {
            throw new DatoInvalidoException("Debe seleccionar el rol.");
        }
        Empleado nuevo;
        if (rol.equals(ROL_ADMINISTRADOR)) {
            nuevo = new Administrador(identificacion.trim(), nombre.trim(), usuario.trim(), contrasena);
        } else {
            nuevo = new Operador(identificacion.trim(), nombre.trim(), usuario.trim(), contrasena);
        }
        cine.registrarEmpleado(nuevo);
        modelFactory.guardar();   // persistencia
    }

    /** Cambia nombre, usuario y contraseña. Si la contraseña se deja vacía, no se cambia. */
    public void modificarEmpleado(String identificacion, String nombre, String usuario, String contrasena)
            throws CinemaException {
        cine.modificarEmpleado(identificacion.trim(), nombre, usuario, contrasena);
        modelFactory.guardar();   // persistencia
    }

    /** Elimina el empleado (no se puede a sí mismo, al último administrador ni a un operador con ventas). */
    public void eliminarEmpleado(String identificacion) throws CinemaException {
        cine.eliminarEmpleado(identificacion.trim(), modelFactory.getEmpleadoActual());
        modelFactory.guardar();   // persistencia
    }

    /** Cantidad de ventas que ha hecho el empleado (solo los operadores venden). */
    public int contarVentas(Empleado empleado) {
        if (empleado instanceof Operador) {
            return ((Operador) empleado).getListVentas().size();
        }
        return 0;
    }
}
