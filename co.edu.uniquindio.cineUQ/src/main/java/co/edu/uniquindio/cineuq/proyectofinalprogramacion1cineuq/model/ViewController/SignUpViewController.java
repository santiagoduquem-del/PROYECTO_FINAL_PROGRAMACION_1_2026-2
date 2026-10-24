package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SignUpViewController {

    @FXML private Button btnVolver;

    @FXML
    private void onVolver() {
        Navegador.irA(btnVolver, "Login.fxml");
    }
}