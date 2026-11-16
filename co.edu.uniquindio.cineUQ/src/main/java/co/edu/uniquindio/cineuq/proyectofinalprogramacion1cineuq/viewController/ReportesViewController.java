package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.ReportesController;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;

public class ReportesViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private Label lblTotalEntradas;
    @FXML private Label lblTotalIngresos;
    @FXML private Label lblPeliculaMasVendida;
    @FXML private ComboBox<Funcion> cbFuncion;
    @FXML private Label lblDisponibles;
    @FXML private Label lblOcupados;
    @FXML private Label lblPorcentaje;
    @FXML private ProgressBar barraOcupacion;
    @FXML private DatePicker dpFecha;
    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colFormato;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, String> colHora;
    @FXML private TableColumn<Funcion, String> colValor;
    @FXML private TableColumn<Funcion, String> colDisponibles;

    private final ReportesController reportesController = new ReportesController();

    @FXML
    public void initialize() {
        colPelicula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colFormato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().describirFormatoIdioma()));
        colSala.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colHora.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHora().toString()));
        colValor.setCellValueFactory(c -> new SimpleStringProperty(Conversor.aPesos(c.getValue().getValorBase())));
        colDisponibles.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().contarAsientosDisponibles())));

        dpFecha.setValue(LocalDate.now());
        onActualizar();
    }

    @FXML
    private void onActualizar() {
        lblTotalEntradas.setText(String.valueOf(reportesController.obtenerTotalEntradasVendidas()));
        lblTotalIngresos.setText(Conversor.aPesos(reportesController.obtenerTotalIngresos()));
        lblPeliculaMasVendida.setText(reportesController.obtenerPeliculaMasVendida());

        Funcion seleccionada = cbFuncion.getValue();
        cbFuncion.getItems().setAll(reportesController.obtenerTodasLasFunciones());
        cbFuncion.setValue(seleccionada);

        onSeleccionarFuncion();
        onSeleccionarFecha();
    }

    @FXML
    private void onSeleccionarFuncion() {
        Funcion funcion = cbFuncion.getValue();
        if (funcion == null) {
            lblDisponibles.setText("-");
            lblOcupados.setText("-");
            lblPorcentaje.setText("-");
            barraOcupacion.setProgress(0);
            return;
        }
        try {
            double porcentaje = reportesController.obtenerPorcentajeOcupacion(funcion);
            lblDisponibles.setText(String.valueOf(funcion.contarAsientosDisponibles()));
            lblOcupados.setText(String.valueOf(funcion.contarAsientosOcupados()));
            lblPorcentaje.setText(String.format("%.1f %%", porcentaje));
            barraOcupacion.setProgress(porcentaje / 100);
        } catch (CinemaException e) {
            Alertas.mostrarError("Ocupación", e.getMessage());
        }
    }

    @FXML
    private void onSeleccionarFecha() {
        try {
            tablaFunciones.getItems().setAll(reportesController.obtenerFuncionesProgramadas(dpFecha.getValue()));
            tablaFunciones.refresh();
        } catch (CinemaException e) {
            tablaFunciones.getItems().clear();
        }
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuAdministrador.fxml");
    }
}
