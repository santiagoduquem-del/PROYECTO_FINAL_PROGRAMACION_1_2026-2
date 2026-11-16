package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.DetalleFormatoController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Controla las pantallas de cada formato (FormatoSignature.fxml, Formato3D.fxml, FormatoIMAX.fxml, etc.,
 * FormatoDolbyCinema.fxml, Formato4DX.fxml y FormatoVipBistro.fxml).
 * Son iguales por dentro: el texto de cada formato está escrito en su FXML (Scene Builder)
 * y este viewController solo llena la parte que cambia: películas y funciones en ese formato.
 * Hereda de BarraSuperiorViewController los botones de la barra superior.
 */
public class DetalleFormatoViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView imgFormato;
    @FXML private Label lblPeliculas;
    @FXML private VBox contenedorFunciones;

    private final DetalleFormatoController detalleFormatoController = new DetalleFormatoController();

    @FXML
    public void initialize() {
        Formato formato = detalleFormatoController.obtenerFormatoSeleccionado();

        // La imagen que se puso en Scene Builder se recorta para llenar el recuadro
        Imagenes.llenarRecuadro(imgFormato);

        lblPeliculas.setText(detalleFormatoController.obtenerPeliculasEnFormato(formato));

        // Una línea por cada función programada en este formato
        List<Funcion> funciones = detalleFormatoController.obtenerFuncionesEnFormato(formato);
        if (funciones.isEmpty()) {
            Label vacio = new Label("No hay funciones programadas en este formato.");
            vacio.getStyleClass().add("etiqueta");
            contenedorFunciones.getChildren().add(vacio);
        }
        for (Funcion funcion : funciones) {
            Label linea = new Label(funcion.getPelicula().getNombre()
                    + "\n" + funcion.getFecha() + "  " + funcion.getHora()
                    + "  ·  " + funcion.getSala().getNombre()
                    + "  ·  " + funcion.contarAsientosDisponibles() + " asientos libres"
                    + "  ·  Valor base " + Conversor.aPesos(funcion.getValorBase()));
            linea.getStyleClass().add("caracteristica");
            linea.setStyle("-fx-font-size: 16px; -fx-padding: 10; -fx-background-color: #1c1c1c; -fx-background-radius: 10;");
            linea.setMaxWidth(Double.MAX_VALUE);
            linea.setWrapText(true);
            contenedorFunciones.getChildren().add(linea);
        }
    }

    @FXML
    private void onVolver() {
        Navegador.volverAtras(rootPane);
    }
}
