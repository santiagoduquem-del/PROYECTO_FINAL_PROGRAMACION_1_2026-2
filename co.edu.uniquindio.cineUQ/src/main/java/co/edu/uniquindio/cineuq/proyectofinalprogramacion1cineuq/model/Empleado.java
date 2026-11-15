package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Autenticable;

/**
 * Clase base abstracta para el personal del cinema (operador y administrador).
 */
public abstract class Empleado extends Persona implements Autenticable {

    // Versión de la clase para la persistencia (no la cambies o no se podrán abrir los datos guardados)
    private static final long serialVersionUID = 6271659438922276245L;

    // Atributos de acceso asumidos: el enunciado no los define.
    private String usuario;
    private String contrasena;

    public Empleado(String identificacion, String nombre, String usuario, String contrasena) {
        super(identificacion, nombre);
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    @Override
    public boolean validarCredenciales(String usuario, String contrasena) {
        return this.usuario.equals(usuario) && this.contrasena.equals(contrasena);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        // La contraseña no se muestra a propósito.
        return "Empleado{" + super.toString() + ", usuario='" + usuario + "'}";
    }
}
