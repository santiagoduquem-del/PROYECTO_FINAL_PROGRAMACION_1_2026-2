package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: reportes (las ventas canceladas no cuentan)")
class CineReportesTest {

    private Cine cine;

    @BeforeEach
    void setUp() throws Exception {
        cine = DatosPrueba.crearCineConFuncion();   // F1: P1 en S1 (20 asientos)
        cine.registrarPelicula(DatosPrueba.crearPelicula("P2"));
        cine.programarFuncion("F2", "P2", "S1", DatosPrueba.MANANA, LocalTime.of(10, 0), 0,
                Formato.IMAX, Idioma.DOBLADA);
        cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);   // V1
        cine.venderEntrada("F1", "A2", "100", TipoEntrada.GENERAL, null, null);   // V2
        cine.venderEntrada("F2", "A1", "100", TipoEntrada.GENERAL, null, null);   // V3
        cine.cancelarVenta("V2");
    }

    @Test
    @DisplayName("calcularTotalEntradasVendidas cuenta solo las ventas activas")
    void totalEntradas() {
        assertEquals(2, cine.calcularTotalEntradasVendidas());
    }

    @Test
    @DisplayName("calcularTotalIngresos suma solo las ventas activas")
    void totalIngresos() {
        assertEquals(30000, cine.calcularTotalIngresos());
    }

    @Test
    @DisplayName("obtenerPeliculaMasVendida devuelve la de más entradas activas (o null si no hay ventas)")
    void peliculaMasVendida() throws Exception {
        cine.venderEntrada("F2", "A2", "100", TipoEntrada.GENERAL, null, null);
        assertEquals("P2", cine.obtenerPeliculaMasVendida().getCodigo());
        assertNull(DatosPrueba.crearCine().obtenerPeliculaMasVendida());
    }

    @Test
    @DisplayName("obtenerOcupacionFuncion da el porcentaje de asientos ocupados")
    void ocupacion() throws Exception {
        assertEquals(5.0, cine.obtenerOcupacionFuncion("F1"));    // 1 de 20
        assertThrows(FuncionNoEncontradaException.class, () -> cine.obtenerOcupacionFuncion("NO"));
    }

    @Test
    @DisplayName("obtenerFuncionesProgramadas de una fecha no incluye las canceladas")
    void funcionesProgramadas() throws Exception {
        assertEquals(2, cine.obtenerFuncionesProgramadas(DatosPrueba.MANANA).size());
        cine.cancelarVenta("V3");
        cine.cancelarFuncion("F2");
        assertEquals(1, cine.obtenerFuncionesProgramadas(DatosPrueba.MANANA).size());
    }
}
