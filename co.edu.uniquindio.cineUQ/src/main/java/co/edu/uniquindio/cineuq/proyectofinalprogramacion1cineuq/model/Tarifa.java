package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
/**
 * Valor vigente de un tipo de entrada. El administrador lo puede modificar.
 */
public class Tarifa implements Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private TipoEntrada tipoEntrada;
    private double valor;

    public Tarifa(TipoEntrada tipoEntrada, double valor) {
        this.tipoEntrada = tipoEntrada;
        this.valor = valor;
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return tipoEntrada.getDescripcion() + " $" + valor;
    }
}
