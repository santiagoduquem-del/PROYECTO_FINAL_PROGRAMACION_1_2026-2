package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoCliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ClienteDuplicadoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ClienteNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

/**
 * Controller de la pantalla Sign Up (paso 1 del registro de un cliente).
 * Valida los datos y los guarda mientras el cliente elige su plan en la siguiente pantalla.
 */
public class SignUpController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public SignUpController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    public void prepararRegistro(String nombre, String identificacion, String telefono, String correo,
                                 TipoCliente tipoCliente) throws CinemaException {
        Cliente cliente = new Cliente(identificacion.trim(), nombre.trim(), telefono.trim(), correo.trim(), tipoCliente);

        // Se revisan los datos desde ya (en este paso), no hasta elegir el plan
        cine.validarDatosCliente(cliente);

        // Se revisa también que la identificación no esté registrada
        try {
            cine.buscarCliente(identificacion.trim());
            throw new ClienteDuplicadoException(identificacion.trim());
        } catch (ClienteNoEncontradoException e) {
            // No existe: se puede continuar
        }

        modelFactory.setClienteEnRegistro(cliente);
    }
}
