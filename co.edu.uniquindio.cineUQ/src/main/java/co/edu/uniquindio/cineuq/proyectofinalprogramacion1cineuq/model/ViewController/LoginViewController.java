package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    @FXML private Button btnIngresarLogin;
    @FXML private Button btnIngresar;
    @FXML private Button btnRegistrarseLogin;

    @FXML
    private void onLogin() {
        // TODO: conectar con la lógica de negocio
        System.out.println("Login: " + txtUsuario.getText());
    }

    // Login: valida usuario y contraseña y abre el menú que corresponde (con fundido)
    @FXML
    private void onIngresarLogin() {
        // TODO: reemplazar estas credenciales fijas por la lógica de negocio
        String usuario = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
        String clave = txtClave.getText() == null ? "" : txtClave.getText();

        if (usuario.equals("operador") && clave.equals("operador123")) {
            Navegador.irA(btnIngresar, "MenuOperador.fxml");
        } else if (usuario.equals("administrador") && clave.equals("admin123")) {
            Navegador.irA(btnIngresar, "MenuAdministrador.fxml");
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error de acceso");
            alerta.setHeaderText(null);
            alerta.setContentText("Usuario o contraseña incorrectos.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void onIngresar() {
        onIngresarLogin();
    }

    @FXML
    private void onOlvide() {
        // TODO: recuperar contraseña
    }

    @FXML
    private void OnRegistrarseLogin() {
        Navegador.irA(btnRegistrarseLogin, "SignUp.fxml");
    }

    @FXML
    private void onRegistrarse() {
        Navegador.irA(btnRegistrarseLogin, "SignUp.fxml");
    }
}