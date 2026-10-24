package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LoginViewController {

    @FXML private Button btnRegistrarse;

    @FXML
    private void onRegistrarse() {
        Navegador.irA(btnRegistrarse, "SignUp.fxml");
    }
}