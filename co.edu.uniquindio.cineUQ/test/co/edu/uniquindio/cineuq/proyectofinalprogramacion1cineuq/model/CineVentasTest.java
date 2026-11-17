package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Valor de una entrada = (tarifa del tipo + valor base de la función) - descuento del plan, redondeado.
 * Tarifas por defecto: GENERAL 15.000, ESTUDIANTE 12.000, NIÑO 10.000, ADULTO MAYOR 11.000.
 */
class CineVentasTest {

    private Cine cine;
    private Operador operador;

    @BeforeEach
    void preparar() throws Exception {
        cine = DatosPrueba.crearCine();
        operador = DatosPrueba.operador("1090293", "Jorge", "gorgi", "jorge123");
        cine.registrarEmpleado(operador);
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "TINTIN", Genero.COMEDIA, 90, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2001, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 3", 50, EstadoSala.DISPONIBLE));
        cine.registrarCliente(DatosPrueba.cliente("1","Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
    }

    private Venta vender(String asiento) throws Exception {
        return cine.venderEntrada("F1", asiento, "1", TipoEntrada.GENERAL, null, operador);
    }

    // ---------------- venta exitosa ----------------

    @Test
    void venderUnaEntradaOcupaElAsientoYRegistraLaVenta() throws Exception {
        Venta venta = vender("A1");

        assertEquals("V1", venta.getCodigo());
        assertEquals(EstadoVenta.ACTIVA, venta.getEstado());
        assertEquals(15000, venta.getValorPagado(), 0.001);
        assertEquals(EstadoAsiento.OCUPADO, cine.buscarFuncion("F1").buscarAsiento("A1").getEstado());
        assertNotNull(venta.getFechaHoraVenta());
        assertEquals(1, cine.consultarVentas().size());
        assertSame(venta, cine.buscarVenta("V1"));
    }

    @Test
    void laVentaQuedaRegistradaEnLaFuncionElClienteYElOperador() throws Exception {
        Venta venta = vender("A1");
        assertTrue(cine.buscarFuncion("F1").getListVentas().contains(venta));
        assertTrue(cine.buscarCliente("1").getListVentas().contains(venta));
        assertTrue(operador.getListVentas().contains(venta));
        assertEquals(1, cine.buscarFuncion("F1").contarEntradasVendidas());
    }

    @Test
    void losCodigosDeVentaSonConsecutivos() throws Exception {
        assertEquals("V1", vender("A1").getCodigo());
        assertEquals("V2", vender("A2").getCodigo());
        assertEquals("V3", vender("A3").getCodigo());
    }

    @Test
    void sePuedeVenderSinOperador() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null);
        assertNull(venta.getOperador());
    }

    @Test
    void elCodigoDelAsientoNoDistingueMayusculas() throws Exception {
        assertEquals("A1", vender("a1").getAsiento().getCodigo());
    }

