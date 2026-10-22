package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Tipos de entrada con su tarifa (valores ilustrativos, ajustables por el grupo).
 */
public enum TipoEntrada {
    GENERAL(15000),
    ESTUDIANTE(12000),
    NIÑO(10000),
    ADULTO_MAYOR(11000);

    private final double valor;

    TipoEntrada(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
