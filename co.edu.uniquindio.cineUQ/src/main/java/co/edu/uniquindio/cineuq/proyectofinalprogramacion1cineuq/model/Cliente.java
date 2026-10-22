package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;



/**
 * Cliente que adquiere entradas en el cinema.
 */
public class Cliente extends Persona {

    private TipoCliente tipoCliente;

    public Cliente(String identificacion, String nombre, TipoCliente tipoCliente) {
        super(identificacion, nombre);
        this.tipoCliente = tipoCliente;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    @Override
    public String toString() {
        return "Cliente{" + super.toString() + ", "
                + "tipoCliente=" + tipoCliente
                + "}";
    }
}
