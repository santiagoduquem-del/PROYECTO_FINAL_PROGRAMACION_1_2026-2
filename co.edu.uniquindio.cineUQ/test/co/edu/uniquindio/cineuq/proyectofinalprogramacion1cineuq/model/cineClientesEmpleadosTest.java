package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CineClientesEmpleadosTest {

    private Cine cine;

    @BeforeEach
    void preparar() {
        cine = DatosPrueba.crearCine();
    }

    private Cliente clienteCon(String id, String nombre, String telefono, String correo) {
        return new Cliente(id, nombre, telefono, correo, TipoCliente.GENERAL, PlanMembresia.FREE);
    }

    // ---------------- clientes ----------------

    @Test
    void registrarClienteLoAgregaYSePuedeBuscar() throws Exception {
        Cliente cliente = DatosPrueba.cliente("1", "juan", "31105758", "hola@h.com", TipoCliente.ESTUDIANTE, PlanMembresia.BASICO);
        cine.registrarCliente(cliente);
        assertEquals(1, cine.consultarClientes().size());
        assertSame(cliente, cine.buscarCliente("1"));
    }

    @Test
    void noPermiteDosClientesConLaMismaIdentificacion() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("1", "juan", "31102345", "hola@h.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        assertThrows(ClienteDuplicadoException.class,
                () -> cine.registrarCliente(DatosPrueba.cliente("1", "juan", "31146890", "hola@h.com", TipoCliente.GENERAL, PlanMembresia.FREE)));
    }

    @Test
    void buscarUnClienteInexistenteLanzaExcepcion() {
        assertThrows(ClienteNoEncontradoException.class, () -> cine.buscarCliente("999"));
    }

    @Test
    void rechazaClientesConCamposObligatoriosVacios() {
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("", "Ana", "3001234567", "ana@correo.com")));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("1", " ", "3001234567", "ana@correo.com")));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("1", "Ana", "", "ana@correo.com")));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("1", "Ana", "3001234567", "")));
        assertEquals(0, cine.consultarClientes().size());
    }

    @Test
    void elTelefonoDebeTenerEntreSieteYDiezDigitos() throws Exception {
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("1", "Ana", "123456", "ana@correo.com")));      // 6
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("2", "Ana", "12345678901", "ana@correo.com"))); // 11
        cine.registrarCliente(clienteCon("3", "Ana", "1234567", "ana@correo.com"));                    // 7
        cine.registrarCliente(clienteCon("4", "Ana", "1234567890", "ana@correo.com"));                 // 10
        assertEquals(2, cine.consultarClientes().size());
    }

    @Test
    void elTelefonoSoloPuedeTenerNumeros() {
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("1", "Ana", "300123abcd", "ana@correo.com")));
        assertThrows(DatoInvalidoException.class,
                () -> cine.registrarCliente(clienteCon("2", "Ana", "300-123-45", "ana@correo.com")));
    }

    @Test
    void elCorreoDebeSerValido() throws Exception {
        String[] invalidos = {"sin-arroba", "@correo.com", "ana@correo", "ana@.com", "ana@correo.", "an a@correo.com"};
        for (String correo : invalidos) {
            assertThrows(DatoInvalidoException.class,
                    () -> cine.registrarCliente(clienteCon("1", "Ana", "3001234567", correo)),
                    "debía rechazar el correo: " + correo);
        }
        cine.registrarCliente(clienteCon("1", "Ana", "3001234567", "ana@correo.com"));
        assertEquals(1, cine.consultarClientes().size());
    }

    @Test
    void exigeTipoDeClienteYPlan() {
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCliente(
                new Cliente("1", "Ana", "3001234567", "ana@correo.com", null, PlanMembresia.FREE)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCliente(
                new Cliente("1", "Ana", "3001234567", "ana@correo.com", TipoCliente.GENERAL, null)));
    }

    @Test
    void modificarClienteCambiaSusDatosPeroNoLaIdentificacion() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("1", "juan", "3001234567", "hhola@h.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.modificarCliente("1", " Ana Nueva ", " 3109876543 ", " nueva@correo.com ",
                TipoCliente.ESTUDIANTE, PlanMembresia.PREMIUM);

        Cliente cliente = cine.buscarCliente("1");
        assertEquals("Ana Nueva", cliente.getNombre());
        assertEquals("3109876543", cliente.getTelefono());
        assertEquals("nueva@correo.com", cliente.getCorreo());
        assertEquals(TipoCliente.ESTUDIANTE, cliente.getTipoCliente());
        assertEquals(PlanMembresia.PREMIUM, cliente.getPlan());
        assertEquals("1", cliente.getIdentificacion());
    }

    @Test
    void siLosDatosNuevosSonInvalidosElClienteNoCambia() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("1", "Cliente 1", "3001234567", "hola@h.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCliente("1", "Nuevo", "abc",
                "x@correo.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCliente("1", "Nuevo", "3001234567",
                "x@correo.com", TipoCliente.GENERAL, null));
        assertEquals("Cliente 1", cine.buscarCliente("1").getNombre());
    }

    @Test
    void modificarUnClienteInexistenteLanzaExcepcion() {
        assertThrows(ClienteNoEncontradoException.class, () -> cine.modificarCliente("999", "Ana", "3001234567",
                "ana@correo.com", TipoCliente.GENERAL, PlanMembresia.FREE));
    }

    @Test
    void eliminarUnClienteSinComprasLoQuita() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("1", "juan", "3001234567", "hola@h.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.eliminarCliente("1");
        assertEquals(0, cine.consultarClientes().size());
    }

    @Test
    void noSePuedeEliminarUnClienteConCompras() throws Exception {
        cine.registrarCliente(DatosPrueba.cliente("1", "juan", "3001234567", "hola@h.com", TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "Signature", Genero.ACCION, 120, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2007, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "F1", 30, EstadoSala.DISPONIBLE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, null, null);

        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarCliente("1"));
        assertEquals(1, cine.consultarClientes().size());
    }

    @Test
    void eliminarUnClienteInexistenteLanzaExcepcion() {
        assertThrows(ClienteNoEncontradoException.class, () -> cine.eliminarCliente("999"));
    }

    // ---------------- empleados ----------------

    @Test
    void registrarEmpleadoLoAgregaYSePuedeBuscar() throws Exception {
        Operador operador = DatosPrueba.operador("1090", "Camilo", "hola@h.com", "camilocc");
        cine.registrarEmpleado(operador);
        assertEquals(1, cine.consultarEmpleados().size());
        assertSame(operador, cine.buscarEmpleado("1090"));
    }

    @Test
    void noPermiteUsuarioNiIdentificacionRepetidos() throws Exception {
        cine.registrarEmpleado(new Operador("1", "Ana", "ana", "clave"));
        assertThrows(UsuarioDuplicadoException.class,
                () -> cine.registrarEmpleado(new Operador("2", "Otra Ana", "ana", "clave")));
        assertThrows(UsuarioDuplicadoException.class,
                () -> cine.registrarEmpleado(new Administrador("1", "Luis", "luis", "clave")));
        assertEquals(1, cine.consultarEmpleados().size());
    }

    @Test
    void rechazaEmpleadosConCamposVacios() {
        assertThrows(DatoInvalidoException.class, () -> cine.registrarEmpleado(new Operador("", "Ana", "ana", "1")));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarEmpleado(new Operador("1", "", "ana", "1")));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarEmpleado(new Operador("1", "Ana", " ", "1")));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarEmpleado(new Operador("1", "Ana", "ana", "")));
    }

    @Test
    void autenticarDevuelveElEmpleadoConCredencialesCorrectas() throws Exception {
        Administrador admin = new Administrador("1", "Luis", "luis", "secreta");
        cine.registrarEmpleado(admin);
        assertSame(admin, cine.autenticarEmpleado("luis", "secreta"));
    }

    @Test
    void autenticarConCredencialesIncorrectasLanzaExcepcion() throws Exception {
        cine.registrarEmpleado(new Administrador("1", "Luis", "luis", "secreta"));
        assertThrows(CredencialesInvalidasException.class, () -> cine.autenticarEmpleado("luis", "mala"));
        assertThrows(CredencialesInvalidasException.class, () -> cine.autenticarEmpleado("nadie", "secreta"));
    }

    @Test
    void buscarUnEmpleadoInexistenteLanzaExcepcion() {
        assertThrows(EmpleadoNoEncontradoException.class, () -> cine.buscarEmpleado("999"));
    }

    @Test
    void modificarEmpleadoCambiaNombreYUsuario() throws Exception {
        cine.registrarEmpleado(new Operador("1", "Ana", "ana", "clave"));
        cine.modificarEmpleado("1", " Ana María ", " anita ", "nueva");
        Empleado empleado = cine.buscarEmpleado("1");
        assertEquals("Ana María", empleado.getNombre());
        assertEquals("anita", empleado.getUsuario());
        assertTrue(empleado.validarCredenciales("anita", "nueva"));
    }

    @Test
    void siLaContrasenaLlegaVaciaSeConservaLaAnterior() throws Exception {
        cine.registrarEmpleado(new Operador("1", "Ana", "ana", "clave"));
        cine.modificarEmpleado("1", "Ana", "ana", "");
        assertTrue(cine.buscarEmpleado("1").validarCredenciales("ana", "clave"));
        cine.modificarEmpleado("1", "Ana", "ana", null);
        assertTrue(cine.buscarEmpleado("1").validarCredenciales("ana", "clave"));
    }

    @Test
    void noSePuedeUsarElUsuarioDeOtroEmpleadoPeroSiElPropio() throws Exception {
        cine.registrarEmpleado(new Operador("1", "Ana", "ana", "clave"));
        cine.registrarEmpleado(new Operador("2", "Beto", "beto", "clave"));
        assertThrows(UsuarioDuplicadoException.class, () -> cine.modificarEmpleado("2", "Beto", "ana", ""));
        assertDoesNotThrow(() -> cine.modificarEmpleado("2", "Beto Nuevo", "beto", ""));
    }

    @Test
    void modificarEmpleadoInvalidoOInexistente() throws Exception {
        cine.registrarEmpleado(new Operador("1", "Ana", "ana", "clave"));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarEmpleado("1", "", "ana", ""));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarEmpleado("1", "Ana", " ", ""));
        assertThrows(EmpleadoNoEncontradoException.class, () -> cine.modificarEmpleado("999", "X", "x", ""));
    }

    @Test
    void eliminarUnOperadorSinVentasLoQuita() throws Exception {
        Administrador admin = new Administrador("A1", "Luis", "luis", "clave");
        cine.registrarEmpleado(admin);
        cine.registrarEmpleado(new Operador("1", "Ana", "ana", "clave"));
        cine.eliminarEmpleado("1", admin);
        assertEquals(1, cine.consultarEmpleados().size());
    }

    @Test
    void nadiePuedeEliminarSuPropiaCuenta() throws Exception {
        Administrador admin = new Administrador("A1", "Luis", "luis", "clave");
        cine.registrarEmpleado(admin);
        cine.registrarEmpleado(new Administrador("A2", "Otro", "otro", "clave"));
        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarEmpleado("A1", admin));
    }

    @Test
    void noSePuedeEliminarElUltimoAdministrador() throws Exception {
        Operador operador = new Operador("1", "Ana", "ana", "clave");
        cine.registrarEmpleado(new Administrador("A1", "Luis", "luis", "clave"));
        cine.registrarEmpleado(operador);
        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarEmpleado("A1", operador));
        assertEquals(2, cine.consultarEmpleados().size());
    }

    @Test
    void siHayOtroAdministradorSePuedeEliminarUno() throws Exception {
        Administrador a1 = new Administrador("A1", "Luis", "luis", "clave");
        Administrador a2 = new Administrador("A2", "Otro", "otro", "clave");
        cine.registrarEmpleado(a1);
        cine.registrarEmpleado(a2);
        cine.eliminarEmpleado("A2", a1);
        assertEquals(1, cine.consultarEmpleados().size());
    }

    @Test
    void noSePuedeEliminarUnOperadorConVentas() throws Exception {
        Administrador admin = new Administrador("A1", "Luis", "luis", "clave");
        Operador operador = new Operador("1", "Ana", "ana", "clave");
        cine.registrarEmpleado(admin);
        cine.registrarEmpleado(operador);
        cine.registrarCliente(DatosPrueba.cliente("9", "juan", "3001234567", "hola@h.com",TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.registrarPelicula(DatosPrueba.pelicula("P1", "PULP FICTION", Genero.ACCION, 120, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2007, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "SALA1", 50, EstadoSala.DISPONIBLE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        cine.venderEntrada("F1", "A1", "9", TipoEntrada.GENERAL, null, operador);

        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarEmpleado("1", admin));
    }

    @Test
    void eliminarUnEmpleadoInexistenteLanzaExcepcion() {
        assertThrows(EmpleadoNoEncontradoException.class, () -> cine.eliminarEmpleado("999", null));
    }
}
