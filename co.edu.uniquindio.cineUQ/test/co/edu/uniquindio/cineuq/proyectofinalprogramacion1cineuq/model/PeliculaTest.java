package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeliculaTest {

    private Pelicula conAnio(int anio) {
        List<Formato> formatos = new ArrayList<>();
        formatos.add(Formato.DOS_D);
        return new Pelicula("P1", "Interestelar", Genero.CIENCIA_FICCION, 169, Clasificacion.MAYORES_12,
                formatos, anio, EstadoPelicula.DISPONIBLE);
    }

    @Test
    void deshabilitarYHabilitarCambianElEstado() {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "P");
        assertTrue(pelicula.estaDisponible());
        pelicula.deshabilitar();
        assertFalse(pelicula.estaDisponible());
        assertEquals(EstadoPelicula.NO_DISPONIBLE, pelicula.getEstado());
        pelicula.habilitar();
        assertTrue(pelicula.estaDisponible());
    }

    @Test
    void guardaUnaCopiaDeLaListaDeFormatos() {
        List<Formato> original = new ArrayList<>();
        original.add(Formato.IMAX);
        Pelicula pelicula = new Pelicula("P1", "X", Genero.DRAMA, 90, Clasificacion.TODOS, original, 2020,
                EstadoPelicula.DISPONIBLE);
        original.add(Formato.TRES_D);
        assertEquals(1, pelicula.getFormatos().size());
    }

    @Test
    void tieneFormatoRevisaLosFormatosDeLaPelicula() {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "H");   // Signature e IMAX
        assertTrue(pelicula.tieneFormato(Formato.IMAX));
        assertFalse(pelicula.tieneFormato(Formato.CUATRO_DX));
    }

    @Test
    void describirFormatosLosUneConPuntos() {
        assertEquals("Signature · IMAX", DatosPrueba.pelicula("P1", "W").describirFormatos());
    }

    @Test
    void sinFormatosDiceSinFormato() {
        Pelicula pelicula = new Pelicula("P1", "X", Genero.DRAMA, 90, Clasificacion.TODOS, new ArrayList<Formato>(),
                2020, EstadoPelicula.DISPONIBLE);
        assertEquals("Sin formato", pelicula.describirFormatos());
    }

    @Test
    void setFormatosReemplazaLosFormatos() {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "Q");
        List<Formato> nuevos = new ArrayList<>();
        nuevos.add(Formato.CUATRO_DX);
        pelicula.setFormatos(nuevos);
        assertEquals(1, pelicula.getFormatos().size());
        assertTrue(pelicula.tieneFormato(Formato.CUATRO_DX));
    }

    @Test
    void esReestrenoSiSeEstrenoAntesDelAnioActual() {
        int actual = LocalDate.now().getYear();
        assertTrue(conAnio(actual - 1).esReestreno());
        assertFalse(conAnio(actual).esReestreno());
        assertFalse(conAnio(0).esReestreno());   // 0 = sin dato
    }

    @Test
    void describirEstrenoSegunElAnio() {
        int actual = LocalDate.now().getYear();
        assertEquals("Sin dato", conAnio(0).describirEstreno());
        assertEquals((actual - 10) + " · Reestreno", conAnio(actual - 10).describirEstreno());
        assertEquals(String.valueOf(actual), conAnio(actual).describirEstreno());
    }

    @Test
    void sinopsisElencoYEstudioVaciosDevuelvenTextoVacio() {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "Juan");
        assertEquals("", pelicula.getSinopsis());
        assertEquals("", pelicula.getElenco());
        assertEquals("", pelicula.getEstudio());
    }

    @Test
    void agregarYRemoverFunciones() {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "J");
        Sala sala = DatosPrueba.sala("S1", "SALA2", 30, EstadoSala.DISPONIBLE);
        Funcion funcion = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        pelicula.agregarFuncion(funcion);
        assertEquals(1, pelicula.getListFunciones().size());
        pelicula.removerFuncion(funcion);
        assertEquals(0, pelicula.getListFunciones().size());
    }

    Cliente cliente = DatosPrueba.cliente("1", "Marcela", "31771958", "marcela@gmail.com",
            TipoCliente.GENERAL, PlanMembresia.FREE);

    @Test
    void cuentaLasEntradasVendidasDeTodasSusFunciones() throws Exception {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "R");
        Sala sala = DatosPrueba.sala("S1", "SALAS IMAX", 3, EstadoSala.DISPONIBLE);
        Funcion f1 = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        Funcion f2 = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        pelicula.agregarFuncion(f1);
        pelicula.agregarFuncion(f2);

        f1.agregarVenta(DatosPrueba.venta("V1", cliente, f1, f1.buscarAsiento("A1")));
        f1.agregarVenta(DatosPrueba.venta("V2", cliente, f1, f1.buscarAsiento("A2")));
        f2.agregarVenta(DatosPrueba.venta("V3", cliente, f1, f1.buscarAsiento("A3")));
        assertEquals(3, pelicula.contarEntradasVendidas());
    }

    @Test
    void lasVentasCanceladasNoCuentan() throws Exception {
        Pelicula pelicula = DatosPrueba.pelicula("P1", "k");
        Sala sala = DatosPrueba.sala("S1", "sala2", 20, EstadoSala.DISPONIBLE);
        Funcion funcion = DatosPrueba.funcion("F1", pelicula, sala, DatosPrueba.manana(),
                LocalTime.of(12, 20), 10500, Formato.IMAX, Idioma.DOBLADA);
        pelicula.agregarFuncion(funcion);
        Venta venta = DatosPrueba.venta("V1", cliente, funcion, funcion.buscarAsiento("A1"));
        funcion.agregarVenta(venta);
        venta.cancelar();
        assertEquals(0, pelicula.contarEntradasVendidas());
    }

    @Test
    void toStringMuestraNombreYCodigo() {
        assertEquals("Pelicula P1 (P1)", DatosPrueba.pelicula("P1", "Pelicula P1").toString());
    }
}
