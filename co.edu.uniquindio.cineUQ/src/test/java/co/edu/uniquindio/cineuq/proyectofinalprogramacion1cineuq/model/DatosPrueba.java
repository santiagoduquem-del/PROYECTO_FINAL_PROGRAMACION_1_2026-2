package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Datos de ejemplo que comparten las pruebas. Cada prueba crea un cine NUEVO,
 * así ninguna prueba depende de lo que hizo otra (y no se toca el archivo de datos real).
 */
public class DatosPrueba {

    public static final LocalDate MANANA = LocalDate.now().plusDays(1);

    /** Cine vacío: solo trae las tarifas, membresías y combos por defecto. */
    public static Cine crearCine() {
        return new Cine("CinemaUQ", "900123456", "cine@uq.edu.co", "6067359300", "Armenia");
    }

    /** Película de 120 minutos disponible en IMAX y Signature (DOS_D). */
    public static Pelicula crearPelicula(String codigo) {
        return new Pelicula(codigo, "Película " + codigo, Genero.ACCION, 120, Clasificacion.MAYORES_12,
                List.of(Formato.IMAX, Formato.DOS_D), LocalDate.now().getYear(), EstadoPelicula.DISPONIBLE);
    }

    public static Sala crearSala(String codigo, int capacidad) {
        return new Sala(codigo, "Sala " + codigo, capacidad, EstadoSala.DISPONIBLE);
    }

    public static Cliente crearCliente(String identificacion, TipoCliente tipo, PlanMembresia plan) {
        return new Cliente(identificacion, "Cliente " + identificacion, "3001234567",
                "cliente" + identificacion + "@correo.com", tipo, plan);
    }

    /**
     * Cine con una película P1, una sala S1 de 20 asientos, un cliente 100 (General, plan Free)
     * y la función F1 de P1 en S1 mañana a las 18:00 en IMAX doblada, sin valor base.
     */
    public static Cine crearCineConFuncion() throws Exception {
        Cine cine = crearCine();
        cine.registrarPelicula(crearPelicula("P1"));
        cine.registrarSala(crearSala("S1", 20));
        cine.registrarCliente(crearCliente("100", TipoCliente.GENERAL, PlanMembresia.FREE));
        cine.programarFuncion("F1", "P1", "S1", MANANA, LocalTime.of(18, 0), 0, Formato.IMAX, Idioma.DOBLADA);
        return cine;
    }
}
