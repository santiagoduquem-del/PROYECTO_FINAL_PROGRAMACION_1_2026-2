package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SalaTest {

    @Test
    void creaTantosAsientosComoLaCapacidad() {
        Sala sala = DatosPrueba.sala("S1", "SALA1", 20, EstadoSala.DISPONIBLE);
        assertEquals(20, sala.getCapacidad());
        assertEquals(20, sala.getAsientos().size());
    }

    @Test
    void lasFilasSonDeDiezAsientosHastaDosCientosSesenta() {
        Sala sala = DatosPrueba.sala("S1", "SALA2", 20, EstadoSala.DISPONIBLE);
        assertEquals(10, sala.getAsientosPorFila());
        assertEquals("A1", sala.getAsientos().get(0).getCodigo());
        assertEquals("A10", sala.getAsientos().get(9).getCodigo());
        assertEquals("B1", sala.getAsientos().get(10).getCodigo());
        assertEquals("B10", sala.getAsientos().get(19).getCodigo());
    }

    @Test
    void laUltimaFilaPuedeQuedarIncompleta() {
        Sala sala = DatosPrueba.sala("S1", "SALA3", 15, EstadoSala.DISPONIBLE);
        assertEquals(15, sala.getAsientos().size());
        assertEquals("A10", sala.getAsientos().get(9).getCodigo());
        assertEquals("B5", sala.getAsientos().get(14).getCodigo());
    }

    @Test
    void conDosCientosSesentaAsientosLlegaHastaLaFilaZ() {
        Sala sala = DatosPrueba.sala("S1", "SALA4", 260, EstadoSala.DISPONIBLE);
        assertEquals(10, sala.getAsientosPorFila());
        assertEquals("Z10", sala.getAsientos().get(259).getCodigo());
    }

    @Test
    void conMasDeDosCientosSesentaLasFilasSeEnsanchan() {
        Sala sala = DatosPrueba.sala("S1", "SALA5", 261, EstadoSala.DISPONIBLE);
        assertEquals(11, sala.getAsientosPorFila());
        assertEquals(261, sala.getAsientos().size());
        for (Asiento asiento : sala.getAsientos()) {
            assertTrue(asiento.getFila() >= 'A' && asiento.getFila() <= 'Z');
        }
    }

    @Test
    void laCapacidadMaximaDeMilAsientosTambienCabeEnLasFilasAZ() {
        Sala sala = DatosPrueba.sala("S1", "SALA6", 1000, EstadoSala.FUERA_DE_SERVICIO);
        assertEquals(1000, sala.getAsientos().size());
        assertEquals(39, sala.getAsientosPorFila());
        assertEquals('Z', sala.getAsientos().get(999).getFila());
    }

    @Test
    void ningunAsientoRepiteCodigo() {
        for (int capacidad : new int[]{261, 20, 1000}) {
            Sala sala = DatosPrueba.sala("S1", "SALA7", 20, EstadoSala.DISPONIBLE);
            Set<String> codigos = new HashSet<>();
            for (Asiento asiento : sala.getAsientos()) {
                codigos.add(asiento.getCodigo());
            }
            assertEquals(capacidad, codigos.size());
        }
    }

    @Test
    void conCapacidadInvalidaNoSeGeneranAsientos() {
        assertEquals(0, DatosPrueba.sala("S1", "SALA9", 0, EstadoSala.DISPONIBLE).getAsientos().size());
        assertEquals(0, DatosPrueba.sala("S2", "SALA10", -5, EstadoSala.DISPONIBLE).getAsientos().size());
        assertEquals(0, DatosPrueba.sala("S3", "SALA11", 0, EstadoSala.DISPONIBLE).getAsientos().size());
    }

    @Test
    void cambiarLaCapacidadVuelveAGenerarLosAsientos() {
        Sala sala = DatosPrueba.sala("S1", "SALA", 20, EstadoSala.DISPONIBLE);
        sala.setCapacidad(35);
        assertEquals(35, sala.getCapacidad());
        assertEquals(35, sala.getAsientos().size());
        assertEquals("D5", sala.getAsientos().get(34).getCodigo());
    }

    @Test
    void reorganizarAsientosConservaLaCapacidad() {
        Sala sala = DatosPrueba.sala("S1", "SALA", 20, EstadoSala.DISPONIBLE);
        sala.reorganizarAsientos();
        assertEquals(20, sala.getAsientos().size());
        assertTrue(sala.tieneDistribucionActual());
    }

    @Test
    void unaSalaNuevaTieneLaDistribucionActual() {
        assertTrue(DatosPrueba.sala("S1", "SALA", 20, EstadoSala.DISPONIBLE).tieneDistribucionActual());
        assertTrue(DatosPrueba.sala("S2", "SALA", 500, EstadoSala.DISPONIBLE).tieneDistribucionActual());
    }

    @Test
    void deshabilitarLaDejaFueraDeServicioYHabilitarLaRegresa() {
        Sala sala = DatosPrueba.sala("S1", "sala", 20, EstadoSala.DISPONIBLE);
        assertTrue(sala.estaDisponible());
        sala.deshabilitar();
        assertFalse(sala.estaDisponible());
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, sala.getEstado());
        sala.habilitar();
        assertTrue(sala.estaDisponible());
    }

    @Test
    void toStringMuestraNombreYCodigo() {
        assertEquals("Sala S1 (S1)", DatosPrueba.sala("S1", "Sala S1", 20, EstadoSala.DISPONIBLE).toString());
    }
}

