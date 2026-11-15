package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Habilitable;

import java.util.ArrayList;
import java.util.List;

/**
 * Sala del cinema con su colección de asientos.
 * Los asientos se generan solos a partir de la capacidad, en filas de 10:
 * A1..A10, B1..B10, etc. Así la capacidad y la cantidad de asientos siempre coinciden.
 */
public class Sala implements Habilitable, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private static final int ASIENTOS_POR_FILA = 10;

    private String codigo;
    private String nombre;
    private int capacidad;
    private EstadoSala estado;
    private List<Asiento> asientos;

    public Sala(String codigo, String nombre, int capacidad, EstadoSala estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.estado = estado;
        this.capacidad = capacidad;
        this.asientos = new ArrayList<>();
        generarAsientos();
    }

    /** Crea los asientos de la sala según la capacidad. */
    private void generarAsientos() {
        asientos.clear();
        for (int i = 0; i < capacidad; i++) {
            char fila = (char) ('A' + i / ASIENTOS_POR_FILA);
            int numero = i % ASIENTOS_POR_FILA + 1;
            asientos.add(new Asiento(fila, numero));
        }
    }

    @Override
    public boolean estaDisponible() {
        return estado == EstadoSala.DISPONIBLE;
    }

    @Override
    public void habilitar() {
        estado = EstadoSala.DISPONIBLE;
    }

    @Override
    public void deshabilitar() {
        estado = EstadoSala.FUERA_DE_SERVICIO;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    /** Al cambiar la capacidad se vuelven a crear los asientos. */
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
        generarAsientos();
    }

    public EstadoSala getEstado() {
        return estado;
    }

    public void setEstado(EstadoSala estado) {
        this.estado = estado;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    // Así se muestra en los ComboBox de la interfaz
    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }
}
