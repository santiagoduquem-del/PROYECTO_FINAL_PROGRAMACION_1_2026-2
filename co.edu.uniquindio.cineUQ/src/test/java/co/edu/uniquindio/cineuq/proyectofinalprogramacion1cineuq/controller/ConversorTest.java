package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Conversor: texto de los campos a números y horas")
class ConversorTest {

    @Test
    @DisplayName("aEntero convierte números y quita espacios")
    void aEntero() throws Exception {
        assertEquals(120, Conversor.aEntero(" 120 ", "duración"));
        assertThrows(DatoInvalidoException.class, () -> Conversor.aEntero("12a", "duración"));
        assertThrows(DatoInvalidoException.class, () -> Conversor.aEntero("", "duración"));
    }

    @Test
    @DisplayName("aDecimal acepta puntos de miles y coma decimal")
    void aDecimal() throws Exception {
        assertEquals(15000, Conversor.aDecimal("15.000", "precio"));
        assertEquals(15000, Conversor.aDecimal("15000", "precio"));
        assertEquals(2.5, Conversor.aDecimal("2,5", "precio"));
        assertThrows(DatoInvalidoException.class, () -> Conversor.aDecimal("abc", "precio"));
    }

    @Test
    @DisplayName("aDecimalOpcional devuelve 0 si el campo está vacío")
    void aDecimalOpcional() throws Exception {
        assertEquals(0, Conversor.aDecimalOpcional("", "valor base"));
        assertEquals(0, Conversor.aDecimalOpcional(null, "valor base"));
        assertEquals(3000, Conversor.aDecimalOpcional("3000", "valor base"));
        assertThrows(DatoInvalidoException.class, () -> Conversor.aDecimalOpcional("x", "valor base"));
    }

    @Test
    @DisplayName("aHora acepta 18:30 y 9:00, y rechaza horas inválidas")
    void aHora() throws Exception {
        assertEquals(LocalTime.of(18, 30), Conversor.aHora("18:30"));
        assertEquals(LocalTime.of(9, 0), Conversor.aHora(" 9:00 "));
        assertThrows(DatoInvalidoException.class, () -> Conversor.aHora("25:00"));
        assertThrows(DatoInvalidoException.class, () -> Conversor.aHora("6 pm"));
    }

    @Test
    @DisplayName("aPesos agrega el signo de pesos")
    void aPesos() {
        assertEquals("$0", Conversor.aPesos(0));
        assertTrue(Conversor.aPesos(15000).startsWith("$15"));
    }
}
