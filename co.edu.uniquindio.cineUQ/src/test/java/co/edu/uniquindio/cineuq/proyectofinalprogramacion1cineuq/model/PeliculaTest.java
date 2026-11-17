package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Película")
class PeliculaTest {

    private Pelicula pelicula(int anio) {
        return new Pelicula("P1", "Dune", Genero.CIENCIA_FICCION, 155, Clasificacion.MAYORES_12,
                List.of(Formato.IMAX, Formato.DOS_D), anio, EstadoPelicula.DISPONIBLE);
    }

    @Test
    @DisplayName("El constructor guarda los datos y copia la lista de formatos")
    void constructor() {
        List<Formato> formatos = new ArrayList<>(List.of(Formato.IMAX));
        Pelicula pelicula = new Pelicula("P1", "Dune", Genero.CIENCIA_FICCION, 155, Clasificacion.MAYORES_12,
                formatos, 2021, EstadoPelicula.DISPONIBLE);
        formatos.add(Formato.XD);   // cambiar la lista original no debe cambiar la película
        assertEquals(List.of(Formato.IMAX), pelicula.getFormatos());
        assertEquals("P1", pelicula.getCodigo());
        assertEquals(155, pelicula.getDuracionMinutos());
        assertTrue(pelicula.getListFunciones().isEmpty());
    }

    @Test
    @DisplayName("habilitar, deshabilitar y estaDisponible")
    void habilitable() {
        Pelicula pelicula = pelicula(2021);
        assertTrue(pelicula.estaDisponible());
        pelicula.deshabilitar();
        assertFalse(pelicula.estaDisponible());
        pelicula.habilitar();
        assertTrue(pelicula.estaDisponible());
    }

    @Test
    @DisplayName("tieneFormato y describirFormatos")
    void formatos() {
        Pelicula pelicula = pelicula(2021);
        assertTrue(pelicula.tieneFormato(Formato.IMAX));
        assertFalse(pelicula.tieneFormato(Formato.CUATRO_DX));
        assertEquals("IMAX · Signature", pelicula.describirFormatos());
        pelicula.setFormatos(List.of());
        assertEquals("Sin formato", pelicula.describirFormatos());
    }

    @Test
    @DisplayName("esReestreno y describirEstreno según el año")
    void estreno() {
        int anioActual = LocalDate.now().getYear();
        assertTrue(pelicula(2014).esReestreno());
        assertEquals("2014 · Reestreno", pelicula(2014).describirEstreno());
        assertFalse(pelicula(anioActual).esReestreno());
        assertEquals(String.valueOf(anioActual), pelicula(anioActual).describirEstreno());
        assertFalse(pelicula(0).esReestreno());
        assertEquals("Sin dato", pelicula(0).describirEstreno());
    }

    @Test
    @DisplayName("agregarFuncion, removerFuncion y contarEntradasVendidas")
    void funciones() throws Exception {
        Cine cine = DatosPrueba.crearCineConFuncion();
        Pelicula pelicula = cine.buscarPelicula("P1");
        assertEquals(0, pelicula.contarEntradasVendidas());
        cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        cine.venderEntrada("F1", "A2", "100", TipoEntrada.GENERAL, null, null);
        assertEquals(2, pelicula.contarEntradasVendidas());

        Funcion otra = new Funcion("F9", pelicula, cine.buscarSala("S1"), DatosPrueba.MANANA,
                LocalTime.of(9, 0), 0, Formato.IMAX, Idioma.DOBLADA);
        pelicula.agregarFuncion(otra);
        assertEquals(2, pelicula.getListFunciones().size());
        pelicula.removerFuncion(otra);
        assertEquals(1, pelicula.getListFunciones().size());
    }

    @Test
    @DisplayName("Sinopsis, elenco y estudio vacíos devuelven texto vacío, nunca null")
    void informacionVacia() {
        Pelicula pelicula = pelicula(2021);
        assertEquals("", pelicula.getSinopsis());
        assertEquals("", pelicula.getElenco());
        assertEquals("", pelicula.getEstudio());
        pelicula.setSinopsis("Arrakis");
        pelicula.setElenco("Timothée Chalamet");
        pelicula.setEstudio("Legendary");
        assertEquals("Arrakis", pelicula.getSinopsis());
        assertEquals("Timothée Chalamet", pelicula.getElenco());
        assertEquals("Legendary", pelicula.getEstudio());
    }

    @Test
    @DisplayName("getListSecciones nunca es null y toString muestra nombre y código")
    void seccionesYToString() {
        Pelicula pelicula = pelicula(2021);
        assertNotNull(pelicula.getListSecciones());
        assertTrue(pelicula.getListSecciones().isEmpty());
        assertEquals("Dune (P1)", pelicula.toString());
    }

    @Test
    @DisplayName("Setters de datos básicos e imágenes")
    void setters() {
        Pelicula pelicula = pelicula(2021);
        pelicula.setCodigo("P2");
        pelicula.setNombre("Dune 2");
        pelicula.setGenero(Genero.AVENTURA);
        pelicula.setDuracionMinutos(166);
        pelicula.setClasificacion(Clasificacion.MAYORES_15);
        pelicula.setAnioEstreno(2024);
        pelicula.setEstado(EstadoPelicula.NO_DISPONIBLE);
        pelicula.setRutaCaratula("c.jpg");
        pelicula.setRutaBanner("b.jpg");
        assertEquals("P2", pelicula.getCodigo());
        assertEquals("Dune 2", pelicula.getNombre());
        assertEquals(Genero.AVENTURA, pelicula.getGenero());
        assertEquals(166, pelicula.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_15, pelicula.getClasificacion());
        assertEquals(2024, pelicula.getAnioEstreno());
        assertEquals(EstadoPelicula.NO_DISPONIBLE, pelicula.getEstado());
        assertEquals("c.jpg", pelicula.getRutaCaratula());
        assertEquals("b.jpg", pelicula.getRutaBanner());
    }
}
