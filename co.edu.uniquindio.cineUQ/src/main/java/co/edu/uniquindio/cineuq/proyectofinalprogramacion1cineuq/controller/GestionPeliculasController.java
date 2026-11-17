package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.util.List;

/**
 * Controller de la pantalla Gestionar películas (administrador).
 */
public class GestionPeliculasController {

    private final Cine cine;

    public GestionPeliculasController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    /** Antes de abrir Secciones del detalle: se guarda cuál película se va a editar. */
    public void prepararSecciones(String codigo) throws CinemaException {
        if (codigo == null || codigo.isBlank()) {
            throw new DatoInvalidoException("Seleccione en la tabla la película a la que le quiere poner secciones.");
        }
        ModelFactoryController.getInstance().setPeliculaSeleccionada(cine.buscarPelicula(codigo.trim()));
    }

    /**
     * rutaCaratula y rutaBanner son la ubicación de las imágenes elegidas por el administrador
     * (pueden ser null si no subió alguna).
     */
    public void registrarPelicula(String codigo, String nombre, Genero genero, String duracionTexto, String anioTexto,
                                  Clasificacion clasificacion, List<Formato> formatos, String sinopsis, String elenco,
                                  String estudio, String rutaCaratula, String rutaBanner)
            throws CinemaException {
        validarListas(genero, clasificacion);
        int duracion = Conversor.aEntero(duracionTexto, "duración");
        int anioEstreno = Conversor.aEntero(anioTexto, "año de estreno");
        Pelicula pelicula = new Pelicula(codigo.trim(), nombre.trim(), genero, duracion,
                clasificacion, formatos, anioEstreno, EstadoPelicula.DISPONIBLE);
        cine.registrarPelicula(pelicula);
        cine.asignarInformacionPelicula(pelicula.getCodigo(), sinopsis, elenco, estudio);
        guardarImagenes(pelicula.getCodigo(), rutaCaratula, rutaBanner);
    }

    public void modificarPelicula(String codigo, String nombre, Genero genero, String duracionTexto, String anioTexto,
                                  Clasificacion clasificacion, List<Formato> formatos, String sinopsis, String elenco,
                                  String estudio, String rutaCaratula, String rutaBanner)
            throws CinemaException {
        validarListas(genero, clasificacion);
        int duracion = Conversor.aEntero(duracionTexto, "duración");
        int anioEstreno = Conversor.aEntero(anioTexto, "año de estreno");
        cine.modificarPelicula(codigo.trim(), nombre.trim(), genero, duracion, clasificacion, formatos, anioEstreno);
        cine.asignarInformacionPelicula(codigo.trim(), sinopsis, elenco, estudio);
        guardarImagenes(codigo.trim(), rutaCaratula, rutaBanner);
    }

    public void deshabilitarPelicula(String codigo) throws CinemaException {
        cine.deshabilitarPelicula(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void eliminarPelicula(String codigo) throws CinemaException {
        cine.eliminarPelicula(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void habilitarPelicula(String codigo) throws CinemaException {
        cine.habilitarPelicula(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Pelicula> obtenerPeliculas() {
        return cine.consultarPeliculas();
    }

    /**
     * Copia las imágenes a la carpeta del programa (para que no se pierdan),
     * se las asigna a la película y guarda todo en el archivo.
     */
    private void guardarImagenes(String codigo, String rutaCaratula, String rutaBanner) throws CinemaException {
        String caratula = Persistencia.copiarImagen(rutaCaratula, codigo + "_caratula");
        String banner = Persistencia.copiarImagen(rutaBanner, codigo + "_banner");
        cine.asignarImagenesPelicula(codigo, caratula, banner);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    private void validarListas(Genero genero, Clasificacion clasificacion) throws DatoInvalidoException {
        if (genero == null) {
            throw new DatoInvalidoException("Debe seleccionar el género.");
        }
        if (clasificacion == null) {
            throw new DatoInvalidoException("Debe seleccionar la clasificación.");
        }
    }
}