    @Test
    void elMismoAsientoSePuedeVenderEnOtraFuncion() throws Exception {
        cine.programarFuncion("F2", "P1", "S1", DatosPrueba.manana(), LocalTime.of(21, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        vender("A1");
        assertDoesNotThrow(() -> cine.venderEntrada("F2", "A1", "1", TipoEntrada.GENERAL, null, operador));
    }

    // ---------------- precios ----------------

    @Test
    void cadaTipoDeEntradaCobraSuTarifa() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("2","Sntiago", "32090923", "santugamer@gmail.com", TipoCliente.ESTUDIANTE, PlanMembresia.FREE));
        cine.registrarCliente(DatosPrueba.cliente("3","Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.NINO, PlanMembresia.FREE));
        cine.registrarCliente(DatosPrueba.cliente("4","Sntiago", "32090923", "santugamer@gmail.com", TipoCliente.ADULTO_MAYOR, PlanMembresia.FREE));

        assertEquals(15000, cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null).getValorPagado(), 0.001);
        assertEquals(12000, cine.venderEntrada("F1", "A2", "2", TipoEntrada.ESTUDIANTE, null, null).getValorPagado(), 0.001);
        assertEquals(10000, cine.venderEntrada("F1", "A3", "3", TipoEntrada.NINO, null, null).getValorPagado(), 0.001);
        assertEquals(11000, cine.venderEntrada("F1", "A4", "4", TipoEntrada.ADULTO_MAYOR, null, null).getValorPagado(), 0.001);
    }

    @Test
    void elPlanDelClienteAplicaDescuentoALaEntrada() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("2", "Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.GENERAL, PlanMembresia.BASICO));    // 10 %
        cine.registrarCliente(DatosPrueba.cliente("3", "Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.GENERAL, PlanMembresia.PREMIUM));   // 30 %

        assertEquals(13500, cine.venderEntrada("F1", "A1", "2", TipoEntrada.GENERAL, null, null).getValorPagado(), 0.001);
        assertEquals(10500, cine.venderEntrada("F1", "A2", "3", TipoEntrada.GENERAL, null, null).getValorPagado(), 0.001);
    }

    @Test
    void elValorBaseDeLaFuncionSeSumaAntesDelDescuento() throws Exception {
        cine.programarFuncion("F2", "P1", "S1", DatosPrueba.manana(), LocalTime.of(21, 0), 5000,
                Formato.DOS_D, Idioma.DOBLADA);
        cine.registrarCliente(DatosPrueba.cliente("2", "Sntiago", "32090923", "santugamer@gmail.com", TipoCliente.GENERAL, PlanMembresia.PREMIUM));

        Venta sinDescuento = cine.venderEntrada("F2", "A1", "1", TipoEntrada.GENERAL, null, null);
        Venta conDescuento = cine.venderEntrada("F2", "A2", "2", TipoEntrada.GENERAL, null, null);
        assertEquals(20000, sinDescuento.getValorPagado(), 0.001);     // 15.000 + 5.000
        assertEquals(14000, conDescuento.getValorPagado(), 0.001);     // 20.000 - 30 %
        assertEquals(5000, conDescuento.getValorBase(), 0.001);
    }

    @Test
    void laVentaConservaLaTarifaConQueSeCobroAunqueCambieDespues() throws Exception {
        Venta venta = vender("A1");
        cine.modificarTarifa(TipoEntrada.GENERAL, 20000);
        assertEquals(15000, venta.getTarifa(), 0.001);
        assertEquals(15000, venta.getValorPagado(), 0.001);
    }

    @Test
    void losCombosSeSumanALaVentaConElDescuentoDelPlan() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("2", "Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.GENERAL, PlanMembresia.BASICO));   // 10 % / 5 %
        List<Combo> combos = new ArrayList<>();
        combos.add(cine.buscarCombo("C1"));   // 18.000

        Venta libre = cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, combos, null);
        Venta basico = cine.venderEntrada("F1", "A2", "2", TipoEntrada.GENERAL, combos, null);
        assertEquals(33000, libre.getValorPagado(), 0.001);      // 15.000 + 18.000
        assertEquals(17100, basico.getValorCombos(), 0.001);     // 18.000 - 5 %
        assertEquals(30600, basico.getValorPagado(), 0.001);     // 13.500 + 17.100
        assertEquals(1, basico.contarCombo(combos.get(0)));
    }

    @Test
    void unComboRepetidoCuentaVariasUnidades() throws Exception {
        List<Combo> combos = new ArrayList<>();
        combos.add(cine.buscarCombo("C2"));
        combos.add(cine.buscarCombo("C2"));
        Venta venta = cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, combos, null);
        assertEquals(64000, venta.getValorCombos(), 0.001);
        assertEquals(2, venta.contarCombo(cine.buscarCombo("C2")));
    }

    // ---------------- validaciones de la venta ----------------

    @Test
    void noSeVendeParaUnaFuncionInexistente() {
        assertThrows(FuncionNoEncontradaException.class,
                () -> cine.venderEntrada("NO-EXISTE", "A1", "1", TipoEntrada.GENERAL, null, null));
    }

    @Test
    void noSeVendeParaUnaFuncionCancelada() throws Exception {
        cine.cancelarFuncion("F1");
        assertThrows(FuncionNoDisponibleException.class, () -> vender("A1"));
    }

    @Test
    void noSeVendeAUnClienteInexistente() {
        assertThrows(ClienteNoEncontradoException.class,
                () -> cine.venderEntrada("F1", "A1", "999", TipoEntrada.GENERAL, null, null));
    }

    @Test
    void noSeVendeSinTipoDeEntrada() {
        assertThrows(DatoInvalidoException.class, () -> cine.venderEntrada("F1", "A1", "1", null, null, null));
    }

    @Test
    void noSeVendeUnAsientoOcupado() throws Exception {
        vender("A1");
        assertThrows(AsientoOcupadoException.class, () -> vender("A1"));
        assertEquals(1, cine.consultarVentas().size());
    }

    @Test
    void noSeVendeUnAsientoQueNoExiste() {
        assertThrows(AsientoNoEncontradoException.class, () -> vender("Z99"));
        assertEquals(0, cine.consultarVentas().size());
    }

    @Test
    void noSeVendeSiLaFuncionEstaLlena() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S2", "SALA 0", 2 , EstadoSala.DISPONIBLE));
        cine.programarFuncion("F2", "P1", "S2", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        cine.venderEntrada("F2", "A1", "1", TipoEntrada.GENERAL, null, null);
        cine.venderEntrada("F2", "A2", "1", TipoEntrada.GENERAL, null, null);
        assertThrows(SinAsientosDisponiblesException.class,
                () -> cine.venderEntrada("F2", "A1", "1", TipoEntrada.GENERAL, null, null));
    }

    @Test
    void unComboDeshabilitadoNoSePuedeVenderYElAsientoQuedaLibre() throws Exception {
        cine.deshabilitarCombo("C1");
        List<Combo> combos = new ArrayList<>();
        combos.add(cine.buscarCombo("C1"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, combos, null));
        assertTrue(cine.buscarFuncion("F1").buscarAsiento("A1").estaDisponible());
        assertEquals(0, cine.consultarVentas().size());
    }

    // ---------------- tipo de entrada permitido ----------------

    @Test
    void cualquierClientePuedeComprarEntradaGeneral() {
        for (TipoCliente tipo : TipoCliente.values()) {
            Cliente cliente = DatosPrueba.cliente("9", "Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.GENERAL, PlanMembresia.FREE);
            assertTrue(cine.esTipoEntradaPermitido(cliente, TipoEntrada.GENERAL));
        }
    }

    @Test
    void losDemasTiposSoloSiCoincidenConElTipoDeCliente() {
        Cliente estudiante = DatosPrueba.cliente("9","Sntiago", "32090923", "santugamer@gmail.com",TipoCliente.ESTUDIANTE, PlanMembresia.FREE);
        assertTrue(cine.esTipoEntradaPermitido(estudiante, TipoEntrada.ESTUDIANTE));
        assertFalse(cine.esTipoEntradaPermitido(estudiante, TipoEntrada.NINO));
        assertFalse(cine.esTipoEntradaPermitido(estudiante, TipoEntrada.ADULTO_MAYOR));
    }

    @Test
    void sinClienteOSinTipoTodaviaNoHayNadaQueComparar() {
        assertTrue(cine.esTipoEntradaPermitido(null, TipoEntrada.NINO));
        assertTrue(cine.esTipoEntradaPermitido(DatosPrueba.cliente("9","Sntiago", "32090923", "santugamer@gmail.com", TipoCliente.GENERAL, PlanMembresia.FREE), null));
    }

    @Test
    void unClienteGeneralNoPuedeComprarEntradaDeEstudiante() throws Exception {
        assertThrows(DatoInvalidoException.class,
                () -> cine.venderEntrada("F1", "A1", "1", TipoEntrada.ESTUDIANTE, null, null));
        assertEquals(0, cine.consultarVentas().size());
        assertTrue(cine.buscarFuncion("F1").buscarAsiento("A1").estaDisponible());
    }

    @Test
    void unEstudiantePuedeComprarSuEntradaYLaGeneral() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("2","Sntiago", "32090923", "santugamer@gmail.com", TipoCliente.ESTUDIANTE, PlanMembresia.FREE));
        assertDoesNotThrow(() -> cine.venderEntrada("F1", "A1", "2", TipoEntrada.ESTUDIANTE, null, null));
        assertDoesNotThrow(() -> cine.venderEntrada("F1", "A2", "2", TipoEntrada.GENERAL, null, null));
        assertThrows(DatoInvalidoException.class,
                () -> cine.venderEntrada("F1", "A3", "2", TipoEntrada.NINO, null, null));
    }

