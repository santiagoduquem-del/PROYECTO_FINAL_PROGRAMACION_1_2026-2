package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EmpleadoTest {

    private Operador operador;
    private Administrador admin;

    @BeforeEach
    void setUp() {
        operador = new Operador("1", "Ana", "ana", "1234");
        admin = new Administrador("2", "Luis", "luis", "secreta");
    }

    // ---------- Credenciales ----------

    @Test
    void validaUsuarioYContrasenaCorrectos() {
        assertTrue(operador.validarCredenciales("ana", "1234"));
    }

    @Test
    void rechazaUsuarioOContrasenaIncorrectos() {
        assertFalse(admin.validarCredenciales("luis", "otra"));
        assertFalse(admin.validarCredenciales("otro", "secreta"));
        assertFalse(admin.validarCredenciales("LUIS", "secreta")); // distingue mayúsculas
    }

    @Test
    void cambiarLaContrasenaCambiaLaValidacion() {
        operador.setContrasena("nueva");
        assertFalse(operador.validarCredenciales("ana", "1234"));
        assertTrue(operador.validarCredenciales("ana", "nueva"));
    }

    @Test
    void elToStringNoMuestraLaContrasena() {
        assertFalse(operador.toString().contains("1234"));
        assertTrue(operador.toString().contains("ana"));
    }

    // ---------- Ventas del operador ----------

    Cliente cliente = DatosPrueba.cliente("1", "Marcela", "31771958", "marcela@gmail.com",
            TipoCliente.GENERAL, PlanMembresia.FREE);

    @Test
    void elOperadorGuardaSusVentas() throws Exception {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "Goku");
        Sala sala = DatosPrueba.sala("S1", "sala1", 50, EstadoSala.DISPONIBLE);
        Funcion funcion = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        Asiento asiento = funcion.buscarAsiento("A1");
        Venta venta = DatosPrueba.venta("V1", cliente, funcion, funcion.buscarAsiento("A1"));

        assertEquals(0, operador.getListVentas().size());
        operador.agregarVenta(venta);
        assertEquals(1, operador.getListVentas().size());
        assertSame(venta, operador.getListVentas().get(0));
    }


    // ---------- Jerarquía de clases ----------

    @Test
    void operadorYAdministradorSonEmpleadosYPersonas() {
        assertTrue(operador instanceof Empleado);
        assertTrue(admin instanceof Empleado);
        assertEquals("Ana", operador.getNombre());
    }
}

