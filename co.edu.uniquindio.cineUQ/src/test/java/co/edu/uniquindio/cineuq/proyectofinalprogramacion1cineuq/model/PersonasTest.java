package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Persona, Cliente, Empleado, Operador y Administrador")
class PersonasTest {

    @Test
    @DisplayName("Cliente sin plan queda en Free; con plan, el que se indique")
    void clientePlan() {
        Cliente sinPlan = new Cliente("1", "Ana", "3001234567", "ana@correo.com", TipoCliente.NINO);
        assertEquals(PlanMembresia.FREE, sinPlan.getPlan());
        Cliente conPlan = new Cliente("2", "Luis", "3001234567", "l@correo.com", TipoCliente.GENERAL,
                PlanMembresia.PREMIUM);
        assertEquals(PlanMembresia.PREMIUM, conPlan.getPlan());
    }

    @Test
    @DisplayName("Cliente: getters, setters, ventas y toString")
    void cliente() {
        Cliente cliente = new Cliente("1", "Ana", "3001234567", "ana@correo.com", TipoCliente.GENERAL);
        cliente.setIdentificacion("9");
        cliente.setNombre("Ana María");
        cliente.setTelefono("3109876543");
        cliente.setCorreo("am@correo.com");
        cliente.setTipoCliente(TipoCliente.ESTUDIANTE);
        cliente.setPlan(PlanMembresia.BASICO);
        assertEquals("9", cliente.getIdentificacion());
        assertEquals("Ana María", cliente.getNombre());
        assertEquals("3109876543", cliente.getTelefono());
        assertEquals("am@correo.com", cliente.getCorreo());
        assertEquals(TipoCliente.ESTUDIANTE, cliente.getTipoCliente());
        assertEquals(PlanMembresia.BASICO, cliente.getPlan());
        assertTrue(cliente.getListVentas().isEmpty());
        assertEquals("Ana María (9) - Plan Básico", cliente.toString());
    }

    @Test
    @DisplayName("Teléfono y correo nulos (datos viejos) se devuelven como texto vacío")
    void clienteDatosNulos() {
        Cliente cliente = new Cliente("1", "Ana", null, null, TipoCliente.GENERAL);
        assertEquals("", cliente.getTelefono());
        assertEquals("", cliente.getCorreo());
    }

    @Test
    @DisplayName("Cliente.agregarVenta y Operador.agregarVenta guardan la venta")
    void agregarVenta() throws Exception {
        Cine cine = DatosPrueba.crearCineConFuncion();
        Operador operador = new Operador("10", "Luis", "luis", "1");
        Venta venta = cine.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, operador);
        Cliente cliente = cine.buscarCliente("100");
        assertEquals(1, cliente.getListVentas().size());
        assertSame(venta, operador.getListVentas().get(0));
        cliente.agregarVenta(venta);
        assertEquals(2, cliente.getListVentas().size());
    }

    @Test
    @DisplayName("validarCredenciales compara usuario y contraseña exactos")
    void validarCredenciales() {
        Empleado empleado = new Operador("10", "Luis", "luis", "Clave1");
        assertTrue(empleado.validarCredenciales("luis", "Clave1"));
        assertFalse(empleado.validarCredenciales("luis", "clave1"));
        assertFalse(empleado.validarCredenciales("Luis", "Clave1"));
    }

    @Test
    @DisplayName("Empleado: setters y toString sin mostrar la contraseña")
    void empleado() {
        Administrador admin = new Administrador("1", "Sofía", "admin", "secreta");
        admin.setUsuario("sofia");
        admin.setContrasena("nueva");
        assertEquals("sofia", admin.getUsuario());
        assertEquals("nueva", admin.getContrasena());
        assertTrue(admin.toString().startsWith("Administrador{"));
        assertFalse(admin.toString().contains("nueva"));

        Operador operador = new Operador("10", "Luis", "luis", "secreta");
        assertTrue(operador.toString().startsWith("Operador{"));
        assertFalse(operador.toString().contains("secreta"));
        assertTrue(operador.getListVentas().isEmpty());
    }
}
