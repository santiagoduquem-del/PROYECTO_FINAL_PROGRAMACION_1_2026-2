package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.VentaYaCanceladaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Venta")
class VentaTest {

    private Cine cine;
    private Combo personal;
    private Combo nachos;

    @BeforeEach
    void setUp() throws Exception {
        cine = DatosPrueba.crearCineConFuncion();
        cine.registrarCliente(DatosPrueba.crearCliente("200", TipoCliente.GENERAL, PlanMembresia.BASICO));
        cine.modificarValorBase("F1", 5000);
        personal = cine.buscarCombo("C1");
        nachos = cine.buscarCombo("C4");
    }

    @Test
    @DisplayName("Guarda los valores del momento de la venta y valorPagado = entrada + combos")
    void valores() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL, List.of(personal), null);
        assertEquals(15000, venta.getTarifa());
        assertEquals(5000, venta.getValorBase());
        assertEquals(18000, venta.getValorEntrada());   // 20.000 - 10 %
        assertEquals(17100, venta.getValorCombos());    // 18.000 - 5 %
        assertEquals(35100, venta.getValorPagado());
        assertEquals(EstadoVenta.ACTIVA, venta.getEstado());
        assertEquals(TipoEntrada.GENERAL, venta.getTipoEntrada());
        assertEquals("A1", venta.getAsiento().getCodigo());
        assertEquals("200", venta.getCliente().getIdentificacion());
        assertEquals("F1", venta.getFuncion().getCodigo());
        assertNull(venta.getOperador());
        assertEquals(LocalDate.now(), venta.getFechaHoraVenta().toLocalDate());
    }

    @Test
    @DisplayName("Cambiar la tarifa después no cambia lo que ya se pagó")
    void valoresCongelados() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL, null, null);
        cine.modificarTarifa(TipoEntrada.GENERAL, 30000);
        assertEquals(15000, venta.getTarifa());
    }

    @Test
    @DisplayName("contarCombo y obtenerCombosDistintos con combos repetidos")
    void combos() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL,
                List.of(personal, nachos, personal), null);
        assertEquals(2, venta.contarCombo(personal));
        assertEquals(1, venta.contarCombo(nachos));
        assertEquals(0, venta.contarCombo(cine.buscarCombo("C3")));
        assertEquals(List.of(personal, nachos), venta.obtenerCombosDistintos());
        assertEquals(3, venta.getCombos().size());
    }

    @Test
    @DisplayName("calcularPorcentajeDescuento devuelve el % del plan (0 sin plan)")
    void porcentajeDescuento() throws Exception {
        assertEquals(10, cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL, null, null)
                .calcularPorcentajeDescuento());
        assertEquals(0, cine.venderEntrada("F1", "A2", "100", TipoEntrada.GENERAL, null, null)
                .calcularPorcentajeDescuento());
    }

    @Test
    @DisplayName("generarInformacionEntrada incluye los datos del tiquete y, si hay, el pago")
    void tiquete() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL, List.of(personal), null);
        String tiquete = venta.generarInformacionEntrada();
        assertTrue(tiquete.contains("Entrada N°: V1"));
        assertTrue(tiquete.contains("Asiento:    A1"));
        assertTrue(tiquete.contains("1 x Combo Personal"));
        assertTrue(tiquete.contains("Descuento plan: -10%"));
        assertFalse(tiquete.contains("Pago:"));

        Pago pago = new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana", "1", 1, 35100, 40000);
        venta.setPago(pago);
        assertSame(pago, venta.getPago());
        assertTrue(venta.generarInformacionEntrada().contains("Visa Débito •••• 4242"));
    }

    @Test
    @DisplayName("cancelar libera el asiento y no se puede cancelar dos veces")
    void cancelar() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL, null, null);
        assertFalse(venta.estaCancelada());
        venta.cancelar();
        assertTrue(venta.estaCancelada());
        assertTrue(venta.getAsiento().estaDisponible());
        assertThrows(VentaYaCanceladaException.class, venta::cancelar);
        venta.setEstado(EstadoVenta.ACTIVA);
        assertFalse(venta.estaCancelada());
    }

    @Test
    @DisplayName("actualizarCombosAnteriores no cambia una venta con combos nuevos; toString tiene el código")
    void combosAnterioresYToString() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "200", TipoEntrada.GENERAL, List.of(personal), null);
        assertFalse(venta.actualizarCombosAnteriores(cine.consultarCombos()));
        assertEquals(1, venta.contarCombo(personal));
        assertTrue(venta.toString().contains("V1"));
    }
}
