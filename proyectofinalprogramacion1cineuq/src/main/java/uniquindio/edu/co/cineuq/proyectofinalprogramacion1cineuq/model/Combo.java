package uniquindio.edu.co.cineuq.proyectofinalprogramacion1cineuq.model;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Habilitable;

import java.io.Serializable;

/**
 * Combo de comida que se puede agregar a una venta (crispetas, gaseosas, nachos...).
 * El administrador los gestiona (CRUD) desde Gestionar funciones -> Gestionar combos,
 * igual que las películas: con código, nombre, contenido, precio y foto.
 * Un combo deshabilitado no aparece en la venta, pero sigue en las ventas anteriores.
 */
public class Combo implements Habilitable, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private String descripcion;   // qué trae el combo, ej.: "Crispetas medianas + gaseosa mediana"
    private double precio;
    private String rutaImagen;    // foto del combo (puede ser null)
    private boolean disponible;

    public Combo(String codigo, String nombre, String descripcion, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = true;
    }

    @Override
    public boolean estaDisponible() {
        return disponible;
    }

    @Override
    public void habilitar() {
        disponible = true;
    }

    @Override
    public void deshabilitar() {
        disponible = false;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
