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
import javafx.scene.layout.Region;

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

        // Si se llegó desde Horarios, la película y la función ya vienen elegidas
        Funcion funcionElegida = registrarVentaController.tomarFuncionElegida();
        if (funcionElegida != null) {
            // setValue no siempre dispara el onAction dentro de initialize,
            // por eso se llaman los métodos a mano
            cbPelicula.setValue(funcionElegida.getPelicula());
            onSeleccionarPelicula();                // muestra la carátula y carga las funciones
            cbFuncion.setValue(funcionElegida);
            onSeleccionarFuncion();                 // dibuja el mapa de asientos
        }
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
                + "\n" + pelicula.getClasificacion().getDescripcion()
                + "\n" + pelicula.describirFormatos());
    }

    @FXML
    private void onSeleccionarFuncion() {
        if (registrarVentaController.quitarCombosNoPermitidos(cbFuncion.getValue())) {
            Alertas.mostrarInfo("Combos quitados", "Se quitaron los combos exclusivos de VIP Bistró, "
                    + "porque la función elegida no es VIP Bistró.");
        }
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
        registrarVentaController.prepararCombos(cbFuncion.getValue());   // para mostrar los combos de esa función
        Navegador.abrirVentana(rootPane, "Combos.fxml", "Combos");
        actualizarValor();
    }

    @FXML
    private void onVender() {
        Funcion funcion = cbFuncion.getValue();
        Cliente cliente = cbCliente.getValue();
        TipoEntrada tipoEntrada = cbTipoEntrada.getValue();
        try {
            // 1. Revisar que la compra esté completa antes de cobrar
            registrarVentaController.validarCompra(funcion, asientosSeleccionados, cliente, tipoEntrada);

            // 2. Pago con tarjeta en una ventana aparte (espera hasta que se cierre)
            double total = registrarVentaController.calcularTotal(funcion, asientosSeleccionados, cliente, tipoEntrada);
            registrarVentaController.prepararPago(total, cliente);
            Navegador.abrirVentana(rootPane, "Pago.fxml", "Pago con tarjeta");
            Pago pago = registrarVentaController.tomarPagoRealizado();
            if (pago == null) {
                return;   // se cerró la ventana sin pagar: no se vende nada
            }

            // 3. Con el pago aprobado se registran las entradas
            List<Venta> ventas = registrarVentaController.venderEntradas(funcion,
                    asientosSeleccionados, cliente, tipoEntrada, pago);
            String tiquete = registrarVentaController.generarTiquete(ventas);
            txtTiquete.setText(tiquete);

            String codigos = "";
            for (Venta venta : ventas) {
                codigos += (codigos.isEmpty() ? "" : ", ") + venta.getCodigo();
            }
            // El tiquete completo también se muestra en una ventana, con espacio para leerlo
            Alertas.mostrarLista("Compra registrada",
                    "Se registraron " + ventas.size() + " entrada(s): " + codigos + ".", tiquete);

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

    /** Espacio vacío del ancho de un pasillo. */
    private Region crearPasillo() {
        Region pasillo = new Region();
        pasillo.setMinWidth(22);
        pasillo.setPrefWidth(22);
        return pasillo;
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

        // Sala normal: filas de 10 con botones grandes.
        // Sala grande (más de 10 por fila, ej.: IMAX 70 mm): botones más pequeños y dos pasillos,
        // quedando tres bloques de sillas: izquierda, centro y derecha.
        int porFila = funcion.getSala().getAsientosPorFila();
        boolean salaGrande = porFila > Sala.ASIENTOS_POR_FILA_NORMAL;
        int lado = porFila / 4;   // sillas de cada bloque lateral
        gridAsientos.setHgap(salaGrande ? 3 : 8);
        gridAsientos.setVgap(salaGrande ? 4 : 8);

        // Ancho de cada silla en salas grandes: se reparte el espacio del mapa (~1060 px) entre la fila
        int anchoSilla = 1010 / porFila - 3;
        if (anchoSilla > 34) {
            anchoSilla = 34;
        }
        if (anchoSilla < 24) {
            anchoSilla = 24;
        }

        if (salaGrande) {
            // Espacios vacíos que forman los pasillos
            gridAsientos.add(crearPasillo(), lado, 0);
            gridAsientos.add(crearPasillo(), porFila - lado + 1, 0);
        }

        for (Asiento asiento : funcion.getAsientos()) {
            Button boton = new Button(asiento.getCodigo());
            if (salaGrande) {
                boton.setStyle("-fx-min-width: " + anchoSilla + "px; -fx-pref-width: " + anchoSilla + "px; -fx-min-height: 28px;"
                        + " -fx-font-size: 9px; -fx-padding: 0; -fx-background-radius: 5;");
            }

            if (asientosSeleccionados.contains(asiento)) {
                boton.getStyleClass().add("asiento-seleccionado");
            } else if (asiento.estaDisponible()) {
                boton.getStyleClass().add("asiento-disponible");
            } else {
                boton.getStyleClass().add("asiento-ocupado");
            }

            boton.setOnAction(e -> seleccionarAsiento(asiento));

            int columna = asiento.getNumero() - 1;     // 1 -> columna 0
            if (salaGrande) {
                if (asiento.getNumero() > lado) {
                    columna++;                         // salta el pasillo izquierdo
                }
                if (asiento.getNumero() > porFila - lado) {
                    columna++;                         // salta el pasillo derecho
                }
            }
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
