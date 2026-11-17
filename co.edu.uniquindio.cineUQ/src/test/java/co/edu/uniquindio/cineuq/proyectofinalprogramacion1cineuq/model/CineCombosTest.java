package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: gestión de combos")
class CineCombosTest {

    private Cine cine;

    @BeforeEach
    void setUp() {
        cine = DatosPrueba.crearCine();
    }

    @Test
    @DisplayName("El cine empieza con 7 combos; C7 es exclusivo de VIP Bistró")
    void combosPorDefecto() throws Exception {
        assertEquals(7, cine.consultarCombos().size());
        assertTrue(cine.buscarCombo("C7").isExclusivoVipBistro());
        assertFalse(cine.buscarCombo("C1").isExclusivoVipBistro());
    }

    @Test
    @DisplayName("registrarCombo agrega el combo; el código no se repite (sin importar mayúsculas)")
    void registrarCombo() throws Exception {
        cine.registrarCombo(new Combo("C8", "Mega", "Crispetas XL", 40000));
        assertEquals(8, cine.consultarCombos().size());
        assertThrows(ComboDuplicadoException.class,
                () -> cine.registrarCombo(new Combo("c8", "Otro", "x", 1000)));
    }

    @Test
    @DisplayName("registrarCombo valida código, nombre, contenido y precio")
    void registrarComboInvalido() {
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("", "A", "B", 100)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C9", " ", "B", 100)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C9", "A", "", 100)));
        assertThrows(DatoInvalidoException.class, () -> cine.registrarCombo(new Combo("C9", "A", "B", 0)));
    }

    @Test
    @DisplayName("buscarCombo encuentra el combo (sin importar mayúsculas) o lanza excepción")
    void buscarCombo() throws Exception {
        assertEquals("Combo Personal", cine.buscarCombo("c1").getNombre());
        assertThrows(ComboNoEncontradoException.class, () -> cine.buscarCombo("C99"));
    }

    @Test
    @DisplayName("consultarCombosDisponibles no incluye los deshabilitados")
    void combosDisponibles() throws Exception {
        cine.deshabilitarCombo("C1");
        assertEquals(6, cine.consultarCombosDisponibles().size());
        assertFalse(cine.consultarCombosDisponibles().contains(cine.buscarCombo("C1")));
        cine.habilitarCombo("C1");
        assertEquals(7, cine.consultarCombosDisponibles().size());
    }

    @Test
    @DisplayName("consultarCombosParaFuncion oculta los exclusivos de VIP fuera de VIP Bistró")
    void combosParaFuncion() throws Exception {
        Combo exclusivo = cine.buscarCombo("C7");
        assertFalse(cine.consultarCombosParaFuncion(null).contains(exclusivo));

        Cine conFuncion = DatosPrueba.crearCineConFuncion();   // F1 en IMAX
        assertFalse(conFuncion.consultarCombosParaFuncion(conFuncion.buscarFuncion("F1"))
                .contains(conFuncion.buscarCombo("C7")));

        conFuncion.registrarPelicula(new Pelicula("P2", "VIP", Genero.DRAMA, 90, Clasificacion.TODOS,
                List.of(Formato.VIP_BISTRO), 2020, EstadoPelicula.DISPONIBLE));
        conFuncion.registrarSala(DatosPrueba.crearSala("S2", 10));
        Funcion vip = conFuncion.programarFuncion("F2", "P2", "S2", DatosPrueba.MANANA, LocalTime.of(10, 0),
                0, Formato.VIP_BISTRO, Idioma.DOBLADA);
        assertEquals(7, conFuncion.consultarCombosParaFuncion(vip).size());
    }

    @Test
    @DisplayName("modificarCombo cambia los datos")
    void modificarCombo() throws Exception {
        cine.modificarCombo("C1", " Personal XL ", " Crispetas grandes ", 20000, true);
        Combo combo = cine.buscarCombo("C1");
        assertEquals("Personal XL", combo.getNombre());
        assertEquals("Crispetas grandes", combo.getDescripcion());
        assertEquals(20000, combo.getPrecio());
        assertTrue(combo.isExclusivoVipBistro());
        assertThrows(DatoInvalidoException.class, () -> cine.modificarCombo("C1", "X", "Y", -1, false));
        assertThrows(ComboNoEncontradoException.class, () -> cine.modificarCombo("C99", "X", "Y", 1, false));
    }

    @Test
    @DisplayName("asignarImagenCombo guarda la ruta de la foto")
    void asignarImagenCombo() throws Exception {
        cine.asignarImagenCombo("C1", "file:/combo.jpg");
        assertEquals("file:/combo.jpg", cine.buscarCombo("C1").getRutaImagen());
    }

    @Test
    @DisplayName("eliminarCombo borra un combo nunca vendido")
    void eliminarCombo() throws Exception {
        cine.eliminarCombo("C1");
        assertThrows(ComboNoEncontradoException.class, () -> cine.buscarCombo("C1"));
    }

    @Test
    @DisplayName("comboFueVendido y eliminarCombo de un combo vendido")
    void eliminarComboVendido() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        Combo combo = conFuncion.buscarCombo("C1");
        assertFalse(conFuncion.comboFueVendido(combo));
        conFuncion.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, List.of(combo), null);
        assertTrue(conFuncion.comboFueVendido(combo));
        assertThrows(EliminacionNoPermitidaException.class, () -> conFuncion.eliminarCombo("C1"));
    }

    @Test
    @DisplayName("actualizarCombosAnteriores no cambia nada cuando no hay combos de la versión vieja")
    void actualizarCombosAnteriores() throws Exception {
        Cine conFuncion = DatosPrueba.crearCineConFuncion();
        conFuncion.venderEntrada("F1", "A1", "100", TipoEntrada.GENERAL, List.of(conFuncion.buscarCombo("C1")), null);
        assertFalse(conFuncion.actualizarCombosAnteriores());
    }
}
