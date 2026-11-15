package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.DetalleFormatoController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Pantalla con la información de un formato (IMAX, Dolby Cinema, 2D, 3D, 4DX o VIP Bistró).
 * Hereda de BarraSuperiorViewController los botones de la barra superior
 * (incluido "Más formatos", que también usa el botón "Ver más formatos").
 */
public class DetalleFormatoViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private Label lblNombre;
    @FXML private Label lblDescripcion;
    @FXML private Label lblIdealPara;
    @FXML private VBox contenedorCaracteristicas;
    @FXML private Label lblPeliculas;
    @FXML private VBox contenedorFunciones;

    private final DetalleFormatoController detalleFormatoController = new DetalleFormatoController();

    @FXML
    public void initialize() {
        Formato formato = detalleFormatoController.obtenerFormatoSeleccionado();

        lblNombre.setText(formato.getNombre());
        lblDescripcion.setText(formato.getDescripcion());
        lblIdealPara.setText(formato.getIdealPara());

        // Una línea por cada característica del formato
        for (String caracteristica : formato.getCaracteristicas()) {
            Label linea = new Label("✔  " + caracteristica);
            linea.getStyleClass().add("caracteristica");
            linea.setWrapText(true);
            contenedorCaracteristicas.getChildren().add(linea);
        }

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
