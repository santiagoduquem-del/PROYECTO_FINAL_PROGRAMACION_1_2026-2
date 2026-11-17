package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CineTarifasMembresiasCombosTest {

    private Cine cine;

    @BeforeEach
    void preparar() {
        cine = DatosPrueba.crearCine();
    }

    // ---------------- tarifas ----------------

    @Test
    void elCineEmpiezaConUnaTarifaPorCadaTipoDeEntrada() {
        assertEquals(4, cine.consultarTarifas().size());
        assertEquals(15000, cine.calcularValorEntrada(TipoEntrada.GENERAL), 0.001);
        assertEquals(12000, cine.calcularValorEntrada(TipoEntrada.ESTUDIANTE), 0.001);
        assertEquals(10000, cine.calcularValorEntrada(TipoEntrada.NINO), 0.001);
        assertEquals(11000, cine.calcularValorEntrada(TipoEntrada.ADULTO_MAYOR), 0.001);
    }

    @Test
    void modificarTarifaCambiaElValorDeEseTipo() throws Exception {
        cine.modificarTarifa(TipoEntrada.ESTUDIANTE, 13500);
        assertEquals(13500, cine.calcularValorEntrada(TipoEntrada.ESTUDIANTE), 0.001);
        assertEquals(13500, cine.buscarTarifa(TipoEntrada.ESTUDIANTE).getValor(), 0.001);
        assertEquals(15000, cine.calcularValorEntrada(TipoEntrada.GENERAL), 0.001);   // las demás no cambian
    }

    @Test
    void laTarifaDebeSerMayorQueCero() {
        assertThrows(DatoInvalidoException.class, () -> cine.modificarTarifa(TipoEntrada.GENERAL, 0));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarTarifa(TipoEntrada.GENERAL, -500));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarTarifa(null, 10000));
        assertEquals(15000, cine.calcularValorEntrada(TipoEntrada.GENERAL), 0.001);
    }

    @Test
    void elValorDeUnaEntradaAplicaValorBaseYDescuentoDelPlan() {
        // Estudiante 12.000, plan Premium (30 %): 12.000 x 0,70
        assertEquals(8400, cine.calcularValorEntrada(TipoEntrada.ESTUDIANTE, PlanMembresia.PREMIUM, 0), 0.001);
        // General 15.000 + valor base 3.000 = 18.000, plan Estándar (20 %): 18.000 x 0,80
        assertEquals(14400, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.ESTANDAR, 3000), 0.001);
        // Plan Free: sin descuento
        assertEquals(15000, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.FREE, 0), 0.001);
    }

    @Test
    void elValorSeRedondeaAPesos() throws Exception {
        cine.modificarTarifa(TipoEntrada.GENERAL, 14999);
        // 14.999 x 0,90 = 13.499,1 -> 13.499
        assertEquals(13499, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.BASICO, 0), 0.001);
    }

    // ---------------- membresías ----------------

    @Test
    void elCineEmpiezaConUnaMembresiaPorCadaPlan() {
        assertEquals(4, cine.consultarMembresias().size());
        Membresia premium = cine.buscarMembresia(PlanMembresia.PREMIUM);
        assertEquals(0.30, premium.getDescuentoEntradas(), 0.0001);
        assertEquals(0.30, premium.getDescuentoCombos(), 0.0001);
        assertEquals(49900, premium.getPrecioMensual(), 0.001);
        assertEquals(0, cine.buscarMembresia(PlanMembresia.FREE).getDescuentoEntradas(), 0.0001);
    }

    @Test
    void modificarMembresiaCambiaLosValoresYLosPrecios() throws Exception {
        cine.modificarMembresia(PlanMembresia.BASICO, 19900, 0.50, 0.25, "  Dos bebidas gratis  ");

        Membresia basico = cine.buscarMembresia(PlanMembresia.BASICO);
        assertEquals(19900, basico.getPrecioMensual(), 0.001);
        assertEquals(0.50, basico.getDescuentoEntradas(), 0.0001);
        assertEquals(0.25, basico.getDescuentoCombos(), 0.0001);
        assertEquals("Dos bebidas gratis", basico.getBeneficioExtra());
        assertEquals(7500, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.BASICO, 0), 0.001);
    }

    @Test
    void modificarMembresiaRechazaValoresInvalidos() {
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(null, 100, 0.1, 0.1, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.BASICO, -1, 0.1, 0.1, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.BASICO, 100, -0.1, 0.1, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.BASICO, 100, 1.1, 0.1, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.BASICO, 100, 0.1, 1.5, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.BASICO, 100, 0.1, 0.1, " "));
        assertEquals(0.10, cine.buscarMembresia(PlanMembresia.BASICO).getDescuentoEntradas(), 0.0001);
    }

    @Test
    void losLimitesDelDescuentoSePermiten() throws Exception {
        cine.modificarMembresia(PlanMembresia.BASICO, 0, 0, 1, "Sin costo");
        assertEquals(0, cine.buscarMembresia(PlanMembresia.BASICO).getPrecioMensual(), 0.001);
        assertEquals(1, cine.buscarMembresia(PlanMembresia.BASICO).getDescuentoCombos(), 0.0001);
    }

    // ---------------- combos ----------------

    @Test
    void elCineEmpiezaConSeisCombosDisponibles() {
        assertEquals(6, cine.consultarCombos().size());
        assertEquals(6, cine.consultarCombosDisponibles().size());
    }

    @Test
    void registrarComboLoAgrega() throws Exception {
        Combo combo = new Combo("C7", "Combo Dulce", "Dulces + gaseosa", 12000);
        cine.registrarCombo(combo);
        assertEquals(7, cine.consultarCombos().size());
        assertSame(combo, cine.buscarCombo("C7"));
    }

    @Test
    void noPermiteDosCombosConElMismoCodigoSinImportarMayusculas() throws Exception {
        cine.registrarCombo(new Combo("C7", "Combo Dulce", "Dulces", 12000));
        assertThrows(ComboDuplicadoException.class,
                () -> cine.registrarCombo(new Combo("c7", "Otro", "Otro", 5000)));
        assertThrows(ComboDuplicadoException.class,
                () -> cine.registrarCombo(new Combo("C1", "Otro", "Otro", 5000)));   // C1 ya existe
    }

    @Test
    void rechazaCombosConDatosInvalidos() {
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("", "X", "x", 1000)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C7", " ", "x", 1000)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C7", "X", "", 1000)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C7", "X", "x", 0)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C7", "X", "x", -1)));
        assertEquals(6, cine.consultarCombos().size());
    }

    @Test
    void buscarUnComboNoDistingueMayusculasYLanzaExcepcionSiNoExiste() throws Exception {
        assertSame(cine.buscarCombo("C1"), cine.buscarCombo("c1"));
        assertThrows(ComboNoEncontradoException.class, () -> cine.buscarCombo("NO-EXISTE"));
    }

    @Test
    void modificarComboCambiaNombreContenidoYPrecio() throws Exception {
        cine.modificarCombo("C1", " Combo Mega ", " Crispetas XL ", 20000);
        Combo combo = cine.buscarCombo("C1");
        assertEquals("Combo Mega", combo.getNombre());
        assertEquals("Crispetas XL", combo.getDescripcion());
        assertEquals(20000, combo.getPrecio(), 0.001);
    }

    @Test
    void modificarComboConDatosInvalidosNoCambiaNada() throws Exception {
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCombo("C1", "", "x", 1000));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCombo("C1", "X", "x", 0));
        assertThrows(ComboNoEncontradoException.class, () -> cine.modificarCombo("NO-EXISTE", "X", "x", 1000));
        assertEquals("Combo Personal", cine.buscarCombo("C1").getNombre());
    }

    @Test
    void deshabilitarUnComboLoSacaDeLosDisponibles() throws Exception {
        cine.deshabilitarCombo("C1");
        assertEquals(6, cine.consultarCombos().size());
        assertEquals(5, cine.consultarCombosDisponibles().size());
        cine.habilitarCombo("C1");
        assertEquals(6, cine.consultarCombosDisponibles().size());
    }

    @Test
    void deshabilitarUnComboInexistenteLanzaExcepcion() {
        assertThrows(ComboNoEncontradoException.class, () -> cine.deshabilitarCombo("NO-EXISTE"));
        assertThrows(ComboNoEncontradoException.class, () -> cine.habilitarCombo("NO-EXISTE"));
    }

    @Test
    void asignarImagenAlCombo() throws Exception {
        cine.asignarImagenCombo("C1", "combo1.png");
        assertEquals("combo1.png", cine.buscarCombo("C1").getRutaImagen());
    }

    @Test
    void calculaElValorDeLosCombosSinYConDescuento() throws Exception {
        List<Combo> combos = new ArrayList<>();
        combos.add(cine.buscarCombo("C1"));   // 18.000
        combos.add(cine.buscarCombo("C2"));   // 32.000
        assertEquals(50000, cine.calcularValorCombosSinDescuento(combos), 0.001);
        assertEquals(50000, cine.calcularValorCombos(combos, PlanMembresia.FREE), 0.001);
        assertEquals(42500, cine.calcularValorCombos(combos, PlanMembresia.ESTANDAR), 0.001);   // 15 %
        assertEquals(0, cine.calcularValorCombos(new ArrayList<Combo>(), PlanMembresia.PREMIUM), 0.001);
    }

    @Test
    void unComboSoloSeEliminaSiNuncaSeHaVendido() throws Exception {
        cine.eliminarCombo("C6");
        assertEquals(5, cine.consultarCombos().size());
        assertThrows(ComboNoEncontradoException.class, () -> cine.eliminarCombo("C6"));

        cine.registrarPelicula(DatosPrueba.pelicula("P1", "TINTIN", Genero.COMEDIA, 90, Clasificacion.TODOS, Arrays.asList(Formato.values()), 2001, EstadoPelicula.DISPONIBLE));
        cine.registrarSala(DatosPrueba.sala("S1", "SALA 1", 50, EstadoSala.DISPONIBLE));
        cine.registrarCliente(DatosPrueba.cliente("1", "Juanma", "32024203", "juanma@checho.com" ,TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.programarFuncion("F1", "P1", "S1", DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        List<Combo> combos = new ArrayList<>();
        combos.add(cine.buscarCombo("C1"));
        cine.venderEntrada("F1", "A1", "1", TipoEntrada.GENERAL, combos, null);

        assertTrue(cine.comboFueVendido(cine.buscarCombo("C1")));
        assertFalse(cine.comboFueVendido(cine.buscarCombo("C2")));
        assertThrows(EliminacionNoPermitidaException.class, () -> cine.eliminarCombo("C1"));
        cine.eliminarCombo("C2");   // este nunca se vendió
        assertEquals(4, cine.consultarCombos().size());   // 6 iniciales - C6 - C2
    }
}
