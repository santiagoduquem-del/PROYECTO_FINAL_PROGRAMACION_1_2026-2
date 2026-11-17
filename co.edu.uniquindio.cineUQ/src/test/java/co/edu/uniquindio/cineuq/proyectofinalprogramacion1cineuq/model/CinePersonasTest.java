package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: clientes y empleados")
class CinePersonasTest {

    private Cine cine;

    @BeforeEach
    void setUp() {
        cine = DatosPrueba.crearCine();
    }

    private Cliente cliente(String id, String telefono, String correo) {
        return new Cliente(id, "Ana", telefono, correo, TipoCliente.GENERAL, PlanMembresia.FREE);
    }

    // ---------------- Clientes ----------------

    @Test
    @DisplayName("registrarCliente agrega el cliente")
    void registrarCliente() throws Exception {
        cine.registrarCliente(cliente("1", "3001234567", "ana@correo.com"));
        assertEquals(1, cine.consultarClientes().size());
    }

    @Test
    @DisplayName("registrarCliente rechaza identificación repetida")
    void registrarClienteDuplicado() throws Exception {
        cine.registrarCliente(cliente("1", "3001234567", "ana@correo.com"));
        assertThrows(ClienteDuplicadoException.class,
                () -> cine.registrarCliente(cliente("1", "3009999999", "otra@correo.com")));
    }

