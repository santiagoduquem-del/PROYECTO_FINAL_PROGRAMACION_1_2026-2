package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Convierte el texto que escribe el usuario en los campos (String) a los tipos que usa el modelo.
 * Si el texto no es válido lanza DatoInvalidoException con un mensaje claro.
 */
public class Conversor {

    private Conversor() { }

    public static int aEntero(String texto, String campo) throws DatoInvalidoException {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser un número entero.");
        }
    }

    public static double aDecimal(String texto, String campo) throws DatoInvalidoException {
        try {
            // Se quitan los puntos de miles por si el usuario escribe 15.000
            return Double.parseDouble(texto.trim().replace(".", "").replace(",", "."));
        } catch (NumberFormatException e) {
            throw new DatoInvalidoException("El campo " + campo + " debe ser un número.");
        }
    }

    /** Igual que aDecimal, pero si el campo está vacío devuelve 0 (para valores opcionales). */
    public static double aDecimalOpcional(String texto, String campo) throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            return 0;
        }
        return aDecimal(texto, campo);
    }

    /** Acepta horas como 18:30 o 9:00 (formato de 24 horas). */
    public static LocalTime aHora(String texto) throws DatoInvalidoException {
        try {
            return LocalTime.parse(texto.trim(), DateTimeFormatter.ofPattern("H:mm"));
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException("La hora debe tener el formato HH:mm (ejemplo: 18:30).");
        }
    }

    /** Da formato de pesos: 15000 -> $15,000 (o $15.000 según la configuración del computador). */
    public static String aPesos(double valor) {
        return "$" + String.format("%,.0f", valor);
    }
}
