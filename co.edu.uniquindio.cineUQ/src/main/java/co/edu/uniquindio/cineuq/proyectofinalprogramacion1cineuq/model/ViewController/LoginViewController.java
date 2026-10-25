package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtClave;
    @FXML private CheckBox chkRecordar;
    @FXML private Hyperlink lnkOlvide;
    @FXML private Button btnLogin;
    @FXML private Button btnRegistrarse;
    @FXML private Button btnIngresarLogin;   // NUEVO: botón Login del diseño

    @FXML
    private void onLogin() {
        // TODO: conectar con la lógica de negocio
        System.out.println("Login: " + txtUsuario.getText());
    }

    // NUEVO: Login -> Menú Operador (con fundido)
    @FXML
    private void onIngresar() {
        // TODO: aquí irá la validación con la lógica de negocio antes de navegar
        Navegador.irA(btnIngresarLogin, "MenuOperador.fxml");
    }

    @FXML
    private void onOlvide() {
        // TODO: recuperar contraseña
    }

    // Sign Up -> ventana SignUp (con fundido)
    @FXML
    private void onRegistrarse() {
        Navegador.irA(btnRegistrarse, "SignUp.fxml");
    }
}