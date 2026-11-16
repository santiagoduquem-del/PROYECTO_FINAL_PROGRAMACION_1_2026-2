package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.PagoController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Franquicia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pago;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoTarjeta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.SaldoInsuficienteException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Ventana de pago con tarjeta. Mientras se escribe, la tarjeta de la izquierda
 * muestra el número, el titular, el vencimiento y el logo de la franquicia.
 */
public class PagoViewController {

    private static final String SIN_FRANQUICIA = "linear-gradient(to bottom right, #2c2c2c, #5a5a5a)";

    @FXML private AnchorPane rootPane;
    @FXML private AnchorPane tarjetaVista;
    @FXML private Label lblTipoTarjeta;
    @FXML private Label lblFranquicia;
    @FXML private ImageView imgFranquicia;
    @FXML private Label lblNumeroTarjeta;
    @FXML private Label lblTitularTarjeta;
    @FXML private Label lblVenceTarjeta;
    @FXML private Label lblTotal;
    @FXML private Label lblResultado;
    @FXML private RadioButton rbDebito;
    @FXML private RadioButton rbCredito;
    @FXML private ComboBox<Integer> cbCuotas;
    @FXML private TextField txtNumero;
    @FXML private Label lblAvisoNumero;
    @FXML private TextField txtTitular;
    @FXML private TextField txtDocumento;
    @FXML private ComboBox<Integer> cbMes;
    @FXML private ComboBox<Integer> cbAnio;
    @FXML private PasswordField txtCvv;
    @FXML private TextField txtMonto;

    private final PagoController pagoController = new PagoController();

    @FXML
    public void initialize() {
        // Solo se puede elegir uno de los dos: débito o crédito
        ToggleGroup grupoTipo = new ToggleGroup();
        rbDebito.setToggleGroup(grupoTipo);
        rbCredito.setToggleGroup(grupoTipo);
        rbDebito.setSelected(true);

        cbCuotas.getItems().setAll(pagoController.obtenerCuotas());
        cbMes.getItems().setAll(pagoController.obtenerMeses());
        cbAnio.getItems().setAll(pagoController.obtenerAnios());

        lblTotal.setText(Conversor.aPesos(pagoController.obtenerTotal()));

        // Se sugiere el cliente como titular (se puede cambiar si paga otra persona)
        Cliente cliente = pagoController.obtenerCliente();
        if (cliente != null) {
            txtTitular.setText(cliente.getNombre());
            txtDocumento.setText(cliente.getIdentificacion());
        }

        onCambiarTipo();
    }

    @FXML
    private void onCambiarTipo() {
        // Las cuotas solo aplican a crédito
        cbCuotas.setDisable(!rbCredito.isSelected());
        if (rbCredito.isSelected() && cbCuotas.getValue() == null) {
            cbCuotas.setValue(1);
        }
        onActualizarTarjeta();
    }

    /** Actualiza la tarjeta de vista previa con lo que se lleva escrito. */
    @FXML
    private void onActualizarTarjeta() {
        String numero = txtNumero.getText().replace(" ", "");
        Franquicia franquicia = pagoController.detectarFranquicia(numero);

        lblTipoTarjeta.setText(rbCredito.isSelected() ? "CRÉDITO" : "DÉBITO");
        lblNumeroTarjeta.setText(agruparNumero(numero, franquicia));
        lblTitularTarjeta.setText(txtTitular.getText().isBlank() ? "NOMBRE DEL TITULAR"
                : txtTitular.getText().toUpperCase());
        lblVenceTarjeta.setText(describirVencimiento());

        mostrarFranquicia(franquicia);

        if (numero.isEmpty()) {
            lblAvisoNumero.setText("Visa empieza por 4 · Mastercard por 51-55 o 2221-2720 · "
                    + "American Express por 34 o 37 · Diners Club por 36, 38 o 300-305");
        } else if (franquicia == null) {
            lblAvisoNumero.setText("Tarjeta no reconocida todavía.");
        } else {
            lblAvisoNumero.setText(franquicia.getNombre() + " · " + numero.length() + " de "
                    + franquicia.getCantidadDigitos() + " dígitos · CVV de " + franquicia.getDigitosCvv());
        }
    }

