package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoOcupadoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Ocupable;

/**
 * Asiento de una sala o función (ej.: A1, A2, B1).
 * El código se arma con la fila y el número, así nunca quedan inconsistentes.
 */
public class Asiento implements Ocupable, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private String codigo;
    private char fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
        this.codigo = "" + fila + numero;   // ej.: 'A' y 1 -> "A1"
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    @Override
    public boolean estaDisponible() {
        return estado == EstadoAsiento.DISPONIBLE;
    }

    @Override
    public void ocupar() throws AsientoOcupadoException {
        if (!estaDisponible()) {
            throw new AsientoOcupadoException(codigo);
        }
        estado = EstadoAsiento.OCUPADO;
    }

    @Override
    public void liberar() {
        estado = EstadoAsiento.DISPONIBLE;
    }

    public String getCodigo() {
        return codigo;
    }

    public char getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return "Asiento{codigo='" + codigo + "', estado=" + estado + "}";
    }
}
