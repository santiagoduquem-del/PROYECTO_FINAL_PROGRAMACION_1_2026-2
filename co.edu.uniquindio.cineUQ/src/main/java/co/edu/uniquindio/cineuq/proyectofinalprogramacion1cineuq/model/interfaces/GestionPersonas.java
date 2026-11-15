package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Empleado;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoCliente;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;

import java.util.List;

/**
 * Operaciones sobre clientes y empleados (operadores y administradores).
 */
public interface GestionPersonas {

    void registrarCliente(Cliente cliente) throws ClienteDuplicadoException, DatoInvalidoException;

    /** Revisa identificación, nombre, teléfono, correo y tipo de cliente. */
    void validarDatosCliente(Cliente cliente) throws DatoInvalidoException;

    Cliente buscarCliente(String identificacion) throws ClienteNoEncontradoException;

    List<Cliente> consultarClientes();

    void modificarCliente(String identificacion, String nombre, String telefono, String correo,
                          TipoCliente tipoCliente, PlanMembresia plan)
            throws ClienteNoEncontradoException, DatoInvalidoException;

    /** Elimina el cliente solo si no tiene compras. */
    void eliminarCliente(String identificacion) throws ClienteNoEncontradoException, EliminacionNoPermitidaException;

    void registrarEmpleado(Empleado empleado) throws UsuarioDuplicadoException, DatoInvalidoException;

    /**
     * Devuelve el empleado (Operador o Administrador) dueño de esas credenciales.
     * La interfaz gráfica usa instanceof para saber qué menú abrir.
     */
    Empleado autenticarEmpleado(String usuario, String contrasena) throws CredencialesInvalidasException;

    List<Empleado> consultarEmpleados();

    Empleado buscarEmpleado(String identificacion) throws EmpleadoNoEncontradoException;

    void modificarEmpleado(String identificacion, String nombre, String usuario, String contrasena)
            throws EmpleadoNoEncontradoException, UsuarioDuplicadoException, DatoInvalidoException;

    /** No se puede eliminar a sí mismo, al último administrador ni a un operador con ventas. */
    void eliminarEmpleado(String identificacion, Empleado empleadoActual)
            throws EmpleadoNoEncontradoException, EliminacionNoPermitidaException;

}
