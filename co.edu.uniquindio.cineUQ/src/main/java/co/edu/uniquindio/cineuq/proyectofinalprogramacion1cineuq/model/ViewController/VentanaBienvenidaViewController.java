package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.ViewController;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VentanaBienvenidaViewController {

    @FXML private StackPane rootPane;
    @FXML private ImageView imageView;

    private static final String BASE =
            "/co/edu/uniquindio/cineuq/proyectofinalprogramacion1cineuq/";
    private static final String CARPETA = BASE + "recursosGraficos/frames/";
    private static final String SIGUIENTE = BASE + "Login.fxml";

    private static final double FPS = 24;
    private static final long NANOS_POR_FRAME = (long) (1e9 / FPS);
    private static final int ADELANTE = 12;     // frames precargados por delante
    private static final int MIN_INICIO = 10;   // buffer antes de arrancar
    private static final int HILOS = 4;         // hilos que decodifican los JPG
    private static final double SEGUNDOS_FUNDIDO = 0.8;

    private final Map<Integer, Image> cache = new ConcurrentHashMap<>();
    private final Set<Integer> pendientes = ConcurrentHashMap.newKeySet();
    private final ExecutorService cargador = Executors.newFixedThreadPool(HILOS, r -> {
        Thread t = new Thread(r, "cargador-frames");
        t.setDaemon(true);
        return t;
    });

    private AnimationTimer timer;
    private int totalFrames = 0;
    private int frameActual = -1;
    private long proximoCambio = -1;
    private boolean terminado = false;

    @FXML
    public void initialize() {
        while (getClass().getResource(CARPETA + nombre(totalFrames)) != null) {
            totalFrames++;
        }
        System.out.println("Fotogramas encontrados: " + totalFrames);
        if (totalFrames == 0) {
            System.err.println("No se encontraron fotogramas en " + CARPETA);
            return;
        }

        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(rootPane.widthProperty());
        imageView.fitHeightProperty().bind(rootPane.heightProperty());

        precargar(0);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (terminado) return;

                // Esperar a tener un buffer inicial
                if (proximoCambio < 0) {
                    if (cache.size() < Math.min(MIN_INICIO, totalFrames)) return;
                    proximoCambio = now;
                }

                if (now < proximoCambio) return;

                int siguiente = frameActual + 1;

                // Ya se mostraron todos los frames
                if (siguiente >= totalFrames) {
                    terminado = true;
                    stop();
                    irAlLogin();
                    return;
                }

                Image img = cache.get(siguiente);
                if (img == null) {
                    // No saltamos frames: esperamos a que cargue
                    precargar(siguiente);
                    return;
                }

                imageView.setImage(img);
                frameActual = siguiente;
                proximoCambio = Math.max(proximoCambio + NANOS_POR_FRAME, now);
                precargar(siguiente);
            }
        };
        timer.start();
    }

    private void irAlLogin() {
        System.out.println("Video terminado, cargando login...");
        Scene scene = rootPane.getScene();
        scene.setFill(Color.BLACK);

        FadeTransition salida = new FadeTransition(Duration.seconds(SEGUNDOS_FUNDIDO), rootPane);
        salida.setFromValue(1);
        salida.setToValue(0);
        salida.setOnFinished(e -> {
            try {
                Parent login = FXMLLoader.load(getClass().getResource(SIGUIENTE));
                login.setOpacity(0);
                scene.setRoot(login);

                cache.clear();
                cargador.shutdownNow();

                FadeTransition entrada = new FadeTransition(Duration.seconds(SEGUNDOS_FUNDIDO), login);
                entrada.setFromValue(0);
                entrada.setToValue(1);
                entrada.play();
            } catch (IOException | RuntimeException ex) {
                ex.printStackTrace();
                rootPane.setOpacity(1);
            }
        });
        salida.play();
    }

    private void precargar(int desde) {
        for (int k = 0; k <= ADELANTE; k++) {
            int i = desde + k;
            if (i >= totalFrames) break;
            if (!cache.containsKey(i) && pendientes.add(i)) {
                cargador.submit(() -> {
                    try {
                        URL url = getClass().getResource(CARPETA + nombre(i));
                        if (url != null) {
                            // Resolución original, carga en segundo plano (false)
                            cache.put(i, new Image(url.toExternalForm(), false));
                        }
                    } finally {
                        pendientes.remove(i);
                    }
                });
            }
        }
        // Liberar memoria de los frames ya mostrados
        cache.keySet().removeIf(key -> key < desde);
    }

    private String nombre(int indice) {
        return String.format("f_%04d.jpg", indice + 1);
    }
}