    /** Cambia el color de la tarjeta y su logo (si el logo no está, escribe el nombre). */
    private void mostrarFranquicia(Franquicia franquicia) {
        String fondo = SIN_FRANQUICIA;
        Image logo = null;
        String nombre = "";

        if (franquicia != null) {
            logo = Imagenes.cargarRecurso("recursosGraficos/" + franquicia.getLogo());
            nombre = franquicia.getNombre();
            if (franquicia == Franquicia.VISA) {
                fondo = "linear-gradient(to bottom right, #1a1f71, #2a5bd7)";
            } else if (franquicia == Franquicia.MASTERCARD) {
                fondo = "linear-gradient(to bottom right, #141414, #5a3a12)";
            } else if (franquicia == Franquicia.AMEX) {
                fondo = "linear-gradient(to bottom right, #006fcf, #3fb6e8)";
            } else if (franquicia == Franquicia.DINERS) {
                fondo = "linear-gradient(to bottom right, #3d4a56, #8796a5)";
            }
        }

        tarjetaVista.setStyle("-fx-background-color: " + fondo + "; -fx-background-radius: 20;");
        imgFranquicia.setImage(logo);
        lblFranquicia.setText(logo == null ? nombre : "");   // el texto solo se ve si no hay logo
    }

    /** "4242424242" -> "4242 4242 42•• ••••" (rellena con puntos hasta la cantidad de dígitos). */
    private String agruparNumero(String numero, Franquicia franquicia) {
        int total = (franquicia == null) ? 16 : franquicia.getCantidadDigitos();
        String texto = "";
        for (int i = 0; i < Math.max(total, numero.length()); i++) {
            if (i > 0 && i % 4 == 0) {
                texto += " ";
            }
            texto += (i < numero.length()) ? numero.charAt(i) : '•';
        }
        return texto;
    }

    /** Ej.: mes 3 y año 2029 -> "03/29". */
    private String describirVencimiento() {
        String mes = (cbMes.getValue() == null) ? "MM" : String.format("%02d", cbMes.getValue());
        String anio = (cbAnio.getValue() == null) ? "AA" : String.format("%02d", cbAnio.getValue() % 100);
        return mes + "/" + anio;
    }

    @FXML
    private void onPagar() {
        TipoTarjeta tipo = rbCredito.isSelected() ? TipoTarjeta.CREDITO : TipoTarjeta.DEBITO;
        try {
            Pago pago = pagoController.pagar(tipo, txtNumero.getText(), txtTitular.getText(),
                    txtDocumento.getText(), cbMes.getValue(), cbAnio.getValue(), txtCvv.getText(),
                    cbCuotas.getValue(), txtMonto.getText());

            String mensaje = "Pago aprobado con " + pago.describir() + ".";
            if (pago.getValorDevuelto() > 0) {
                mensaje += "\nSe cobraron " + Conversor.aPesos(pago.getValorAPagar())
                        + " y el restante, " + Conversor.aPesos(pago.getValorDevuelto())
                        + ", se ha devuelto a su cuenta.";
            }
            Alertas.mostrarInfo("Pago aprobado", mensaje);
            cerrar();
        } catch (SaldoInsuficienteException e) {
            lblResultado.setText("Saldo insuficiente");
            Alertas.mostrarError("Saldo insuficiente", e.getMessage());
        } catch (CinemaException e) {
            lblResultado.setText("");
            Alertas.mostrarError("Revise los datos de la tarjeta", e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();   // sin pago: la compra no se registra
    }

    private void cerrar() {
        Stage ventana = (Stage) rootPane.getScene().getWindow();
        ventana.close();
    }
}
