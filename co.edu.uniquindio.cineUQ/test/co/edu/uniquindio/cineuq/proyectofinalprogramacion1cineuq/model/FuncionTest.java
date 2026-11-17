package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.AsientoNoEncontradoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.FuncionNoDisponibleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class FuncionTest {

    private Pelicula pelicula;   // dura 120 minutos
    private Sala sala;           // 20 asientos
    private Funcion funcion;

    @BeforeEach
    void preparar() {
        pelicula = DatosPrueba.pelicula("P1", "kickAss");
        sala = DatosPrueba.sala("S1", "sala2", 20, EstadoSala.DISPONIBLE);
        funcion = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
    }

    @Test
    void unaFuncionNuevaEstaProgramada() {
        assertEquals(EstadoFuncion.PROGRAMADA, funcion.getEstado());
        assertFalse(funcion.estaCancelada());
    }

    @Test
    void copiaLosAsientosDeLaSalaComoObjetosNuevos() {
        assertEquals(20, funcion.getAsientos().size());
        assertNotSame(sala.getAsientos().get(0), funcion.getAsientos().get(0));
        assertEquals(sala.getAsientos().get(0).getCodigo(), funcion.getAsientos().get(0).getCodigo());
    }

    @Test
    void ocuparUnAsientoEnUnaFuncionNoLoOcupaEnLaSala() throws Exception {
        funcion.buscarAsiento("A1").ocupar();
        assertTrue(sala.getAsientos().get(0).estaDisponible());
    }

    @Test
    void ocuparUnAsientoNoLoOcupaEnOtraFuncionDeLaMismaSala() throws Exception {
        Funcion otra = DatosPrueba.funcion("F2", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        funcion.buscarAsiento("A1").ocupar();
        assertTrue(otra.buscarAsiento("A1").estaDisponible());
    }

    @Test
    void buscarAsientoNoDistingueMayusculasDeMinusculas() throws Exception {
        assertSame(funcion.buscarAsiento("A1"), funcion.buscarAsiento("a1"));
    }

    @Test
    void buscarUnAsientoQueNoExisteLanzaExcepcion() {
        assertThrows(AsientoNoEncontradoException.class, () -> funcion.buscarAsiento("Z99"));
    }

    @Test
    void cuentaAsientosDisponiblesYOcupados() throws Exception {
        assertEquals(20, funcion.contarAsientosDisponibles());
        assertEquals(0, funcion.contarAsientosOcupados());
        funcion.buscarAsiento("A1").ocupar();
        funcion.buscarAsiento("B3").ocupar();
        assertEquals(18, funcion.contarAsientosDisponibles());
        assertEquals(2, funcion.contarAsientosOcupados());
    }

    @Test
    void calculaInicioFinYMomentoEnQueLaSalaQuedaLibre() {
        Funcion f = new Funcion("F9", pelicula, sala, LocalDate.of(2030, 5, 10), LocalTime.of(18, 30), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(LocalDateTime.of(2030, 5, 10, 18, 30), f.getInicio());
        assertEquals(LocalDateTime.of(2030, 5, 10, 20, 30), f.getFin());                         // 120 min
        assertEquals(LocalDateTime.of(2030, 5, 10, 20, 45), f.getSalaLibreDesde());              // + 15 min de limpieza
    }

    @Test
    void unaFuncionQuePasaDeMedianocheTerminaAlDiaSiguiente() {
        Funcion f = new Funcion("F9", pelicula, sala, LocalDate.of(2030, 5, 10), LocalTime.of(23, 0), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(LocalDateTime.of(2030, 5, 11, 1, 0), f.getFin());
    }

    @Test
    void calculaElMinutoDelDiaEnQueEmpiezaYTermina() {
        Funcion f = new Funcion("F9", pelicula, sala, DatosPrueba.manana(), LocalTime.of(14, 30), 0,
                Formato.DOS_D, Idioma.DOBLADA);
        assertEquals(870, f.getMinutoInicio());   // 14 x 60 + 30
        assertEquals(990, f.getMinutoFin());      // + 120 min
    }

    @Test
    void cancelarCambiaElEstadoACancelada() throws Exception {
        funcion.cancelar();
        assertEquals(EstadoFuncion.CANCELADA, funcion.getEstado());
        assertTrue(funcion.estaCancelada());
    }

    @Test
    void noSePuedeCancelarDosVeces() throws Exception {
        funcion.cancelar();
        assertThrows(FuncionNoDisponibleException.class, () -> funcion.cancelar());
    }

    @Test
    void noSePuedeCancelarUnaFuncionFinalizada() {
        funcion.setEstado(EstadoFuncion.FINALIZADA);
        assertThrows(FuncionNoDisponibleException.class, () -> funcion.cancelar());
    }

    Cliente cliente = DatosPrueba.cliente("1", "Marcela", "31771958", "marcela@gmail.com",
            TipoCliente.GENERAL, PlanMembresia.FREE);

    @Test
    void soloCuentaLasEntradasConVentaActiva() throws Exception {
        Venta v1 = DatosPrueba.venta("V1", cliente, funcion, funcion.buscarAsiento("A1"));
        Venta v2 = DatosPrueba.venta("V2", cliente, funcion, funcion.buscarAsiento("A2"));
        funcion.agregarVenta(v1);
        funcion.agregarVenta(v2);
        assertEquals(2, funcion.contarEntradasVendidas());
        v2.cancelar();
        assertEquals(1, funcion.contarEntradasVendidas());
    }

    @Test
    void reconstruirAsientosCopiaDeNuevoLaSalaSiNoHayVentas() {
        sala.setCapacidad(30);
        assertEquals(20, funcion.getAsientos().size());   // todavía tiene los viejos
        assertTrue(funcion.reconstruirAsientos());
        assertEquals(30, funcion.getAsientos().size());
    }

    @Test
    void reconstruirAsientosNoHaceNadaSiYaHayVentas() throws Exception {
        funcion.agregarVenta(DatosPrueba.venta("V1", cliente, funcion, funcion.buscarAsiento("A1")));
        sala.setCapacidad(30);
        assertFalse(funcion.reconstruirAsientos());
        assertEquals(20, funcion.getAsientos().size());
    }

    @Test
    void sinFormatoUsaElPrimerFormatoDeLaPeliculaYSinIdiomaEsDoblada() {
        Funcion f = new Funcion("F9", pelicula, sala, DatosPrueba.manana(), LocalTime.of(18, 0), 0, null, null);
        assertEquals(Formato.DOS_D, f.getFormato());
        assertEquals(Idioma.DOBLADA, f.getIdioma());
    }

    @Test
    void describeElFormatoYElIdioma() {
        Funcion f = new Funcion("F9", pelicula, sala, DatosPrueba.manana(), LocalTime.of(18, 0), 0,
                Formato.IMAX, Idioma.SUBTITULADA);
        assertEquals("IMAX · Subtitulada", f.describirFormatoIdioma());
    }

    @Test
    void toStringIncluyeCodigoPeliculaYSala() {
        String texto = funcion.toString();
        assertTrue(texto.contains(funcion.getCodigo()));
        assertTrue(texto.contains(funcion.getPelicula().getNombre()));
        assertTrue(texto.contains(funcion.getSala().getNombre()));
    }
}


