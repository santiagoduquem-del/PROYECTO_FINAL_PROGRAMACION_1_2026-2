package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Función")
class FuncionTest {

    private static final LocalDate FECHA = LocalDate.of(2030, 5, 10);
    private Sala sala;
    private Funcion funcion;

    @BeforeEach
    void setUp() {
        Pelicula pelicula = DatosPrueba.crearPelicula("P1");   // 120 minutos
        sala = DatosPrueba.crearSala("S1", 12);
        funcion = new Funcion("F1", pelicula, sala, FECHA, LocalTime.of(22, 30), 4000,
                Formato.IMAX, Idioma.SUBTITULADA);
    }

    @Test
    @DisplayName("Empieza PROGRAMADA y con una copia propia de los asientos de la sala")
    void constructor() {
        assertEquals(EstadoFuncion.PROGRAMADA, funcion.getEstado());
        assertEquals(12, funcion.getAsientos().size());
        assertNotSame(sala.getAsientos().get(0), funcion.getAsientos().get(0));
        assertEquals(sala.getAsientos().get(0).getCodigo(), funcion.getAsientos().get(0).getCodigo());
    }

    @Test
    @DisplayName("Ocupar un asiento en la función no lo ocupa en la sala")
    void asientosIndependientes() throws Exception {
        funcion.buscarAsiento("A1").ocupar();
        assertTrue(sala.getAsientos().get(0).estaDisponible());
        assertEquals(11, funcion.contarAsientosDisponibles());
        assertEquals(1, funcion.contarAsientosOcupados());
    }

    @Test
    @DisplayName("buscarAsiento ignora mayúsculas y lanza excepción si no existe")
    void buscarAsiento() throws Exception {
        assertEquals("B2", funcion.buscarAsiento("b2").getCodigo());
        assertThrows(AsientoNoEncontradoException.class, () -> funcion.buscarAsiento("Z1"));
    }

    @Test
    @DisplayName("Horarios: inicio, fin, sala libre (fin + 15 min) y minutos del día; puede pasar de medianoche")
    void horarios() {
        assertEquals(LocalDateTime.of(FECHA, LocalTime.of(22, 30)), funcion.getInicio());
        assertEquals(LocalDateTime.of(FECHA.plusDays(1), LocalTime.of(0, 30)), funcion.getFin());
        assertEquals(LocalDateTime.of(FECHA.plusDays(1), LocalTime.of(0, 45)), funcion.getSalaLibreDesde());
        assertEquals(22 * 60 + 30, funcion.getMinutoInicio());
        assertEquals(22 * 60 + 30 + 120, funcion.getMinutoFin());
    }

    @Test
    @DisplayName("cancelar cambia el estado y no se puede cancelar dos veces")
    void cancelar() throws Exception {
        assertFalse(funcion.estaCancelada());
        funcion.cancelar();
        assertTrue(funcion.estaCancelada());
        assertThrows(FuncionNoDisponibleException.class, funcion::cancelar);
    }

    @Test
    @DisplayName("contarEntradasVendidas solo cuenta las ventas activas; agregarVenta las registra")
    void entradasVendidas() throws Exception {
        Cine cine = DatosPrueba.crearCineConFuncion();
        Funcion f1 = cine.buscarFuncion("F1");
        Venta v1 = cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        cine.venderEntrada("F1", "A2", "100", TipoEntrada.GENERAL, null, null);
        assertEquals(2, f1.getListVentas().size());
        assertEquals(2, f1.contarEntradasVendidas());
        cine.cancelarVenta(v1.getCodigo());
        assertEquals(1, f1.contarEntradasVendidas());
    }

    @Test
    @DisplayName("reconstruirAsientos copia de nuevo la sala solo si la función no tiene ventas")
    void reconstruirAsientos() throws Exception {
        sala.setCapacidad(20);
        assertTrue(funcion.reconstruirAsientos());
        assertEquals(20, funcion.getAsientos().size());

        Cine cine = DatosPrueba.crearCineConFuncion();
        cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertFalse(cine.buscarFuncion("F1").reconstruirAsientos());
    }

    @Test
    @DisplayName("Formato, idioma, describirFormatoIdioma y toString")
    void formatoIdioma() {
        assertEquals(Formato.IMAX, funcion.getFormato());
        assertEquals(Idioma.SUBTITULADA, funcion.getIdioma());
        assertEquals("IMAX · Subtitulada", funcion.describirFormatoIdioma());
        assertTrue(funcion.toString().startsWith("F1 - Película P1 (IMAX · Subtitulada)"));
    }

    @Test
    @DisplayName("Sin formato ni idioma (datos viejos): usa el primer formato de la película y Doblada")
    void valoresPorDefecto() {
        Funcion vieja = new Funcion("F2", DatosPrueba.crearPelicula("P1"), sala, FECHA, LocalTime.NOON, 0, null, null);
        assertEquals(Formato.IMAX, vieja.getFormato());
        assertEquals(Idioma.DOBLADA, vieja.getIdioma());
    }

    @Test
    @DisplayName("Setters de código, fecha, hora, valor base y estado")
    void setters() {
        funcion.setCodigo("F9");
        funcion.setFecha(FECHA.plusDays(1));
        funcion.setHora(LocalTime.of(10, 0));
        funcion.setValorBase(0);
        funcion.setEstado(EstadoFuncion.FINALIZADA);
        assertEquals("F9", funcion.getCodigo());
        assertEquals(FECHA.plusDays(1), funcion.getFecha());
        assertEquals(LocalTime.of(10, 0), funcion.getHora());
        assertEquals(0, funcion.getValorBase());
        assertEquals(EstadoFuncion.FINALIZADA, funcion.getEstado());
        assertEquals("P1", funcion.getPelicula().getCodigo());
        assertSame(sala, funcion.getSala());
    }
}
