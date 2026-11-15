package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.CombosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Ventana de combos. Cada botón + / − tiene en "userData" el nombre del combo
 * (PERSONAL, PAREJA, FAMILIAR, NACHOS, PERRO, INFANTIL).
 */
public class CombosViewController {

    @FXML private AnchorPane rootPane;
    @FXML private Label lblCantPersonal;
    @FXML private Label lblCantPareja;
    @FXML private Label lblCantFamiliar;
    @FXML private Label lblCantNachos;
    @FXML private Label lblCantPerro;
    @FXML private Label lblCantInfantil;
    @FXML private Label lblSubtotal;

    private final CombosController combosController = new CombosController();

    @FXML
    public void initialize() {
        actualizar();
    }

    @FXML
    private void onAgregar(ActionEvent evento) {
        combosController.agregarCombo(comboDelBoton(evento));
        actualizar();
    }

    @FXML
    private void onQuitar(ActionEvent evento) {
        combosController.quitarCombo(comboDelBoton(evento));
        actualizar();
    }

    @FXML
    private void onQuitarTodos() {
        combosController.quitarTodos();
        actualizar();
    }

    /** Cierra la ventana; la pantalla de venta lee los combos elegidos. */
    @FXML
    private void onListo() {
        Stage ventana = (Stage) rootPane.getScene().getWindow();
        ventana.close();
    }

    /** Lee el combo guardado en el userData del botón que se pulsó. */
    private Combo comboDelBoton(ActionEvent evento) {
        Node boton = (Node) evento.getSource();
        return Combo.valueOf((String) boton.getUserData());
    }

    private void actualizar() {
        lblCantPersonal.setText(String.valueOf(combosController.contarCombo(Combo.PERSONAL)));
        lblCantPareja.setText(String.valueOf(combosController.contarCombo(Combo.PAREJA)));
        lblCantFamiliar.setText(String.valueOf(combosController.contarCombo(Combo.FAMILIAR)));
        lblCantNachos.setText(String.valueOf(combosController.contarCombo(Combo.NACHOS)));
        lblCantPerro.setText(String.valueOf(combosController.contarCombo(Combo.PERRO)));
        lblCantInfantil.setText(String.valueOf(combosController.contarCombo(Combo.INFANTIL)));
        lblSubtotal.setText("Subtotal combos: " + Conversor.aPesos(combosController.calcularSubtotal()));
    }
}
