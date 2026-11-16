package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Idioma;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Sala;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionFuncionesController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class GestionFuncionesViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtCodigo;
    @FXML private ComboBox<Pelicula> cbPelicula;
    @FXML private ComboBox<Formato> cbFormato;
    @FXML private ComboBox<Idioma> cbIdioma;
    @FXML private ComboBox<Sala> cbSala;
    @FXML private DatePicker dpFecha;
    @FXML private TextField txtHora;
    @FXML private TextField txtValorBase;
    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colCodigo;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colFormato;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, String> colFecha;
    @FXML private TableColumn<Funcion, String> colHora;
    @FXML private TableColumn<Funcion, String> colValor;
    @FXML private TableColumn<Funcion, String> colDisponibles;
    @FXML private TableColumn<Funcion, String> colEstado;

    private final GestionFuncionesController gestionFuncionesController = new GestionFuncionesController();

    @FXML
    public void initialize() {
        // Se muestran TODAS las películas y salas: si se elige una deshabilitada,
        // el sistema muestra el error correspondiente (validación del enunciado).
        cbPelicula.getItems().setAll(gestionFuncionesController.obtenerPeliculas());
        cbSala.getItems().setAll(gestionFuncionesController.obtenerSalas());
        cbIdioma.getItems().setAll(Idioma.values());

        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colPelicula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colFormato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().describirFormatoIdioma()));
        colSala.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().toString()));
        colHora.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHora().toString()));
        colValor.setCellValueFactory(c -> new SimpleStringProperty(Conversor.aPesos(c.getValue().getValorBase())));
        colDisponibles.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().contarAsientosDisponibles())));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().toString()));

        refrescarTabla();
    }

    @FXML
    private void onProgramar() {
        try {
            gestionFuncionesController.programarFuncion(txtCodigo.getText(), cbPelicula.getValue(), cbSala.getValue(),
                    dpFecha.getValue(), txtHora.getText(), txtValorBase.getText(), cbFormato.getValue(), cbIdioma.getValue());
            Alertas.mostrarInfo("Función programada", "La función se programó correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo programar", e.getMessage());
        }
    }

    /** Al elegir la película, el ComboBox de formato muestra solo los formatos de esa película. */
    @FXML
    private void onSeleccionarPelicula() {
        cbFormato.getItems().setAll(gestionFuncionesController.obtenerFormatosDePelicula(cbPelicula.getValue()));
        if (cbFormato.getItems().size() == 1) {
            cbFormato.setValue(cbFormato.getItems().get(0));   // si solo hay uno, se elige solo
        }
    }

    /** Al hacer clic en una función, sus datos pasan al formulario (para modificarla). */
    @FXML
    private void onSeleccionarFuncion() {
        Funcion funcion = tablaFunciones.getSelectionModel().getSelectedItem();
        if (funcion != null) {
            txtCodigo.setText(funcion.getCodigo());
            txtCodigo.setDisable(true);   // el código no se cambia
            cbPelicula.setValue(funcion.getPelicula());   // también recarga los formatos de la película
            cbFormato.setValue(funcion.getFormato());
            cbIdioma.setValue(funcion.getIdioma());
            cbSala.setValue(funcion.getSala());
            dpFecha.setValue(funcion.getFecha());
            txtHora.setText(funcion.getHora().toString());
            txtValorBase.setText(String.format("%.0f", funcion.getValorBase()));
        }
    }

    /** Modifica la función seleccionada (solo si no tiene ventas). */
    @FXML
    private void onModificarFuncion() {
        try {
            gestionFuncionesController.modificarFuncion(txtCodigo.getText(), cbPelicula.getValue(), cbSala.getValue(),
                    dpFecha.getValue(), txtHora.getText(), txtValorBase.getText(), cbFormato.getValue(), cbIdioma.getValue());
            Alertas.mostrarInfo("Función modificada", "Los datos de la función se actualizaron.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onActualizarValorBase() {
        try {
            gestionFuncionesController.modificarValorBase(tablaFunciones.getSelectionModel().getSelectedItem(),
                    txtValorBase.getText());
            Alertas.mostrarInfo("Valor base actualizado", "El nuevo valor base se aplicará en las próximas ventas.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo actualizar", e.getMessage());
        }
    }

    @FXML
    private void onCancelarFuncion() {
        Funcion funcion = tablaFunciones.getSelectionModel().getSelectedItem();

        if (funcion != null && !Alertas.confirmar("Cancelar función",
                "¿Seguro que desea cancelar la función " + funcion.getCodigo() + "?\n\n"
                + "La función no se borra: queda en el historial con estado CANCELADA.")) {
            return;
        }
        try {
            gestionFuncionesController.cancelarFuncion(funcion);
            Alertas.mostrarInfo("Función cancelada", "La función " + funcion.getCodigo()
                    + " quedó CANCELADA y se conserva en el historial.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo cancelar", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        tablaFunciones.getSelectionModel().clearSelection();
        cbPelicula.setValue(null);
        cbFormato.getItems().clear();
        cbFormato.setValue(null);
        cbIdioma.setValue(null);
        cbSala.setValue(null);
        dpFecha.setValue(null);
        txtHora.clear();
        txtValorBase.clear();
    }

    /** Abre el CRUD de combos (los combos que el operador puede agregar a una venta). */
    @FXML
    private void onGestionarCombos() {
        Navegador.irA(rootPane, "GestionCombos.fxml");
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuAdministrador.fxml");
    }

    private void refrescarTabla() {
        tablaFunciones.getItems().setAll(gestionFuncionesController.obtenerFunciones());
        tablaFunciones.refresh();
    }
}
