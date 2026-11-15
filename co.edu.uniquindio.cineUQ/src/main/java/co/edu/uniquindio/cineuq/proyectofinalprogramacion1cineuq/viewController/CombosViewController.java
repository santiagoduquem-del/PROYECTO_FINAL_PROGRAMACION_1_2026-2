package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.CombosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Ventana de combos de la venta. Crea una tarjeta por cada combo habilitado
 * (foto, nombre, contenido, precio y botones − / +).
 */
public class CombosViewController {

    @FXML private AnchorPane rootPane;
    @FXML private FlowPane contenedorCombos;
    @FXML private Label lblSubtotal;

    private final CombosController combosController = new CombosController();

    @FXML
    public void initialize() {
        dibujarTarjetas();
    }

    private void dibujarTarjetas() {
        contenedorCombos.getChildren().clear();
        if (combosController.obtenerCombosDisponibles().isEmpty()) {
            Label vacio = new Label("No hay combos disponibles. El administrador los registra en Gestionar combos.");
            vacio.getStyleClass().add("etiqueta");
            contenedorCombos.getChildren().add(vacio);
        }
        for (Combo combo : combosController.obtenerCombosDisponibles()) {
            contenedorCombos.getChildren().add(crearTarjeta(combo));
        }
        lblSubtotal.setText("Subtotal combos: " + Conversor.aPesos(combosController.calcularSubtotal()));
    }

    /** Tarjeta de un combo: foto, nombre, contenido, precio y la cantidad elegida con − y +. */
    private VBox crearTarjeta(Combo combo) {
        Label sinImagen = new Label("Sin foto");
        sinImagen.getStyleClass().add("sin-imagen");
        ImageView foto = new ImageView();
        foto.setFitWidth(425);
        foto.setFitHeight(190);
        foto.setPreserveRatio(true);
        Imagenes.mostrar(foto, combo.getRutaImagen());
        StackPane marco = new StackPane(sinImagen, foto);
        marco.getStyleClass().add("marco-imagen");
        marco.setPrefHeight(190);

        Label nombre = new Label(combo.getNombre());
        nombre.getStyleClass().add("nombre-combo");

        Label contenido = new Label(combo.getDescripcion());
        contenido.getStyleClass().add("etiqueta");
        contenido.setWrapText(true);

        Label precio = new Label(Conversor.aPesos(combo.getPrecio()));
        precio.getStyleClass().add("dato");
        precio.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(precio, Priority.ALWAYS);

        Button quitar = new Button("−");
        quitar.getStyleClass().add("boton-cantidad");
        quitar.setOnAction(e -> {
            combosController.quitarCombo(combo);
            dibujarTarjetas();
        });

        Label cantidad = new Label(String.valueOf(combosController.contarCombo(combo)));
        cantidad.getStyleClass().add("cantidad-combo");

        Button agregar = new Button("+");
        agregar.getStyleClass().add("boton-cantidad");
        agregar.setOnAction(e -> {
            combosController.agregarCombo(combo);
            dibujarTarjetas();
        });

        HBox filaPrecio = new HBox(10, precio, quitar, cantidad, agregar);
        filaPrecio.setAlignment(Pos.CENTER_LEFT);

        VBox tarjeta = new VBox(8, marco, nombre, contenido, filaPrecio);
        tarjeta.getStyleClass().add("tarjeta-combo");
        tarjeta.setPrefWidth(455);
        return tarjeta;
    }

    @FXML
    private void onQuitarTodos() {
        combosController.quitarTodos();
        dibujarTarjetas();
    }

    /** Cierra la ventana; la pantalla de venta lee los combos elegidos. */
    @FXML
    private void onListo() {
        Stage ventana = (Stage) rootPane.getScene().getWindow();
        ventana.close();
    }
}
