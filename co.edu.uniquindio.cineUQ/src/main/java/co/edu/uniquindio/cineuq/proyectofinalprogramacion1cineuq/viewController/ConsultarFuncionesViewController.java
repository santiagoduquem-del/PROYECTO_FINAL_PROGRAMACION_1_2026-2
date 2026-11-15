package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Asiento;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.ConsultarFuncionesController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 * Consulta de funciones programadas y de la disponibilidad de una función.
 */
public class ConsultarFuncionesViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Funcion> tablaFunciones;
    @FXML private TableColumn<Funcion, String> colPelicula;
    @FXML private TableColumn<Funcion, String> colFormato;
    @FXML private TableColumn<Funcion, String> colSala;
    @FXML private TableColumn<Funcion, String> colFecha;
    @FXML private TableColumn<Funcion, String> colHora;
    @FXML private TableColumn<Funcion, String> colValor;
    @FXML private TableColumn<Funcion, String> colDisponibles;
    @FXML private Label lblFuncion;
    @FXML private Label lblTotal;
    @FXML private Label lblDisponibles;
    @FXML private Label lblOcupados;
    @FXML private GridPane gridAsientos;

    private final ConsultarFuncionesController consultarFuncionesController = new ConsultarFuncionesController();

    @FXML
    public void initialize() {
        colPelicula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colFormato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPelicula().getFormato().toString()));
        colSala.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().toString()));
        colHora.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHora().toString()));
        colValor.setCellValueFactory(c -> new SimpleStringProperty(Conversor.aPesos(c.getValue().getValorBase())));
        colDisponibles.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(c.getValue().contarAsientosDisponibles())));

        tablaFunciones.getItems().setAll(consultarFuncionesController.obtenerFuncionesProgramadas());
    }

    @FXML
    private void onSeleccionarFuncion() {
        Funcion funcion = tablaFunciones.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            return;
        }

        lblFuncion.setText("Función: " + funcion.getPelicula().getNombre()
                + "\nFormato: " + funcion.getPelicula().getFormato()
                + "\nSala: " + funcion.getSala().getNombre()
                + "\nFecha y hora: " + funcion.getFecha() + " " + funcion.getHora());
        lblTotal.setText(String.valueOf(funcion.getAsientos().size()));
        lblDisponibles.setText(String.valueOf(funcion.contarAsientosDisponibles()));
        lblOcupados.setText(String.valueOf(funcion.contarAsientosOcupados()));

        // Mapa de asientos: verde = disponible, rojo = ocupado
        gridAsientos.getChildren().clear();
        for (Asiento asiento : funcion.getAsientos()) {
            Label etiqueta = new Label(asiento.getCodigo());
            if (asiento.estaDisponible()) {
                etiqueta.getStyleClass().add("asiento-disponible");
            } else {
                etiqueta.getStyleClass().add("asiento-ocupado");
            }
            gridAsientos.add(etiqueta, asiento.getNumero() - 1, asiento.getFila() - 'A');
        }
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuOperador.fxml");
    }
}
