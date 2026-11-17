package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * La película de prueba dura 120 minutos y cada función deja 15 minutos de limpieza:
 * una función de las 6:00 p. m. ocupa la sala hasta las 8:15 p. m.
 */
class CineFuncionesTest {

    private Cine cine;

    @BeforeEach
    void preparar() throws Exception {
        cine = DatosPrueba.crearCine();
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "Rambo", Genero.ACCION, 120, Clasificacion.TODOS, List.of(Formato.IMAX, Formato.DOS_D), 2005, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "20", 50, EstadoSala.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S2", "20", 50, EstadoSala.DISPONIBLE));
        cine.registrarCliente(DatosPrueba.cliente("1", "Raul", "31132020", "Raul@brainner.com", TipoCliente.GENERAL, PlanMembresia.FREE));
    }

    private Funcion programar(String codigo, String sala, LocalTime hora) throws Exception {
        return cine.programarFuncion(codigo, "P1", sala, DatosPrueba.manana(), hora, 0,
                Formato.DOS_D, Idioma.DOBLADA);
    }

    // ---------------- programar ----------------

    @Test
    void programarFuncionLaRegistraProgramadaYLaRelacionaConLaPelicula() throws Exception {
        Funcion funcion = programar("F1", "S1", LocalTime.of(18, 0));
        assertEquals(EstadoFuncion.PROGRAMADA, funcion.getEstado());
        assertSame(funcion, cine.buscarFuncion("F1"));
        assertEquals(1, cine.consultarFunciones().size());
        assertTrue(cine.buscarPelicula("P1").getListFunciones().contains(funcion));
        assertEquals(50, funcion.getAsientos().size());
    }

