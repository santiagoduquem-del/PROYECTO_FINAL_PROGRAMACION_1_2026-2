package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Venta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.CancelarVentaController;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class CancelarVentaViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtCodigoVenta;
    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, String> colCodigo;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colPelicula;
    @FXML private TableColumn<Venta, String> colFuncion;
    @FXML private TableColumn<Venta, String> colAsiento;
    @FXML private TableColumn<Venta, String> colTipo;
    @FXML private TableColumn<Venta, String> colValor;
    @FXML private TableColumn<Venta, String> colEstado;

    private final CancelarVentaController cancelarVentaController = new CancelarVentaController();

    @FXML
    public void initialize() {
        // Qué dato muestra cada columna
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));
        colPelicula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFuncion().getPelicula().getNombre()));
        colFuncion.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFuncion().getFecha() + " " + c.getValue().getFuncion().getHora()));
        colAsiento.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAsiento().getCodigo()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoEntrada().getDescripcion()));
        colValor.setCellValueFactory(c -> new SimpleStringProperty(Conversor.aPesos(c.getValue().getValorPagado())));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().toString()));

        refrescarTabla();
    }

    @FXML
    private void onSeleccionarVenta() {
        Venta venta = tablaVentas.getSelectionModel().getSelectedItem();
        if (venta != null) {
            txtCodigoVenta.setText(venta.getCodigo());
        }
    }

    @FXML
    private void onCancelarVenta() {
        try {
            String mensaje = cancelarVentaController.cancelarVenta(txtCodigoVenta.getText());
            Alertas.mostrarInfo("Venta cancelada", mensaje);
            txtCodigoVenta.clear();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo cancelar", e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuOperador.fxml");
    }

    private void refrescarTabla() {
        tablaVentas.getItems().setAll(cancelarVentaController.obtenerVentas());
        tablaVentas.refresh();
    }
}
