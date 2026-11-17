package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CinePeliculasTest {

    private Cine cine;

    @BeforeEach
    void preparar() {
        cine = DatosPrueba.crearCine();
    }

    private Pelicula peliculaCon(String codigo, String nombre, int duracion, List<Formato> formatos, int anio) {
        return new Pelicula(codigo, nombre, Genero.DRAMA, duracion, Clasificacion.TODOS, formatos, anio,
                EstadoPelicula.DISPONIBLE);
    }

    private List<Formato> formatos(Formato... formatos) {
        List<Formato> lista = new ArrayList<>();
        for (Formato formato : formatos) {
            lista.add(formato);
        }
        return lista;
    }

    // ---------------- registrar y buscar ----------------

    @Test
    void registrarPeliculaLaAgregaAlCatalogo() throws Exception {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE);
        cine.registrarPelicula(pelicula);
        assertEquals(1, cine.consultarPeliculas().size());
        assertSame(pelicula, cine.buscarPelicula("P1"));
    }

    @Test
    void noPermiteDosPeliculasConElMismoCodigo() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        assertThrows(PeliculaDuplicadaException.class, () -> cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE)));
        assertEquals(1, cine.consultarPeliculas().size());
    }

    @Test
    void dosPeliculasConCodigosDistintosSePermiten() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.registrarPelicula(DatosPrueba.pelicula("P2", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        assertEquals(2, cine.consultarPeliculas().size());
    }

    @Test
    void rechazaUnaPeliculaSinCodigo() {
        Pelicula pelicula = peliculaCon("  ", "Interestelar", 169, formatos(Formato.IMAX), 2014);
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(pelicula));
    }

    @Test
    void rechazaUnaPeliculaSinNombre() {
        Pelicula pelicula = peliculaCon("P1", "", 169, formatos(Formato.IMAX), 2014);
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(pelicula));
    }

    @Test
    void rechazaDuracionCeroONegativa() {
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarPelicula(peliculaCon("P1", "X", 0, formatos(Formato.IMAX), 2014)));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarPelicula(peliculaCon("P2", "X", -10, formatos(Formato.IMAX), 2014)));
    }

    @Test
    void rechazaUnaPeliculaSinFormatos() {
        Pelicula pelicula = peliculaCon("P1", "X", 120, formatos(), 2014);
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(pelicula));
    }

    @Test
    void elAnioDeEstrenoDebeEstarEntre1895YDosAniosDespuesDelActual() throws Exception {
        int maximo = LocalDate.now().getYear() + 2;
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarPelicula(peliculaCon("P1", "X", 90, formatos(Formato.DOS_D), 1894)));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarPelicula(peliculaCon("P2", "X", 90, formatos(Formato.DOS_D), maximo + 1)));
        cine.registrarPelicula(peliculaCon("P3", "X", 90, formatos(Formato.DOS_D), 1895));
        cine.registrarPelicula(peliculaCon("P4", "X", 90, formatos(Formato.DOS_D), maximo));
        assertEquals(2, cine.consultarPeliculas().size());
    }

    @Test
    void buscarUnaPeliculaInexistenteLanzaExcepcion() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.buscarPelicula("NO-EXISTE"));
    }

    // ---------------- consultar, habilitar, deshabilitar ----------------

    @Test
    void consultarPeliculasDisponiblesNoIncluyeLasDeshabilitadas() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.registrarPelicula(DatosPrueba.pelicula("P2", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.deshabilitarPelicula("P2");

        assertEquals(2, cine.consultarPeliculas().size());
        assertEquals(1, cine.consultarPeliculasDisponibles().size());
        assertEquals("P1", cine.consultarPeliculasDisponibles().get(0).getCodigo());
    }

    @Test
    void deshabilitarYHabilitarCambianElEstado() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.deshabilitarPelicula("P1");
        assertEquals(EstadoPelicula.NO_DISPONIBLE, cine.buscarPelicula("P1").getEstado());
        cine.habilitarPelicula("P1");
        assertEquals(EstadoPelicula.DISPONIBLE, cine.buscarPelicula("P1").getEstado());
    }

    @Test
    void deshabilitarUnaPeliculaInexistenteLanzaExcepcion() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.deshabilitarPelicula("NO-EXISTE"));
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.habilitarPelicula("NO-EXISTE"));
    }

    // ---------------- modificar ----------------

    @Test
    void modificarPeliculaCambiaSusDatos() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.modificarPelicula("P1", "Nuevo nombre", Genero.TERROR, 95, Clasificacion.MAYORES_18,
                formatos(Formato.TRES_D), 2020);

        Pelicula pelicula = cine.buscarPelicula("P1");
        assertEquals("Nuevo nombre", pelicula.getNombre());
        assertEquals(Genero.TERROR, pelicula.getGenero());
        assertEquals(95, pelicula.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_18, pelicula.getClasificacion());
        assertEquals(1, pelicula.getFormatos().size());
        assertEquals(2020, pelicula.getAnioEstreno());
    }

    @Test
    void modificarPeliculaConDatosInvalidosNoCambiaNada() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "Película P1", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "", Genero.DRAMA, 90,
                Clasificacion.TODOS, formatos(Formato.DOS_D), 2020));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "X", Genero.DRAMA, 0,
                Clasificacion.TODOS, formatos(Formato.DOS_D), 2020));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "X", Genero.DRAMA, 90,
                Clasificacion.TODOS, formatos(), 2020));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "X", Genero.DRAMA, 90,
                Clasificacion.TODOS, null, 2020));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "X", Genero.DRAMA, 90,
                Clasificacion.TODOS, formatos(Formato.DOS_D), 1500));
        assertEquals("Película P1", cine.buscarPelicula("P1").getNombre());
    }

    @Test
    void modificarUnaPeliculaInexistenteLanzaExcepcion() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.modificarPelicula("NO-EXISTE", "X",
                Genero.DRAMA, 90, Clasificacion.TODOS, formatos(Formato.DOS_D), 2020));
    }

    @Test
    void noSePuedeQuitarUnFormatoQueUsaUnaFuncionProgramada() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));      // Signature e IMAX
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.IMAX, Idioma.DOBLADA);

        assertThrows(ModificacionNoPermitidaException.class, () -> cine.modificarPelicula("P1", "X",
                Genero.DRAMA, 90, Clasificacion.TODOS, formatos(Formato.DOS_D), 2020));
        // quitar un formato que ninguna función usa sí se puede
        cine.modificarPelicula("P1", "X", Genero.DRAMA, 90, Clasificacion.TODOS,
                formatos(Formato.IMAX, Formato.TRES_D), 2020);
        assertTrue(cine.buscarPelicula("P1").tieneFormato(Formato.TRES_D));
    }

    @Test
    void asignarInformacionGuardaSinopsisElencoYEstudio() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.asignarInformacionPelicula("P1", "  Una sinopsis  ", "Actor Uno", null);
        Pelicula pelicula = cine.buscarPelicula("P1");
        assertEquals("Una sinopsis", pelicula.getSinopsis());
        assertEquals("Actor Uno", pelicula.getElenco());
        assertEquals("", pelicula.getEstudio());
    }

    @Test
    void asignarImagenesGuardaLasRutas() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.asignarImagenesPelicula("P1", "caratula.png", "banner.png");
        assertEquals("caratula.png", cine.buscarPelicula("P1").getRutaCaratula());
        assertEquals("banner.png", cine.buscarPelicula("P1").getRutaBanner());
    }

    // ---------------- eliminar ----------------

    @Test
    void eliminarUnaPeliculaSinFuncionesLaQuitaDelCatalogo() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.eliminarPelicula("P1");
        assertEquals(0, cine.consultarPeliculas().size());
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.buscarPelicula("P1"));
    }

    @Test
    void noSePuedeEliminarUnaPeliculaConFunciones() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "EL DIABLO", Genero.TERROR, 110, Clasificacion.MAYORES_15, Arrays.asList(Formato.values()), 2002, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarPelicula("P1"));
        assertEquals(1, cine.consultarPeliculas().size());
    }

    @Test
    void eliminarUnaPeliculaInexistenteLanzaExcepcion() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.eliminarPelicula("NO-EXISTE"));
    }
}

