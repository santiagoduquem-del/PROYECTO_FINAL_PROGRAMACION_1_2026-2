package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;



/**
 * Asiento de una sala o función (ej.: A1, A2, B1).
 */
public class Asiento {

    private String codigo;
    private char fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(String codigo, char fila, int numero, EstadoAsiento estado) {
        this.codigo = codigo;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public char getFila() {
        return fila;
    }

    public void setFila(char fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Asiento{codigo='" + codigo + "', fila=" + fila + ", numero=" + numero + ", estado=" + estado + "}";
    }
}
