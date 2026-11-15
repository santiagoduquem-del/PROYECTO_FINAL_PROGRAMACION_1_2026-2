package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoDisponibleException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Cancelable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.ContadorEntradas;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.RegistroVentas;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Presentación de una película en una sala, fecha y hora determinadas.
 */
public class Funcion implements Cancelable, ContadorEntradas, RegistroVentas, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private String codigo;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double valorBase;
    private EstadoFuncion estado;

    // Asientos propios de la función: la ocupación es por función, no por sala.
    private List<Asiento> asientos;
    private List<Venta> listVentas;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase) {
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.valorBase = valorBase;
        this.estado = EstadoFuncion.PROGRAMADA;
        this.listVentas = new ArrayList<>();

        // Copia REAL de los asientos: objetos nuevos, para que ocupar un asiento
        // en esta función no lo ocupe en las demás funciones de la misma sala.
        this.asientos = new ArrayList<>();
        for (Asiento asientoSala : sala.getAsientos()) {
            asientos.add(new Asiento(asientoSala.getFila(), asientoSala.getNumero()));
        }
    }

    public Asiento buscarAsiento(String codigoAsiento) throws AsientoNoEncontradoException {
        for (Asiento asiento : asientos) {
            if (asiento.getCodigo().equalsIgnoreCase(codigoAsiento)) {
                return asiento;
            }
        }
        throw new AsientoNoEncontradoException(codigoAsiento);
    }

    public int contarAsientosDisponibles() {
        int disponibles = 0;
        for (Asiento asiento : asientos) {
            if (asiento.estaDisponible()) {
                disponibles++;
            }
        }
        return disponibles;
    }

    public int contarAsientosOcupados() {
        return asientos.size() - contarAsientosDisponibles();
    }

    /** Minuto del día en que empieza (ej.: 14:30 -> 870). */
    public int getMinutoInicio() {
        return hora.getHour() * 60 + hora.getMinute();
    }

    /** Minuto del día en que termina, según la duración de la película. */
    public int getMinutoFin() {
        return getMinutoInicio() + pelicula.getDuracionMinutos();
    }

    @Override
    public void cancelar() throws FuncionNoDisponibleException {
        if (estado != EstadoFuncion.PROGRAMADA) {
            throw new FuncionNoDisponibleException(codigo);
        }
        estado = EstadoFuncion.CANCELADA;
        // Al cancelar la función también se cancelan sus ventas activas.
        for (Venta venta : listVentas) {
            if (venta.getEstado() == EstadoVenta.ACTIVA) {
                venta.setEstado(EstadoVenta.CANCELADA);
                venta.getAsiento().liberar();
            }
        }
    }

    @Override
    public boolean estaCancelada() {
        return estado == EstadoFuncion.CANCELADA;
    }

    @Override
    public int contarEntradasVendidas() {
        int total = 0;
        for (Venta venta : listVentas) {
            if (venta.getEstado() == EstadoVenta.ACTIVA) {
                total++;
            }
        }
        return total;
    }

    @Override
    public void agregarVenta(Venta venta) {
        listVentas.add(venta);
    }

    @Override
    public List<Venta> getListVentas() {
        return listVentas;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public void setEstado(EstadoFuncion estado) {
        this.estado = estado;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    // Así se muestra en los ComboBox de la interfaz
    @Override
    public String toString() {
        return codigo + " - " + pelicula.getNombre() + " (" + pelicula.getFormato() + ") - "
                + fecha + " " + hora + " - " + sala.getNombre();
    }
}
