package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: gestión de salas")
class CineSalasTest {

    private Cine cine;

    @BeforeEach
    void setUp() {
        cine = DatosPrueba.crearCine();
    }

    @Test
    @DisplayName("registrarSala agrega la sala con sus asientos")
    void registrarSala() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S1", 30));
        assertEquals(1, cine.consultarSalas().size());
        assertEquals(30, cine.buscarSala("S1").getAsientos().size());
    }

    @Test
    @DisplayName("registrarSala rechaza código repetido")
    void registrarSalaDuplicada() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S1", 30));
        assertThrows(SalaDuplicadaException.class, () -> cine.registrarSala(DatosPrueba.crearSala("S1", 10)));
    }

    @Test
    @DisplayName("registrarSala exige capacidad entre 1 y 1000 y código y nombre")
    void registrarSalaInvalida() {
        assertThrows(DatoInvalidoException.class, () -> cine.registrarSala(DatosPrueba.crearSala("S1", 0)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarSala(DatosPrueba.crearSala("S2", 1001)));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarSala(new Sala("", "Sala", 10, EstadoSala.DISPONIBLE)));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarSala(new Sala("S3", " ", 10, EstadoSala.DISPONIBLE)));
        assertDoesNotThrow(() -> cine.registrarSala(DatosPrueba.crearSala("S4", 1000)));
    }

    @Test
    @DisplayName("buscarSala encuentra la sala o lanza excepción")
    void buscarSala() throws Exception {
        Sala sala = DatosPrueba.crearSala("S1", 10);
        cine.registrarSala(sala);
        assertSame(sala, cine.buscarSala("S1"));
        assertThrows(SalaNoEncontradaException.class, () -> cine.buscarSala("NO"));
    }

    @Test
    @DisplayName("modificarSala cambia nombre y capacidad (y regenera los asientos)")
    void modificarSala() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S1", 10));
        cine.modificarSala("S1", "Sala grande", 40);
        Sala sala = cine.buscarSala("S1");
        assertEquals("Sala grande", sala.getNombre());
        assertEquals(40, sala.getCapacidad());
        assertEquals(40, sala.getAsientos().size());
    }

    @Test
    @DisplayName("modificarSala valida los datos")
    void modificarSalaInvalida() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S1", 10));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarSala("S1", "", 10));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarSala("S1", "X", 0));
        assertThrows(SalaNoEncontradaException.class, () -> cine.modificarSala("NO", "X", 10));
    }

    @Test
    @DisplayName("modificarSala no cambia la capacidad si hay funciones programadas, pero sí el nombre")
    void modificarSalaConFunciones() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        assertThrows(DatoInvalidoException.class, () -> conFuncion.modificarSala("S1", "X", 50));
        conFuncion.modificarSala("S1", "Nombre nuevo", 20);   // misma capacidad: se permite
        assertEquals("Nombre nuevo", conFuncion.buscarSala("S1").getNombre());
    }

    @Test
    @DisplayName("eliminarSala borra una sala sin funciones")
    void eliminarSala() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S1", 10));
        cine.eliminarSala("S1");
        assertTrue(cine.consultarSalas().isEmpty());
    }

    @Test
    @DisplayName("eliminarSala no borra una sala con funciones")
    void eliminarSalaConFunciones() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        assertThrows(EliminacionNoPermitidaException.class, () -> conFuncion.eliminarSala("S1"));
    }

    @Test
    @DisplayName("habilitarSala y deshabilitarSala cambian el estado")
    void habilitarYDeshabilitarSala() throws Exception {
        cine.registrarSala(DatosPrueba.crearSala("S1", 10));
        cine.deshabilitarSala("S1");
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, cine.buscarSala("S1").getEstado());
        cine.habilitarSala("S1");
        assertEquals(EstadoSala.DISPONIBLE, cine.buscarSala("S1").getEstado());
    }

    @Test
    @DisplayName("actualizarDistribucionSalas no cambia nada si las salas ya están al día")
    void actualizarDistribucionSalasSinCambios() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        assertFalse(conFuncion.actualizarDistribucionSalas());
    }
}
