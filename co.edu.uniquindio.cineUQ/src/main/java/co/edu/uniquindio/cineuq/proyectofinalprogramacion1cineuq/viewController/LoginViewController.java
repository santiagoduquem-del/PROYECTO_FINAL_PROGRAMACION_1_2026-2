package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.LoginController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Empleado;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CredencialesInvalidasException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private Button btnIngresar;
    @FXML private Button btnRegistrarse;

    private final LoginController loginController = new LoginController();

    // Se ejecuta al pulsar "Login" o al presionar Enter en la contraseña.
    @FXML
    private void onIngresar() {
        try {
            Empleado empleado = loginController.iniciarSesion(txtUsuario.getText(), txtClave.getText());

            if (loginController.esAdministrador(empleado)) {
                Navegador.iniciarEn(btnIngresar, "MenuAdministrador.fxml");
            } else {
                Navegador.iniciarEn(btnIngresar, "MenuOperador.fxml");
            }
        } catch (CredencialesInvalidasException e) {
            Alertas.mostrarError("Error de acceso", e.getMessage());
            txtClave.clear();
        }
    }

    @FXML
    private void onRegistrarse() {
        Navegador.irA(btnRegistrarse, "SignUp.fxml");
    }
}
