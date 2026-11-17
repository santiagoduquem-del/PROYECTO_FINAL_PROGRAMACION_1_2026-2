package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoOcupadoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsientoTest {

    @Test
    void elCodigoSeArmaConLaFilaYElNumero() {
        Asiento asiento = new Asiento('B', 7);
        assertEquals("B7", asiento.getCodigo());
        assertEquals('B', asiento.getFila());
        assertEquals(7, asiento.getNumero());
    }

    @Test
    void unAsientoNuevoEstaDisponible() {
        Asiento asiento = new Asiento('A', 1);
        assertTrue(asiento.estaDisponible());
        Assertions.assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstado());
    }

    @Test
    void ocuparCambiaElEstadoAOcupado() throws Exception {
        Asiento asiento = new Asiento('A', 1);
        asiento.ocupar();
        assertFalse(asiento.estaDisponible());
        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());
    }

    @Test
    void ocuparUnAsientoOcupadoLanzaExcepcion() throws Exception {
        Asiento asiento = new Asiento('A', 1);
        asiento.ocupar();
        assertThrows(AsientoOcupadoException.class, () -> asiento.ocupar());
    }

    @Test
    void liberarDejaElAsientoDisponible() throws Exception {
        Asiento asiento = new Asiento('A', 1);
        asiento.ocupar();
        asiento.liberar();
        assertTrue(asiento.estaDisponible());
    }

    @Test
    void liberarUnAsientoYaLibreNoCambiaNada() {
        Asiento asiento = new Asiento('A', 1);
        asiento.liberar();
        assertTrue(asiento.estaDisponible());
    }

    @Test
    void despuesDeLiberarSePuedeOcuparOtraVez() throws Exception {
        Asiento asiento = new Asiento('C', 3);
        asiento.ocupar();
        asiento.liberar();
        assertDoesNotThrow(() -> asiento.ocupar());
    }
}
