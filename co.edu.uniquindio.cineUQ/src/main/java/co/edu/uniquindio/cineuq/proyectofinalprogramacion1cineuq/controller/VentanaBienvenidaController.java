package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

/**
 * Controller de la ventana de bienvenida.
 * Mientras se reproduce la intro, deja listo el sistema (crea el Cine y carga los datos iniciales).
 */
public class VentanaBienvenidaController {

    public void iniciarSistema() {
        // La primera llamada a getInstance() crea el Cine y carga los datos de prueba
        ModelFactoryController.getInstance();
    }
}
