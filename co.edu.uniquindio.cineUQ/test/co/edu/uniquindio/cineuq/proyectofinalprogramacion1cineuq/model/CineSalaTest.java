package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CineSalasTest {

    private Cine cine;

    @BeforeEach
    void preparar() {
        cine = DatosPrueba.crearCine();
    }

    private void programarFuncionEnS1() throws Exception {
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "TINTIN", Genero.COMEDIA, 90, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2001, EstadoPelicula.DISPONIBLE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
    }

    @Test
    void registrarSalaLaAgregaYGeneraSusAsientos() throws Exception {
        Sala sala = DatosPrueba.sala("S1", "SALA 8", 20, EstadoSala.DISPONIBLE);
        cine.registrarSala(sala);
        assertEquals(1, cine.consultarSalas().size());
        assertSame(sala, cine.buscarSala("S1"));
        assertEquals(20, cine.buscarSala("S1").getAsientos().size());
    }

    @Test
    void noPermiteDosSalasConElMismoCodigo() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        assertThrows(SalaDuplicadaException.class, () -> cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE)));
        assertEquals(1, cine.consultarSalas().size());
    }

    @Test
    void rechazaUnaSalaSinCodigoONombre() {
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarSala(new Sala(" ", "Sala 1", 20, EstadoSala.DISPONIBLE)));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarSala(new Sala("S1", "", 20, EstadoSala.DISPONIBLE)));
    }

    @Test
    void rechazaCapacidadCeroNegativaOMayorAlMaximo() throws Exception {
        assertThrows(DatoInvalidoException.class, () -> cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 0, EstadoSala.DISPONIBLE)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarSala(DatosPrueba.sala("S2", "SALA 8", -5, EstadoSala.DISPONIBLE)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarSala(DatosPrueba.sala("S3", "Sala 6" ,Sala.CAPACIDAD_MAXIMA + 1, EstadoSala.DISPONIBLE)));
        cine.registrarSala(DatosPrueba.sala("S4", "SALA 5", Sala.CAPACIDAD_MAXIMA, EstadoSala.DISPONIBLE ));   // el máximo sí se permite
        assertEquals(1, cine.consultarSalas().size());
    }

    @Test
    void buscarUnaSalaInexistenteLanzaExcepcion() {
        assertThrows(SalaNoEncontradaException.class, () -> cine.buscarSala("NO-EXISTE"));
    }

    @Test
    void deshabilitarYHabilitarUnaSala() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        cine.deshabilitarSala("S1");
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, cine.buscarSala("S1").getEstado());
        cine.habilitarSala("S1");
        assertEquals(EstadoSala.DISPONIBLE, cine.buscarSala("S1").getEstado());
    }

    @Test
    void deshabilitarUnaSalaInexistenteLanzaExcepcion() {
        assertThrows(SalaNoEncontradaException.class, () -> cine.deshabilitarSala("NO-EXISTE"));
        assertThrows(SalaNoEncontradaException.class, () -> cine.habilitarSala("NO-EXISTE"));
    }

    @Test
    void modificarSalaCambiaNombreYCapacidad() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        cine.modificarSala("S1", "Sala VIP", 35);
        Sala sala = cine.buscarSala("S1");
        assertEquals("Sala VIP", sala.getNombre());
        assertEquals(35, sala.getCapacidad());
        assertEquals(35, sala.getAsientos().size());
    }

    @Test
    void modificarSalaConDatosInvalidosLanzaExcepcion() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 20, EstadoSala.DISPONIBLE));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarSala("S1", "", 20));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarSala("S1", "X", 0));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarSala("S1", "X", 1001));
        assertEquals(20, cine.buscarSala("S1").getCapacidad());
    }

    @Test
    void modificarUnaSalaInexistenteLanzaExcepcion() {
        assertThrows(SalaNoEncontradaException.class, () -> cine.modificarSala("NO-EXISTE", "X", 20));
    }

    @Test
    void noSePuedeCambiarLaCapacidadSiHayFuncionesProgramadas() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 20, EstadoSala.DISPONIBLE));
        programarFuncionEnS1();
        assertThrows(DatoInvalidoException.class, () -> cine.modificarSala("S1", "Sala 1", 40));
        assertEquals(20, cine.buscarSala("S1").getCapacidad());
    }

    @Test
    void conFuncionesProgramadasSiSePuedeCambiarSoloElNombre() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 20, EstadoSala.DISPONIBLE));
        programarFuncionEnS1();
        cine.modificarSala("S1", "Sala Renovada", 20);
        assertEquals("Sala Renovada", cine.buscarSala("S1").getNombre());
    }

    @Test
    void eliminarUnaSalaSinFuncionesLaQuita() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        cine.eliminarSala("S1");
        assertEquals(0, cine.consultarSalas().size());
    }

    @Test
    void noSePuedeEliminarUnaSalaQueTuvoFunciones() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        programarFuncionEnS1();
        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarSala("S1"));
        assertEquals(1, cine.consultarSalas().size());
    }

    @Test
    void eliminarUnaSalaInexistenteLanzaExcepcion() {
        assertThrows(SalaNoEncontradaException.class, () -> cine.eliminarSala("NO-EXISTE"));
    }

    @Test
    void actualizarDistribucionNoCambiaNadaSiLasSalasYaEstanActualizadas() throws Exception {
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 8", 50, EstadoSala.DISPONIBLE));
        assertFalse(cine.actualizarDistribucionSalas());
    }
}

