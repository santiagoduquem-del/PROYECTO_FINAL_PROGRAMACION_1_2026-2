package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions;

/** El monto ingresado en el pago es menor que el total de la compra. */
public class SaldoInsuficienteException extends CinemaException {

    public SaldoInsuficienteException(double total, double montoIngresado) {
        super("Saldo insuficiente. El total es $" + String.format("%,.0f", total)
                + " y se ingresaron $" + String.format("%,.0f", montoIngresado)
                + ". Faltan $" + String.format("%,.0f", total - montoIngresado) + ".");
    }
}
