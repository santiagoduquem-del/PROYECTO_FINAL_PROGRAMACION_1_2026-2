package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SignUpViewController {

    @FXML private Button btnVolver;
    @FXML private Button btnVolverLogin;   // NUEVO: coincide con el fx:id de Scene Builder

    @FXML
    private void onVolver() {
        Navegador.irA(btnVolverLogin, "Login.fxml");
    }

    @FXML
    private void onVolverLogin() {
        Navegador.irA(btnVolverLogin, "Login.fxml");
    }
}