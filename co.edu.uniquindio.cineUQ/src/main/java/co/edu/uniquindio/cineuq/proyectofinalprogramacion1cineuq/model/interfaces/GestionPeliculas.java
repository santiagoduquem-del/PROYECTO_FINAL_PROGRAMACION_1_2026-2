package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Clasificacion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Genero;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.EliminacionNoPermitidaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.ModificacionNoPermitidaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.PeliculaDuplicadaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.PeliculaNoEncontradaException;

import java.util.List;

/**
 * Operaciones de administración de películas.
 */
public interface GestionPeliculas {

    void registrarPelicula(Pelicula pelicula) throws PeliculaDuplicadaException, DatoInvalidoException;

    Pelicula buscarPelicula(String codigo) throws PeliculaNoEncontradaException;

    List<Pelicula> consultarPeliculas();

    /** Solo las películas en estado DISPONIBLE (la cartelera). */
    List<Pelicula> consultarPeliculasDisponibles();

    void modificarPelicula(String codigo, String nombre, Genero genero, int duracionMinutos,
                           Clasificacion clasificacion, List<Formato> formatos, int anioEstreno)
            throws PeliculaNoEncontradaException, DatoInvalidoException, ModificacionNoPermitidaException;

    /** Guarda la carátula y el banner de la película (pueden ser null si no se subieron). */
    void asignarImagenesPelicula(String codigo, String rutaCaratula, String rutaBanner)
            throws PeliculaNoEncontradaException;

    void habilitarPelicula(String codigo) throws PeliculaNoEncontradaException;

    void deshabilitarPelicula(String codigo) throws PeliculaNoEncontradaException;

    /** Elimina la película solo si no tiene funciones. */
    void eliminarPelicula(String codigo) throws PeliculaNoEncontradaException, EliminacionNoPermitidaException;

}
