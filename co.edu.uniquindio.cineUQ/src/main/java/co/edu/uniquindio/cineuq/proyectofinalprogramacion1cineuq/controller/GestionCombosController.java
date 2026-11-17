package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.util.List;

/**
 * Controller de la pantalla Gestionar combos (administrador): CRUD de combos con foto.
 */
public class GestionCombosController {

    private final Cine cine;

    public GestionCombosController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public void registrarCombo(String codigo, String nombre, String descripcion, String precioTexto,
                               boolean exclusivoVipBistro, String rutaFoto) throws CinemaException {
        double precio = Conversor.aDecimal(precioTexto, "precio");
        Combo combo = new Combo(codigo.trim(), nombre.trim(), descripcion.trim(), precio);
        combo.setExclusivoVipBistro(exclusivoVipBistro);
        cine.registrarCombo(combo);
        guardarFoto(combo.getCodigo(), rutaFoto);
    }

    public void modificarCombo(String codigo, String nombre, String descripcion, String precioTexto,
                               boolean exclusivoVipBistro, String rutaFoto) throws CinemaException {
        double precio = Conversor.aDecimal(precioTexto, "precio");
        cine.modificarCombo(codigo.trim(), nombre, descripcion, precio, exclusivoVipBistro);
        guardarFoto(codigo.trim(), rutaFoto);
    }

    public void deshabilitarCombo(String codigo) throws CinemaException {
        cine.deshabilitarCombo(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void habilitarCombo(String codigo) throws CinemaException {
        cine.habilitarCombo(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void eliminarCombo(String codigo) throws CinemaException {
        cine.eliminarCombo(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Combo> obtenerCombos() {
        return cine.consultarCombos();
    }

    /** Copia la foto a la carpeta de imágenes del programa y la asigna al combo. */
    private void guardarFoto(String codigo, String rutaFoto) throws CinemaException {
        String copia = Persistencia.copiarImagen(rutaFoto, "combo_" + codigo);
        cine.asignarImagenCombo(codigo, copia);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }
}
