package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.PeliculaNoEncontradaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: secciones del detalle de la película")
class CineSeccionesTest {

    private Cine cine;

    @BeforeEach
    void setUp() throws Exception {
        cine = DatosPrueba.crearCine();
        cine.registrarPelicula(DatosPrueba.crearPelicula("P1"));
    }

    private List<SeccionPelicula> secciones() throws Exception {
        return cine.buscarPelicula("P1").getListSecciones();
    }

    @Test
    @DisplayName("Una película nueva no tiene secciones")
    void sinSecciones() throws Exception {
        assertTrue(secciones().isEmpty());
    }

    @Test
    @DisplayName("agregarSeccionPelicula agrega al final y quita los espacios sobrantes")
    void agregarSeccion() throws Exception {
        cine.agregarSeccionPelicula("P1", " Exclusivo ", " Sorteo ", " Gana premios ", null);
        cine.agregarSeccionPelicula("P1", null, "Pretzels", "", "file:/foto.jpg");
        assertEquals(2, secciones().size());
        SeccionPelicula primera = secciones().get(0);
        assertEquals("Exclusivo", primera.getEtiqueta());
        assertEquals("Sorteo", primera.getTitulo());
        assertEquals("Gana premios", primera.getTexto());
        assertFalse(primera.tieneImagen());
        SeccionPelicula segunda = secciones().get(1);
        assertEquals("", segunda.getEtiqueta());
        assertTrue(segunda.tieneImagen());
    }

    @Test
    @DisplayName("agregarSeccionPelicula exige título o texto")
    void agregarSeccionVacia() {
        assertThrows(DatoInvalidoException.class,
                () -> cine.agregarSeccionPelicula("P1", "Etiqueta", " ", null, "file:/foto.jpg"));
        assertThrows(PeliculaNoEncontradaException.class,
                () -> cine.agregarSeccionPelicula("NO", "", "Título", "", null));
    }

    @Test
    @DisplayName("modificarSeccionPelicula cambia todos los datos de la sección")
    void modificarSeccion() throws Exception {
        cine.agregarSeccionPelicula("P1", "", "Viejo", "texto", null);
        cine.modificarSeccionPelicula("P1", 0, "Nuevo", "Título nuevo", "Texto nuevo", "file:/f.jpg");
        SeccionPelicula seccion = secciones().get(0);
        assertEquals("Nuevo", seccion.getEtiqueta());
        assertEquals("Título nuevo", seccion.getTitulo());
        assertEquals("Texto nuevo", seccion.getTexto());
        assertEquals("file:/f.jpg", seccion.getRutaImagen());
    }

    @Test
    @DisplayName("modificarSeccionPelicula rechaza una posición que no existe")
    void modificarSeccionPosicionInvalida() throws Exception {
        cine.agregarSeccionPelicula("P1", "", "Título", "", null);
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarSeccionPelicula("P1", -1, "", "X", "", null));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarSeccionPelicula("P1", 1, "", "X", "", null));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarSeccionPelicula("P1", 0, "", "", "", null));
    }

    @Test
    @DisplayName("eliminarSeccionPelicula quita la sección indicada")
    void eliminarSeccion() throws Exception {
        cine.agregarSeccionPelicula("P1", "", "A", "", null);
        cine.agregarSeccionPelicula("P1", "", "B", "", null);
        cine.eliminarSeccionPelicula("P1", 0);
        assertEquals(1, secciones().size());
        assertEquals("B", secciones().get(0).getTitulo());
        assertThrows(DatoInvalidoException.class, () -> cine.eliminarSeccionPelicula("P1", 5));
    }

    @Test
    @DisplayName("moverSeccionPelicula sube y baja secciones y devuelve la nueva posición")
    void moverSeccion() throws Exception {
        cine.agregarSeccionPelicula("P1", "", "A", "", null);
        cine.agregarSeccionPelicula("P1", "", "B", "", null);
        cine.agregarSeccionPelicula("P1", "", "C", "", null);

        assertEquals(1, cine.moverSeccionPelicula("P1", 0, 1));    // A baja: B, A, C
        assertEquals("B", secciones().get(0).getTitulo());
        assertEquals("A", secciones().get(1).getTitulo());

        assertEquals(1, cine.moverSeccionPelicula("P1", 2, -1));   // C sube: B, C, A
        assertEquals("C", secciones().get(1).getTitulo());
        assertEquals("A", secciones().get(2).getTitulo());
    }

    @Test
    @DisplayName("moverSeccionPelicula no sube la primera ni baja la última")
    void moverSeccionEnLosBordes() throws Exception {
        cine.agregarSeccionPelicula("P1", "", "A", "", null);
        cine.agregarSeccionPelicula("P1", "", "B", "", null);
        assertThrows(DatoInvalidoException.class, () -> cine.moverSeccionPelicula("P1", 0, -1));
        assertThrows(DatoInvalidoException.class, () -> cine.moverSeccionPelicula("P1", 1, 1));
    }
}