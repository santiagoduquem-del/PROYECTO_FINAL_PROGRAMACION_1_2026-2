package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionCombosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Pantalla Gestionar combos del administrador (se abre desde Gestionar funciones).
 * CRUD: registrar, consultar (tabla), modificar, eliminar, y además deshabilitar/habilitar y subir foto.
 */
public class GestionCombosViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private CheckBox chkExclusivoBistro;
    @FXML private ImageView imgCombo;
    @FXML private TableView<Combo> tablaCombos;
    @FXML private TableColumn<Combo, String> colCodigo;
    @FXML private TableColumn<Combo, String> colNombre;
    @FXML private TableColumn<Combo, String> colDescripcion;
    @FXML private TableColumn<Combo, String> colPrecio;
    @FXML private TableColumn<Combo, String> colFoto;
    @FXML private TableColumn<Combo, String> colSalas;
    @FXML private TableColumn<Combo, String> colEstado;

    private final GestionCombosController gestionCombosController = new GestionCombosController();

    // Foto elegida en el formulario (null si no se ha subido ninguna)
    private String rutaFoto;

    @FXML
    public void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colPrecio.setCellValueFactory(c -> new SimpleStringProperty(Conversor.aPesos(c.getValue().getPrecio())));
        colFoto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRutaImagen() == null ? "Sin foto" : "Sí"));
        colSalas.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isExclusivoVipBistro() ? "Solo VIP Bistró" : "Todas las salas"));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().estaDisponible() ? "DISPONIBLE" : "NO_DISPONIBLE"));
        refrescarTabla();
    }

    // Al hacer clic en una fila, sus datos pasan al formulario
    @FXML
    private void onSeleccionarCombo() {
        Combo combo = tablaCombos.getSelectionModel().getSelectedItem();
        if (combo != null) {
            txtCodigo.setText(combo.getCodigo());
            txtCodigo.setDisable(true);   // el código identifica al combo: no se modifica
            txtNombre.setText(combo.getNombre());
            txtDescripcion.setText(combo.getDescripcion());
            txtPrecio.setText(String.valueOf((long) combo.getPrecio()));
            chkExclusivoBistro.setSelected(combo.isExclusivoVipBistro());
            rutaFoto = combo.getRutaImagen();
            Imagenes.mostrar(imgCombo, rutaFoto);
        }
    }

    @FXML
    private void onSubirFoto() {
        String ruta = Imagenes.elegirImagen(rootPane.getScene().getWindow(), "Seleccione la foto del combo");
        if (ruta != null) {
            rutaFoto = ruta;
            Imagenes.mostrar(imgCombo, rutaFoto);
        }
    }

    @FXML
    private void onRegistrar() {
        try {
            gestionCombosController.registrarCombo(txtCodigo.getText(), txtNombre.getText(),
                    txtDescripcion.getText(), txtPrecio.getText(), chkExclusivoBistro.isSelected(), rutaFoto);
            Alertas.mostrarInfo("Combo registrado", "El combo se registró y ya aparece en la venta.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void onModificar() {
        try {
            gestionCombosController.modificarCombo(txtCodigo.getText(), txtNombre.getText(),
                    txtDescripcion.getText(), txtPrecio.getText(), chkExclusivoBistro.isSelected(), rutaFoto);
            Alertas.mostrarInfo("Combo modificado", "Los datos del combo se actualizaron.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        if (!Alertas.confirmar("Eliminar combo",
                "¿Seguro que desea eliminar el combo " + txtCodigo.getText() + "? Esta acción no se puede deshacer.")) {
            return;
        }
        try {
            gestionCombosController.eliminarCombo(txtCodigo.getText());
            Alertas.mostrarInfo("Combo eliminado", "El combo se eliminó correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo eliminar", e.getMessage());
        }
    }

    @FXML
    private void onDeshabilitar() {
        try {
            gestionCombosController.deshabilitarCombo(txtCodigo.getText());
            Alertas.mostrarInfo("Combo deshabilitado", "El combo ya no aparece en la venta.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo deshabilitar", e.getMessage());
        }
    }

    @FXML
    private void onHabilitar() {
        try {
            gestionCombosController.habilitarCombo(txtCodigo.getText());
            Alertas.mostrarInfo("Combo habilitado", "El combo vuelve a aparecer en la venta.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo habilitar", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        txtNombre.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        chkExclusivoBistro.setSelected(false);
        rutaFoto = null;
        Imagenes.mostrar(imgCombo, null);
        tablaCombos.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "GestionFunciones.fxml");
    }

    private void refrescarTabla() {
        tablaCombos.getItems().setAll(gestionCombosController.obtenerCombos());
        tablaCombos.refresh();
    }
}
