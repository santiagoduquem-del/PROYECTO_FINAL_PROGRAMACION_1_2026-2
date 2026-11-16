package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.ConsultarPeliculasController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Cartelera del operador: mosaico con la carátula de cada película disponible.
 * Al hacer clic en una carátula se abre DetallePelicula.fxml con el banner, la carátula,
 * todos los datos y las funciones programadas.
 */
public class ConsultarPeliculasViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private FlowPane contenedorPeliculas;

    private final ConsultarPeliculasController consultarPeliculasController = new ConsultarPeliculasController();

    @FXML
    public void initialize() {
        List<Pelicula> disponibles = consultarPeliculasController.obtenerPeliculasDisponibles();
        if (disponibles.isEmpty()) {
            Label vacio = new Label("No hay películas disponibles en cartelera.");
            vacio.getStyleClass().add("etiqueta");
            contenedorPeliculas.getChildren().add(vacio);
        }
        for (Pelicula pelicula : disponibles) {
            contenedorPeliculas.getChildren().add(crearTarjeta(pelicula));
        }
    }

    /** Tarjeta del mosaico: carátula grande, nombre y una línea con año y formato. */
    private VBox crearTarjeta(Pelicula pelicula) {
        Label sinCaratula = new Label("Sin carátula");
        sinCaratula.getStyleClass().add("sin-imagen");
        ImageView caratula = new ImageView();
        caratula.setFitWidth(250);
        caratula.setFitHeight(375);   // proporción de póster (2:3)
        caratula.setPreserveRatio(true);
        Imagenes.mostrar(caratula, pelicula.getRutaCaratula());
        StackPane marco = new StackPane(sinCaratula, caratula);
        marco.getStyleClass().add("marco-imagen");
        marco.setPrefHeight(375);

        Label nombre = new Label(pelicula.getNombre());
        nombre.getStyleClass().add("nombre-combo");
        nombre.setWrapText(true);

        Label datos = new Label(pelicula.describirEstreno() + "  ·  " + pelicula.describirFormatos());
        datos.getStyleClass().add("etiqueta");
        datos.setWrapText(true);
        datos.setMaxWidth(250);

        VBox tarjeta = new VBox(10, marco, nombre, datos);
        tarjeta.setPrefWidth(250);
        tarjeta.setMaxWidth(250);
        tarjeta.setStyle("-fx-cursor: hand;");

        // Clic en la tarjeta: se guarda la película elegida y se abre su detalle
        tarjeta.setOnMouseClicked(e -> {
            consultarPeliculasController.seleccionarPelicula(pelicula);
            Navegador.irA(rootPane, "DetallePelicula.fxml");
        });
        return tarjeta;
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuOperador.fxml");
    }
}
