package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.util.Optional;

/**
 * Métodos de ayuda para mostrar mensajes sin repetir código en cada controlador.
 */
public class Alertas {

    private Alertas() { }

    public static void mostrarError(String titulo, String mensaje) {
        mostrar(Alert.AlertType.ERROR, titulo, mensaje);
    }

    public static void mostrarInfo(String titulo, String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, titulo, mensaje);
    }

    /** Pregunta "¿Seguro?" y devuelve true solo si el usuario pulsa Aceptar. */
    public static boolean confirmar(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }

    /**
     * Mensaje con un texto largo (por ejemplo, una lista) en un recuadro que se puede
     * desplazar, seleccionar y copiar.
     */
    public static void mostrarLista(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);

        TextArea texto = new TextArea(contenido);
        texto.setEditable(false);
        texto.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 14px;");
        texto.setPrefSize(760, 380);

        alerta.getDialogPane().setContent(texto);
        alerta.setResizable(true);
        alerta.showAndWait();
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
