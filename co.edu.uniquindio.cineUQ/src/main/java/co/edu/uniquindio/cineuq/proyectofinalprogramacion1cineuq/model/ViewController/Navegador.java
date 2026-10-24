package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class Navegador {

    private static final String BASE =
            "/co/edu/uniquindio/cineuq/proyectofinalprogramacion1cineuq/";
    private static final double SEGUNDOS = 0.5;

    private Navegador() { }

    public static void irA(Node origen, String fxml) {
        URL destino = Navegador.class.getResource(BASE + fxml);
        if (destino == null) {
            System.err.println("No se encontró " + BASE + fxml);
            return;
        }

        Scene scene = origen.getScene();
        scene.setFill(Color.BLACK);
        Parent actual = scene.getRoot();

        FadeTransition salida = new FadeTransition(Duration.seconds(SEGUNDOS), actual);
        salida.setFromValue(1);
        salida.setToValue(0);
        salida.setOnFinished(e -> {
            try {
                Parent nueva = FXMLLoader.load(destino);
                nueva.setOpacity(0);
                scene.setRoot(nueva);

                FadeTransition entrada = new FadeTransition(Duration.seconds(SEGUNDOS), nueva);
                entrada.setFromValue(0);
                entrada.setToValue(1);
                entrada.play();
            } catch (IOException | RuntimeException ex) {
                ex.printStackTrace();
                actual.setOpacity(1);
            }
        });
        salida.play();
    }
}