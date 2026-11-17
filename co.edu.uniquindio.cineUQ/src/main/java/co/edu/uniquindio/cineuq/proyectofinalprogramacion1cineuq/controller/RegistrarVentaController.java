package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoOcupadoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller de la pantalla Generar venta de entrada (operador).
 * Una compra puede tener varios asientos: se registra UNA VENTA POR CADA ASIENTO
 * (así cada entrada se puede cancelar por separado). Los combos quedan en la primera venta.
 */
public class RegistrarVentaController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public RegistrarVentaController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    /**
     * Función elegida en la pantalla de Horarios (o null si se abrió la venta directamente).
     * Se "toma" una sola vez: después se borra para que la próxima venta empiece vacía.
     */
    public Funcion tomarFuncionElegida() {
        Funcion funcion = modelFactory.getFuncionParaVenta();
        modelFactory.setFuncionParaVenta(null);
        return funcion;
    }

    public List<Pelicula> obtenerPeliculasDisponibles() {
        return cine.consultarPeliculasDisponibles();
    }

    /** Funciones programadas de la película elegida. */
    public List<Funcion> obtenerFunciones(Pelicula pelicula) {
        if (pelicula == null) {
            return new ArrayList<>();
        }
        return cine.consultarFuncionesProgramadas(pelicula.getCodigo());
    }

    public List<Cliente> obtenerClientes() {
        return cine.consultarClientes();
    }

    /** Sugiere el tipo de entrada que corresponde al tipo de cliente (ESTUDIANTE -> ESTUDIANTE...). */
    public TipoEntrada sugerirTipoEntrada(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return TipoEntrada.valueOf(cliente.getTipoCliente().name());
    }

    /** true si el tipo de entrada elegido no es GENERAL ni el tipo del cliente (hay que actualizar el cliente). */
    public boolean requiereActualizarTipoCliente(Cliente cliente, TipoEntrada tipoEntrada) {
        return !cine.esTipoEntradaPermitido(cliente, tipoEntrada);
    }

    /** Cambia el tipo de cliente para que coincida con el tipo de entrada (ej.: NIÑO -> ESTUDIANTE). */
    public void actualizarTipoCliente(Cliente cliente, TipoEntrada tipoEntrada) throws CinemaException {
        if (cliente == null || tipoEntrada == null) {
            throw new DatoInvalidoException("Debe seleccionar el cliente y el tipo de entrada.");
        }
        cine.modificarCliente(cliente.getIdentificacion(), cliente.getNombre(), cliente.getTelefono(),
                cliente.getCorreo(), TipoCliente.valueOf(tipoEntrada.name()), cliente.getPlan());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    // ---------------- Combos de la venta en curso ----------------

    public List<Combo> obtenerCombosSeleccionados() {
        return modelFactory.getCombosSeleccionados();
    }

    /** Antes de abrir la ventana de combos: se le dice qué función es, para mostrar solo los que aplican. */
    public void prepararCombos(Funcion funcion) {
        modelFactory.setFuncionEnCombos(funcion);
    }

    /**
     * Si se cambia a una función que no es VIP Bistró, se quitan los combos exclusivos ya elegidos.
     * Devuelve true si se quitó alguno (para avisarle al operador).
     */
    public boolean quitarCombosNoPermitidos(Funcion funcion) {
        if (funcion == null) {
            return false;
        }
        List<Combo> elegidos = modelFactory.getCombosSeleccionados();
        boolean seQuito = false;
        for (int i = elegidos.size() - 1; i >= 0; i--) {   // de atrás hacia adelante para poder borrar
            if (!elegidos.get(i).sePuedeVenderEn(funcion.getFormato())) {
                elegidos.remove(i);
                seQuito = true;
            }
        }
        return seQuito;
    }

    public void limpiarCombos() {
        modelFactory.getCombosSeleccionados().clear();
    }

    /** Texto corto con los combos elegidos, ej.: "2 x Combo Personal, 1 x Combo Nachos". */
    public String describirCombos() {
        List<Combo> elegidos = modelFactory.getCombosSeleccionados();
        String texto = "";
        List<Combo> yaContados = new ArrayList<>();
        for (Combo combo : elegidos) {
            if (yaContados.contains(combo)) {
                continue;   // ese combo ya se escribió con su cantidad
            }
            yaContados.add(combo);
            int cantidad = 0;
            for (Combo c : elegidos) {
                if (c == combo) {
                    cantidad++;
                }
            }
            if (!texto.isEmpty()) {
                texto += ", ";
            }
            texto += cantidad + " x " + combo.getNombre();
        }
        if (texto.isEmpty()) {
            return "Sin combos";
        }
        return texto;
    }

    // ---------------- Precios ----------------

    private PlanMembresia planDe(Cliente cliente) {
        return (cliente == null) ? PlanMembresia.FREE : cliente.getPlan();
    }

    /** Valor de UNA entrada: (tarifa + valor base de la función) - descuento del plan. */
    public double calcularValorEntrada(TipoEntrada tipoEntrada, Cliente cliente, Funcion funcion) {
        if (tipoEntrada == null) {
            return 0;
        }
        double valorBase = (funcion == null) ? 0 : funcion.getValorBase();
        return cine.calcularValorEntrada(tipoEntrada, planDe(cliente), valorBase);
    }

    /** Precio normal de UNA entrada, sin plan: tarifa + valor base de la función. */
    public double calcularValorEntradaSinPlan(TipoEntrada tipoEntrada, Funcion funcion) {
        if (tipoEntrada == null) {
            return 0;
        }
        double valorBase = (funcion == null) ? 0 : funcion.getValorBase();
        return cine.calcularValorEntrada(tipoEntrada) + valorBase;
    }

    /** Precio normal de los combos elegidos, sin plan. */
    public double calcularValorCombosSinPlan() {
        return cine.calcularValorCombosSinDescuento(modelFactory.getCombosSeleccionados());
    }

    /** Porcentaje de descuento en entradas del plan del cliente (ej.: 30). */
    public long obtenerDescuentoEntradas(Cliente cliente) {
        return Math.round(cine.buscarMembresia(planDe(cliente)).getDescuentoEntradas() * 100);
    }

    /** Porcentaje de descuento en combos del plan del cliente (ej.: 30). */
    public long obtenerDescuentoCombos(Cliente cliente) {
        return Math.round(cine.buscarMembresia(planDe(cliente)).getDescuentoCombos() * 100);
    }

    /** Valor de los combos elegidos con el descuento de combos del plan. */
    public double calcularValorCombos(Cliente cliente) {
        return cine.calcularValorCombos(modelFactory.getCombosSeleccionados(), planDe(cliente));
    }

    /** Texto con el plan del cliente y sus descuentos, ej.: "Plan Premium: -30% entradas, -30% combos". */
    public String describirPlan(Cliente cliente) {
        if (cliente == null) {
            return "Seleccione un cliente para ver su plan";
        }
        Membresia membresia = cine.buscarMembresia(cliente.getPlan());   // valores vigentes del plan
        return "Cliente " + cliente.getTipoCliente().getDescripcion() + " · Plan " + membresia.getPlan().getNombre() + ": -" + Math.round(membresia.getDescuentoEntradas() * 100)
                + "% entradas, -" + Math.round(membresia.getDescuentoCombos() * 100) + "% combos";
    }

    // ---------------- Venta ----------------

    /**
     * Revisa que la compra esté completa ANTES de abrir la ventana de pago,
     * para no cobrar una compra que después no se puede registrar.
     */
    public void validarCompra(Funcion funcion, List<Asiento> asientos, Cliente cliente,
                              TipoEntrada tipoEntrada) throws CinemaException {
        if (funcion == null) {
            throw new DatoInvalidoException("Debe seleccionar la película y la función.");
        }
        if (cliente == null) {
            throw new DatoInvalidoException("Debe seleccionar el cliente.");
        }
        if (tipoEntrada == null) {
            throw new DatoInvalidoException("Debe seleccionar el tipo de entrada.");
        }
        if (!cine.esTipoEntradaPermitido(cliente, tipoEntrada)) {
            throw new DatoInvalidoException("El cliente " + cliente.getNombre() + " está registrado como "
                    + cliente.getTipoCliente().getDescripcion() + ". Solo puede comprar entrada General o "
                    + cliente.getTipoCliente().getDescripcion() + ".");
        }
        if (asientos == null || asientos.isEmpty()) {
            throw new DatoInvalidoException("Debe seleccionar al menos un asiento en el mapa de la sala.");
        }
        for (Asiento asiento : asientos) {
            if (!asiento.estaDisponible()) {
                throw new AsientoOcupadoException(asiento.getCodigo());
            }
        }
        for (Combo combo : modelFactory.getCombosSeleccionados()) {
            if (!combo.sePuedeVenderEn(funcion.getFormato())) {
                throw new DatoInvalidoException("El combo " + combo.getNombre()
                        + " es exclusivo de las funciones VIP Bistró. Quítelo en Elegir combos.");
            }
        }
    }

    /** Total de la compra: (valor de una entrada x cantidad de asientos) + combos. */
    public double calcularTotal(Funcion funcion, List<Asiento> asientos, Cliente cliente, TipoEntrada tipoEntrada) {
        return calcularValorEntrada(tipoEntrada, cliente, funcion) * asientos.size() + calcularValorCombos(cliente);
    }

    /** Deja listo el total y el cliente para la ventana de pago. */
    public void prepararPago(double total, Cliente cliente) {
        modelFactory.setTotalAPagar(total);
        modelFactory.setClienteEnPago(cliente);
        modelFactory.setPagoRealizado(null);
    }

    /** Pago aprobado en la ventana de pago, o null si el usuario cerró sin pagar. Se toma una sola vez. */
    public Pago tomarPagoRealizado() {
        Pago pago = modelFactory.getPagoRealizado();
        modelFactory.setPagoRealizado(null);
        return pago;
    }

    /**
     * Registra la compra ya pagada: una venta por cada asiento elegido, todas con el mismo tipo de entrada
     * y el mismo pago. El operador es el empleado que inició sesión. Devuelve las ventas para el tiquete.
     */
    public List<Venta> venderEntradas(Funcion funcion, List<Asiento> asientos, Cliente cliente,
                                      TipoEntrada tipoEntrada, Pago pago) throws CinemaException {
        validarCompra(funcion, asientos, cliente, tipoEntrada);
        if (pago == null) {
            throw new DatoInvalidoException("La compra no se ha pagado.");
        }

        Operador operador = null;
        if (modelFactory.getEmpleadoActual() instanceof Operador) {
            operador = (Operador) modelFactory.getEmpleadoActual();
        }

        List<Venta> ventas = new ArrayList<>();
        for (int i = 0; i < asientos.size(); i++) {
            // Los combos solo se cobran una vez: van en la primera venta de la compra
            List<Combo> combosDeEstaVenta = (i == 0) ? modelFactory.getCombosSeleccionados() : new ArrayList<>();
            Venta venta = cine.venderEntrada(funcion.getCodigo(), asientos.get(i).getCodigo(),
                    cliente.getIdentificacion(), tipoEntrada, combosDeEstaVenta, operador);
            venta.setPago(pago);   // todas las entradas de la compra comparten el pago
            ventas.add(venta);
        }

        limpiarCombos();   // la siguiente compra empieza sin combos
        ModelFactoryController.getInstance().guardar();   // persistencia
        return ventas;
    }

    /** Tiquete de la compra: si es un solo asiento, el de la venta; si son varios, un resumen. */
    public String generarTiquete(List<Venta> ventas) {
        if (ventas.size() == 1) {
            return ventas.get(0).generarInformacionEntrada();
        }
        Venta primera = ventas.get(0);
        Funcion funcion = primera.getFuncion();
        Cliente cliente = primera.getCliente();

        String texto = "========== CINEMAUQ ==========\n"
                + "Compra de " + ventas.size() + " entradas\n"
                + "Película:   " + funcion.getPelicula().getNombre()
                + (funcion.getPelicula().getAnioEstreno() > 0 ? " (" + funcion.getPelicula().getAnioEstreno() + ")" : "") + "\n"
                + "Formato:    " + funcion.getFormato() + "\n"
                + "Idioma:     " + funcion.getIdioma() + "\n"
                + "Sala:       " + funcion.getSala().getNombre() + "\n"
                + "Fecha:      " + funcion.getFecha() + "  " + funcion.getHora() + "\n"
                + "Cliente:    " + cliente.getNombre() + "  (Plan " + cliente.getPlan().getNombre() + ")\n"
                + "Tipo:       " + primera.getTipoEntrada().getDescripcion() + "\n";

        // Desglose del precio de cada entrada: tarifa + valor base - descuento del plan
        texto += "Cada entrada: tarifa " + Conversor.aPesos(primera.getTarifa());
        if (primera.getValorBase() > 0) {
            texto += " + valor base " + Conversor.aPesos(primera.getValorBase());
        }
        if (primera.calcularPorcentajeDescuento() > 0) {
            texto += " - " + primera.calcularPorcentajeDescuento() + "% plan";
        }
        texto += "\nEntradas:\n";

        double total = 0;
        for (Venta venta : ventas) {
            texto += "  " + venta.getCodigo() + "  Asiento " + venta.getAsiento().getCodigo()
                    + "  " + Conversor.aPesos(venta.getValorEntrada()) + "\n";
            total += venta.getValorPagado();
        }

        if (!primera.getCombos().isEmpty()) {
            texto += "Combos:\n";
            for (Combo combo : primera.obtenerCombosDistintos()) {
                texto += "  " + primera.contarCombo(combo) + " x " + combo.getNombre() + "\n";
            }
            texto += "Total combos: " + Conversor.aPesos(primera.getValorCombos()) + "\n";
        }

        texto += "TOTAL PAGADO: " + Conversor.aPesos(total) + "\n";
        if (primera.getPago() != null) {
            texto += primera.getPago().generarComprobante();
        }
        texto += "==============================";
        return texto;
    }
}
