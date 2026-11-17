package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.CombosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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
        actualizarSubtotal();
    }

    private void actualizarSubtotal() {
        lblSubtotal.setText("Subtotal combos: " + Conversor.aPesos(combosController.calcularSubtotal()));
    }

    /** Tarjeta de un combo: foto, nombre, contenido, precio y la cantidad elegida con − y +. */
    private VBox crearTarjeta(Combo combo) {
        Label sinImagen = new Label("Sin foto");
        sinImagen.getStyleClass().add("sin-imagen");
        // Fondo: la misma foto, recortada para llenar todo el recuadro, desenfocada y oscura
        ImageView fondo = new ImageView();
        fondo.setFitWidth(410);
        fondo.setFitHeight(280);
        fondo.setPreserveRatio(true);
        Imagenes.mostrar(fondo, combo.getRutaImagen());
        fondo.setEffect(new GaussianBlur(30));
        fondo.setOpacity(0.45);

        // Encima: la foto completa, sin recortar (si es vertical o cuadrada, el fondo llena los lados)
        ImageView foto = new ImageView();
        foto.setFitWidth(410);
        foto.setFitHeight(280);
        foto.setPreserveRatio(true);
        Imagenes.mostrarSinRecortar(foto, combo.getRutaImagen());

        StackPane marco = new StackPane(sinImagen, fondo, foto);
        marco.getStyleClass().add("marco-imagen");
        marco.setPrefHeight(280);
        marco.setClip(new Rectangle(410, 280));   // el desenfoque no se sale del recuadro

        Label nombre = new Label(combo.getNombre());
        nombre.getStyleClass().add("nombre-combo");

        Label contenido = new Label(combo.getDescripcion());
        contenido.getStyleClass().add("etiqueta");
        contenido.setWrapText(true);

        Label precio = new Label(Conversor.aPesos(combo.getPrecio()));
        precio.getStyleClass().add("dato");
        precio.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(precio, Priority.ALWAYS);

        Label cantidad = new Label(String.valueOf(combosController.contarCombo(combo)));
        cantidad.getStyleClass().add("cantidad-combo");

        // Con − y + solo se actualizan la cantidad de esta tarjeta y el subtotal.
        // (Si se volvieran a dibujar todas las tarjetas, el scroll saltaría hasta arriba.)
        Button quitar = new Button("−");
        quitar.getStyleClass().add("boton-cantidad");
        quitar.setOnAction(e -> {
            combosController.quitarCombo(combo);
            cantidad.setText(String.valueOf(combosController.contarCombo(combo)));
            actualizarSubtotal();
        });

        Button agregar = new Button("+");
        agregar.getStyleClass().add("boton-cantidad");
        agregar.setOnAction(e -> {
            combosController.agregarCombo(combo);
            cantidad.setText(String.valueOf(combosController.contarCombo(combo)));
            actualizarSubtotal();
        });

        HBox filaPrecio = new HBox(10, precio, quitar, cantidad, agregar);
        filaPrecio.setAlignment(Pos.CENTER_LEFT);

        VBox tarjeta = new VBox(8, marco, nombre, contenido, filaPrecio);
        if (combo.isExclusivoVipBistro()) {
            Label exclusivo = new Label("Exclusivo VIP Bistró");
            exclusivo.setStyle("-fx-text-fill: #d4af37; -fx-font-weight: bold; -fx-font-size: 14px;");
            tarjeta.getChildren().add(1, exclusivo);   // debajo de la foto
        }
        tarjeta.getStyleClass().add("tarjeta-combo");
        tarjeta.setPrefWidth(440);   // 3 tarjetas por fila
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