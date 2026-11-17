package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.VentaYaCanceladaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    private Funcion funcion;
    private Asiento asiento;
    private Cliente cliente;

    @BeforeEach
    void preparar() throws Exception {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "Interestelar");
        Sala sala = DatosPrueba.sala("S1", "SALA", 20, EstadoSala.DISPONIBLE);
        funcion = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        asiento = funcion.buscarAsiento("A1");
        asiento.ocupar();
        cliente = DatosPrueba.cliente("1","JUAN", "32102102", "juan@juan.com" , TipoCliente.GENERAL, PlanMembresia.FREE);
    }

    private Venta crear(double tarifa, double valorBase, double valorEntrada, List<Combo> combos, double valorCombos) {
        return new Venta("V1", cliente, funcion, asiento, TipoEntrada.GENERAL, null, LocalDateTime.now(),
                tarifa, valorBase, valorEntrada, combos, valorCombos);
    }

    @Test
    void unaVentaNuevaEstaActivaYSumaEntradaMasCombos() {
        Venta venta = crear(15000, 0, 15000, new ArrayList<Combo>(), 0);
        assertEquals(EstadoVenta.ACTIVA, venta.getEstado());
        assertFalse(venta.estaCancelada());
        assertEquals(15000, venta.getValorPagado(), 0.001);

        Venta conCombos = crear(15000, 0, 13500, new ArrayList<Combo>(), 17100);
        assertEquals(30600, conCombos.getValorPagado(), 0.001);
    }

    @Test
    void cancelarCambiaElEstadoYLiberaElAsiento() throws Exception {
        Venta venta = crear(15000, 0, 15000, new ArrayList<Combo>(), 0);
        venta.cancelar();
        assertEquals(EstadoVenta.CANCELADA, venta.getEstado());
        assertTrue(venta.estaCancelada());
        assertTrue(asiento.estaDisponible());
    }

    @Test
    void noSePuedeCancelarDosVeces() throws Exception {
        Venta venta = crear(15000, 0, 15000, new ArrayList<Combo>(), 0);
        venta.cancelar();
        assertThrows(VentaYaCanceladaException.class, () -> venta.cancelar());
    }

    @Test
    void cuentaLasUnidadesDeCadaCombo() {
        Combo pareja = new Combo("C2", "Combo Pareja", "Crispetas + 2 gaseosas", 32000);
        Combo nachos = new Combo("C4", "Combo Nachos", "Nachos + gaseosa", 24000);
        List<Combo> combos = new ArrayList<>();
        combos.add(pareja);
        combos.add(nachos);
        combos.add(pareja);
        Venta venta = crear(15000, 0, 15000, combos, 88000);

        assertEquals(2, venta.contarCombo(pareja));
        assertEquals(1, venta.contarCombo(nachos));
        assertEquals(2, venta.obtenerCombosDistintos().size());
    }

    @Test
    void guardaUnaCopiaDeLaListaDeCombos() {
        List<Combo> combos = new ArrayList<>();
        Venta venta = crear(15000, 0, 15000, combos, 0);
        combos.add(new Combo("C1", "Otro", "x", 1000));
        assertEquals(0, venta.getCombos().size());
    }

    @Test
    void calculaElPorcentajeDeDescuentoConLoQueSeCobro() {
        assertEquals(30, crear(15000, 0, 10500, new ArrayList<Combo>(), 0).calcularPorcentajeDescuento());
        assertEquals(0, crear(15000, 0, 15000, new ArrayList<Combo>(), 0).calcularPorcentajeDescuento());
        assertEquals(20, crear(15000, 5000, 16000, new ArrayList<Combo>(), 0).calcularPorcentajeDescuento());
    }

    @Test
    void sinTarifaNiValorBaseElDescuentoEsCero() {
        assertEquals(0, crear(0, 0, 0, new ArrayList<Combo>(), 0).calcularPorcentajeDescuento());
    }

    @Test
    void laEntradaMuestraLosDatosDeLaCompra() {
        Venta venta = crear(15000, 0, 15000, new ArrayList<Combo>(), 0);
        String texto = venta.generarInformacionEntrada();

        assertTrue(texto.contains("V1"));
        assertTrue(texto.contains(venta.getFuncion().getPelicula().getNombre()));
        assertTrue(texto.contains(venta.getFuncion().getSala().getNombre()));
        assertTrue(texto.contains(venta.getAsiento().getCodigo()));
        assertTrue(texto.contains(venta.getCliente().getNombre()));

        // el total, sin importar si usa punto o coma como separador de miles
        int pos = texto.indexOf("TOTAL PAGADO: $");
        assertTrue(pos >= 0);
        String lineaTotal = texto.substring(pos).split("\n")[0];
        assertEquals("15000", lineaTotal.replaceAll("[^0-9]", ""));

        assertFalse(texto.contains("Combos:"));
        assertFalse(texto.contains("Descuento plan"));
        assertFalse(texto.contains("Valor base"));
    }

    @Test
    void laEntradaMuestraDescuentoValorBaseYCombosCuandoAplican() {
        Combo pareja = new Combo("C2", "Combo Pareja", "Crispetas + 2 gaseosas", 32000);
        List<Combo> combos = new ArrayList<>();
        combos.add(pareja);
        combos.add(pareja);
        String texto = crear(15000, 5000, 14000, combos, 60000).generarInformacionEntrada();

        assertTrue(texto.contains("Valor base función"));
        assertTrue(texto.contains("Descuento plan: -30%"));
        assertTrue(texto.contains("2 x Combo Pareja"));
        assertTrue(texto.contains("TOTAL PAGADO: $" + String.format("%,.0f", 74000.0)));
    }

    @Test
    void laEntradaIncluyeElComprobanteDelPagoSiHayPago() {
        Venta venta = crear(15000, 0, 15000, new ArrayList<Combo>(), 0);
        venta.setPago(new Pago(TipoTarjeta.DEBITO, Franquicia.VISA, "4242", "Ana Gómez", "123", 1, 15000, 15000));
        assertTrue(venta.generarInformacionEntrada().contains("Visa Débito"));
    }

    @Test
    void toStringMuestraCodigoClienteAsientoYEstado() {
        String texto = crear(15000, 0, 15000, new ArrayList<Combo>(), 0).toString();
        assertTrue(texto.contains("V1"));
        assertTrue(texto.contains("A1"));
        assertTrue(texto.contains("ACTIVA"));
    }
}

