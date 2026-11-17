package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Membresía y Tarifa")
class MembresiaYTarifaTest {

    @Test
    @DisplayName("Membresia(plan) copia los valores por defecto del plan")
    void membresiaPorDefecto() {
        Membresia estandar = new Membresia(PlanMembresia.ESTANDAR);
        assertEquals(PlanMembresia.ESTANDAR, estandar.getPlan());
        assertEquals(29900, estandar.getPrecioMensual());
        assertEquals(0.20, estandar.getDescuentoEntradas());
        assertEquals(0.15, estandar.getDescuentoCombos());
        assertEquals(PlanMembresia.ESTANDAR.getBeneficioExtra(), estandar.getBeneficioExtra());
        assertEquals("Estándar", estandar.toString());
    }

    @Test
    @DisplayName("Setters de la membresía")
    void settersMembresia() {
        Membresia membresia = new Membresia(PlanMembresia.FREE, 0, 0, 0, "Puntos");
        membresia.setPrecioMensual(1000);
        membresia.setDescuentoEntradas(0.05);
        membresia.setDescuentoCombos(0.02);
        membresia.setBeneficioExtra("Otro");
        assertEquals(1000, membresia.getPrecioMensual());
        assertEquals(0.05, membresia.getDescuentoEntradas());
        assertEquals(0.02, membresia.getDescuentoCombos());
        assertEquals("Otro", membresia.getBeneficioExtra());
    }

    @Test
    @DisplayName("Tarifa: valor, setter y toString")
    void tarifa() {
        Tarifa tarifa = new Tarifa(TipoEntrada.ESTUDIANTE, 12000);
        assertEquals(TipoEntrada.ESTUDIANTE, tarifa.getTipoEntrada());
        assertEquals(12000, tarifa.getValor());
        tarifa.setValor(13000);
        assertEquals(13000, tarifa.getValor());
        assertTrue(tarifa.toString().startsWith("Estudiante $"));
    }
}
