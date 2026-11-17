package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pago y Franquicia")
class PagoYFranquiciaTest {

    @Test
    @DisplayName("Franquicia.detectar reconoce la tarjeta por sus primeros dígitos")
    void detectar() {
        assertEquals(Franquicia.VISA, Franquicia.detectar("4"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("51"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("55"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("2221"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("2720"));
        assertEquals(Franquicia.AMEX, Franquicia.detectar("34"));
        assertEquals(Franquicia.AMEX, Franquicia.detectar("37"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("36"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("38"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("300"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("305"));
        assertEquals(Franquicia.VISA, Franquicia.detectar("4242 4242"));   // ignora espacios
    }

    @Test
    @DisplayName("Franquicia.detectar devuelve null si todavía no se reconoce")
    void noDetectar() {
        assertNull(Franquicia.detectar(""));
        assertNull(Franquicia.detectar(null));
        assertNull(Franquicia.detectar("5"));
        assertNull(Franquicia.detectar("56"));
        assertNull(Franquicia.detectar("306"));
        assertNull(Franquicia.detectar("2721"));
        assertNull(Franquicia.detectar("6011"));
    }

    @Test
    @DisplayName("Cada franquicia tiene su cantidad de dígitos, CVV y logo")
    void datosFranquicia() {
        assertEquals(16, Franquicia.VISA.getCantidadDigitos());
        assertEquals(15, Franquicia.AMEX.getCantidadDigitos());
        assertEquals(14, Franquicia.DINERS.getCantidadDigitos());
        assertEquals(4, Franquicia.AMEX.getDigitosCvv());
        assertEquals(3, Franquicia.MASTERCARD.getDigitosCvv());
        assertEquals("logo_visa.png", Franquicia.VISA.getLogo());
        assertEquals("American Express", Franquicia.AMEX.getNombre());
        assertEquals("Diners Club", Franquicia.DINERS.toString());
    }

    @Test
    @DisplayName("Pago calcula el valor devuelto y guarda la fecha")
    void pago() {
        Pago pago = new Pago(TipoTarjeta.CREDITO, Franquicia.MASTERCARD, "5100", "Ana", "123", 3, 30000, 50000);
        assertEquals(20000, pago.getValorDevuelto());
        assertEquals(30000, pago.getValorAPagar());
        assertEquals(50000, pago.getMontoIngresado());
        assertEquals(TipoTarjeta.CREDITO, pago.getTipoTarjeta());
        assertEquals(Franquicia.MASTERCARD, pago.getFranquicia());
        assertEquals("5100", pago.getUltimosDigitos());
        assertEquals("Ana", pago.getTitular());
        assertEquals("123", pago.getDocumentoTitular());
        assertEquals(3, pago.getCuotas());
        assertEquals(LocalDate.now(), pago.getFechaHora().toLocalDate());
    }

    @Test
    @DisplayName("describir muestra franquicia, tipo, últimos dígitos y cuotas (solo crédito)")
    void describir() {
        assertEquals("Mastercard Crédito •••• 5100 (3 cuotas)",
                new Pago(TipoTarjeta.CREDITO, Franquicia.MASTERCARD, "5100", "Ana", "1", 3, 100, 100).describir());
        assertEquals("Visa Crédito •••• 4242 (1 cuota)",
                new Pago(TipoTarjeta.CREDITO, Franquicia.VISA, "4242", "Ana", "1", 1, 100, 100).describir());
        assertEquals("Visa Débito •••• 4242",
                new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana", "1", 1, 100, 100).describir());
    }

    @Test
    @DisplayName("generarComprobante muestra lo devuelto solo si hubo devolución")
    void comprobante() {
        String conDevolucion = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana", "1", 1, 100, 150)
                .generarComprobante();
        String exacto = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana", "1", 1, 100, 100)
                .generarComprobante();
        assertTrue(conDevolucion.contains("Titular:    Ana"));
        assertTrue(conDevolucion.contains("Devuelto a su cuenta"));
        assertFalse(exacto.contains("Devuelto a su cuenta"));
    }
}
