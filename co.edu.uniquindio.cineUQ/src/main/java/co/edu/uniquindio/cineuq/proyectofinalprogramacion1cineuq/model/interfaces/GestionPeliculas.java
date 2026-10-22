package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import java.util.List;

/**
 * Operaciones de administración de películas.
 */
public interface GestionPeliculas {

    void registrarPelicula(Pelicula pelicula) throws PeliculaDuplicadaException;

    Pelicula buscarPelicula(String codigo) throws PeliculaNoEncontradaException;

    List<Pelicula> consultarPeliculas();

    void modificarPelicula(String codigo, String nombre, Genero genero, int duracionMinutos,
                           Clasificacion clasificacion) throws PeliculaNoEncontradaException;

    void deshabilitarPelicula(String codigo) throws PeliculaNoEncontradaException;

}