    // ---------------- cancelar venta ----------------

    @Test
    void cancelarUnaVentaLiberaElAsientoYCambiaElEstado() throws Exception {
        Venta venta = vender("A1");
        cine.cancelarVenta("V1");
        assertEquals(EstadoVenta.CANCELADA, venta.getEstado());
        assertTrue(cine.buscarFuncion("F1").buscarAsiento("A1").estaDisponible());
    }

    @Test
    void laVentaCanceladaSeConservaEnElHistorial() throws Exception {
        vender("A1");
        cine.cancelarVenta("V1");
        assertEquals(1, cine.consultarVentas().size());
    }

    @Test
    void despuesDeCancelarElAsientoSePuedeVenderDeNuevo() throws Exception {
        vender("A1");
        cine.cancelarVenta("V1");
        assertDoesNotThrow(() -> vender("A1"));
    }

    @Test
    void noSePuedeCancelarUnaVentaInexistente() {
        assertThrows(VentaNoEncontradaException.class, () -> cine.cancelarVenta("V99"));
    }

    @Test
    void noSePuedeCancelarDosVecesLaMismaVenta() throws Exception {
        vender("A1");
        cine.cancelarVenta("V1");
        assertThrows(VentaYaCanceladaException.class, () -> cine.cancelarVenta("V1"));
    }

    @Test
    void buscarUnaVentaInexistenteLanzaExcepcion() {
        assertThrows(VentaNoEncontradaException.class, () -> cine.buscarVenta("V99"));
    }
}

