package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionMembresiasController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Membresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Pantalla Gestionar membresías (se abre desde Gestionar tarifas).
 */
public class GestionMembresiasViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private ComboBox<PlanMembresia> cbPlan;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtDescuentoEntradas;
    @FXML private TextField txtDescuentoCombos;
    @FXML private TextField txtBeneficio;
    @FXML private TableView<Membresia> tablaMembresias;
    @FXML private TableColumn<Membresia, String> colPlan;
    @FXML private TableColumn<Membresia, String> colPrecio;
    @FXML private TableColumn<Membresia, String> colDescuentoEntradas;
    @FXML private TableColumn<Membresia, String> colDescuentoCombos;
    @FXML private TableColumn<Membresia, String> colBeneficio;

    private final GestionMembresiasController gestionMembresiasController = new GestionMembresiasController();

    @FXML
    public void initialize() {
        cbPlan.getItems().setAll(PlanMembresia.values());

        colPlan.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlan().getNombre()));
        colPrecio.setCellValueFactory(c -> new SimpleStringProperty(Conversor.aPesos(c.getValue().getPrecioMensual())));
        colDescuentoEntradas.setCellValueFactory(c -> new SimpleStringProperty(
                gestionMembresiasController.aPorcentaje(c.getValue().getDescuentoEntradas())));
        colDescuentoCombos.setCellValueFactory(c -> new SimpleStringProperty(
                gestionMembresiasController.aPorcentaje(c.getValue().getDescuentoCombos())));
        colBeneficio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBeneficioExtra()));

        refrescarTabla();
    }

    /** Clic en una fila de la tabla: sus valores pasan al formulario. */
    @FXML
    private void onSeleccionarPlanTabla() {
        Membresia membresia = tablaMembresias.getSelectionModel().getSelectedItem();
        if (membresia != null) {
            cbPlan.setValue(membresia.getPlan());   // esto también llena el formulario (onSeleccionarPlanLista)
        }
    }

    /** Al elegir un plan en la lista se cargan sus valores actuales. */
    @FXML
    private void onSeleccionarPlanLista() {
        PlanMembresia plan = cbPlan.getValue();
        if (plan == null) {
            return;
        }
        for (Membresia membresia : gestionMembresiasController.obtenerMembresias()) {
            if (membresia.getPlan() == plan) {
                txtPrecio.setText(String.format("%.0f", membresia.getPrecioMensual()));
                txtDescuentoEntradas.setText(String.valueOf(Math.round(membresia.getDescuentoEntradas() * 100)));
                txtDescuentoCombos.setText(String.valueOf(Math.round(membresia.getDescuentoCombos() * 100)));
                txtBeneficio.setText(membresia.getBeneficioExtra());
            }
        }
    }

    @FXML
    private void onGuardar() {
        try {
            gestionMembresiasController.modificarMembresia(cbPlan.getValue(), txtPrecio.getText(),
                    txtDescuentoEntradas.getText(), txtDescuentoCombos.getText(), txtBeneficio.getText());
            Alertas.mostrarInfo("Membresía actualizada",
                    "Los nuevos valores del plan se aplicarán en las próximas ventas.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo guardar", e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "GestionTarifas.fxml");
    }

    private void refrescarTabla() {
        tablaMembresias.getItems().setAll(gestionMembresiasController.obtenerMembresias());
        tablaMembresias.refresh();
    }
}
