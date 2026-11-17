package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/** Las ventas canceladas no cuentan en ningún reporte. */
class CineReportesTest {

    private Cine cine;

    @BeforeEach
    void preparar() throws Exception {
        cine = DatosPrueba.crearCine();
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "TINTIN", Genero.COMEDIA, 90, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2001, EstadoPelicula.DISPONIBLE));
        cine.registrarPelicula(DatosPrueba.pelicula("P2", "TINTIN", Genero.COMEDIA, 90, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2001, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 2D", 50, EstadoSala.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S2", "SALA 2D", 50, EstadoSala.DISPONIBLE));
        cine.registrarCliente(DatosPrueba.cliente("1", "Sebas", "31132030", "sebas@pro.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        cine.programarFuncion("F2", "P2", "S2", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
    }

    private Venta vender(String funcion, String asiento) throws Exception {
        return cine.venderEntrada(funcion, asiento, "1", TipoEntrada.GENERAL, null, null);
    }

    // ---------------- sin ventas ----------------

    @Test
    void sinVentasTodoEstaEnCero() {
        assertEquals(0, cine.calcularTotalEntradasVendidas());
        assertEquals(0, cine.calcularTotalIngresos(), 0.001);
        assertNull(cine.obtenerPeliculaMasVendida());
    }

    // ---------------- total de entradas e ingresos ----------------

    @Test
    void cuentaLasEntradasYSumaLosIngresos() throws Exception {
        vender("F1", "A1");
        vender("F1", "A2");
        vender("F2", "A1");
        assertEquals(3, cine.calcularTotalEntradasVendidas());
        assertEquals(45000, cine.calcularTotalIngresos(), 0.001);
    }

    @Test
    void losIngresosSumanLoQueSePagoDeVerdadConDescuentosYCombos() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("2", "Sebas", "31132030", "sebas@pro.com", TipoCliente.ESTUDIANTE, PlanMembresia.PREMIUM));
        vender("F1", "A1");                                                                     // 15.000
        cine.venderEntrada("F1", "A2", "2", TipoEntrada.ESTUDIANTE, null, null);                // 12.000 - 30 % = 8.400
        assertEquals(23400, cine.calcularTotalIngresos(), 0.001);
    }

    @Test
    void lasVentasCanceladasNoCuentanNiEnEntradasNiEnIngresos() throws Exception {
        vender("F1", "A1");
        vender("F1", "A2");
        cine.cancelarVenta("V2");
        assertEquals(1, cine.calcularTotalEntradasVendidas());
        assertEquals(15000, cine.calcularTotalIngresos(), 0.001);
    }

    // ---------------- película más vendida ----------------

    @Test
    void laPeliculaMasVendidaEsLaQueTieneMasEntradas() throws Exception {
        vender("F1", "A1");
        vender("F2", "A1");
        vender("F2", "A2");
        assertEquals("P2", cine.obtenerPeliculaMasVendida().getCodigo());
    }

    @Test
    void alCancelarVentasCambiaLaPeliculaMasVendida() throws Exception {
        vender("F1", "A1");
        vender("F2", "A1");
        vender("F2", "A2");
        cine.cancelarVenta("V3");
        cine.cancelarVenta("V2");   // P2 se queda en 0
        assertEquals("P1", cine.obtenerPeliculaMasVendida().getCodigo());
    }

    @Test
    void siSeCancelanTodasLasVentasNoHayPeliculaMasVendida() throws Exception {
        vender("F1", "A1");
        cine.cancelarVenta("V1");
        assertNull(cine.obtenerPeliculaMasVendida());
    }

    @Test
    void cuentaLasEntradasDeTodasLasFuncionesDeLaPelicula() throws Exception {
        cine.programarFuncion("F3", "P1", "S1", DatosPrueba.manana(), LocalTime.of(21, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        vender("F1", "A1");
        vender("F3", "A1");
        vender("F3", "A2");
        vender("F2", "A1");
        vender("F2", "A2");   // P1: 3 entradas en dos funciones; P2: 2 entradas
        assertEquals("P1", cine.obtenerPeliculaMasVendida().getCodigo());
    }

    // ---------------- ocupación ----------------

    @Test
    void laOcupacionEsElPorcentajeDeAsientosOcupados() throws Exception {
        assertEquals(0, cine.obtenerOcupacionFuncion("F1"), 0.001);
        for (int i = 1; i <= 5; i++) {
            vender("F1", "A" + i);
        }
        assertEquals(10.0, cine.obtenerOcupacionFuncion("F1"), 0.001);   // 5 de 20
        assertEquals(0, cine.obtenerOcupacionFuncion("F2"), 0.001);      // la otra función no cambia
    }

    @Test
    void conTodosLosAsientosVendidosLaOcupacionEsCien() throws Exception {
        for (int i = 1; i <= 10; i++) {
            vender("F1", "A" + i);
            vender("F1", "B" + i);
        }
        assertEquals(40.0, cine.obtenerOcupacionFuncion("F1"), 0.001);
    }

    @Test
    void cancelarUnaVentaBajaLaOcupacion() throws Exception {
        vender("F1", "A1");
        vender("F1", "A2");
        cine.cancelarVenta("V1");
        assertEquals(2.0, cine.obtenerOcupacionFuncion("F1"), 0.001);    // 1 de 20
    }

    @Test
    void laOcupacionDeUnaFuncionInexistenteLanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class, () -> cine.obtenerOcupacionFuncion("NO-EXISTE"));
    }

    // ---------------- funciones programadas por fecha ----------------

    @Test
    void funcionesProgramadasDeUnaFecha() throws Exception {
        cine.programarFuncion("F3", "P1", "S1", DatosPrueba.manana().plusDays(2), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(2, cine.obtenerFuncionesProgramadas(DatosPrueba.manana()).size());
        assertEquals(1, cine.obtenerFuncionesProgramadas(DatosPrueba.manana().plusDays(2)).size());
        assertEquals(0, cine.obtenerFuncionesProgramadas(DatosPrueba.manana().plusDays(1)).size());
    }

    @Test
    void lasFuncionesCanceladasNoApareceneEnLasProgramadasDeLaFecha() throws Exception {
        cine.cancelarFuncion("F2");
        assertEquals(1, cine.obtenerFuncionesProgramadas(DatosPrueba.manana()).size());
        assertEquals("F1", cine.obtenerFuncionesProgramadas(DatosPrueba.manana()).get(0).getCodigo());
    }
}