    @Test
    @DisplayName("registrarCliente exige plan de membresía")
    void registrarClienteSinPlan() {
        Cliente sinPlan = new Cliente("1", "Ana", "3001234567", "ana@correo.com", TipoCliente.GENERAL, null);
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCliente(sinPlan));
    }

    @Test
    @DisplayName("validarDatosCliente acepta datos correctos")
    void validarDatosClienteCorrectos() {
        assertDoesNotThrow(() -> cine.validarDatosCliente(cliente("1", "7350000", "a@b.co")));
        assertDoesNotThrow(() -> cine.validarDatosCliente(cliente("1", "3001234567", "ana.gomez@uq.edu.co")));
    }

    @Test
    @DisplayName("validarDatosCliente revisa campos vacíos y el tipo de cliente")
    void validarDatosClienteVacios() {
        assertThrows(DatoInvalidoException.class, () -> cine.validarDatosCliente(cliente("", "3001234567", "a@b.co")));
        assertThrows(DatoInvalidoException.class, () -> cine.validarDatosCliente(
                new Cliente("1", " ", "3001234567", "a@b.co", TipoCliente.GENERAL)));
        assertThrows(DatoInvalidoException.class, () -> cine.validarDatosCliente(
                new Cliente("1", "Ana", "3001234567", "a@b.co", null)));
    }

    @Test
    @DisplayName("validarDatosCliente revisa el teléfono: solo números, de 7 a 10 dígitos")
    void validarTelefono() {
        assertThrows(DatoInvalidoException.class, () -> cine.validarDatosCliente(cliente("1", "123456", "a@b.co")));
        assertThrows(DatoInvalidoException.class, () -> cine.validarDatosCliente(cliente("1", "12345678901", "a@b.co")));
        assertThrows(DatoInvalidoException.class, () -> cine.validarDatosCliente(cliente("1", "300-123456", "a@b.co")));
    }

    @Test
    @DisplayName("validarDatosCliente revisa el formato del correo")
    void validarCorreo() {
        String[] malos = {"anacorreo.com", "@correo.com", "ana@correo", "ana@.com", "ana@correo.", "ana @correo.com"};
        for (String correo : malos) {
            assertThrows(DatoInvalidoException.class,
                    () -> cine.validarDatosCliente(cliente("1", "3001234567", correo)), correo);
        }
    }

    @Test
    @DisplayName("buscarCliente encuentra el cliente o lanza excepción")
    void buscarCliente() throws Exception {
        Cliente cliente = cliente("1", "3001234567", "ana@correo.com");
        cine.registrarCliente(cliente);
        assertSame(cliente, cine.buscarCliente("1"));
        assertThrows(ClienteNoEncontradoException.class, () -> cine.buscarCliente("2"));
    }

    @Test
    @DisplayName("modificarCliente cambia los datos (sin espacios sobrantes)")
    void modificarCliente() throws Exception {
        cine.registrarCliente(cliente("1", "3001234567", "ana@correo.com"));
        cine.modificarCliente("1", " Ana María ", "3007654321", " nueva@correo.com ",
                TipoCliente.ESTUDIANTE, PlanMembresia.PREMIUM);
        Cliente cliente = cine.buscarCliente("1");
        assertEquals("Ana María", cliente.getNombre());
        assertEquals("3007654321", cliente.getTelefono());
        assertEquals("nueva@correo.com", cliente.getCorreo());
        assertEquals(TipoCliente.ESTUDIANTE, cliente.getTipoCliente());
        assertEquals(PlanMembresia.PREMIUM, cliente.getPlan());
    }

    @Test
    @DisplayName("modificarCliente no cambia nada si un dato es inválido")
    void modificarClienteInvalido() throws Exception {
        cine.registrarCliente(cliente("1", "3001234567", "ana@correo.com"));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCliente("1", "Otra", "12",
                "ana@correo.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCliente("1", "Otra", "3001234567",
                "ana@correo.com", TipoCliente.GENERAL, null));
        assertEquals("Ana", cine.buscarCliente("1").getNombre());
        assertThrows(ClienteNoEncontradoException.class, () -> cine.modificarCliente("9", "X", "3001234567",
                "a@b.co", TipoCliente.GENERAL, PlanMembresia.FREE));
    }

    @Test
    @DisplayName("eliminarCliente borra un cliente sin compras")
    void eliminarCliente() throws Exception {
        cine.registrarCliente(cliente("1", "3001234567", "ana@correo.com"));
        cine.eliminarCliente("1");
        assertTrue(cine.consultarClientes().isEmpty());
    }

    @Test
    @DisplayName("eliminarCliente no borra un cliente con compras")
    void eliminarClienteConCompras() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        conFuncion.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, null);
        assertThrows(EliminacionNoPermitidaException.class, () -> conFuncion.eliminarCliente("100"));
    }

    // ---------------- Empleados ----------------

    @Test
    @DisplayName("registrarEmpleado agrega operadores y administradores")
    void registrarEmpleado() throws Exception {
        cine.registrarEmpleado(new Operador("10", "Luis", "luis", "123"));
        cine.registrarEmpleado(new Administrador("20", "Sofía", "admin", "abc"));
        assertEquals(2, cine.consultarEmpleados().size());
    }

    @Test
    @DisplayName("registrarEmpleado rechaza usuario o identificación repetidos y campos vacíos")
    void registrarEmpleadoInvalido() throws Exception {
        cine.registrarEmpleado(new Operador("10", "Luis", "luis", "123"));
        assertThrows(UsuarioDuplicadoException.class,
                () -> cine.registrarEmpleado(new Operador("11", "Otro", "luis", "x")));
        assertThrows(UsuarioDuplicadoException.class,
                () -> cine.registrarEmpleado(new Operador("10", "Otro", "otro", "x")));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarEmpleado(new Operador("12", "Ana", "ana", "")));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarEmpleado(new Operador("13", "Ana", " ", "x")));
    }

    @Test
    @DisplayName("autenticarEmpleado devuelve el empleado con usuario y contraseña correctos")
    void autenticarEmpleado() throws Exception {
        Operador operador = new Operador("10", "Luis", "luis", "123");
        cine.registrarEmpleado(operador);
        assertSame(operador, cine.autenticarEmpleado("luis", "123"));
        assertThrows(CredencialesInvalidasException.class, () -> cine.autenticarEmpleado("luis", "mala"));
        assertThrows(CredencialesInvalidasException.class, () -> cine.autenticarEmpleado("nadie", "123"));
    }

    @Test
    @DisplayName("buscarEmpleado encuentra el empleado o lanza excepción")
    void buscarEmpleado() throws Exception {
        cine.registrarEmpleado(new Operador("10", "Luis", "luis", "123"));
        assertEquals("Luis", cine.buscarEmpleado("10").getNombre());
        assertThrows(EmpleadoNoEncontradoException.class, () -> cine.buscarEmpleado("99"));
    }

    @Test
    @DisplayName("modificarEmpleado cambia los datos; contraseña vacía conserva la anterior")
    void modificarEmpleado() throws Exception {
        cine.registrarEmpleado(new Operador("10", "Luis", "luis", "123"));
        cine.modificarEmpleado("10", " Luis Pérez ", " lperez ", "");
        Empleado empleado = cine.buscarEmpleado("10");
        assertEquals("Luis Pérez", empleado.getNombre());
        assertEquals("lperez", empleado.getUsuario());
        assertEquals("123", empleado.getContrasena());
        cine.modificarEmpleado("10", "Luis", "lperez", "nueva");
        assertEquals("nueva", cine.buscarEmpleado("10").getContrasena());
    }

    @Test
    @DisplayName("modificarEmpleado no deja usar el usuario de otro empleado")
    void modificarEmpleadoUsuarioRepetido() throws Exception {
        cine.registrarEmpleado(new Operador("10", "Luis", "luis", "123"));
        cine.registrarEmpleado(new Operador("11", "Ana", "ana", "123"));
        assertThrows(UsuarioDuplicadoException.class, () -> cine.modificarEmpleado("11", "Ana", "luis", ""));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarEmpleado("11", "", "ana", ""));
    }

    @Test
    @DisplayName("eliminarEmpleado borra un operador sin ventas")
    void eliminarEmpleado() throws Exception {
        Administrador admin = new Administrador("1", "Admin", "admin", "a");
        cine.registrarEmpleado(admin);
        cine.registrarEmpleado(new Operador("10", "Luis", "luis", "123"));
        cine.eliminarEmpleado("10", admin);
        assertEquals(1, cine.consultarEmpleados().size());
    }

    @Test
    @DisplayName("eliminarEmpleado: no a sí mismo, no al último administrador, no a un operador con ventas")
    void eliminarEmpleadoNoPermitido() throws Exception {
        Administrador admin = new Administrador("1", "Admin", "admin", "a");
        Operador operador = new Operador("10", "Luis", "luis", "123");
        cine.registrarEmpleado(admin);
        cine.registrarEmpleado(operador);

        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarEmpleado("1", admin));
        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarEmpleado("1", operador));

        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        conFuncion.registrarEmpleado(admin);
        conFuncion.registrarEmpleado(operador);
        conFuncion.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, null, operador);
        assertThrows(EliminacionNoPermitidaException.class, () -> conFuncion.eliminarEmpleado("10", admin));
    }

    @Test
    @DisplayName("Con dos administradores sí se puede eliminar uno")
    void eliminarAdministradorConOtro() throws Exception {
        Administrador admin1 = new Administrador("1", "Admin 1", "admin1", "a");
        cine.registrarEmpleado(admin1);
        cine.registrarEmpleado(new Administrador("2", "Admin 2", "admin2", "a"));
        cine.eliminarEmpleado("2", admin1);
        assertEquals(1, cine.consultarEmpleados().size());
    }
}
