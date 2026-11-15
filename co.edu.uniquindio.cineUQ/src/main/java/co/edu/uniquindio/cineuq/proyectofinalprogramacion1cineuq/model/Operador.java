package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.RegistroVentas;

import java.util.ArrayList;
import java.util.List;

/**
 * Personal que vende y cancela entradas y consulta películas, funciones y disponibilidad.
 */
public class Operador extends Empleado implements RegistroVentas {

    // Versión de la clase para la persistencia (no la cambies o no se podrán abrir los datos guardados)
    private static final long serialVersionUID = 1411373441265146385L;

    private List<Venta> listVentas;

    public Operador(String identificacion, String nombre, String usuario, String contrasena) {
        super(identificacion, nombre, usuario, contrasena);
        this.listVentas = new ArrayList<>();
    }

    @Override
    public void agregarVenta(Venta venta) {
        listVentas.add(venta);
    }

    @Override
    public List<Venta> getListVentas() {
        return listVentas;
    }

    @Override
    public String toString() {
        return "Operador{" + super.toString() + "}";
    }
}
