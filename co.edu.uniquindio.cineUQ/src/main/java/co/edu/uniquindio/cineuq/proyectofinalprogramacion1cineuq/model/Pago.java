package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Pago con tarjeta de una compra (simulado: no se conecta con ningún banco).
 * Por seguridad NO se guarda el número completo ni el CVV: solo los últimos 4 dígitos,
 * como hacen los datáfonos reales en el comprobante.
 * Si una compra tiene varias entradas, todas las ventas comparten el mismo pago.
 */
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    private TipoTarjeta tipoTarjeta;
    private Franquicia franquicia;
    private String ultimosDigitos;
    private String titular;
    private String documentoTitular;
    private int cuotas;              // 1 en débito
    private double valorAPagar;      // total de la compra
    private double montoIngresado;   // lo que el cliente autorizó
    private double valorDevuelto;    // montoIngresado - valorAPagar
    private LocalDateTime fechaHora;

    public Pago(TipoTarjeta tipoTarjeta, Franquicia franquicia, String ultimosDigitos, String titular,
                String documentoTitular, int cuotas, double valorAPagar, double montoIngresado) {
        this.tipoTarjeta = tipoTarjeta;
        this.franquicia = franquicia;
        this.ultimosDigitos = ultimosDigitos;
        this.titular = titular;
        this.documentoTitular = documentoTitular;
        this.cuotas = cuotas;
        this.valorAPagar = valorAPagar;
        this.montoIngresado = montoIngresado;
        this.valorDevuelto = montoIngresado - valorAPagar;
        this.fechaHora = LocalDateTime.now();
    }

    /** Ej.: "Visa Débito •••• 4242" o "Mastercard Crédito •••• 5100 (3 cuotas)". */
    public String describir() {
        String texto = franquicia.getNombre() + " " + tipoTarjeta.getNombre() + " •••• " + ultimosDigitos;
        if (tipoTarjeta == TipoTarjeta.CREDITO) {
            texto += " (" + cuotas + (cuotas == 1 ? " cuota)" : " cuotas)");
        }
        return texto;
    }

    /** Líneas del pago para el tiquete. */
    public String generarComprobante() {
        String texto = "Pago:       " + describir() + "\n"
                + "Titular:    " + titular + "\n"
                + "Total de la compra: $" + String.format("%,.0f", valorAPagar) + "\n"
                + "Monto recibido: $" + String.format("%,.0f", montoIngresado) + "\n";
        if (valorDevuelto > 0) {
            texto += "Devuelto a su cuenta: $" + String.format("%,.0f", valorDevuelto) + "\n";
        }
        return texto;
    }

    public TipoTarjeta getTipoTarjeta() { return tipoTarjeta; }
    public Franquicia getFranquicia() { return franquicia; }
    public String getUltimosDigitos() { return ultimosDigitos; }
    public String getTitular() { return titular; }
    public String getDocumentoTitular() { return documentoTitular; }
    public int getCuotas() { return cuotas; }
    public double getValorAPagar() { return valorAPagar; }
    public double getMontoIngresado() { return montoIngresado; }
    public double getValorDevuelto() { return valorDevuelto; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
