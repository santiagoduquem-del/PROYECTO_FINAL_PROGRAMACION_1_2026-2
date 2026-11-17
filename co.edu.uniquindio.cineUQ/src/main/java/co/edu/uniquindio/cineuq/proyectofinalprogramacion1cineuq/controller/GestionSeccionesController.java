package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.SeccionPelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.util.List;

/**
 * Controller de la pantalla Secciones del detalle (administrador): las secciones de texto y foto
 * que aparecen debajo del detalle de una película. Se abre desde Gestionar películas.
 */
public class GestionSeccionesController {

    private final ModelFactoryController modelFactory;
    private final Cine cine;

    public GestionSeccionesController() {
        modelFactory = ModelFactoryController.getInstance();
        cine = modelFactory.getCine();
    }

    /** Película elegida en Gestionar películas. */
    public Pelicula obtenerPelicula() {
        return modelFactory.getPeliculaSeleccionada();
    }

    public List<SeccionPelicula> obtenerSecciones() {
        return obtenerPelicula().getListSecciones();
    }

    public void agregarSeccion(String etiqueta, String titulo, String texto, String rutaFoto) throws CinemaException {
        String codigo = obtenerPelicula().getCodigo();
        cine.agregarSeccionPelicula(codigo, etiqueta, titulo, texto, copiarFoto(codigo, rutaFoto));
        modelFactory.guardar();   // persistencia
    }

    public void modificarSeccion(int posicion, String etiqueta, String titulo, String texto, String rutaFoto)
            throws CinemaException {
        String codigo = obtenerPelicula().getCodigo();
        cine.modificarSeccionPelicula(codigo, posicion, etiqueta, titulo, texto, copiarFoto(codigo, rutaFoto));
        modelFactory.guardar();
    }

    public void eliminarSeccion(int posicion) throws CinemaException {
        cine.eliminarSeccionPelicula(obtenerPelicula().getCodigo(), posicion);
        modelFactory.guardar();
    }

    /** Sube (-1) o baja (+1) la sección y devuelve su nueva posición. */
    public int moverSeccion(int posicion, int desplazamiento) throws CinemaException {
        int nueva = cine.moverSeccionPelicula(obtenerPelicula().getCodigo(), posicion, desplazamiento);
        modelFactory.guardar();
        return nueva;
    }

    /**
     * Copia la foto a la carpeta de imágenes del programa. Cada copia lleva la hora en el nombre
     * para que dos secciones no se reemplacen la foto entre sí. Si ya es una copia, no se copia otra vez.
     */
    private String copiarFoto(String codigoPelicula, String rutaFoto) {
        return Persistencia.copiarImagen(rutaFoto, codigoPelicula + "_seccion_" + System.currentTimeMillis());
    }
}
