package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.RegistroVentas;

import java.util.ArrayList;
import java.util.List;

/**
 * Cliente que adquiere entradas en el cinema.
 * Tiene un plan de membresía que le da descuentos en entradas y combos.
 */
public class Cliente extends Persona implements RegistroVentas {

    // Versión de la clase para la persistencia. Es el número que tenía antes de agregar teléfono y correo,
    // para que los datos guardados antes se sigan pudiendo abrir. No lo cambies.
    private static final long serialVersionUID = -4305241336948565694L;

    private String telefono;
    private String correo;
    private TipoCliente tipoCliente;
    private PlanMembresia plan;
    private List<Venta> listVentas;

    /** Cliente con el plan FREE (sin descuentos). */
    public Cliente(String identificacion, String nombre, String telefono, String correo, TipoCliente tipoCliente) {
        this(identificacion, nombre, telefono, correo, tipoCliente, PlanMembresia.FREE);
    }

    public Cliente(String identificacion, String nombre, String telefono, String correo,
                   TipoCliente tipoCliente, PlanMembresia plan) {
        super(identificacion, nombre);
        this.telefono = telefono;
        this.correo = correo;
        this.tipoCliente = tipoCliente;
        this.plan = plan;
        this.listVentas = new ArrayList<>();
    }

    /** Devuelve "" en clientes guardados antes de existir este dato. */
    public String getTelefono() {
        return telefono == null ? "" : telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** Devuelve "" en clientes guardados antes de existir este dato. */
    public String getCorreo() {
        return correo == null ? "" : correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public PlanMembresia getPlan() {
        return plan;
    }

    public void setPlan(PlanMembresia plan) {
        this.plan = plan;
    }

    @Override
    public void agregarVenta(Venta venta) {
        listVentas.add(venta);
    }

    @Override
    public List<Venta> getListVentas() {
        return listVentas;
    }

    // Así se muestra en los ComboBox de la interfaz
    @Override
    public String toString() {
        return getNombre() + " (" + getIdentificacion() + ") - Plan " + plan.getNombre();
    }
}
