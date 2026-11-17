package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** El pago es simulado: se revisan los datos de la tarjeta y que el monto alcance. */
class CinePagoTest {

    private static final String VISA = "4111111111111111";            // 16 dígitos
    private static final String MASTERCARD = "5500000000000004";      // 16 dígitos
    private static final String AMEX = "378282246310005";             // 15 dígitos
    private static final String DINERS = "30569309025904";            // 14 dígitos

    private Cine cine;
    private int anioValido;

    @BeforeEach
    void preparar() {
        cine = DatosPrueba.crearCine();
        anioValido = LocalDate.now().getYear() + 1;   // la tarjeta vence el año que viene
    }

    private Pago pagarConVisa(double total, double monto) throws Exception {
        return cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana Gómez", "123456", 12, anioValido, "123", 1, total, monto);
    }

    // ---------------- pagos aprobados ----------------

    @Test
    void apruebaUnPagoConDatosCorrectos() throws Exception {
        Pago pago = pagarConVisa(30000, 30000);
        assertEquals(Franquicia.VISA, pago.getFranquicia());
        assertEquals("1111", pago.getUltimosDigitos());
        assertEquals("Ana Gómez", pago.getTitular());
        assertEquals(30000, pago.getValorAPagar(), 0.001);
        assertEquals(0, pago.getValorDevuelto(), 0.001);
    }

    @Test
    void siElMontoEsMayorQueElTotalElRestoSeDevuelve() throws Exception {
        Pago pago = pagarConVisa(30000, 35000);
        assertEquals(5000, pago.getValorDevuelto(), 0.001);
    }

    @Test
    void elNumeroPuedeTraerEspacios() throws Exception {
        Pago pago = cine.procesarPago(TipoTarjeta.DEBITO, "4111 1111 1111 1111", "Ana", "1", 12, anioValido,
                "123", 1, 1000, 1000);
        assertEquals("1111", pago.getUltimosDigitos());
    }

    @Test
    void quitaLosEspaciosDelTitularYDelDocumento() throws Exception {
        Pago pago = cine.procesarPago(TipoTarjeta.DEBITO, VISA, "  Ana Gómez  ", "  123  ", 12, anioValido,
                "123", 1, 1000, 1000);
        assertEquals("Ana Gómez", pago.getTitular());
        assertEquals("123", pago.getDocumentoTitular());
    }

    @Test
    void aceptaMastercardAmexYDiners() throws Exception {
        assertEquals(Franquicia.MASTERCARD, cine.procesarPago(TipoTarjeta.DEBITO, MASTERCARD, "Ana", "1", 12,
                anioValido, "123", 1, 1000, 1000).getFranquicia());
        assertEquals(Franquicia.AMEX, cine.procesarPago(TipoTarjeta.DEBITO, AMEX, "Ana", "1", 12,
                anioValido, "1234", 1, 1000, 1000).getFranquicia());
        assertEquals(Franquicia.DINERS, cine.procesarPago(TipoTarjeta.DEBITO, DINERS, "Ana", "1", 12,
                anioValido, "123", 1, 1000, 1000).getFranquicia());
    }

    @Test
    void conDebitoLasCuotasSiempreSonUna() throws Exception {
        Pago pago = cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12, anioValido, "123", 6, 1000, 1000);
        assertEquals(1, pago.getCuotas());
    }

    @Test
    void conCreditoSeConservanLasCuotas() throws Exception {
        Pago pago = cine.procesarPago(TipoTarjeta.CREDITO, VISA, "Ana", "1", 12, anioValido, "123", 6, 1000, 1000);
        assertEquals(6, pago.getCuotas());
    }

    @Test
    void unaTarjetaQueVenceEsteMesTodaviaSirve() throws Exception {
        LocalDate hoy = LocalDate.now();
        assertDoesNotThrow(() -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1",
                hoy.getMonthValue(), hoy.getYear(), "123", 1, 1000, 1000));
    }

    // ---------------- saldo ----------------

    @Test
    void siElMontoNoAlcanzaLanzaSaldoInsuficiente() {
        assertThrows(SaldoInsuficienteException.class, () -> pagarConVisa(30000, 29999));
    }

    // ---------------- tarjeta ----------------

    @Test
    void exigeElTipoDeTarjeta() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(null, VISA, "Ana", "1", 12,
                anioValido, "123", 1, 1000, 1000));
    }

    @Test
    void elNumeroSoloPuedeTenerDigitosYNoPuedeEstarVacio() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "41111111abcd1111",
                "Ana", "1", 12, anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "",
                "Ana", "1", 12, anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, null,
                "Ana", "1", 12, anioValido, "123", 1, 1000, 1000));
    }

    @Test
    void rechazaUnaFranquiciaQueNoSeAcepta() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "9999999999999999",
                "Ana", "1", 12, anioValido, "123", 1, 1000, 1000));
    }

    @Test
    void elNumeroDebeTenerLosDigitosDeSuFranquicia() {
        // Visa de 15 dígitos y Amex de 16
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "411111111111111",
                "Ana", "1", 12, anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "3782822463100050",
                "Ana", "1", 12, anioValido, "1234", 1, 1000, 1000));
    }

    @Test
    void exigeTitularYDocumento() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, " ", "1", 12,
                anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, null, "1", 12,
                anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "", 12,
                anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", null, 12,
                anioValido, "123", 1, 1000, 1000));
    }

    // ---------------- vencimiento ----------------

    @Test
    void rechazaUnMesOUnAnioInvalidos() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 0,
                anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 13,
                anioValido, "123", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12,
                0, "123", 1, 1000, 1000));
    }

    @Test
    void rechazaUnaTarjetaVencida() {
        int anioPasado = LocalDate.now().getYear() - 1;
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12,
                anioPasado, "123", 1, 1000, 1000));
    }

    // ---------------- CVV ----------------

    @Test
    void elCvvDebeTenerLosDigitosDeLaFranquicia() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12,
                anioValido, "12", 1, 1000, 1000));      // Visa: 3
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12,
                anioValido, "1234", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, AMEX, "Ana", "1", 12,
                anioValido, "123", 1, 1000, 1000));     // Amex: 4
    }

    @Test
    void elCvvSoloPuedeTenerDigitosYNoPuedeSerNulo() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12,
                anioValido, "12a", 1, 1000, 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1", 12,
                anioValido, null, 1, 1000, 1000));
    }

    // ---------------- cuotas y montos ----------------

    @Test
    void conCreditoDebeSeleccionarLasCuotas() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.CREDITO, VISA, "Ana", "1", 12,
                anioValido, "123", 0, 1000, 1000));
    }

    @Test
    void elTotalYElMontoDebenSerMayoresQueCero() {
        assertThrows(DatoInvalidoException.class, () -> pagarConVisa(0, 1000));
        assertThrows(DatoInvalidoException.class, () -> pagarConVisa(-5, 1000));
        assertThrows(DatoInvalidoException.class, () -> pagarConVisa(1000, 0));
        assertThrows(DatoInvalidoException.class, () -> pagarConVisa(1000, -1));
    }
}
