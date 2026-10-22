package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Personal que gestiona películas, salas, funciones y tarifas, y genera reportes.
 */
public class Administrador extends Empleado {


    public Administrador(String identificacion, String nombre, String usuario, String contrasena) {
        super(identificacion, nombre, usuario, contrasena);
    }

    @Override
    public String toString() {
        return "Administrador{" + super.toString() + "}";
    }
}
