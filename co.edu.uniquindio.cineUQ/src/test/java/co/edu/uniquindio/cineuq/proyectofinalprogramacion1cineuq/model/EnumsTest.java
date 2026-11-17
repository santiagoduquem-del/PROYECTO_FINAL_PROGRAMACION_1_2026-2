package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Enums: nombres para mostrar y valores")
class EnumsTest {

    @Test
    @DisplayName("Formato: nombre, vista FXML, logo; DOS_D se muestra como Signature")
    void formato() {
        assertEquals("Signature", Formato.DOS_D.toString());
        assertEquals("FormatoSignature.fxml", Formato.DOS_D.getVista());
        assertEquals("logo_signature.png", Formato.DOS_D.getLogo());
        assertEquals("IMAX 70 mm", Formato.IMAX_70MM.getNombre());
        assertEquals(11, Formato.values().length);
        for (Formato formato : Formato.values()) {
            assertTrue(formato.getVista().endsWith(".fxml"));
            assertTrue(formato.getLogo().endsWith(".png"));
        }
    }

    @Test
    @DisplayName("Genero, Clasificacion, Idioma y TipoTarjeta muestran su nombre en español")
    void nombres() {
        assertEquals("Ciencia ficción", Genero.CIENCIA_FICCION.toString());
        assertEquals("Ciencia ficción", Genero.CIENCIA_FICCION.getNombre());
        assertEquals("Mayores de 12 años", Clasificacion.MAYORES_12.toString());
        assertEquals("Mayores de 12 años", Clasificacion.MAYORES_12.getDescripcion());
        assertEquals("Subtitulada", Idioma.SUBTITULADA.toString());
        assertEquals("Doblada", Idioma.DOBLADA.getNombre());
        assertEquals("Crédito", TipoTarjeta.CREDITO.toString());
        assertEquals("Débito", TipoTarjeta.DEBITO.getNombre());
    }

    @Test
    @DisplayName("TipoEntrada y TipoCliente: descripción y valor por defecto")
    void tipos() {
        assertEquals(15000, TipoEntrada.GENERAL.getValorPorDefecto());
        assertEquals(12000, TipoEntrada.ESTUDIANTE.getValorPorDefecto());
        assertEquals(10000, TipoEntrada.NINO.getValorPorDefecto());
        assertEquals(11000, TipoEntrada.ADULTO_MAYOR.getValorPorDefecto());
        assertEquals("Niño", TipoEntrada.NINO.getDescripcion());
        assertEquals("Adulto mayor", TipoCliente.ADULTO_MAYOR.getDescripcion());
        // Cada tipo de entrada (menos General) tiene un tipo de cliente con el mismo nombre
        for (TipoCliente tipo : TipoCliente.values()) {
            assertEquals(tipo.name(), TipoEntrada.valueOf(tipo.name()).name());
        }
    }

    @Test
    @DisplayName("PlanMembresia: precios y descuentos crecen de Free a Premium")
    void planes() {
        assertEquals(0, PlanMembresia.FREE.getPrecioMensual());
        assertEquals("Premium", PlanMembresia.PREMIUM.getNombre());
        PlanMembresia[] planes = PlanMembresia.values();
        for (int i = 1; i < planes.length; i++) {
            assertTrue(planes[i].getPrecioMensual() > planes[i - 1].getPrecioMensual());
            assertTrue(planes[i].getDescuentoEntradas() >= planes[i - 1].getDescuentoEntradas());
        }
    }

    @Test
    @DisplayName("Los enums de estado tienen sus valores")
    void estados() {
        assertEquals(2, EstadoAsiento.values().length);
        assertEquals(3, EstadoFuncion.values().length);
        assertEquals(2, EstadoPelicula.values().length);
        assertEquals(2, EstadoSala.values().length);
        assertEquals(2, EstadoVenta.values().length);
    }
}
