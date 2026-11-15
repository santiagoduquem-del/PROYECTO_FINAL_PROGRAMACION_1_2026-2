package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Habilitable;

import java.util.ArrayList;
import java.util.List;

/**
 * Sala del cinema con su colección de asientos.
 * Los asientos se generan solos a partir de la capacidad:
 *  - Hasta 260 asientos: filas de 10 (A1..A10, B1..B10, ...). 26 letras x 10 = 260.
 *  - Más de 260 (ej.: sala IMAX 70 mm): como ya no hay más letras, las filas se hacen
 *    más anchas (más sillas hacia los lados): asientos por fila = capacidad / 26, redondeado hacia arriba.
 * Así la capacidad y la cantidad de asientos siempre coinciden y las filas siempre van de la A a la Z.
 */
public class Sala implements Habilitable, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    public static final int ASIENTOS_POR_FILA_NORMAL = 10;
    public static final int MAXIMO_FILAS = 26;            // letras de la A a la Z
    public static final int CAPACIDAD_MAXIMA = 1000;

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

    /**
     * Cuántas sillas tiene cada fila. Normal: 10.
     * Si la sala tiene más de 260 asientos, la fila se ensancha para no pasar de la fila Z.
     */
    public int getAsientosPorFila() {
        if (capacidad <= ASIENTOS_POR_FILA_NORMAL * MAXIMO_FILAS) {
            return ASIENTOS_POR_FILA_NORMAL;
        }
        return (capacidad + MAXIMO_FILAS - 1) / MAXIMO_FILAS;   // división redondeada hacia arriba
    }

    /** Crea los asientos de la sala según la capacidad. */
    private void generarAsientos() {
        asientos.clear();
        if (capacidad <= 0 || capacidad > CAPACIDAD_MAXIMA) {
            return;   // capacidad inválida: Cine la rechaza con una excepción
        }
        int porFila = getAsientosPorFila();
        for (int i = 0; i < capacidad; i++) {
            char fila = (char) ('A' + i / porFila);
            int numero = i % porFila + 1;
            asientos.add(new Asiento(fila, numero));
        }
    }

    /** true si los asientos guardados siguen la distribución actual (sirve para datos de versiones viejas). */
    public boolean tieneDistribucionActual() {
        if (asientos.size() != capacidad) {
            return false;
        }
        int porFila = getAsientosPorFila();
        for (int i = 0; i < asientos.size(); i++) {
            Asiento asiento = asientos.get(i);
            if (asiento.getFila() != (char) ('A' + i / porFila) || asiento.getNumero() != i % porFila + 1) {
                return false;
            }
        }
        return true;
    }

    /** Vuelve a crear los asientos con la distribución actual. */
    public void reorganizarAsientos() {
        generarAsientos();
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
