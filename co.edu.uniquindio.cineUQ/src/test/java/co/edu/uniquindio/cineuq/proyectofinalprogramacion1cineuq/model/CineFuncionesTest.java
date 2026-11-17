package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: gestión de funciones")
class CineFuncionesTest {

    private static final LocalDate MANANA = DatosPrueba.MANANA;
    private Cine cine;

    @BeforeEach
    void setUp() throws Exception {
        cine = DatosPrueba.crearCineConFuncion();   // F1: P1 en S1, mañana 18:00, IMAX, 120 min
    }

    @Test
    @DisplayName("programarFuncion crea la función con asientos propios y la asocia a la película")
    void programarFuncion() throws Exception {
        Funcion funcion = cine.buscarFuncion("F1");
        assertEquals(EstadoFuncion.PROGRAMADA, funcion.getEstado());
        assertEquals(20, funcion.getAsientos().size());
        assertNotSame(cine.buscarSala("S1").getAsientos().get(0), funcion.getAsientos().get(0));
        assertTrue(cine.buscarPelicula("P1").getListFunciones().contains(funcion));
    }

    @Test
    @DisplayName("programarFuncion rechaza código repetido")
    void programarFuncionDuplicada() {
        assertThrows(FuncionDuplicadaException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                MANANA.plusDays(5), LocalTime.of(10, 0), 0, Formato.IMAX, Idioma.DOBLADA));
    }

    @Test
    @DisplayName("programarFuncion valida fecha, hora, valor base, formato e idioma")
    void programarFuncionDatosInvalidos() {
        LocalTime hora = LocalTime.of(9, 0);
        LocalDate ayer = LocalDate.now().minusDays(1);
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", ayer, hora, 0, Formato.IMAX, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", null, hora, 0, Formato.IMAX, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", MANANA, null, 0, Formato.IMAX, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", MANANA, hora, -1, Formato.IMAX, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", MANANA, hora, 0, null, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class,   // P1 no está en 4DX
                () -> cine.programarFuncion("F2", "P1", "S1", MANANA, hora, 0, Formato.CUATRO_DX, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", MANANA, hora, 0, Formato.IMAX, null));
        assertThrows(DatoInvalidoException.class,
                () -> cine.programarFuncion(" ", "P1", "S1", MANANA, hora, 0, Formato.IMAX, Idioma.DOBLADA));
    }

    @Test
    @DisplayName("programarFuncion exige película y sala existentes y sala disponible")
    void programarFuncionPeliculaYSala() throws Exception {
        LocalTime hora = LocalTime.of(9, 0);
        assertThrows(PeliculaNoEncontradaException.class,
                () -> cine.programarFuncion("F2", "NO", "S1", MANANA, hora, 0, Formato.IMAX, Idioma.DOBLADA));
        assertThrows(SalaNoEncontradaException.class,
                () -> cine.programarFuncion("F2", "P1", "NO", MANANA, hora, 0, Formato.IMAX, Idioma.DOBLADA));
        cine.deshabilitarSala("S1");
        assertThrows(SalaNoDisponibleException.class,
                () -> cine.programarFuncion("F2", "P1", "S1", MANANA, hora, 0, Formato.IMAX, Idioma.DOBLADA));
    }

    @Test
    @DisplayName("programarFuncion detecta el cruce de horario (película + 15 min de limpieza)")
    void conflictoDeHorario() {
        // F1 ocupa la sala de 18:00 a 20:15. 20:00 se cruza; 20:15 ya no.
        assertThrows(ConflictoHorarioException.class, () -> cine.programarFuncion("F2", "P1", "S1",
                MANANA, LocalTime.of(20, 0), 0, Formato.IMAX, Idioma.DOBLADA));
        assertThrows(ConflictoHorarioException.class, () -> cine.programarFuncion("F2", "P1", "S1",
                MANANA, LocalTime.of(16, 30), 0, Formato.IMAX, Idioma.DOBLADA));
        assertDoesNotThrow(() -> cine.programarFuncion("F3", "P1", "S1",
                MANANA, LocalTime.of(20, 15), 0, Formato.IMAX, Idioma.DOBLADA));
    }

    @Test
    @DisplayName("Una función cancelada ya no ocupa la sala")
    void funcionCanceladaLiberaHorario() throws Exception {
        cine.cancelarFuncion("F1");
        assertDoesNotThrow(() -> cine.programarFuncion("F2", "P1", "S1",
                MANANA, LocalTime.of(18, 0), 0, Formato.DOS_D, Idioma.SUBTITULADA));
    }

    @Test
    @DisplayName("buscarFuncion encuentra la función o lanza excepción")
    void buscarFuncion() throws Exception {
        assertEquals("F1", cine.buscarFuncion("F1").getCodigo());
        assertThrows(FuncionNoEncontradaException.class, () -> cine.buscarFuncion("NO"));
    }

    @Test
    @DisplayName("consultarFunciones y consultarFuncionesProgramadas (todas y por película)")
    void consultarFunciones() throws Exception {
        cine.programarFuncion("F2", "P1", "S1", MANANA.plusDays(1), LocalTime.of(10, 0), 0,
                Formato.IMAX, Idioma.DOBLADA);
        cine.cancelarFuncion("F2");
        assertEquals(2, cine.consultarFunciones().size());
        assertEquals(1, cine.consultarFuncionesProgramadas().size());
        assertEquals(1, cine.consultarFuncionesProgramadas("P1").size());
        assertTrue(cine.consultarFuncionesProgramadas("OTRA").isEmpty());
    }

    @Test
    @DisplayName("consultarFuncionesPorFecha filtra por día")
    void consultarFuncionesPorFecha() {
        assertEquals(1, cine.consultarFuncionesPorFecha(MANANA).size());
        assertTrue(cine.consultarFuncionesPorFecha(MANANA.plusDays(3)).isEmpty());
    }

    @Test
    @DisplayName("consultarAsientos devuelve los asientos de la función")
    void consultarAsientos() throws Exception {
        List<Asiento> asientos = cine.consultarAsientos("F1");
        assertEquals(20, asientos.size());
        assertEquals("A1", asientos.get(0).getCodigo());
        assertThrows(FuncionNoEncontradaException.class, () -> cine.consultarAsientos("NO"));
    }

    @Test
    @DisplayName("modificarFuncion cambia los datos de una función sin ventas")
    void modificarFuncion() throws Exception {
        Funcion nueva = cine.modificarFuncion("F1", "P1", "S1", MANANA, LocalTime.of(21, 0), 5000,
                Formato.DOS_D, Idioma.SUBTITULADA);
        assertEquals(LocalTime.of(21, 0), nueva.getHora());
        assertEquals(5000, nueva.getValorBase());
        assertEquals(Formato.DOS_D, nueva.getFormato());
        assertEquals(Idioma.SUBTITULADA, nueva.getIdioma());
        assertEquals(1, cine.consultarFunciones().size());
    }

    @Test
    @DisplayName("modificarFuncion con datos inválidos deja la función como estaba")
    void modificarFuncionInvalida() throws Exception {
        Funcion vieja = cine.buscarFuncion("F1");
        assertThrows(DatoInvalidoException.class, () -> cine.modificarFuncion("F1", "P1", "S1",
                MANANA, LocalTime.of(21, 0), -1, Formato.IMAX, Idioma.DOBLADA));
        assertSame(vieja, cine.buscarFuncion("F1"));
        assertTrue(cine.buscarPelicula("P1").getListFunciones().contains(vieja));
    }

    @Test
    @DisplayName("modificarFuncion no se permite con ventas activas ni en funciones canceladas")
    void modificarFuncionNoPermitida() throws Exception {
        cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertThrows(ModificacionNoPermitidaException.class, () -> cine.modificarFuncion("F1", "P1", "S1",
                MANANA, LocalTime.of(21, 0), 0, Formato.IMAX, Idioma.DOBLADA));

        cine.programarFuncion("F2", "P1", "S1", MANANA.plusDays(2), LocalTime.of(10, 0), 0,
                Formato.IMAX, Idioma.DOBLADA);
        cine.cancelarFuncion("F2");
        assertThrows(ModificacionNoPermitidaException.class, () -> cine.modificarFuncion("F2", "P1", "S1",
                MANANA.plusDays(2), LocalTime.of(11, 0), 0, Formato.IMAX, Idioma.DOBLADA));
    }

    @Test
    @DisplayName("modificarValorBase cambia el recargo y no acepta negativos")
    void modificarValorBase() throws Exception {
        cine.modificarValorBase("F1", 3000);
        assertEquals(3000, cine.buscarFuncion("F1").getValorBase());
        assertThrows(DatoInvalidoException.class, () -> cine.modificarValorBase("F1", -1));
        assertThrows(FuncionNoEncontradaException.class, () -> cine.modificarValorBase("NO", 100));
    }

    @Test
    @DisplayName("cancelarFuncion deja la función en el historial como CANCELADA")
    void cancelarFuncion() throws Exception {
        cine.cancelarFuncion("F1");
        assertEquals(EstadoFuncion.CANCELADA, cine.buscarFuncion("F1").getEstado());
        assertEquals(1, cine.consultarFunciones().size());
        assertThrows(FuncionNoDisponibleException.class, () -> cine.cancelarFuncion("F1"));
    }

    @Test
    @DisplayName("cancelarFuncion no se permite con entradas activas, pero sí si se cancelaron")
    void cancelarFuncionConVentas() throws Exception {
        Venta venta = cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertThrows(CancelacionNoPermitidaException.class, () -> cine.cancelarFuncion("F1"));
        cine.cancelarVenta(venta.getCodigo());
        assertDoesNotThrow(() -> cine.cancelarFuncion("F1"));
    }
}
