package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.VentaYaCanceladaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Cancelable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Venta de una entrada (más los combos que se agreguen); conserva el historial para los reportes.
 * Los valores se guardan aparte porque las tarifas y el plan del cliente pueden cambiar después.
 */
public class Venta implements Cancelable, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private String codigo;
    private Cliente cliente;
    private Funcion funcion;
    private Asiento asiento;
    private TipoEntrada tipoEntrada;
    private Operador operador;
    private LocalDateTime fechaHoraVenta;
    private double tarifa;           // tarifa del tipo de entrada al momento de la venta
    private double valorBase;        // recargo de la función al momento de la venta
    private double valorEntrada;     // (tarifa + valorBase) ya con el descuento del plan
    private List<Combo> combos;      // un combo repetido = varias unidades
    private double valorCombos;      // ya con el descuento del plan
    private double valorPagado;      // valorEntrada + valorCombos
    private EstadoVenta estado;
    private Pago pago;               // pago con tarjeta de la compra (null en ventas de versiones anteriores)

    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento, TipoEntrada tipoEntrada,
                 Operador operador, LocalDateTime fechaHoraVenta, double tarifa, double valorBase,
                 double valorEntrada, List<Combo> combos, double valorCombos) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.tipoEntrada = tipoEntrada;
        this.operador = operador;
        this.fechaHoraVenta = fechaHoraVenta;
        this.tarifa = tarifa;
        this.valorBase = valorBase;
        this.valorEntrada = valorEntrada;
        this.combos = new ArrayList<>(combos);
        this.valorCombos = valorCombos;
        this.valorPagado = valorEntrada + valorCombos;
        this.estado = EstadoVenta.ACTIVA;
    }

    /** Cuántas unidades de ese combo tiene la venta. */
    public int contarCombo(Combo combo) {
        int cantidad = 0;
        for (Combo c : combos) {
            if (c == combo) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /** Los combos de la venta sin repetir (para mostrar "2 x Combo Pareja" una sola vez). */
    public List<Combo> obtenerCombosDistintos() {
        List<Combo> distintos = new ArrayList<>();
        for (Combo combo : combos) {
            if (!distintos.contains(combo)) {
                distintos.add(combo);
            }
        }
        return distintos;
    }

    /**
     * Solo para datos de la versión anterior: allí los combos eran un enum (model.legado.Combo).
     * Cada combo viejo se cambia por el combo del catálogo que tiene el mismo nombre.
     * Devuelve true si cambió algo.
     */
    public boolean actualizarCombosAnteriores(List<Combo> catalogo) {
        boolean cambio = false;
        List<Combo> nuevos = new ArrayList<>();
        for (Object elemento : combos) {
            if (elemento instanceof co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.legado.Combo) {
                String nombreViejo = ((co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.legado.Combo) elemento).getNombre();
                for (Combo combo : catalogo) {
                    if (combo.getNombre().equals(nombreViejo)) {
                        nuevos.add(combo);
                    }
                }
                cambio = true;
            } else {
                nuevos.add((Combo) elemento);
            }
        }
        combos = nuevos;
        return cambio;
    }

    /** Información de la entrada (el "tiquete") que se le muestra al cliente. */
    public String generarInformacionEntrada() {
        String texto = "========== CINEMAUQ ==========\n"
                + "Entrada N°: " + codigo + "\n"
                + "Película:   " + funcion.getPelicula().getNombre()
                + (funcion.getPelicula().getAnioEstreno() > 0 ? " (" + funcion.getPelicula().getAnioEstreno() + ")" : "") + "\n"
                + "Formato:    " + funcion.getFormato() + "\n"
                + "Idioma:     " + funcion.getIdioma() + "\n"
                + "Sala:       " + funcion.getSala().getNombre() + "\n"
                + "Fecha:      " + funcion.getFecha() + "  " + funcion.getHora() + "\n"
                + "Asiento:    " + asiento.getCodigo() + "\n"
                + "Cliente:    " + cliente.getNombre() + "\n"
                + "Plan:       " + cliente.getPlan().getNombre() + "\n"
                + "Tarifa " + tipoEntrada.getDescripcion() + ": $" + String.format("%,.0f", tarifa) + "\n";
        if (valorBase > 0) {
            texto += "Valor base función: $" + String.format("%,.0f", valorBase) + "\n";
        }
        if (calcularPorcentajeDescuento() > 0) {
            texto += "Descuento plan: -" + calcularPorcentajeDescuento() + "%\n";
        }
        texto += "Entrada: $" + String.format("%,.0f", valorEntrada) + "\n";

        if (!combos.isEmpty()) {
            texto += "Combos:\n";
            for (Combo combo : obtenerCombosDistintos()) {
                texto += "  " + contarCombo(combo) + " x " + combo.getNombre() + "\n";
            }
            texto += "Total combos: $" + String.format("%,.0f", valorCombos) + "\n";
        }

        texto += "TOTAL PAGADO: $" + String.format("%,.0f", valorPagado) + "\n";
        if (pago != null) {
            texto += pago.generarComprobante();
        }
        texto += "Vendida: " + fechaHoraVenta.toLocalDate() + " " + fechaHoraVenta.toLocalTime().withNano(0) + "\n"
                + "==============================";
        return texto;
    }

    /** Porcentaje de descuento que se aplicó a la entrada (ej.: 30), calculado con lo que se cobró. */
    public long calcularPorcentajeDescuento() {
        double sinDescuento = tarifa + valorBase;
        if (sinDescuento <= 0) {
            return 0;
        }
        return Math.round((1 - valorEntrada / sinDescuento) * 100);
    }

    /** Cancela la venta y deja el asiento libre otra vez. */
    @Override
    public void cancelar() throws VentaYaCanceladaException {
        if (estado == EstadoVenta.CANCELADA) {
            throw new VentaYaCanceladaException(codigo);
        }
        estado = EstadoVenta.CANCELADA;
        asiento.liberar();
    }

    @Override
    public boolean estaCancelada() {
        return estado == EstadoVenta.CANCELADA;
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public Operador getOperador() {
        return operador;
    }

    public LocalDateTime getFechaHoraVenta() {
        return fechaHoraVenta;
    }

    public double getTarifa() {
        return tarifa;
    }

    public double getValorBase() {
        return valorBase;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

    public List<Combo> getCombos() {
        return combos;
    }

    public double getValorCombos() {
        return valorCombos;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public EstadoVenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Venta{codigo='" + codigo + "', cliente=" + cliente.getNombre()
                + ", funcion=" + funcion.getCodigo() + ", asiento=" + asiento.getCodigo()
                + ", valorPagado=" + valorPagado + ", estado=" + estado + "}";
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
