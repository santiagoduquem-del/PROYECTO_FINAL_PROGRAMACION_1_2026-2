package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * Métodos de ayuda para elegir y mostrar las imágenes de las películas.
 */
public class Imagenes {

    private Imagenes() { }

    /**
     * Abre el explorador de archivos para elegir una imagen (png o jpg).
     * Devuelve la ruta de la imagen como texto, o null si el usuario cerró la ventana sin elegir.
     */
    public static String elegirImagen(Window ventana, String titulo) {
        FileChooser selector = new FileChooser();
        selector.setTitle(titulo);
        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes (png, jpg)", "*.png", "*.jpg", "*.jpeg"));
        File archivo = selector.showOpenDialog(ventana);
        if (archivo == null) {
            return null;
        }
        return archivo.toURI().toString();   // ej.: file:/home/usuario/caratula.jpg
    }

    /**
     * Muestra la imagen de esa ruta en el ImageView, llenando TODO el recuadro (sin espacios vacíos).
     * Si no hay ruta o la imagen falla, deja el recuadro vacío.
     */
    public static void mostrar(ImageView visor, String ruta) {
        visor.setViewport(null);
        if (ruta == null) {
            visor.setImage(null);
            return;
        }
        try {
            Image imagen = new Image(ruta);
            if (imagen.isError()) {
                visor.setImage(null);
            } else {
                visor.setImage(imagen);
                recortarParaLlenar(visor, imagen);
            }
        } catch (IllegalArgumentException e) {
            visor.setImage(null);
        }
    }

    /**
     * Para imágenes puestas en Scene Builder: si el ImageView ya tiene imagen,
     * la recorta para que llene todo el recuadro (sin franjas vacías).
     */
    public static void llenarRecuadro(ImageView visor) {
        if (visor != null && visor.getImage() != null && !visor.getImage().isError()) {
            visor.setPreserveRatio(true);
            recortarParaLlenar(visor, visor.getImage());
        }
    }

    /**
     * Recorta la imagen (centrada) para que tenga la misma proporción que el recuadro.
     * Así, al escalarla, cubre todo el recuadro sin deformarse y sin dejar franjas vacías.
     *
     * Ejemplo: banner de 860 x 330 (muy ancho) y una foto cuadrada de 1000 x 1000:
     * se toma solo la franja central de 1000 x 384 y esa franja se escala al banner.
     */
    private static void recortarParaLlenar(ImageView visor, Image imagen) {
        double anchoRecuadro = visor.getFitWidth();
        double altoRecuadro = visor.getFitHeight();
        double anchoImagen = imagen.getWidth();
        double altoImagen = imagen.getHeight();

        if (anchoRecuadro <= 0 || altoRecuadro <= 0 || anchoImagen <= 0 || altoImagen <= 0) {
            return;   // sin medidas no se puede calcular el recorte
        }

        double proporcionRecuadro = anchoRecuadro / altoRecuadro;
        double proporcionImagen = anchoImagen / altoImagen;

        if (proporcionImagen > proporcionRecuadro) {
            // La imagen es más ancha que el recuadro: se recortan los lados
            double anchoRecorte = altoImagen * proporcionRecuadro;
            double x = (anchoImagen - anchoRecorte) / 2;
            visor.setViewport(new Rectangle2D(x, 0, anchoRecorte, altoImagen));
        } else {
            // La imagen es más alta que el recuadro: se recorta arriba y abajo
            double altoRecorte = anchoImagen / proporcionRecuadro;
            double y = (altoImagen - altoRecorte) / 2;
            visor.setViewport(new Rectangle2D(0, y, anchoImagen, altoRecorte));
        }
    }
}
