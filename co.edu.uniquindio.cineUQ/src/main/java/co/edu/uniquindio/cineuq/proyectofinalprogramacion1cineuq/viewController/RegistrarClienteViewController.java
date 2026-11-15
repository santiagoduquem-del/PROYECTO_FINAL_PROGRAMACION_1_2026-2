package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoCliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.RegistrarClienteController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class RegistrarClienteViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private ComboBox<TipoCliente> cbTipoCliente;
    @FXML private ComboBox<PlanMembresia> cbPlan;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colIdentificacion;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colCorreo;
    @FXML private TableColumn<Cliente, String> colTipo;
    @FXML private TableColumn<Cliente, String> colPlan;

    private final RegistrarClienteController registrarClienteController = new RegistrarClienteController();

    @FXML
    public void initialize() {
        cbTipoCliente.getItems().setAll(TipoCliente.values());
        cbPlan.getItems().setAll(PlanMembresia.values());
        cbPlan.setValue(PlanMembresia.FREE);

        colIdentificacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdentificacion()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colCorreo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCorreo()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoCliente().getDescripcion()));

        colPlan.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPlan().getNombre()));

        refrescarTabla();
    }

    @FXML
    private void onRegistrar() {
        try {
            registrarClienteController.registrarCliente(txtIdentificacion.getText(), txtNombre.getText(),
                    txtTelefono.getText(), txtCorreo.getText(), cbTipoCliente.getValue(), cbPlan.getValue());
            Alertas.mostrarInfo("Cliente registrado", "El cliente se registró correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    /** Al hacer clic en un cliente, sus datos pasan al formulario. */
    @FXML
    private void onSeleccionarCliente() {
        Cliente cliente = tablaClientes.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            txtIdentificacion.setText(cliente.getIdentificacion());
            txtIdentificacion.setDisable(true);   // la identificación no se cambia
            txtNombre.setText(cliente.getNombre());
            txtTelefono.setText(cliente.getTelefono());
            txtCorreo.setText(cliente.getCorreo());
            cbTipoCliente.setValue(cliente.getTipoCliente());
            cbPlan.setValue(cliente.getPlan());
        }
    }

    @FXML
    private void onModificar() {
        try {
            registrarClienteController.modificarCliente(txtIdentificacion.getText(), txtNombre.getText(),
                    txtTelefono.getText(), txtCorreo.getText(), cbTipoCliente.getValue(), cbPlan.getValue());
            Alertas.mostrarInfo("Cliente modificado", "Los datos del cliente se actualizaron.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        if (!Alertas.confirmar("Eliminar cliente",
                "¿Seguro que desea eliminar el cliente " + txtIdentificacion.getText() + "? Esta acción no se puede deshacer.")) {
            return;
        }
        try {
            registrarClienteController.eliminarCliente(txtIdentificacion.getText());
            Alertas.mostrarInfo("Cliente eliminado", "El cliente se eliminó correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo eliminar", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        txtIdentificacion.clear();
        txtIdentificacion.setDisable(false);
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        cbTipoCliente.setValue(null);
        cbPlan.setValue(PlanMembresia.FREE);
        tablaClientes.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuOperador.fxml");
    }

    private void refrescarTabla() {
        tablaClientes.getItems().setAll(registrarClienteController.obtenerClientes());
        tablaClientes.refresh();   // para que se vean los datos modificados
    }
}
