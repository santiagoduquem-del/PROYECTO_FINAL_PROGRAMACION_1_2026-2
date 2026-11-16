package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;

import java.util.List;

/**
 * Controller de la pantalla Detalle de película (se abre desde la cartelera).
 */
public class DetallePeliculaController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public DetallePeliculaController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    /** La película en la que se hizo clic en la cartelera. */
    public Pelicula obtenerPeliculaSeleccionada() {
        return modelFactory.getPeliculaSeleccionada();
    }

    /** "Comprar entradas": la pantalla de horarios mostrará solo esta película. */
    public void prepararCompra(Pelicula pelicula) {
        modelFactory.setPeliculaHorarios(pelicula);
    }

    /** Funciones programadas (no canceladas) de la película. */
    public List<Funcion> obtenerFuncionesProgramadas(Pelicula pelicula) {
        return cine.consultarFuncionesProgramadas(pelicula.getCodigo());
    }
}
