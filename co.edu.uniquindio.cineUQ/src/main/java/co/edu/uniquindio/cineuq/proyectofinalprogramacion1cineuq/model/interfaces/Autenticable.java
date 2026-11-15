package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces;

/**
 * Entidad que puede validar credenciales de acceso (Empleado).
 */
public interface Autenticable {

    boolean validarCredenciales(String usuario, String contrasena);

}
