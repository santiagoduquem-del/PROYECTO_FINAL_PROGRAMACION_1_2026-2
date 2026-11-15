package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Sala;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionSalasController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class GestionSalasViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCapacidad;
    @FXML private TableView<Sala> tablaSalas;
    @FXML private TableColumn<Sala, String> colCodigo;
    @FXML private TableColumn<Sala, String> colNombre;
    @FXML private TableColumn<Sala, String> colCapacidad;
    @FXML private TableColumn<Sala, String> colEstado;

    private final GestionSalasController gestionSalasController = new GestionSalasController();

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colCapacidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCapacidad() + " asientos"));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().toString()));

        refrescarTabla();
    }

    @FXML
    private void onSeleccionarSala() {
        Sala sala = tablaSalas.getSelectionModel().getSelectedItem();
        if (sala != null) {
            txtCodigo.setText(sala.getCodigo());
            txtNombre.setText(sala.getNombre());
            txtCapacidad.setText(String.valueOf(sala.getCapacidad()));
        }
    }

    @FXML
    private void onRegistrar() {
        try {
            gestionSalasController.registrarSala(txtCodigo.getText(), txtNombre.getText(), txtCapacidad.getText());
            Alertas.mostrarInfo("Sala registrada", "La sala se registró correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void onModificar() {
        try {
            gestionSalasController.modificarSala(txtCodigo.getText(), txtNombre.getText(), txtCapacidad.getText());
            Alertas.mostrarInfo("Sala modificada", "Los datos de la sala se actualizaron.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onDeshabilitar() {
        try {
            gestionSalasController.deshabilitarSala(txtCodigo.getText());
            Alertas.mostrarInfo("Sala fuera de servicio", "La sala quedó FUERA_DE_SERVICIO.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo cambiar el estado", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        if (!Alertas.confirmar("Eliminar sala",
                "¿Seguro que desea eliminar la sala " + txtCodigo.getText() + "? Esta acción no se puede deshacer.")) {
            return;
        }
        try {
            gestionSalasController.eliminarSala(txtCodigo.getText());
            Alertas.mostrarInfo("Sala eliminada", "La sala se eliminó correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo eliminar", e.getMessage());
        }
    }

    @FXML
    private void onHabilitar() {
        try {
            gestionSalasController.habilitarSala(txtCodigo.getText());
            Alertas.mostrarInfo("Sala habilitada", "La sala quedó DISPONIBLE.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo cambiar el estado", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        txtCodigo.clear();
        txtNombre.clear();
        txtCapacidad.clear();
        tablaSalas.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuAdministrador.fxml");
    }

    private void refrescarTabla() {
        tablaSalas.getItems().setAll(gestionSalasController.obtenerSalas());
        tablaSalas.refresh();
    }
}
