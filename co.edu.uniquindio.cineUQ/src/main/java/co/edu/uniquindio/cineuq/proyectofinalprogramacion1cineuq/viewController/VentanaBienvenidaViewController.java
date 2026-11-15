package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.VentanaBienvenidaController;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Reproduce la intro (secuencia de imágenes JPG) y luego abre el login.
 * Con un clic sobre la pantalla se puede saltar la intro.
 */
public class VentanaBienvenidaViewController {

    @FXML private StackPane rootPane;
    @FXML private ImageView imageView;

    private static final String BASE =
            "/co/edu/uniquindio/cineuq/proyectofinalprogramacion1cineuq/";
    private static final String CARPETA = BASE + "recursosGraficos/frames/";

    private static final double FPS = 24;
    private static final long NANOS_POR_FRAME = (long) (1e9 / FPS);
    private static final int ADELANTE = 12;      // frames precargados por delante
    private static final int MIN_INICIO = 10;    // buffer antes de arrancar
    private static final int HILOS = 4;          // hilos que decodifican los JPG
    private static final int MAX_ESPERAS = 300;  // ~5 segundos esperando un frame -> se salta al login

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
    private int esperas = 0;
    private boolean terminado = false;

    private final VentanaBienvenidaController ventanaBienvenidaController = new VentanaBienvenidaController();

    @FXML
    public void initialize() {
        // Mientras corre la intro se deja listo el sistema (Cine y datos iniciales)
        ventanaBienvenidaController.iniciarSistema();

        // Clic en cualquier parte: saltar la intro
        rootPane.setOnMouseClicked(e -> terminar());

        while (getClass().getResource(CARPETA + nombre(totalFrames)) != null) {
            totalFrames++;
        }
        System.out.println("Fotogramas encontrados: " + totalFrames);
        if (totalFrames == 0) {
            System.err.println("No se encontraron fotogramas en " + CARPETA);
            // Se espera a que la ventana esté lista y se va directo al login
            Platform.runLater(this::terminar);
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
                    if (cache.size() < Math.min(MIN_INICIO, totalFrames)) {
                        esperarFrame();
                        return;
                    }
                    proximoCambio = now;
                }

                if (now < proximoCambio) return;

                int siguiente = frameActual + 1;

                // Ya se mostraron todos los frames
                if (siguiente >= totalFrames) {
                    terminar();
                    return;
                }

                Image img = cache.get(siguiente);
                if (img == null) {
                    // No saltamos frames: esperamos a que cargue
                    precargar(siguiente);
                    esperarFrame();
                    return;
                }

                esperas = 0;
                imageView.setImage(img);
                frameActual = siguiente;
                proximoCambio = Math.max(proximoCambio + NANOS_POR_FRAME, now);
                precargar(siguiente);
            }
        };
        timer.start();
    }

    /** Si un frame tarda demasiado (por ejemplo, un archivo dañado), se va al login. */
    private void esperarFrame() {
        esperas++;
        if (esperas > MAX_ESPERAS) {
            System.err.println("La intro tardó demasiado en cargar, se abre el login.");
            terminar();
        }
    }

    private void terminar() {
        if (terminado) return;   // evita que se ejecute dos veces
        terminado = true;
        if (timer != null) {
            timer.stop();
        }
        cargador.shutdownNow();
        cache.clear();
        Navegador.irA(rootPane, "Login.fxml");
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
                            // false = carga completa aquí mismo; ya estamos en un hilo del pool,
                            // así que no se bloquea la ventana
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
