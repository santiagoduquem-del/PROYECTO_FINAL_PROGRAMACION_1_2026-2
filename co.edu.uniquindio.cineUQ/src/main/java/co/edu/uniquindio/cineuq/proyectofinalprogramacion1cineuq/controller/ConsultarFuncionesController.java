package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de la pantalla de Horarios (antes "Consultar funciones").
 * Si se llega desde "Comprar entradas" muestra una película; desde el menú, todas.
 */
public class ConsultarFuncionesController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public ConsultarFuncionesController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    /** La película elegida en el detalle, o null si se deben mostrar todas. */
    public Pelicula obtenerPeliculaElegida() {
        return modelFactory.getPeliculaHorarios();
    }

    /** Películas a mostrar: la elegida, o todas las que tienen funciones próximas. */
    public List<Pelicula> obtenerPeliculasAMostrar() {
        List<Pelicula> peliculas = new ArrayList<>();
        if (obtenerPeliculaElegida() != null) {
            peliculas.add(obtenerPeliculaElegida());
            return peliculas;
        }
        for (Pelicula pelicula : cine.consultarPeliculasDisponibles()) {
            if (!obtenerFuncionesProximas(pelicula).isEmpty()) {
                peliculas.add(pelicula);
            }
        }
        return peliculas;
    }

    /**
     * Funciones programadas de la película que todavía no han empezado,
     * ordenadas por fecha y hora (de la más cercana a la más lejana).
     */
    public List<Funcion> obtenerFuncionesProximas(Pelicula pelicula) {
        LocalDateTime ahora = LocalDateTime.now();
        List<Funcion> proximas = new ArrayList<>();
        for (Funcion funcion : cine.consultarFuncionesProgramadas(pelicula.getCodigo())) {
            if (funcion.getInicio().isAfter(ahora)) {
                proximas.add(funcion);
            }
        }
        proximas.sort((a, b) -> a.getInicio().compareTo(b.getInicio()));
        return proximas;
    }

    /** Se eligió un horario: Generar venta abrirá con esta función ya seleccionada. */
    public void seleccionarFuncion(Funcion funcion) {
        modelFactory.setFuncionParaVenta(funcion);
    }
}
