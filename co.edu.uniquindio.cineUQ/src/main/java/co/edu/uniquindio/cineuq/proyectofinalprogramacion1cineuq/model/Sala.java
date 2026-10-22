package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Sala del cinema con su colección de asientos.
 */
public class Sala {

    private String codigo;
    private String nombre;
    private int capacidad;
    private EstadoSala estado;
    private List<Asiento> asientos;

    public Sala(String codigo, String nombre, int capacidad, EstadoSala estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.estado = estado;
        this.asientos = new ArrayList<>();
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

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
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

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    @Override
    public String toString() {
        return "Sala{codigo='" + codigo + "', nombre='" + nombre + "', capacidad=" + capacidad
                + ", estado=" + estado + ", asientos=" + asientos.size() + "}";
    }
}
