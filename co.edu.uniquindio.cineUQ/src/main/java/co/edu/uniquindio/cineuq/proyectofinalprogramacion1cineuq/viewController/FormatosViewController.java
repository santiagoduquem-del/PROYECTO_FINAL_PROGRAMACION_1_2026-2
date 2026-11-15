package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.FormatosController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Pantalla "Más formatos": 4 tarjetas fijas (2D, 3D, 4DX y VIP Bistró) escritas en Formatos.fxml.
 * Cada tarjeta y su botón tienen en userData el formato que abren (ej.: "CUATRO_DX").
 */
public class FormatosViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView imgDosD;
    @FXML private ImageView imgTresD;
    @FXML private ImageView imgCuatroDX;
    @FXML private ImageView imgVipBistro;

    private final FormatosController formatosController = new FormatosController();

    @FXML
    public void initialize() {
        // Las imágenes puestas en Scene Builder se recortan para llenar su recuadro
        Imagenes.llenarRecuadro(imgDosD);
        Imagenes.llenarRecuadro(imgTresD);
        Imagenes.llenarRecuadro(imgCuatroDX);
        Imagenes.llenarRecuadro(imgVipBistro);
    }

    /** Clic en una tarjeta o en su botón "Ver información". */
    @FXML
    private void onVerFormato(Event evento) {
        Node origen = (Node) evento.getSource();
        Formato formato = formatosController.seleccionarFormato((String) origen.getUserData());
        Navegador.irA(rootPane, formato.getVista());
    }

    @FXML
    private void onVolver() {
        Navegador.volverAtras(rootPane);
    }
}
