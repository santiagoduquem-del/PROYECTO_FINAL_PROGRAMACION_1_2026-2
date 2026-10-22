package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del sistema: agrupa salas, películas, funciones, clientes, operarios y ventas.
 */
public class Cine {

    private String nombre;
    private String nit;
    private String correo;
    private String telefono;
    private String direccion;

    // Relaciones
    private List<Sala> listSalas;
    private List<Pelicula> listPeliculas;
    private List<Funcion> listFunciones;
    private List<Cliente> listClientes;
    private List<Operador> listOperarios;
    private List<Venta> listVentas;

    public Cine(String nombre) {
        this.nombre = nombre;
        this.listSalas = new ArrayList<>();
        this.listPeliculas = new ArrayList<>();
        this.listFunciones = new ArrayList<>();
        this.listClientes = new ArrayList<>();
        this.listOperarios = new ArrayList<>();
        this.listVentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Sala> getListSalas() {
        return listSalas;
    }

    public void setListSalas(List<Sala> listSalas) {
        this.listSalas = listSalas;
    }

    public List<Pelicula> getListPeliculas() {
        return listPeliculas;
    }

    public void setListPeliculas(List<Pelicula> listPeliculas) {
        this.listPeliculas = listPeliculas;
    }

    public List<Funcion> getListFunciones() {
        return listFunciones;
    }

    public void setListFunciones(List<Funcion> listFunciones) {
        this.listFunciones = listFunciones;
    }

    public List<Cliente> getListClientes() {
        return listClientes;
    }

    public void setListClientes(List<Cliente> listClientes) {
        this.listClientes = listClientes;
    }

    public List<Operador> getListOperarios() {
        return listOperarios;
    }

    public void setListOperarios(List<Operador> listOperarios) {
        this.listOperarios = listOperarios;
    }

    public List<Venta> getListVentas() {
        return listVentas;
    }

    public void setListVentas(List<Venta> listVentas) {
        this.listVentas = listVentas;
    }

    @Override
    public String toString() {
        return "Cine{nombre='" + nombre + "', listSalas=" + listSalas.size()
                + ", listPeliculas=" + listPeliculas.size() + ", listFunciones=" + listFunciones.size()
                + ", listClientes=" + listClientes.size() + ", listOperarios=" + listOperarios.size()
                + ", listVentas=" + listVentas.size() + "}";
    }
}
