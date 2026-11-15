package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.PlanesController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Membresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.PlanMembresia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Pantalla Planes: paso 2 del registro de un cliente (elegir la membresía).
 * Cada tarjeta tiene en "userData" el nombre del plan (FREE, BASICO, ESTANDAR, PREMIUM).
 */
public class PlanesViewController {

    @FXML private AnchorPane rootPane;
    @FXML private Label lblSaludo;
    @FXML private Label lblPlanElegido;
    @FXML private VBox tarjetaFree;
    @FXML private VBox tarjetaBasico;
    @FXML private VBox tarjetaEstandar;
    @FXML private VBox tarjetaPremium;
    @FXML private Label checkFree;
    @FXML private Label checkBasico;
    @FXML private Label checkEstandar;
    @FXML private Label checkPremium;

    // Valores de cada tarjeta (se llenan con lo que puso el administrador)
    @FXML private Label lblPrecioFree;
    @FXML private Label lblEntradasFree;
    @FXML private Label lblCombosFree;
    @FXML private Label lblBeneficioFree;
    @FXML private Label lblPrecioBasico;
    @FXML private Label lblEntradasBasico;
    @FXML private Label lblCombosBasico;
    @FXML private Label lblBeneficioBasico;
    @FXML private Label lblPrecioEstandar;
    @FXML private Label lblEntradasEstandar;
    @FXML private Label lblCombosEstandar;
    @FXML private Label lblBeneficioEstandar;
    @FXML private Label lblPrecioPremium;
    @FXML private Label lblEntradasPremium;
    @FXML private Label lblCombosPremium;
    @FXML private Label lblBeneficioPremium;

    private final PlanesController planesController = new PlanesController();

    private PlanMembresia planSeleccionado;

    @FXML
    public void initialize() {
        String nombre = planesController.obtenerNombreCliente();
        if (!nombre.isEmpty()) {
            lblSaludo.setText("Hola, " + nombre + ". Paso 2 de 2: elige tu membresía");
        }

        mostrarValores(PlanMembresia.FREE, lblPrecioFree, lblEntradasFree, lblCombosFree, lblBeneficioFree);
        mostrarValores(PlanMembresia.BASICO, lblPrecioBasico, lblEntradasBasico, lblCombosBasico, lblBeneficioBasico);
        mostrarValores(PlanMembresia.ESTANDAR, lblPrecioEstandar, lblEntradasEstandar, lblCombosEstandar, lblBeneficioEstandar);
        mostrarValores(PlanMembresia.PREMIUM, lblPrecioPremium, lblEntradasPremium, lblCombosPremium, lblBeneficioPremium);
    }

    /** Escribe en una tarjeta el precio, los descuentos y el beneficio vigentes del plan. */
    private void mostrarValores(PlanMembresia plan, Label precio, Label entradas, Label combos, Label beneficio) {
        Membresia membresia = planesController.obtenerMembresia(plan);
        precio.setText("COP " + Conversor.aPesos(membresia.getPrecioMensual()).substring(1));   // sin el "$"
        entradas.setText(Math.round(membresia.getDescuentoEntradas() * 100) + " %");
        combos.setText(Math.round(membresia.getDescuentoCombos() * 100) + " %");
        beneficio.setText(membresia.getBeneficioExtra());
    }

    /** Se ejecuta al hacer clic en cualquiera de las tarjetas. */
    @FXML
    private void onSeleccionarPlan(MouseEvent evento) {
        Node tarjeta = (Node) evento.getSource();
        planSeleccionado = PlanMembresia.valueOf((String) tarjeta.getUserData());

        marcar(tarjetaFree, checkFree, planSeleccionado == PlanMembresia.FREE);
        marcar(tarjetaBasico, checkBasico, planSeleccionado == PlanMembresia.BASICO);
        marcar(tarjetaEstandar, checkEstandar, planSeleccionado == PlanMembresia.ESTANDAR);
        marcar(tarjetaPremium, checkPremium, planSeleccionado == PlanMembresia.PREMIUM);

        lblPlanElegido.setText("Plan seleccionado: " + planSeleccionado.getNombre());
    }

    /** Resalta (o deja normal) una tarjeta. */
    private void marcar(VBox tarjeta, Label check, boolean seleccionada) {
        tarjeta.getStyleClass().remove("plan-seleccionado");
        if (seleccionada) {
            tarjeta.getStyleClass().add("plan-seleccionado");
        }
        check.setVisible(seleccionada);
    }

    @FXML
    private void onRegistrarme() {
        try {
            planesController.registrarConPlan(planSeleccionado);
            Alertas.mostrarInfo("Registro exitoso",
                    "¡Bienvenido a CinemaUQ! Quedaste registrado con el plan " + planSeleccionado.getNombre() + ".");
            Navegador.iniciarEn(rootPane, "Login.fxml");
        } catch (CinemaException e) {
            Alertas.mostrarError("Registro", e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        Navegador.volverAtras(rootPane);
    }
}