    @Test
    void noPermiteDosFuncionesConElMismoCodigo() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertThrows(FuncionDuplicadaException.class, () -> programar("F1", "S2", LocalTime.of(18, 0)));
    }

    @Test
    void noPermiteProgramarConUnaPeliculaInexistente() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.programarFuncion("F1", "NO-EXISTE", "S1",
                DatosPrueba.manana(), LocalTime.of(18, 0), 0, Formato.DOS_D, Idioma.DOBLADA));
    }

    @Test
    void noPermiteProgramarConUnaPeliculaDeshabilitada() throws Exception {
        cine.deshabilitarPelicula("P1");
        assertThrows(PeliculaNoDisponibleException.class, () -> programar("F1", "S1", LocalTime.of(18, 0)));
    }

    @Test
    void noPermiteProgramarEnUnaSalaInexistente() {
        assertThrows(SalaNoEncontradaException.class, () -> programar("F1", "NO-EXISTE", LocalTime.of(18, 0)));
    }

    @Test
    void noPermiteProgramarEnUnaSalaFueraDeServicio() throws Exception {
        cine.deshabilitarSala("S1");
        assertThrows(SalaNoDisponibleException.class, () -> programar("F1", "S1", LocalTime.of(18, 0)));
        assertEquals(0, cine.consultarFunciones().size());
    }

    @Test
    void rechazaDatosIncompletosOInvalidos() {
        // sin código
        assertThrows(DatoInvalidoException.class, () -> programar(" ", "S1", LocalTime.of(18, 0)));
        // sin fecha o sin hora
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1", null,
                LocalTime.of(18, 0), 0, Formato.DOS_D, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), null, 0, Formato.DOS_D, Idioma.DOBLADA));
        // fecha pasada
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                LocalDate.now().minusDays(1), LocalTime.of(18, 0), 0, Formato.DOS_D, Idioma.DOBLADA));
        // valor base negativo
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(18, 0), -1, Formato.DOS_D, Idioma.DOBLADA));
        // sin formato, sin idioma
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(18, 0), 0, null, Idioma.DOBLADA));
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(18, 0), 0, Formato.DOS_D, null));
        assertEquals(0, cine.consultarFunciones().size());
    }

    @Test
    void elFormatoDebeSerUnoDeLosFormatosDeLaPelicula() {
        assertThrows(DatoInvalidoException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(18, 0), 0, Formato.CUATRO_DX, Idioma.DOBLADA));
    }

    @Test
    void elValorBaseEnCeroEsValido() throws Exception {
        assertEquals(0, programar("F1", "S1", LocalTime.of(18, 0)).getValorBase(), 0.001);
    }

    @Test
    void sePuedeProgramarUnaFuncionHoy() throws Exception {
        Funcion funcion = cine.programarFuncion("F1", "P1", "S1", LocalDate.now(), LocalTime.of(23, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(LocalDate.now(), funcion.getFecha());
    }

    // ---------------- cruces de horario ----------------

    @Test
    void noPermiteDosFuncionesALaMismaHoraEnLaMismaSala() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertThrows(ConflictoHorarioException.class, () -> programar("F2", "S1", LocalTime.of(18, 0)));
    }

    @Test
    void noPermiteEmpezarEnMedioDeOtraFuncion() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertThrows(ConflictoHorarioException.class, () -> programar("F2", "S1", LocalTime.of(19, 0)));
    }

    @Test
    void noPermiteTerminarDespuesDeQueLaOtraFuncionEmpiece() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertThrows(ConflictoHorarioException.class, () -> programar("F2", "S1", LocalTime.of(16, 30)));
    }

    @Test
    void ladoALadoConLimpiezaSePermite() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));                 // libre desde las 8:15 p. m.
        programar("F2", "S1", LocalTime.of(20, 15));                // justo cuando se libera
        programar("F3", "S1", LocalTime.of(15, 45));                // termina a las 5:45 + 15 = 6:00 p. m.
        assertEquals(3, cine.consultarFunciones().size());
    }

    @Test
    void unMinutoAntesDeQueSeLibereLaSalaSiHayCruce() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertThrows(ConflictoHorarioException.class, () -> programar("F2", "S1", LocalTime.of(20, 14)));
    }

    @Test
    void laMismaHoraEnOtraSalaSePermite() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        programar("F2", "S2", LocalTime.of(18, 0));
        assertEquals(2, cine.consultarFunciones().size());
    }

    @Test
    void laMismaHoraEnOtroDiaSePermite() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.programarFuncion("F2", "P1", "S1", DatosPrueba.manana().plusDays(1), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(2, cine.consultarFunciones().size());
    }

    @Test
    void detectaElCruceDeUnaFuncionQuePasaDeMedianoche() throws Exception {
        programar("F1", "S1", LocalTime.of(23, 0));                 // termina 1:00 a. m. del día siguiente
        assertThrows(ConflictoHorarioException.class, () -> cine.programarFuncion("F2", "P1", "S1",
                DatosPrueba.manana().plusDays(1), LocalTime.of(0, 30), 0, Formato.DOS_D, Idioma.DOBLADA));
    }

    @Test
    void unaFuncionCanceladaYaNoBloqueaLaSala() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.cancelarFuncion("F1");
        programar("F2", "S1", LocalTime.of(18, 0));
        assertEquals(2, cine.consultarFunciones().size());
    }

    // ---------------- consultar ----------------

    @Test
    void consultarFuncionesProgramadasNoIncluyeLasCanceladas() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        programar("F2", "S2", LocalTime.of(18, 0));
        cine.cancelarFuncion("F2");
        assertEquals(2, cine.consultarFunciones().size());
        assertEquals(1, cine.consultarFuncionesProgramadas().size());
        assertEquals("F1", cine.consultarFuncionesProgramadas().get(0).getCodigo());
    }

    @Test
    void consultarFuncionesProgramadasDeUnaPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P2", "Amrican Psycho", Genero.TERROR, 130, Clasificacion.MAYORES_12, Arrays.asList(Formato.values()), 2003, EstadoPelicula.DISPONIBLE));
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.programarFuncion("F2", "P2", "S2", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(1, cine.consultarFuncionesProgramadas("P1").size());
        assertEquals("F2", cine.consultarFuncionesProgramadas("P2").get(0).getCodigo());
        assertEquals(0, cine.consultarFuncionesProgramadas("NO-EXISTE").size());
    }

    @Test
    void consultarFuncionesPorFecha() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.programarFuncion("F2", "P1", "S1", DatosPrueba.manana().plusDays(3), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(1, cine.consultarFuncionesPorFecha(DatosPrueba.manana()).size());
        assertEquals(1, cine.consultarFuncionesPorFecha(DatosPrueba.manana().plusDays(3)).size());
        assertEquals(0, cine.consultarFuncionesPorFecha(DatosPrueba.manana().plusDays(1)).size());
    }

    @Test
    void consultarAsientosDevuelveLosDeLaFuncion() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertEquals(50, cine.consultarAsientos("F1").size());
    }

    @Test
    void buscarOConsultarAsientosDeUnaFuncionInexistenteLanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class, () -> cine.buscarFuncion("NO-EXISTE"));
        assertThrows(FuncionNoEncontradaException.class, () -> cine.consultarAsientos("NO-EXISTE"));
    }

    // ---------------- cancelar ----------------

    @Test
    void cancelarUnaFuncionLaDejaCanceladaPeroEnElHistorial() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.cancelarFuncion("F1");
        assertEquals(EstadoFuncion.CANCELADA, cine.buscarFuncion("F1").getEstado());
        assertEquals(1, cine.consultarFunciones().size());
    }

    @Test
    void noSePuedeCancelarUnaFuncionConEntradasActivas() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null);
        assertThrows(CancelacionNoPermitidaException.class, () -> cine.cancelarFuncion("F1"));
        assertEquals(EstadoFuncion.PROGRAMADA, cine.buscarFuncion("F1").getEstado());
    }

    @Test
    void siSeCancelaronLasVentasLaFuncionSePuedeCancelar() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        Venta venta = cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null);
        cine.cancelarVenta(venta.getCodigo());
        cine.cancelarFuncion("F1");
        assertEquals(EstadoFuncion.CANCELADA, cine.buscarFuncion("F1").getEstado());
    }

    @Test
    void noSePuedeCancelarDosVecesLaMismaFuncion() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.cancelarFuncion("F1");
        assertThrows(FuncionNoDisponibleException.class, () -> cine.cancelarFuncion("F1"));
    }

    @Test
    void cancelarUnaFuncionInexistenteLanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class, () -> cine.cancelarFuncion("NO-EXISTE"));
    }

    // ---------------- modificar ----------------

    @Test
    void modificarFuncionCambiaHoraYSala() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        Funcion nueva = cine.modificarFuncion("F1", "P1", "S2", DatosPrueba.manana(), LocalTime.of(20, 0), 3000,
                Formato.IMAX, Idioma.SUBTITULADA);

        assertEquals(1, cine.consultarFunciones().size());
        assertSame(nueva, cine.buscarFuncion("F1"));
        assertEquals("S2", nueva.getSala().getCodigo());
        assertEquals(LocalTime.of(20, 0), nueva.getHora());
        assertEquals(3000, nueva.getValorBase(), 0.001);
        assertEquals(Formato.IMAX, nueva.getFormato());
        assertEquals(1, cine.buscarPelicula("P1").getListFunciones().size());
    }

    @Test
    void modificarFuncionConEntradasActivasNoEstaPermitido() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null);
        assertThrows(ModificacionNoPermitidaException.class, () -> cine.modificarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(20, 0), 0, Formato.DOS_D, Idioma.DOBLADA));
    }

    @Test
    void modificarUnaFuncionCanceladaNoEstaPermitido() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.cancelarFuncion("F1");
        assertThrows(ModificacionNoPermitidaException.class, () -> cine.modificarFuncion("F1", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(20, 0), 0, Formato.DOS_D, Idioma.DOBLADA));
    }

    @Test
    void siLosDatosNuevosSonInvalidosLaFuncionQuedaComoEstaba() throws Exception {
        Funcion original = programar("F1", "S1", LocalTime.of(18, 0));
        cine.deshabilitarSala("S2");
        assertThrows(SalaNoDisponibleException.class, () -> cine.modificarFuncion("F1", "P1", "S2",
                DatosPrueba.manana(), LocalTime.of(20, 0), 0, Formato.DOS_D, Idioma.DOBLADA));

        assertEquals(1, cine.consultarFunciones().size());
        assertSame(original, cine.buscarFuncion("F1"));
        assertEquals(LocalTime.of(18, 0), cine.buscarFuncion("F1").getHora());
        assertTrue(cine.buscarPelicula("P1").getListFunciones().contains(original));
    }

    @Test
    void modificarUnaFuncionInexistenteLanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class, () -> cine.modificarFuncion("NO-EXISTE", "P1", "S1",
                DatosPrueba.manana(), LocalTime.of(20, 0), 0, Formato.DOS_D, Idioma.DOBLADA));
    }

    @Test
    void modificarElValorBaseSePermiteAunConVentas() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null);
        cine.modificarValorBase("F1", 5000);
        assertEquals(5000, cine.buscarFuncion("F1").getValorBase(), 0.001);
    }

    @Test
    void elValorBaseNoPuedeSerNegativo() throws Exception {
        programar("F1", "S1", LocalTime.of(18, 0));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarValorBase("F1", -1));
        assertThrows(FuncionNoEncontradaException.class, () -> cine.modificarValorBase("NO-EXISTE", 1000));
    }
}
