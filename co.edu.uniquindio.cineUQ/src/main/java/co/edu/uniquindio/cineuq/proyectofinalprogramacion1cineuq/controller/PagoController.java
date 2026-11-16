package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Franquicia;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pago;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.TipoTarjeta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de la ventana de pago con tarjeta (se abre desde Generar venta).
 * El pago es simulado: el monto que se escribe hace de "saldo" de la cuenta.
 */
public class PagoController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public PagoController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    public double obtenerTotal() {
        return modelFactory.getTotalAPagar();
    }

    /** Cliente de la compra, para sugerir el titular de la tarjeta (puede ser otra persona). */
    public Cliente obtenerCliente() {
        return modelFactory.getClienteEnPago();
    }

    /** Franquicia según cómo empieza el número (null si no se reconoce todavía). */
    public Franquicia detectarFranquicia(String numero) {
        return Franquicia.detectar(numero);
    }

    public List<Integer> obtenerMeses() {
        List<Integer> meses = new ArrayList<>();
        for (int mes = 1; mes <= 12; mes++) {
            meses.add(mes);
        }
        return meses;
    }

    /** Este año y los 10 siguientes. */
    public List<Integer> obtenerAnios() {
        List<Integer> anios = new ArrayList<>();
        int actual = LocalDate.now().getYear();
        for (int anio = actual; anio <= actual + 10; anio++) {
            anios.add(anio);
        }
        return anios;
    }

    public List<Integer> obtenerCuotas() {
        return List.of(1, 2, 3, 6, 12, 24, 36);
    }

    /**
     * Procesa el pago. Si se aprueba, queda guardado para que Generar venta registre la compra.
     * El monto llega como texto (ej.: "45.000" o "45000").
     */
    public Pago pagar(TipoTarjeta tipoTarjeta, String numero, String titular, String documento,
                      Integer mes, Integer anio, String cvv, Integer cuotas, String montoTexto)
            throws CinemaException {
        double monto = convertirMonto(montoTexto);
        Pago pago = cine.procesarPago(tipoTarjeta, numero, titular, documento,
                (mes == null) ? 0 : mes, (anio == null) ? 0 : anio, cvv,
                (cuotas == null) ? 0 : cuotas, obtenerTotal(), monto);
        modelFactory.setPagoRealizado(pago);
        return pago;
    }

    /** Pasa "45.000", "$45,000" o "45000" a número. */
    private double convertirMonto(String texto) throws DatoInvalidoException {
        if (texto == null) {
            throw new DatoInvalidoException("Ingrese el monto a pagar.");
        }
        String limpio = texto.replace("$", "").replace(".", "").replace(",", "").replace(" ", "");
        if (limpio.isEmpty()) {
            throw new DatoInvalidoException("Ingrese el monto a pagar.");
        }
        try {
            return Double.parseDouble(limpio);
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El monto solo puede tener números (ej.: 45000).");
        }
    }
}
