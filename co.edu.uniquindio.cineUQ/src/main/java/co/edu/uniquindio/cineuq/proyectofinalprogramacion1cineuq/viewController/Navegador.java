package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Cambia de pantalla con un efecto de fundido y recuerda las pantallas visitadas
 * para poder "volver atrás".
 * "origen" puede ser cualquier nodo de la pantalla actual (un botón, un campo, el panel raíz).
 */
public class Navegador {

    private static final String BASE =
            "/co/edu/uniquindio/cineuq/proyectofinalprogramacion1cineuq/";
    private static final double SEGUNDOS = 0.5;

    // Historial de pantallas: funciona como una pila (la última que entra es la primera que sale)
    private static final List<String> historial = new ArrayList<>();
    private static String pantallaActual;

    private Navegador() { }

    /** Va a otra pantalla y guarda la actual en el historial. */
    public static void irA(Node origen, String fxml) {
        if (pantallaActual != null && !pantallaActual.equals(fxml)) {
            historial.add(pantallaActual);
        }
        cargar(origen, fxml);
    }

    /** Vuelve a la pantalla anterior (si hay alguna). */
    public static void volverAtras(Node origen) {
        if (historial.isEmpty()) {
            return;
        }
        String anterior = historial.remove(historial.size() - 1);
        cargar(origen, anterior);
    }

    /**
     * Va a una pantalla borrando el historial.
     * Se usa al iniciar o cerrar sesión, para que "atrás" no regrese a pantallas de otra sesión.
     */
    public static void iniciarEn(Node origen, String fxml) {
        historial.clear();
        pantallaActual = null;
        cargar(origen, fxml);
    }

    /**
     * Abre una pantalla en una ventana aparte (por ejemplo, los combos).
     * La pantalla de atrás queda bloqueada hasta que se cierre la ventana.
     */
    public static void abrirVentana(Node origen, String fxml, String titulo) {
        URL destino = Navegador.class.getResource(BASE + fxml);
        if (destino == null) {
            Alertas.mostrarError("Error", "No se encontró la pantalla " + fxml);
            return;
        }
        try {
            Parent raiz = FXMLLoader.load(destino);
            Stage ventana = new Stage();
            ventana.setTitle(titulo);
            ventana.initOwner(origen.getScene().getWindow());
            ventana.initModality(Modality.WINDOW_MODAL);
            ventana.setScene(new Scene(raiz));
            ventana.showAndWait();   // espera hasta que el usuario cierre la ventana
        } catch (IOException | RuntimeException ex) {
            ex.printStackTrace();
            Alertas.mostrarError("Error", "No se pudo abrir la ventana " + fxml
                    + ".\nRevisa la consola para ver el detalle.");
        }
    }

    private static void cargar(Node origen, String fxml) {
        URL destino = Navegador.class.getResource(BASE + fxml);
        if (destino == null) {
            Alertas.mostrarError("Error", "No se encontró la pantalla " + fxml);
            return;
        }

        // Se desactiva para que un doble clic no lance dos transiciones
        origen.setDisable(true);

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
                pantallaActual = fxml;

                FadeTransition entrada = new FadeTransition(Duration.seconds(SEGUNDOS), nueva);
                entrada.setFromValue(0);
                entrada.setToValue(1);
                entrada.play();
            } catch (IOException | RuntimeException ex) {
                ex.printStackTrace();
                actual.setOpacity(1);
                origen.setDisable(false);
                // Dentro de una animación no se puede abrir un mensaje directamente:
                // Platform.runLater lo muestra apenas termina la animación
                Platform.runLater(() -> Alertas.mostrarError("Error", "No se pudo abrir la pantalla " + fxml
                        + ".\nRevisa la consola para ver el detalle."));
            }
        });
        salida.play();
    }
}
