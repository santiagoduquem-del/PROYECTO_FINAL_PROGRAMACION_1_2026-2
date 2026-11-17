package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FranquiciaTest {

    @Test
    void visaEmpiezaPorCuatro() {
        assertEquals(Franquicia.VISA, Franquicia.detectar("4"));
        assertEquals(Franquicia.VISA, Franquicia.detectar("4111111111111111"));
    }

    @Test
    void ignoraLosEspaciosDelNumero() {
        assertEquals(Franquicia.VISA, Franquicia.detectar("4111 1111 1111 1111"));
    }

    @Test
    void americanExpressEmpiezaPor34o37() {
        assertEquals(Franquicia.AMEX, Franquicia.detectar("340000000000009"));
        assertEquals(Franquicia.AMEX, Franquicia.detectar("378282246310005"));
    }

    @Test
    void dinersEmpiezaPor36_38_39o300a305() {
        assertEquals(Franquicia.DINERS, Franquicia.detectar("36"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("38"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("39"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("300"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("30569309025904"));
        assertEquals(Franquicia.DINERS, Franquicia.detectar("305"));
    }

    @Test
    void mastercardEmpiezaPor51a55() {
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("51"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("5500000000000004"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("55"));
    }

    @Test
    void mastercardTambienEmpiezaPor2221a2720() {
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("2221"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("2500000000000000"));
        assertEquals(Franquicia.MASTERCARD, Franquicia.detectar("2720"));
    }

    @Test
    void losLimitesFueraDeRangoNoSeReconocen() {
        assertNull(Franquicia.detectar("50"));
        assertNull(Franquicia.detectar("56"));
        assertNull(Franquicia.detectar("306"));
        assertNull(Franquicia.detectar("299"));
        assertNull(Franquicia.detectar("2220"));
        assertNull(Franquicia.detectar("2721"));
    }

    @Test
    void unNumeroNuloOVacioNoSeReconoce() {
        assertNull(Franquicia.detectar(null));
        assertNull(Franquicia.detectar(""));
        assertNull(Franquicia.detectar("5"));
        assertNull(Franquicia.detectar("9999999999999999"));
    }

    @Test
    void cadaFranquiciaTieneSuCantidadDeDigitosYDeCvv() {
        assertEquals(16, Franquicia.VISA.getCantidadDigitos());
        assertEquals(3, Franquicia.VISA.getDigitosCvv());
        assertEquals(16, Franquicia.MASTERCARD.getCantidadDigitos());
        assertEquals(3, Franquicia.MASTERCARD.getDigitosCvv());
        assertEquals(15, Franquicia.AMEX.getCantidadDigitos());
        assertEquals(4, Franquicia.AMEX.getDigitosCvv());
        assertEquals(14, Franquicia.DINERS.getCantidadDigitos());
        assertEquals(3, Franquicia.DINERS.getDigitosCvv());
    }
}

