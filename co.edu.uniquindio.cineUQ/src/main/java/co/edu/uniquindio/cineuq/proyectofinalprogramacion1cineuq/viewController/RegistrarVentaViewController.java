package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.Conversor;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.RegistrarVentaController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla "Generar venta de entrada" del operador.
 * Pasos: película -> función -> cliente -> tipo de entrada -> asientos (uno o varios) -> Vender.
 */
public class RegistrarVentaViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private ComboBox<Pelicula> cbPelicula;
    @FXML private ComboBox<Funcion> cbFuncion;
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<TipoEntrada> cbTipoEntrada;
    @FXML private Label lblAsiento;
    @FXML private Label lblValor;
    @FXML private Label lblDisponibilidad;
    @FXML private GridPane gridAsientos;
    @FXML private TextArea txtTiquete;
    @FXML private ImageView imgCaratula;
    @FXML private Label lblInfoPelicula;
    @FXML private Label lblPlan;
    @FXML private Label lblCombos;
    @FXML private Label lblDetalleValor;

    private final RegistrarVentaController registrarVentaController = new RegistrarVentaController();

    // Asientos marcados en el mapa (en amarillo) para esta compra
    private final List<Asiento> asientosSeleccionados = new ArrayList<>();

    @FXML
    public void initialize() {
        cbPelicula.getItems().setAll(registrarVentaController.obtenerPeliculasDisponibles());
        cbCliente.getItems().setAll(registrarVentaController.obtenerClientes());
        cbTipoEntrada.getItems().setAll(TipoEntrada.values());
        lblPlan.setWrapText(true);   // "Cliente ... · Plan ...: -x% entradas, -y% combos" puede ocupar dos líneas

        // Cada compra nueva empieza sin combos
        registrarVentaController.limpiarCombos();
        actualizarValor();
    }

    @FXML
    private void onSeleccionarPelicula() {
        Pelicula pelicula = cbPelicula.getValue();
        if (pelicula != null) {
            cbFuncion.getItems().setAll(registrarVentaController.obtenerFunciones(pelicula));
        } else {
            cbFuncion.getItems().clear();
        }
        cbFuncion.setValue(null);
        asientosSeleccionados.clear();
        mostrarPelicula(pelicula);
        dibujarMapa();
        actualizarValor();
    }

    /** Muestra la carátula y los datos de la película elegida (los que puso el administrador). */
    private void mostrarPelicula(Pelicula pelicula) {
        if (pelicula == null) {
            Imagenes.mostrar(imgCaratula, null);
            lblInfoPelicula.setText("Seleccione una película");
            return;
        }
        Imagenes.mostrar(imgCaratula, pelicula.getRutaCaratula());
        lblInfoPelicula.setText(pelicula.getNombre()
                + "\n" + pelicula.describirEstreno()
                + "\n" + pelicula.getGenero()
                + "\n" + pelicula.getDuracionMinutos() + " minutos"
                + "\n" + pelicula.getClasificacion().getDescripcion());
    }

    @FXML
    private void onSeleccionarFuncion() {
        asientosSeleccionados.clear();
        dibujarMapa();
        actualizarValor();   // el valor base de la función cambia el precio
    }

    // Al elegir el cliente se sugiere el tipo de entrada según su tipo de cliente
    @FXML
    private void onSeleccionarCliente() {
        if (cbCliente.getValue() != null) {
            cbTipoEntrada.setValue(registrarVentaController.sugerirTipoEntrada(cbCliente.getValue()));
        }
        lblPlan.setText(registrarVentaController.describirPlan(cbCliente.getValue()));
        actualizarValor();
    }

    /**
     * General se puede vender a cualquier cliente. Si se elige otro tipo distinto al del cliente
     * (ej.: cliente NIÑO y entrada ESTUDIANTE), se pregunta si desea actualizar el tipo de cliente.
     */
    @FXML
    private void onSeleccionarTipoEntrada() {
        Cliente cliente = cbCliente.getValue();
        TipoEntrada tipo = cbTipoEntrada.getValue();

        if (registrarVentaController.requiereActualizarTipoCliente(cliente, tipo)) {
            boolean actualizar = Alertas.confirmar("Actualizar tipo de cliente",
                    cliente.getNombre() + " está registrado como " + cliente.getTipoCliente().getDescripcion() + ".\n\n"
                    + "Para venderle una entrada " + tipo.getDescripcion() + " hay que actualizar su tipo de cliente.\n"
                    + "¿Desea cambiarlo a " + tipo.getDescripcion() + "?\n\n"
                    + "(La entrada General se puede vender a cualquier cliente sin cambiar nada.)");
            if (actualizar) {
                try {
                    registrarVentaController.actualizarTipoCliente(cliente, tipo);
                    lblPlan.setText(registrarVentaController.describirPlan(cliente));
                    Alertas.mostrarInfo("Cliente actualizado",
                            cliente.getNombre() + " ahora es cliente " + tipo.getDescripcion() + ".");
                } catch (CinemaException e) {
                    Alertas.mostrarError("No se pudo actualizar el cliente", e.getMessage());
                    cbTipoEntrada.setValue(registrarVentaController.sugerirTipoEntrada(cliente));
                }
            } else {
                // Vuelve al tipo que corresponde al cliente
                cbTipoEntrada.setValue(registrarVentaController.sugerirTipoEntrada(cliente));
            }
        }
        actualizarValor();
    }

    /** Abre la ventana de combos; al cerrarla se actualiza el resumen y el total. */
    @FXML
    private void onElegirCombos() {
        Navegador.abrirVentana(rootPane, "Combos.fxml", "Combos");
        actualizarValor();
    }

    @FXML
    private void onVender() {
        try {
            List<Venta> ventas = registrarVentaController.venderEntradas(cbFuncion.getValue(),
                    asientosSeleccionados, cbCliente.getValue(), cbTipoEntrada.getValue());
            txtTiquete.setText(registrarVentaController.generarTiquete(ventas));

            String codigos = "";
            for (Venta venta : ventas) {
                codigos += (codigos.isEmpty() ? "" : ", ") + venta.getCodigo();
            }
            Alertas.mostrarInfo("Compra registrada",
                    "Se registraron " + ventas.size() + " entrada(s): " + codigos + ".");

            asientosSeleccionados.clear();
            dibujarMapa();
            actualizarValor();   // la compra dejó los combos en cero
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo realizar la venta", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        cbPelicula.setValue(null);
        cbFuncion.getItems().clear();
        mostrarPelicula(null);
        cbCliente.setValue(null);
        cbTipoEntrada.setValue(null);
        txtTiquete.clear();
        registrarVentaController.limpiarCombos();
        asientosSeleccionados.clear();
        actualizarValor();
        dibujarMapa();
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuOperador.fxml");
    }

    /** Primer clic marca el asiento; segundo clic lo desmarca. */
    private void seleccionarAsiento(Asiento asiento) {
        if (asientosSeleccionados.contains(asiento)) {
            asientosSeleccionados.remove(asiento);
        } else {
            asientosSeleccionados.add(asiento);
        }
        dibujarMapa();
        actualizarValor();
    }

    /** Muestra los combos elegidos y el total: entradas (con valor base y plan) + combos. */
    private void actualizarValor() {
        Cliente cliente = cbCliente.getValue();
        Funcion funcion = cbFuncion.getValue();
        double porEntrada = registrarVentaController.calcularValorEntrada(cbTipoEntrada.getValue(), cliente, funcion);
        int cantidad = asientosSeleccionados.size();
        double entradas = porEntrada * cantidad;
        double combos = registrarVentaController.calcularValorCombos(cliente);

        lblCombos.setText("Combos: " + registrarVentaController.describirCombos());
        lblValor.setText(Conversor.aPesos(entradas + combos));

        String detalle = cantidad + " entrada(s) x " + Conversor.aPesos(porEntrada) + " + combos " + Conversor.aPesos(combos);

        // Precio normal (sin plan) para que el cliente vea cuánto ahorra
        double normalEntrada = registrarVentaController.calcularValorEntradaSinPlan(cbTipoEntrada.getValue(), funcion);
        double normalCombos = registrarVentaController.calcularValorCombosSinPlan();
        if (normalEntrada > 0) {
            detalle += "\nPrecio normal por entrada: " + Conversor.aPesos(normalEntrada);
            if (funcion != null && funcion.getValorBase() > 0) {
                detalle += " (incluye valor base " + Conversor.aPesos(funcion.getValorBase()) + ")";
            }
        }
        double ahorro = (normalEntrada * cantidad + normalCombos) - (entradas + combos);
        if (ahorro > 0) {
            detalle += "\nDescuento del plan: -" + registrarVentaController.obtenerDescuentoEntradas(cliente)
                    + "% entradas, -" + registrarVentaController.obtenerDescuentoCombos(cliente) + "% combos"
                    + "\nSin plan pagaría " + Conversor.aPesos(normalEntrada * cantidad + normalCombos)
                    + " · Ahorra " + Conversor.aPesos(ahorro);
        }
        lblDetalleValor.setText(detalle);
    }

    /**
     * Dibuja un botón por cada asiento de la función, ubicado según su fila y número.
     * Los ocupados también se pueden marcar: al vender, el sistema mostrará
     * el error "asiento ocupado" (validación que pide el enunciado).
     */
    private void dibujarMapa() {
        gridAsientos.getChildren().clear();
        Funcion funcion = cbFuncion.getValue();

        if (funcion == null) {
            lblDisponibilidad.setText("Seleccione una función para ver los asientos");
            lblAsiento.setText("Ninguno");
            return;
        }

        lblDisponibilidad.setText("Total: " + funcion.getAsientos().size()
                + "     Disponibles: " + funcion.contarAsientosDisponibles()
                + "     Ocupados: " + funcion.contarAsientosOcupados()
                + "     (puede marcar varios; clic de nuevo para quitar)");

        for (Asiento asiento : funcion.getAsientos()) {
            Button boton = new Button(asiento.getCodigo());

            if (asientosSeleccionados.contains(asiento)) {
                boton.getStyleClass().add("asiento-seleccionado");
            } else if (asiento.estaDisponible()) {
                boton.getStyleClass().add("asiento-disponible");
            } else {
                boton.getStyleClass().add("asiento-ocupado");
            }

            boton.setOnAction(e -> seleccionarAsiento(asiento));

            int columna = asiento.getNumero() - 1;     // 1 -> columna 0
            int fila = asiento.getFila() - 'A';        // 'A' -> fila 0
            gridAsientos.add(boton, columna, fila);
        }

        // Lista de asientos marcados, ej.: "C3, C4, C5"
        String codigos = "";
        for (Asiento asiento : asientosSeleccionados) {
            codigos += (codigos.isEmpty() ? "" : ", ") + asiento.getCodigo();
        }
        lblAsiento.setText(codigos.isEmpty() ? "Ninguno" : codigos);
    }
}
