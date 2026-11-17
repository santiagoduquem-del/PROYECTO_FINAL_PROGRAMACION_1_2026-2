package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cine: tarifas y membresías")
class CineTarifasTest {

    private Cine cine;

    @BeforeEach
    void setUp() {
        cine = DatosPrueba.crearCine();
    }

    @Test
    @DisplayName("El cine empieza con una tarifa por cada tipo de entrada, con su valor por defecto")
    void tarifasPorDefecto() {
        assertEquals(TipoEntrada.values().length, cine.consultarTarifas().size());
        for (TipoEntrada tipo : TipoEntrada.values()) {
            assertEquals(tipo.getValorPorDefecto(), cine.buscarTarifa(tipo).getValor());
        }
    }

    @Test
    @DisplayName("modificarTarifa cambia el valor y valida los datos")
    void modificarTarifa() throws Exception {
        cine.modificarTarifa(TipoEntrada.GENERAL, 18000);
        assertEquals(18000, cine.calcularValorEntrada(TipoEntrada.GENERAL));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarTarifa(TipoEntrada.GENERAL, 0));
        assertThrows(DatoInvalidoException.class, () -> cine.modificarTarifa(null, 1000));
    }

    @Test
    @DisplayName("calcularValorEntrada suma el valor base y resta el descuento del plan")
    void calcularValorEntradaConPlan() {
        assertEquals(15000, cine.calcularValorEntrada(TipoEntrada.GENERAL));
        assertEquals(15000, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.FREE, 0));
        assertEquals(18000, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.FREE, 3000));
        // (10.000 + 2.000) - 10 % = 10.800
        assertEquals(10800, cine.calcularValorEntrada(TipoEntrada.NINO, PlanMembresia.BASICO, 2000));
        // 15.000 - 30 % = 10.500
        assertEquals(10500, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.PREMIUM, 0));
    }

    @Test
    @DisplayName("El cine empieza con una membresía por plan, con los valores del enum")
    void membresiasPorDefecto() {
        assertEquals(PlanMembresia.values().length, cine.consultarMembresias().size());
        Membresia premium = cine.buscarMembresia(PlanMembresia.PREMIUM);
        assertEquals(49900, premium.getPrecioMensual());
        assertEquals(0.30, premium.getDescuentoEntradas());
        assertEquals(0.30, premium.getDescuentoCombos());
    }

    @Test
    @DisplayName("modificarMembresia cambia los valores del plan")
    void modificarMembresia() throws Exception {
        cine.modificarMembresia(PlanMembresia.BASICO, 19900, 0.15, 0.10, " Una bebida gratis ");
        Membresia basico = cine.buscarMembresia(PlanMembresia.BASICO);
        assertEquals(19900, basico.getPrecioMensual());
        assertEquals(0.15, basico.getDescuentoEntradas());
        assertEquals(0.10, basico.getDescuentoCombos());
        assertEquals("Una bebida gratis", basico.getBeneficioExtra());
        // El nuevo descuento ya se usa en el precio: 15.000 - 15 % = 12.750
        assertEquals(12750, cine.calcularValorEntrada(TipoEntrada.GENERAL, PlanMembresia.BASICO, 0));
    }

    @Test
    @DisplayName("modificarMembresia valida plan, precio, descuentos (0 a 1) y beneficio")
    void modificarMembresiaInvalida() {
        assertThrows(DatoInvalidoException.class, () -> cine.modificarMembresia(null, 0, 0, 0, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.FREE, -1, 0, 0, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.FREE, 0, 1.5, 0, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.FREE, 0, 0, -0.1, "x"));
        assertThrows(DatoInvalidoException.class,
                () -> cine.modificarMembresia(PlanMembresia.FREE, 0, 0, 0, " "));
    }
}
