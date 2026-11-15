package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.MenuAdministradorController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class MenuAdministradorViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;

    private final MenuAdministradorController menuAdministradorController = new MenuAdministradorController();

    @FXML
    private void onGestionarPeliculas() {
        Navegador.irA(rootPane, "GestionPeliculas.fxml");
    }

    @FXML
    private void onGestionarSalas() {
        Navegador.irA(rootPane, "GestionSalas.fxml");
    }

    @FXML
    private void onGestionarTarifas() {
        Navegador.irA(rootPane, "GestionTarifas.fxml");
    }

    @FXML
    private void onGestionarFunciones() {
        Navegador.irA(rootPane, "GestionFunciones.fxml");
    }

    @FXML
    private void onGenerarReportes() {
        Navegador.irA(rootPane, "Reportes.fxml");
    }

    @FXML
    private void onGestionarEmpleados() {
        Navegador.irA(rootPane, "GestionEmpleados.fxml");
    }

    @FXML
    private void onCerrarSesion() {
        menuAdministradorController.cerrarSesion();
        Navegador.iniciarEn(rootPane, "Login.fxml");
    }
}
