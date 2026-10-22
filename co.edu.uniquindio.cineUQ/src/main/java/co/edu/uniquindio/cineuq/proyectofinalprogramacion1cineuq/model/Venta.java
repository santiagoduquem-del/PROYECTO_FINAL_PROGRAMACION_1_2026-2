package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;


import java.time.LocalDateTime;

/**
 * Venta de una entrada; conserva el historial necesario para los reportes.
 */
public class Venta {

    private String codigo;
    private Cliente cliente;
    private Funcion funcion;
    private Asiento asiento;
    private TipoEntrada tipoEntrada;
    private LocalDateTime fechaHoraVenta;
    private double valorPagado;
    private EstadoVenta estado;

    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento, TipoEntrada tipoEntrada, LocalDateTime fechaHoraVenta, double valorPagado, EstadoVenta estado) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.tipoEntrada = tipoEntrada;
        this.fechaHoraVenta = fechaHoraVenta;
        this.valorPagado = valorPagado;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Funcion getFuncion() {
        return funcion;
    }

    public void setFuncion(Funcion funcion) {
        this.funcion = funcion;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public LocalDateTime getFechaHoraVenta() {
        return fechaHoraVenta;
    }

    public void setFechaHoraVenta(LocalDateTime fechaHoraVenta) {
        this.fechaHoraVenta = fechaHoraVenta;
    }

    public double getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(double valorPagado) {
        this.valorPagado = valorPagado;
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
                + ", tipoEntrada=" + tipoEntrada + ", fechaHoraVenta=" + fechaHoraVenta
                + ", valorPagado=" + valorPagado + ", estado=" + estado + "}";
    }
}
