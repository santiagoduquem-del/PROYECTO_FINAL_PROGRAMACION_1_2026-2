package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import com.sun.tools.javac.util.List;

/**
 * Personal que vende y cancela entradas y consulta películas, funciones y disponibilidad.
 */
public class Operador extends Empleado {

    List<Venta> listVentas;


    public Operador(String identificacion, String nombre, String usuario, String contrasena) {
        super(identificacion, nombre, usuario, contrasena);
    }

    @Override
    public String toString() {
        return "Operador{" + super.toString() + "}";
    }
}
