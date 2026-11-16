package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;

import java.util.List;

/**
 * Controller de la pantalla Consultar películas (operador).
 */
public class ConsultarPeliculasController {

    private final Cine cine;

    public ConsultarPeliculasController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    /** Películas en cartelera (estado DISPONIBLE). */
    public List<Pelicula> obtenerPeliculasDisponibles() {
        return cine.consultarPeliculasDisponibles();
    }

    /** Guarda la película elegida en el mosaico para que la pantalla de detalle la muestre. */
    public void seleccionarPelicula(Pelicula pelicula) {
        ModelFactoryController.getInstance().setPeliculaSeleccionada(pelicula);
    }
}
