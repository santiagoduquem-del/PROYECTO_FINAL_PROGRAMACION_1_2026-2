package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;

/**
 * Operaciones sobre clientes y operarios.
 */
public interface GestionPersonas {

    void registrarCliente(Cliente cliente);

    Cliente buscarCliente(String identificacion) throws ClienteNoEncontradoException;

    void registrarOperario(Operador operador);

    Operador autenticarOperario(String usuario, String contrasena) throws CredencialesInvalidasException;

}
