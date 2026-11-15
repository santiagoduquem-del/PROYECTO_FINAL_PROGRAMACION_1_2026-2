package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionEmpleadosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Empleado;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Pantalla Gestionar empleados (administrador): CRUD de operadores y administradores.
 */
public class GestionEmpleadosViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtIdentificacion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private ComboBox<String> cbRol;
    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, String> colIdentificacion;
    @FXML private TableColumn<Empleado, String> colNombre;
    @FXML private TableColumn<Empleado, String> colUsuario;
    @FXML private TableColumn<Empleado, String> colRol;
    @FXML private TableColumn<Empleado, String> colVentas;

    private final GestionEmpleadosController gestionEmpleadosController = new GestionEmpleadosController();

    @FXML
    public void initialize() {
        cbRol.getItems().setAll(GestionEmpleadosController.ROL_OPERADOR, GestionEmpleadosController.ROL_ADMINISTRADOR);

        colIdentificacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIdentificacion()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colUsuario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsuario()));
        colRol.setCellValueFactory(c -> new SimpleStringProperty(gestionEmpleadosController.obtenerRol(c.getValue())));
        colVentas.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(gestionEmpleadosController.contarVentas(c.getValue()))));

        refrescarTabla();
    }

    /** Al hacer clic en un empleado, sus datos pasan al formulario (la contraseña no se muestra). */
    @FXML
    private void onSeleccionarEmpleado() {
        Empleado empleado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (empleado != null) {
            txtIdentificacion.setText(empleado.getIdentificacion());
            txtIdentificacion.setDisable(true);   // la identificación no se cambia
            txtNombre.setText(empleado.getNombre());
            txtUsuario.setText(empleado.getUsuario());
            txtContrasena.clear();
            cbRol.setValue(gestionEmpleadosController.obtenerRol(empleado));
            cbRol.setDisable(true);               // el rol no se cambia
        }
    }

    @FXML
    private void onRegistrar() {
        try {
            gestionEmpleadosController.registrarEmpleado(txtIdentificacion.getText(), txtNombre.getText(),
                    txtUsuario.getText(), txtContrasena.getText(), cbRol.getValue());
            Alertas.mostrarInfo("Empleado registrado", "El empleado se registró correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void onModificar() {
        try {
            gestionEmpleadosController.modificarEmpleado(txtIdentificacion.getText(), txtNombre.getText(),
                    txtUsuario.getText(), txtContrasena.getText());
            Alertas.mostrarInfo("Empleado modificado", "Los datos del empleado se actualizaron.");
            txtContrasena.clear();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        if (!Alertas.confirmar("Eliminar empleado",
                "¿Seguro que desea eliminar al empleado " + txtIdentificacion.getText() + "? Esta acción no se puede deshacer.")) {
            return;
        }
        try {
            gestionEmpleadosController.eliminarEmpleado(txtIdentificacion.getText());
            Alertas.mostrarInfo("Empleado eliminado", "El empleado se eliminó correctamente.");
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
        txtUsuario.clear();
        txtContrasena.clear();
        cbRol.setValue(null);
        cbRol.setDisable(false);
        tablaEmpleados.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuAdministrador.fxml");
    }

    private void refrescarTabla() {
        tablaEmpleados.getItems().setAll(gestionEmpleadosController.obtenerEmpleados());
        tablaEmpleados.refresh();
    }
}
