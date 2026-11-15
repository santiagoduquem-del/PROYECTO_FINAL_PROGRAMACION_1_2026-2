package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.SignUpController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoCliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Pantalla Sign Up: paso 1 del registro de un CLIENTE (sus datos).
 * El paso 2 es elegir el plan de membresía en la pantalla Planes.
 */
public class SignUpViewController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<TipoCliente> cbTipoCliente;

    private final SignUpController signUpController = new SignUpController();

    @FXML
    public void initialize() {
        cbTipoCliente.getItems().setAll(TipoCliente.values());
    }

    @FXML
    private void onContinuar() {
        try {
            signUpController.prepararRegistro(txtNombre.getText(), txtIdentificacion.getText(),
                    txtTelefono.getText(), txtCorreo.getText(), cbTipoCliente.getValue());
            Navegador.irA(txtNombre, "Planes.fxml");
        } catch (CinemaException e) {
            Alertas.mostrarError("Registro", e.getMessage());
        }
    }

    // Lo usan el botón "Log in" y la flecha de volver
    @FXML
    private void onVolverLogin() {
        Navegador.irA(txtNombre, "Login.fxml");
    }
}
