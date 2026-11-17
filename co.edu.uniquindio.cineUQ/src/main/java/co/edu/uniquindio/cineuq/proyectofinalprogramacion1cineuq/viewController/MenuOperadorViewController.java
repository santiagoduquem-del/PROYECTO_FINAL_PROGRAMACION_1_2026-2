package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.MenuOperadorController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MenuOperadorViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;

    private final MenuOperadorController menuOperadorController = new MenuOperadorController();

    /**
     * Como en las páginas de cine: la venta empieza en la cartelera.
     * Cartelera -> detalle de la película -> Comprar entradas -> horario -> Generar venta.
     */
    @FXML
    private void onGenerarVenta() {
        Navegador.irA(rootPane, "RegistrarVenta.fxml");
    }

    @FXML
    private void onConsultarPeliculas() {
        Navegador.irA(rootPane, "ConsultarPeliculas.fxml");
    }

    @FXML
    private void onConsultarFunciones() {
        menuOperadorController.prepararConsultaFunciones();
        Navegador.irA(rootPane, "ConsultarFunciones.fxml");
    }

    @FXML
    private void onCancelarVenta() {
        Navegador.irA(rootPane, "CancelarVenta.fxml");
    }

    @FXML
    private void onRegistrarClientes() {
        Navegador.irA(rootPane, "RegistrarCliente.fxml");
    }

    @FXML
    private void onCerrarSesion() {
        menuOperadorController.cerrarSesion();
        Navegador.iniciarEn(rootPane, "Login.fxml");
    }
}
