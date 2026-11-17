package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: pago con tarjeta (simulado)")
class CinePagoTest {

    private static final int ANIO = LocalDate.now().getYear() + 2;   // tarjeta vigente
    private static final String VISA = "4242424242424242";
    private Cine cine;

    @BeforeEach
    void setUp() {
        cine = DatosPrueba.crearCine();
    }

    @Test
    @DisplayName("Pago con el monto exacto: aprobado y sin devolución")
    void pagoExacto() throws Exception {
        Pago pago = cine.procesarPago(TipoTarjeta.DEBITO, VISA, " Ana ", " 123 ", 12, ANIO, "123", 0, 30000, 30000);
        assertEquals(Franquicia.VISA, pago.getFranquicia());
        assertEquals("4242", pago.getUltimosDigitos());
        assertEquals("Ana", pago.getTitular());
        assertEquals("123", pago.getDocumentoTitular());
        assertEquals(1, pago.getCuotas());   // débito: siempre 1
        assertEquals(0, pago.getValorDevuelto());
    }

    @Test
    @DisplayName("Pago con más dinero: aprobado y el restante se devuelve")
    void pagoConDevolucion() throws Exception {
        Pago pago = cine.procesarPago(TipoTarjeta.CREDITO, "5100 0000 0000 0000", "Ana", "1", 1, ANIO,
                "123", 6, 30000, 50000);
        assertEquals(Franquicia.MASTERCARD, pago.getFranquicia());
        assertEquals(6, pago.getCuotas());
        assertEquals(20000, pago.getValorDevuelto());
    }

    @Test
    @DisplayName("Pago con menos dinero: saldo insuficiente")
    void saldoInsuficiente() {
        assertThrows(SaldoInsuficienteException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA,
                "Ana", "1", 12, ANIO, "123", 0, 30000, 29999));
    }

    @Test
    @DisplayName("American Express usa 15 dígitos y CVV de 4; Diners 14 dígitos")
    void amexYDiners() throws Exception {
        assertEquals(Franquicia.AMEX, cine.procesarPago(TipoTarjeta.DEBITO, "371234567890123", "Ana", "1",
                12, ANIO, "1234", 0, 100, 100).getFranquicia());
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO,
                "371234567890123", "Ana", "1", 12, ANIO, "123", 0, 100, 100));
        assertEquals(Franquicia.DINERS, cine.procesarPago(TipoTarjeta.DEBITO, "36123456789012", "Ana", "1",
                12, ANIO, "123", 0, 100, 100).getFranquicia());
    }

    @Test
    @DisplayName("Rechaza número con letras, no reconocido o con dígitos de más/menos")
    void numeroInvalido() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "4242abcd42424242",
                "Ana", "1", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "6011000000000000",
                "Ana", "1", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "424242424242",
                "Ana", "1", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, "",
                "Ana", "1", 12, ANIO, "123", 0, 100, 100));
    }

    @Test
    @DisplayName("Rechaza datos faltantes: tipo, titular, documento, vencimiento, cuotas, montos")
    void datosFaltantes() {
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(null, VISA,
                "Ana", "1", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA,
                " ", "1", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA,
                "Ana", "", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA,
                "Ana", "1", 13, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.CREDITO, VISA,
                "Ana", "1", 12, ANIO, "123", 0, 100, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA,
                "Ana", "1", 12, ANIO, "123", 0, 0, 100));
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA,
                "Ana", "1", 12, ANIO, "123", 0, 100, 0));
    }

    @Test
    @DisplayName("Rechaza tarjeta vencida; la del mes actual todavía sirve")
    void tarjetaVencida() {
        LocalDate hoy = LocalDate.now();
        LocalDate mesPasado = hoy.minusMonths(1);
        assertThrows(DatoInvalidoException.class, () -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1",
                mesPasado.getMonthValue(), mesPasado.getYear(), "123", 0, 100, 100));
        assertDoesNotThrow(() -> cine.procesarPago(TipoTarjeta.DEBITO, VISA, "Ana", "1",
                hoy.getMonthValue(), hoy.getYear(), "123", 0, 100, 100));
    }
}
