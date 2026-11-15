package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.FormatosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Pantalla "Más formatos": los formatos que no tienen botón en la barra superior.
 * Al hacer clic en una tarjeta se abre la pantalla con la información de ese formato.
 */
public class FormatosViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private FlowPane contenedorFormatos;

    private final FormatosController formatosController = new FormatosController();

    @FXML
    public void initialize() {
        for (Formato formato : formatosController.obtenerFormatosRestantes()) {
            contenedorFormatos.getChildren().add(crearTarjeta(formato));
        }
    }

    /** Tarjeta con el nombre, un resumen y un botón para ver la información completa. */
    private VBox crearTarjeta(Formato formato) {
        Label nombre = new Label(formato.getNombre());
        nombre.getStyleClass().add("titulo-pelicula");

        Label idealPara = new Label("Ideal para: " + formato.getIdealPara());
        idealPara.getStyleClass().add("fila-plan-titulo");
        idealPara.setWrapText(true);

        Label descripcion = new Label(formato.getDescripcion());
        descripcion.getStyleClass().add("etiqueta");
        descripcion.setStyle("-fx-font-size: 17px;");
        descripcion.setWrapText(true);

        Button verInfo = new Button("Ver información");
        verInfo.getStyleClass().add("boton-principal");
        verInfo.setOnAction(e -> abrirDetalle(formato));

        VBox tarjeta = new VBox(14, nombre, idealPara, descripcion, verInfo);
        tarjeta.getStyleClass().add("tarjeta-formato");
        tarjeta.setPrefWidth(420);
        tarjeta.setMinHeight(380);
        // Toda la tarjeta también se puede pulsar
        tarjeta.setOnMouseClicked(e -> abrirDetalle(formato));
        return tarjeta;
    }

    private void abrirDetalle(Formato formato) {
        formatosController.seleccionarFormato(formato);
        Navegador.irA(rootPane, "DetalleFormato.fxml");
    }

    @FXML
    private void onVolver() {
        Navegador.volverAtras(rootPane);
    }
}
