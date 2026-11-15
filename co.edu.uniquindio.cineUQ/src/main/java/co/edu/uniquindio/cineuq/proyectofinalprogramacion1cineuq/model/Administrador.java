package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Personal que gestiona películas, salas, funciones y tarifas, y genera reportes.
 */
public class Administrador extends Empleado {

    // Versión de la clase para la persistencia (no la cambies o no se podrán abrir los datos guardados)
    private static final long serialVersionUID = -95629247018716428L;


    public Administrador(String identificacion, String nombre, String usuario, String contrasena) {
        super(identificacion, nombre, usuario, contrasena);
    }

    @Override
    public String toString() {
        return "Administrador{" + super.toString() + "}";
    }
}
