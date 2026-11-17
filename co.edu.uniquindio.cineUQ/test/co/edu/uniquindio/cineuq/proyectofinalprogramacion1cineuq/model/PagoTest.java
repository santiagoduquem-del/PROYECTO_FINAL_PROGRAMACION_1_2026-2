package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagoTest {

    @Test
    void calculaElValorDevuelto() {
        Pago pago = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 30000, 35000);
        assertEquals(5000, pago.getValorDevuelto(), 0.001);
        assertEquals(30000, pago.getValorAPagar(), 0.001);
        assertEquals(35000, pago.getMontoIngresado(), 0.001);
    }

    @Test
    void siPagaExactoNoHayDevuelto() {
        Pago pago = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 30000, 30000);
        assertEquals(0, pago.getValorDevuelto(), 0.001);
    }

    @Test
    void describeUnPagoConDebito() {
        Pago pago = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 30000, 30000);
        assertEquals("Visa Débito •••• 4242", pago.describir());
    }

    @Test
    void describeUnPagoConCreditoYSusCuotas() {
        Pago tres = new Pago(TipoTarjeta.CREDITO, Franquicia.MASTERCARD, "5100", "Ana Gómez", "123", 3, 30000, 30000);
        assertEquals("Mastercard Crédito •••• 5100 (3 cuotas)", tres.describir());

        Pago una = new Pago(TipoTarjeta.CREDITO, Franquicia.MASTERCARD, "5100", "Ana Gómez", "123", 1, 30000, 30000);
        assertEquals("Mastercard Crédito •••• 5100 (1 cuota)", una.describir());
    }

    @Test
    void elComprobanteMuestraElDevueltoSoloSiHay() {
        Pago conDevuelto = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 30000, 35000);
        String texto = conDevuelto.generarComprobante();
        assertTrue(texto.contains("Ana Gómez"));
        assertTrue(texto.contains("Devuelto a su cuenta: $" + String.format("%,.0f", 5000.0)));

        Pago exacto = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 30000, 30000);
        assertFalse(exacto.generarComprobante().contains("Devuelto"));
    }

    @Test
    void soloGuardaLosUltimosCuatroDigitos() {
        Pago pago = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 100, 100);
        assertEquals("4242", pago.getUltimosDigitos());
        assertFalse(pago.generarComprobante().contains("4111"));
    }
}
