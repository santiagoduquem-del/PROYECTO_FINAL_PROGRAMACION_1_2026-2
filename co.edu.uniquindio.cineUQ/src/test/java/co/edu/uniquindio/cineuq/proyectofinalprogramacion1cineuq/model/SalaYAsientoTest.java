package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoOcupadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sala y Asiento")
class SalaYAsientoTest {

    @Test
    @DisplayName("Una sala normal tiene 10 asientos por fila: A1..A10, B1...")
    void asientosSalaNormal() {
        Sala sala = DatosPrueba.crearSala("S1", 25);
        assertEquals(25, sala.getAsientos().size());
        assertEquals(10, sala.getAsientosPorFila());
        assertEquals("A1", sala.getAsientos().get(0).getCodigo());
        assertEquals("A10", sala.getAsientos().get(9).getCodigo());
        assertEquals("B1", sala.getAsientos().get(10).getCodigo());
        assertEquals("C5", sala.getAsientos().get(24).getCodigo());
    }

    @Test
    @DisplayName("Una sala de más de 260 asientos usa filas más anchas (máximo 26 filas, A a Z)")
    void asientosSalaGrande() {
        Sala sala = DatosPrueba.crearSala("S1", 1000);
        assertEquals(39, sala.getAsientosPorFila());   // 1000 / 26 redondeado hacia arriba
        assertEquals(1000, sala.getAsientos().size());
        char ultimaFila = sala.getAsientos().get(999).getFila();
        assertTrue(ultimaFila <= 'Z');
    }

    @Test
    @DisplayName("Capacidad inválida: la sala queda sin asientos (Cine la rechaza)")
    void capacidadInvalida() {
        assertTrue(DatosPrueba.crearSala("S1", 0).getAsientos().isEmpty());
        assertTrue(DatosPrueba.crearSala("S1", 1001).getAsientos().isEmpty());
    }

    @Test
    @DisplayName("setCapacidad regenera los asientos y tieneDistribucionActual lo confirma")
    void setCapacidadYDistribucion() {
        Sala sala = DatosPrueba.crearSala("S1", 10);
        assertTrue(sala.tieneDistribucionActual());
        sala.setCapacidad(300);
        assertEquals(300, sala.getAsientos().size());
        assertTrue(sala.tieneDistribucionActual());
        sala.reorganizarAsientos();
        assertTrue(sala.tieneDistribucionActual());
    }

    @Test
    @DisplayName("habilitar, deshabilitar, estaDisponible, setters y toString de la sala")
    void estadoYDatosSala() {
        Sala sala = DatosPrueba.crearSala("S1", 10);
        assertTrue(sala.estaDisponible());
        sala.deshabilitar();
        assertFalse(sala.estaDisponible());
        sala.habilitar();
        assertTrue(sala.estaDisponible());
        sala.setEstado(EstadoSala.FUERA_DE_SERVICIO);
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, sala.getEstado());
        sala.setCodigo("S2");
        sala.setNombre("Sala IMAX");
        assertEquals("Sala IMAX (S2)", sala.toString());
    }

    @Test
    @DisplayName("Asiento: código, ocupar, liberar y no ocupar dos veces")
    void asiento() throws Exception {
        Asiento asiento = new Asiento('B', 7);
        assertEquals("B7", asiento.getCodigo());
        assertEquals('B', asiento.getFila());
        assertEquals(7, asiento.getNumero());
        assertTrue(asiento.estaDisponible());
        assertEquals(EstadoAsiento.DISPONIBLE, asiento.getEstado());

        asiento.ocupar();
        assertFalse(asiento.estaDisponible());
        assertEquals(EstadoAsiento.OCUPADO, asiento.getEstado());
        assertThrows(AsientoOcupadoException.class, asiento::ocupar);

        asiento.liberar();
        assertTrue(asiento.estaDisponible());
        assertTrue(asiento.toString().contains("B7"));
    }
}
