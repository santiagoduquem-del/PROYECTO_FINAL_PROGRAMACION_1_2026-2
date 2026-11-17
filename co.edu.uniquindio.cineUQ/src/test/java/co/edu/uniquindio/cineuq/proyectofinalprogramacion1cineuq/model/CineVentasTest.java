package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: venta de entradas")
class CineVentasTest {

    private Cine cine;

    @BeforeEach
    void setUp() throws Exception {
        cine = DatosPrueba.crearCineConFuncion();   // F1 sin valor base; cliente 100 General con plan Free
    }

    @Test
    @DisplayName("venderEntrada ocupa el asiento, calcula el valor y registra la venta en todos lados")
    void venderEntrada() throws Exception {
        Operador operador = new Operador("10", "Luis", "luis", "1");
        Venta venta = cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, operador);

        assertEquals("V1", venta.getCodigo());
        assertEquals(15000, venta.getValorPagado());   // tarifa General, sin descuento ni recargo
        assertFalse(cine.buscarFuncion("F1").buscarAsiento("A1").estaDisponible());
        assertTrue(cine.consultarVentas().contains(venta));
        assertTrue(cine.buscarFuncion("F1").getListVentas().contains(venta));
        assertTrue(cine.buscarCliente("100").getListVentas().contains(venta));
        assertTrue(operador.getListVentas().contains(venta));
    }

    @Test
    @DisplayName("Los códigos de venta son consecutivos: V1, V2, V3...")
    void codigosConsecutivos() throws Exception {
        assertEquals("V1", cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null).getCodigo());
        assertEquals("V2", cine.venderEntrada("F1", "A2", "100", TipoEntrada.GENERAL, null, null).getCodigo());
    }

    @Test
    @DisplayName("venderEntrada aplica el valor base y el descuento del plan")
    void venderEntradaConPlanYValorBase() throws Exception {
        cine.registrarCliente(DatosPrueba.crearCliente("200", TipoCliente.ESTUDIANTE, PlanMembresia.ESTANDAR));
        cine.modificarValorBase("F1", 5000);
        Combo combo = cine.buscarCombo("C1");   // 18.000
        Venta venta = cine.venderEntrada("F1", "B1", "200", TipoEntrada.ESTUDIANTE, List.of(combo, combo), null);
        // Entrada: (12.000 + 5.000) - 20 % = 13.600 ; combos: 36.000 - 15 % = 30.600
        assertEquals(13600, venta.getValorEntrada());
        assertEquals(30600, venta.getValorCombos());
        assertEquals(44200, venta.getValorPagado());
    }

    @Test
    @DisplayName("venderEntrada no vende un asiento ocupado ni uno que no existe")
    void venderAsientoOcupadoOInexistente() throws Exception {
        cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertThrows(AsientoOcupadoException.class,
                () -> cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null));
        assertThrows(AsientoNoEncontradoException.class,
                () -> cine.venderEntrada("F1", "Z99", "100", TipoEntrada.GENERAL, null, null));
    }

    @Test
    @DisplayName("venderEntrada valida función, cliente y tipo de entrada")
    void venderEntradaDatosInvalidos() throws Exception {
        assertThrows(DatoInvalidoException.class,
                () -> cine.venderEntrada("F1", "A1", "100", null, null, null));
        assertThrows(FuncionNoEncontradaException.class,
                () -> cine.venderEntrada("NO", "A1", "100", TipoEntrada.GENERAL, null, null));
        assertThrows(ClienteNoEncontradoException.class,
                () -> cine.venderEntrada("F1", "A1", "999", TipoEntrada.GENERAL, null, null));
        cine.cancelarFuncion("F1");
        assertThrows(FuncionNoDisponibleException.class,
                () -> cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null));
    }

    @Test
    @DisplayName("venderEntrada solo permite General o el tipo del cliente")
    void venderTipoNoPermitido() {
        assertThrows(DatoInvalidoException.class,   // el cliente 100 es General
                () -> cine.venderEntrada("F1", "A1", "100", TipoEntrada.ESTUDIANTE, null, null));
    }

    @Test
    @DisplayName("venderEntrada falla si la función está llena")
    void funcionLlena() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S2", 1));
        cine.programarFuncion("F2", "P1", "S2", DatosPrueba.MANANA, LocalTime.of(10, 0), 0,
                Formato.IMAX, Idioma.DOBLADA);
        cine.venderEntrada("F2", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertThrows(SinAsientosDisponiblesException.class,
                () -> cine.venderEntrada("F2", "A1", "100", TipoEntrada.GENERAL, null, null));
    }

    @Test
    @DisplayName("venderEntrada rechaza combos deshabilitados y exclusivos de VIP Bistró fuera de esa sala")
    void venderCombosNoPermitidos() throws Exception {
        cine.deshabilitarCombo("C2");
        assertThrows(DatoInvalidoException.class, () -> cine.venderEntrada("F1", "A1", "100",
                TipoEntrada.GENERAL, List.of(cine.buscarCombo("C2")), null));
        assertThrows(DatoInvalidoException.class, () -> cine.venderEntrada("F1", "A1", "100",
                TipoEntrada.GENERAL, List.of(cine.buscarCombo("C7")), null));   // C7 es exclusivo de VIP
        assertTrue(cine.buscarFuncion("F1").buscarAsiento("A1").estaDisponible(), "el asiento no se debe ocupar");
    }

    @Test
    @DisplayName("Un combo exclusivo sí se vende en una función VIP Bistró")
    void venderComboExclusivoEnVip() throws Exception {
        cine.registrarPelicula(new Pelicula("P2", "VIP", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(Formato.VIP_BISTRO), 2020, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.crearSala("S2", 10));
        cine.programarFuncion("F2", "P2", "S2", DatosPrueba.MANANA, LocalTime.of(10, 0), 0,
                Formato.VIP_BISTRO, Idioma.DOBLADA);
        Venta venta = cine.venderEntrada("F2", "A1", "100", TipoEntrada.GENERAL,
                List.of(cine.buscarCombo("C7")), null);
        assertEquals(1, venta.contarCombo(cine.buscarCombo("C7")));
    }

    @Test
    @DisplayName("esTipoEntradaPermitido: General siempre; los demás solo si coinciden con el cliente")
    void esTipoEntradaPermitido() {
        Cliente nino = DatosPrueba.crearCliente("1", TipoCliente.NINO, PlanMembresia.FREE);
        assertTrue(cine.esTipoEntradaPermitido(nino, TipoEntrada.GENERAL));
        assertTrue(cine.esTipoEntradaPermitido(nino, TipoEntrada.NINO));
        assertFalse(cine.esTipoEntradaPermitido(nino, TipoEntrada.ESTUDIANTE));
        assertFalse(cine.esTipoEntradaPermitido(nino, TipoEntrada.ADULTO_MAYOR));
        assertTrue(cine.esTipoEntradaPermitido(null, TipoEntrada.NINO));
        assertTrue(cine.esTipoEntradaPermitido(nino, null));
    }

    @Test
    @DisplayName("calcularValorCombos aplica el descuento del plan; SinDescuento no")
    void calcularValorCombos() throws Exception {
        List<Combo> combos = List.of(cine.buscarCombo("C1"), cine.buscarCombo("C6"));   // 18.000 + 16.000
        assertEquals(34000, cine.calcularValorCombosSinDescuento(combos));
        assertEquals(34000, cine.calcularValorCombos(combos, PlanMembresia.FREE));
        assertEquals(23800, cine.calcularValorCombos(combos, PlanMembresia.PREMIUM));   // -30 %
        assertEquals(0, cine.calcularValorCombos(List.of(), PlanMembresia.PREMIUM));
    }

    @Test
    @DisplayName("cancelarVenta libera el asiento y no se puede cancelar dos veces")
    void cancelarVenta() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        cine.cancelarVenta(venta.getCodigo());
        assertEquals(EstadoVenta.CANCELADA, venta.getEstado());
        assertTrue(cine.buscarFuncion("F1").buscarAsiento("A1").estaDisponible());
        assertThrows(VentaYaCanceladaException.class, () -> cine.cancelarVenta(venta.getCodigo()));
        assertThrows(VentaNoEncontradaException.class, () -> cine.cancelarVenta("V99"));
    }

    @Test
    @DisplayName("buscarVenta y consultarVentas")
    void buscarYConsultarVentas() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertSame(venta, cine.buscarVenta("V1"));
        assertEquals(1, cine.consultarVentas().size());
        assertThrows(VentaNoEncontradaException.class, () -> cine.buscarVenta("V2"));
    }
}
