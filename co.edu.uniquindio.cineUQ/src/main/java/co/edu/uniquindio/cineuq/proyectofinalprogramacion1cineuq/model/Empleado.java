package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

/**
 * Clase base abstracta para el personal del cinema (operador y administrador).
 */
public abstract class Empleado extends Persona {

    // Atributos de acceso asumidos: el enunciado no los define.
    private String usuario;
    private String contrasena;

    public Empleado(String identificacion, String nombre, String usuario, String contrasena) {
        super(identificacion, nombre);
        this.usuario = usuario;
        this.contrasena = contrasena;
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
        return "Empleado{" + super.toString() + ", usuario='" + usuario + "'}";
    }
}
