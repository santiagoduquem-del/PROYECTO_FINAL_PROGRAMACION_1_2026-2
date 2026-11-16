package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Franquicia de la tarjeta. Se reconoce por los primeros dígitos del número:
 *   Visa             -> empieza por 4                         (16 dígitos, CVV de 3)
 *   Mastercard       -> empieza por 51 a 55 o 2221 a 2720     (16 dígitos, CVV de 3)
 *   American Express -> empieza por 34 o 37                   (15 dígitos, CVV de 4)
 *   Diners Club      -> empieza por 300 a 305, 36, 38 o 39    (14 dígitos, CVV de 3)
 */
public enum Franquicia {
    VISA("Visa", 16, 3, "logo_visa.png"),
    MASTERCARD("Mastercard", 16, 3, "logo_mastercard.png"),
    AMEX("American Express", 15, 4, "logo_amex.png"),
    DINERS("Diners Club", 14, 3, "logo_diners.png");

    private final String nombre;
    private final int cantidadDigitos;
    private final int digitosCvv;
    private final String logo;   // archivo dentro de recursosGraficos

    Franquicia(String nombre, int cantidadDigitos, int digitosCvv, String logo) {
        this.nombre = nombre;
        this.cantidadDigitos = cantidadDigitos;
        this.digitosCvv = digitosCvv;
        this.logo = logo;
    }

    /**
     * Devuelve la franquicia según cómo empieza el número, o null si todavía no se reconoce.
     * El número puede traer espacios (se ignoran).
     */
    public static Franquicia detectar(String numero) {
        if (numero == null) {
            return null;
        }
        String digitos = numero.replace(" ", "");

        if (digitos.startsWith("4")) {
            return VISA;
        }
        if (digitos.startsWith("34") || digitos.startsWith("37")) {
            return AMEX;
        }
        if (digitos.startsWith("36") || digitos.startsWith("38") || digitos.startsWith("39")) {
            return DINERS;
        }
        if (digitos.length() >= 2) {
            int dos = Integer.parseInt(digitos.substring(0, 2));
            if (dos >= 51 && dos <= 55) {
                return MASTERCARD;
            }
        }
        if (digitos.length() >= 3) {
            int tres = Integer.parseInt(digitos.substring(0, 3));
            if (tres >= 300 && tres <= 305) {
                return DINERS;
            }
        }
        if (digitos.length() >= 4) {
            int cuatro = Integer.parseInt(digitos.substring(0, 4));
            if (cuatro >= 2221 && cuatro <= 2720) {
                return MASTERCARD;
            }
        }
        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadDigitos() {
        return cantidadDigitos;
    }

    public int getDigitosCvv() {
        return digitosCvv;
    }

    public String getLogo() {
        return logo;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
