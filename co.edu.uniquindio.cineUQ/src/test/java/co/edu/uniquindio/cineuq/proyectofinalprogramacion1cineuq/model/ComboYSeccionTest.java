package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Combo y SeccionPelicula")
class ComboYSeccionTest {

    @Test
    @DisplayName("Un combo nuevo está disponible, sin foto y no es exclusivo")
    void comboNuevo() {
        Combo combo = new Combo("C1", "Personal", "Crispetas + gaseosa", 18000);
        assertTrue(combo.estaDisponible());
        assertNull(combo.getRutaImagen());
        assertFalse(combo.isExclusivoVipBistro());
        assertEquals("Personal", combo.toString());
    }

    @Test
    @DisplayName("habilitar y deshabilitar el combo")
    void habilitable() {
        Combo combo = new Combo("C1", "Personal", "x", 18000);
        combo.deshabilitar();
        assertFalse(combo.estaDisponible());
        combo.habilitar();
        assertTrue(combo.estaDisponible());
    }

    @Test
    @DisplayName("sePuedeVenderEn: normal en cualquier formato; exclusivo solo en VIP Bistró")
    void sePuedeVenderEn() {
        Combo normal = new Combo("C1", "Personal", "x", 18000);
        Combo exclusivo = new Combo("C7", "Bistró", "x", 48000);
        exclusivo.setExclusivoVipBistro(true);
        assertTrue(normal.sePuedeVenderEn(Formato.IMAX));
        assertTrue(normal.sePuedeVenderEn(null));
        assertTrue(exclusivo.sePuedeVenderEn(Formato.VIP_BISTRO));
        assertFalse(exclusivo.sePuedeVenderEn(Formato.IMAX));
        assertFalse(exclusivo.sePuedeVenderEn(null));
    }

    @Test
    @DisplayName("Setters del combo")
    void settersCombo() {
        Combo combo = new Combo("C1", "Personal", "x", 18000);
        combo.setNombre("Mega");
        combo.setDescripcion("Crispetas XL");
        combo.setPrecio(30000);
        combo.setRutaImagen("file:/c.jpg");
        assertEquals("C1", combo.getCodigo());
        assertEquals("Mega", combo.getNombre());
        assertEquals("Crispetas XL", combo.getDescripcion());
        assertEquals(30000, combo.getPrecio());
        assertEquals("file:/c.jpg", combo.getRutaImagen());
    }

    @Test
    @DisplayName("SeccionPelicula: datos, tieneImagen y toString")
    void seccion() {
        SeccionPelicula seccion = new SeccionPelicula("Exclusivo", "Sorteo", "Texto", null);
        assertFalse(seccion.tieneImagen());
        assertEquals("Sorteo", seccion.toString());

        seccion.setEtiqueta("Nuevo");
        seccion.setTitulo("Título");
        seccion.setTexto("Otro texto");
        seccion.setRutaImagen("file:/f.jpg");
        assertEquals("Nuevo", seccion.getEtiqueta());
        assertEquals("Título", seccion.getTitulo());
        assertEquals("Otro texto", seccion.getTexto());
        assertEquals("file:/f.jpg", seccion.getRutaImagen());
        assertTrue(seccion.tieneImagen());
    }
}