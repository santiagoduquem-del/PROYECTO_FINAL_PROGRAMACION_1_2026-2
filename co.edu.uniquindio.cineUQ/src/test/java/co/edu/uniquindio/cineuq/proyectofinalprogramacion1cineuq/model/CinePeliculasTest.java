package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: gestión de películas")
class CinePeliculasTest {

    private Cine cine;

    @BeforeEach
    void setUp() {
        cine = DatosPrueba.crearCine();
    }

    @Test
    @DisplayName("registrarPelicula agrega la película")
    void registrarPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        assertEquals(1, cine.consultarPeliculas().size());
    }

    @Test
    @DisplayName("registrarPelicula rechaza un código repetido")
    void registrarPeliculaDuplicada() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        assertThrows(PeliculaDuplicadaException.class,
                () -> cine.registrarPelicula(DatosPrueba.crearPelicula("P1")));
    }

    @Test
    @DisplayName("registrarPelicula rechaza código o nombre vacíos")
    void registrarPeliculaSinTexto() {
        Pelicula sinCodigo = new Pelicula(" ", "X", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(Formato.DOS_D), 2020, EstadoPelicula.DISPONIBLE);
        Pelicula sinNombre = new Pelicula("P9", "", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(Formato.DOS_D), 2020, EstadoPelicula.DISPONIBLE);
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(sinCodigo));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(sinNombre));
    }

    @Test
    @DisplayName("registrarPelicula rechaza duración cero, sin formatos o año fuera de rango")
    void registrarPeliculaDatosInvalidos() {
        Pelicula duracionCero = new Pelicula("P2", "X", Genero.DRAMA, 0, Clasificacion.TODOS,
                List.of(Formato.DOS_D), 2020, EstadoPelicula.DISPONIBLE);
        Pelicula sinFormatos = new Pelicula("P3", "X", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(), 2020, EstadoPelicula.DISPONIBLE);
        Pelicula anioViejo = new Pelicula("P4", "X", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(Formato.DOS_D), 1800, EstadoPelicula.DISPONIBLE);
        Pelicula anioFuturo = new Pelicula("P5", "X", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(Formato.DOS_D), LocalDate.now().getYear() + 3, EstadoPelicula.DISPONIBLE);
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(duracionCero));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(sinFormatos));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(anioViejo));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarPelicula(anioFuturo));
    }

    @Test
    @DisplayName("buscarPelicula encuentra la película o lanza excepción")
    void buscarPelicula() throws Exception {
        Pelicula pelicula = DatosPrueba.crearPelicula("P1");
        cine.registrarPelicula(pelicula);
        assertSame(pelicula, cine.buscarPelicula("P1"));
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.buscarPelicula("NO"));
    }

    @Test
    @DisplayName("consultarPeliculasDisponibles no incluye las deshabilitadas")
    void consultarPeliculasDisponibles() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.registrarPelicula(DatosPrueba.crearPelicula("P2"));
        cine.deshabilitarPelicula("P2");
        assertEquals(1, cine.consultarPeliculasDisponibles().size());
        assertEquals("P1", cine.consultarPeliculasDisponibles().get(0).getCodigo());
    }

    @Test
    @DisplayName("modificarPelicula cambia los datos")
    void modificarPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.modificarPelicula("P1", "Nuevo nombre", Genero.TERROR, 95, Clasificacion.MAYORES_18,
                List.of(Formato.TRES_D), 2010);
        Pelicula pelicula = cine.buscarPelicula("P1");
        assertEquals("Nuevo nombre", pelicula.getNombre());
        assertEquals(Genero.TERROR, pelicula.getGenero());
        assertEquals(95, pelicula.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_18, pelicula.getClasificacion());
        assertEquals(List.of(Formato.TRES_D), pelicula.getFormatos());
        assertEquals(2010, pelicula.getAnioEstreno());
    }

    @Test
    @DisplayName("modificarPelicula valida los datos nuevos")
    void modificarPeliculaInvalida() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "", Genero.DRAMA, 90,
                Clasificacion.TODOS, List.of(Formato.DOS_D), 2020));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "X", Genero.DRAMA, -5,
                Clasificacion.TODOS, List.of(Formato.DOS_D), 2020));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarPelicula("P1", "X", Genero.DRAMA, 90,
                Clasificacion.TODOS, List.of(), 2020));
        assertThrows(PeliculaNoEncontradaException.class, () -> cine.modificarPelicula("NO", "X", Genero.DRAMA,
                90, Clasificacion.TODOS, List.of(Formato.DOS_D), 2020));
    }

    @Test
    @DisplayName("modificarPelicula no deja quitar un formato que usa una función programada")
    void modificarPeliculaQuitandoFormatoEnUso() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();   // F1 está en IMAX
        assertThrows(ModificacionNoPermitidaException.class, () -> conFuncion.modificarPelicula("P1", "X",
                Genero.DRAMA, 120, Clasificacion.TODOS, List.of(Formato.DOS_D), 2020));
    }

    @Test
    @DisplayName("asignarImagenesPelicula guarda las rutas de carátula y banner")
    void asignarImagenesPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.asignarImagenesPelicula("P1", "file:/caratula.jpg", "file:/banner.jpg");
        assertEquals("file:/caratula.jpg", cine.buscarPelicula("P1").getRutaCaratula());
        assertEquals("file:/banner.jpg", cine.buscarPelicula("P1").getRutaBanner());
    }

    @Test
    @DisplayName("asignarInformacionPelicula guarda sinopsis, elenco y estudio sin espacios sobrantes")
    void asignarInformacionPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.asignarInformacionPelicula("P1", "  Una historia  ", "Actor 1, Actor 2", null);
        Pelicula pelicula = cine.buscarPelicula("P1");
        assertEquals("Una historia", pelicula.getSinopsis());
        assertEquals("Actor 1, Actor 2", pelicula.getElenco());
        assertEquals("", pelicula.getEstudio());
    }

    @Test
    @DisplayName("eliminarPelicula borra una película sin funciones")
    void eliminarPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.eliminarPelicula("P1");
        assertTrue(cine.consultarPeliculas().isEmpty());
    }

    @Test
    @DisplayName("eliminarPelicula no borra una película con funciones")
    void eliminarPeliculaConFunciones() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        assertThrows(EliminacionNoPermitidaException.class, () -> conFuncion.eliminarPelicula("P1"));
    }

    @Test
    @DisplayName("habilitarPelicula y deshabilitarPelicula cambian el estado")
    void habilitarYDeshabilitarPelicula() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.deshabilitarPelicula("P1");
        assertEquals(EstadoPelicula.NO_DISPONIBLE, cine.buscarPelicula("P1").getEstado());
        cine.habilitarPelicula("P1");
        assertEquals(EstadoPelicula.DISPONIBLE, cine.buscarPelicula("P1").getEstado());
    }

    @Test
    @DisplayName("No se puede programar una función de una película deshabilitada")
    void funcionDePeliculaDeshabilitada() throws Exception {
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
        cine.registrarSala(DatosPrueba.crearSala("S1", 10));
        cine.deshabilitarPelicula("P1");
        assertThrows(PeliculaNoDisponibleException.class, () -> cine.programarFuncion("F1", "P1", "S1",
                DatosPrueba.MANANA, LocalTime.of(10, 0), 0, Formato.IMAX, Idioma.DOBLADA));
    }
}
