package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Idioma;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Sala;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de la pantalla Gestionar funciones (administrador).
 */
public class GestionFuncionesController {

    private final Cine cine;

    public GestionFuncionesController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public void programarFuncion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha,
                                 String horaTexto, String valorBaseTexto, Formato formato, Idioma idioma)
            throws CinemaException {
        if (pelicula == null) {
            throw new DatoInvalidoException("Debe seleccionar la película.");
        }
        if (sala == null) {
            throw new DatoInvalidoException("Debe seleccionar la sala.");
        }
        if (fecha == null) {
            throw new DatoInvalidoException("Debe seleccionar la fecha.");
        }
        LocalTime hora = Conversor.aHora(horaTexto);
        double valorBase = Conversor.aDecimalOpcional(valorBaseTexto, "valor base");   // vacío = 0
        cine.programarFuncion(codigo.trim(), pelicula.getCodigo(), sala.getCodigo(), fecha, hora, valorBase,
                formato, idioma);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Cuántas entradas vendidas (activas) tiene la función: se usa para pedir confirmación. */
    public int contarEntradasVendidas(Funcion funcion) {
        return (funcion == null) ? 0 : funcion.contarEntradasVendidas();
    }

    /**
     * Cancela la función. No se borra: queda en el historial con estado CANCELADA.
     * Si tiene entradas activas, el modelo no deja cancelarla (primero se cancelan esas ventas).
     */
    public void cancelarFuncion(Funcion funcion) throws CinemaException {
        if (funcion == null) {
            throw new DatoInvalidoException("Seleccione en la tabla la función que desea cancelar.");
        }
        cine.cancelarFuncion(funcion.getCodigo());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Modifica la función con los datos del formulario (solo si no tiene ventas). */
    public void modificarFuncion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha,
                                 String horaTexto, String valorBaseTexto, Formato formato, Idioma idioma)
            throws CinemaException {
        if (codigo.trim().isEmpty()) {
            throw new DatoInvalidoException("Seleccione en la tabla la función que desea modificar.");
        }
        if (pelicula == null) {
            throw new DatoInvalidoException("Debe seleccionar la película.");
        }
        if (sala == null) {
            throw new DatoInvalidoException("Debe seleccionar la sala.");
        }
        if (fecha == null) {
            throw new DatoInvalidoException("Debe seleccionar la fecha.");
        }
        LocalTime hora = Conversor.aHora(horaTexto);
        double valorBase = Conversor.aDecimalOpcional(valorBaseTexto, "valor base");
        cine.modificarFuncion(codigo.trim(), pelicula.getCodigo(), sala.getCodigo(), fecha, hora, valorBase,
                formato, idioma);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Cambia el valor base (recargo) de la función seleccionada en la tabla. */
    public void modificarValorBase(Funcion funcion, String valorBaseTexto) throws CinemaException {
        if (funcion == null) {
            throw new DatoInvalidoException("Seleccione en la tabla la función a la que le quiere cambiar el valor base.");
        }
        double valorBase = Conversor.aDecimalOpcional(valorBaseTexto, "valor base");
        cine.modificarValorBase(funcion.getCodigo(), valorBase);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Funcion> obtenerFunciones() {
        return cine.consultarFunciones();
    }

    /** Todas las películas (si se elige una deshabilitada, el modelo lanza la excepción). */
    public List<Pelicula> obtenerPeliculas() {
        return cine.consultarPeliculas();
    }

    /** Formatos en que está disponible la película elegida (para el ComboBox de formato). */
    public List<Formato> obtenerFormatosDePelicula(Pelicula pelicula) {
        if (pelicula == null) {
            return new ArrayList<>();
        }
        return pelicula.getFormatos();
    }

    /** Todas las salas (si se elige una fuera de servicio, el modelo lanza la excepción). */
    public List<Sala> obtenerSalas() {
        return cine.consultarSalas();
    }
}
