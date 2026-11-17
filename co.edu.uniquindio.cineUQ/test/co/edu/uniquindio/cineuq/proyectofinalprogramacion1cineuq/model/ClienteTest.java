package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;


import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void elConstructorCortoLeDaElPlanFree() {
        Cliente cliente = new Cliente("1", "Ana", "3001234567", "ana@correo.com", TipoCliente.ESTUDIANTE);
        assertEquals(PlanMembresia.FREE, cliente.getPlan());
        assertEquals(TipoCliente.ESTUDIANTE, cliente.getTipoCliente());
    }

    @Test
    void telefonoYCorreoNulosDevuelvenTextoVacio() {
        Cliente cliente = new Cliente("1", "Ana", null, null, TipoCliente.GENERAL);
        assertEquals("", cliente.getTelefono());
        assertEquals("", cliente.getCorreo());
    }

    @Test
    void guardaSusVentas() throws Exception {
        Cliente cliente = DatosPrueba.cliente("77", "Marcela", "31771958", "marcela@gmail.com",
                TipoCliente.GENERAL, PlanMembresia.FREE);

        Pelicula pelicula = DatosPrueba.pelicula("P1", "Rambo", Genero.ACCION, 120,
                Clasificacion.TODOS, Arrays.asList(Formato.values()), 2007, EstadoPelicula.DISPONIBLE);

        Sala sala = DatosPrueba.sala("S1", "SALA1", 50, EstadoSala.DISPONIBLE);

        Funcion funcion = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);

        assertEquals(0, cliente.getListVentas().size());

        cliente.agregarVenta(DatosPrueba.venta("V1", cliente, funcion, funcion.buscarAsiento("A1")));

        assertEquals(1, cliente.getListVentas().size());
    }

    @Test
    void toStringMuestraNombreIdentificacionYPlan() {
        Cliente cliente = DatosPrueba.cliente("77","Cliente 77", "31771958", "marcela@gmail.com", TipoCliente.GENERAL, PlanMembresia.PREMIUM);
        assertEquals("Cliente 77 (77) - Plan Premium", cliente.toString());
    }
}
