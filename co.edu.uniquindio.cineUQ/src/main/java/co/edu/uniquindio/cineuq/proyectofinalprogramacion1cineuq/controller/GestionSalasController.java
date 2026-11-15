package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.EstadoSala;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Sala;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.util.List;

/**
 * Controller de la pantalla Gestionar salas (administrador).
 */
public class GestionSalasController {

    private final Cine cine;

    public GestionSalasController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public void registrarSala(String codigo, String nombre, String capacidadTexto) throws CinemaException {
        int capacidad = Conversor.aEntero(capacidadTexto, "capacidad");
        Sala sala = new Sala(codigo.trim(), nombre.trim(), capacidad, EstadoSala.DISPONIBLE);
        cine.registrarSala(sala);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void modificarSala(String codigo, String nombre, String capacidadTexto) throws CinemaException {
        int capacidad = Conversor.aEntero(capacidadTexto, "capacidad");
        cine.modificarSala(codigo.trim(), nombre.trim(), capacidad);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void deshabilitarSala(String codigo) throws CinemaException {
        cine.deshabilitarSala(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void eliminarSala(String codigo) throws CinemaException {
        cine.eliminarSala(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void habilitarSala(String codigo) throws CinemaException {
        cine.habilitarSala(codigo.trim());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Sala> obtenerSalas() {
        return cine.consultarSalas();
    }
}
