package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatosPrueba {

//crear cine

    public static Cine crearCine(){
        return new Cine(
                "Cine UQ",
                "1234",
                "cineuq@virtual.com",
                "321556",
                "CL 23N CR 12 - ARMENIA-QUINDIO"
        );
    }

//crear cliente

    public static Cliente cliente(String identificacion, String nombre, String telefono, String correo, TipoCliente tipoCliente, PlanMembresia planMembresia){
        return new Cliente(
                identificacion,
                nombre,
                telefono,
                correo,
                tipoCliente,
                planMembresia
        );
    }

    //crear pelicula

    public static Pelicula pelicula(String codigo, String nombre, Genero genero, int duracionMinutos,
                                    Clasificacion clasificacion, List<Formato> formatos, int anioEstreno, EstadoPelicula estado){
        return new Pelicula(
                codigo,
                nombre,
                genero,
                duracionMinutos,
                clasificacion,
                formatos,
                anioEstreno,
                estado
        );
    }
//crear sala

    public static Sala sala(String codigo, String nombre, int capacidad, EstadoSala estado){
        return new Sala(
                codigo,
                nombre,
                capacidad,
                EstadoSala.DISPONIBLE
        );
    }
//crear operador

    public static Operador operador(String identificacion, String nombre, String usuario, String contrasena){
        return new Operador(
                identificacion,
                nombre,
                usuario,
                contrasena
        );
    }
//crear funcion
    public static Funcion funcion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase,
                           Formato formato, Idioma idioma){
        return new Funcion(
        codigo,
        pelicula,
        sala,
        fecha,
        hora,
        valorBase,
        formato,
        idioma
        );
    }

//crear venta
public static Venta venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento) {
    return new Venta(codigo, cliente, funcion, asiento, TipoEntrada.GENERAL,
            null, LocalDateTime.now(), 10000, funcion.getValorBase(),
            10000, new ArrayList<>(), 0);
    }

    static Pelicula pelicula(String codigo, String nombre) {
        List<Formato> formatos = new ArrayList<>();
        formatos.add(Formato.DOS_D);
        formatos.add(Formato.IMAX);
        return new Pelicula(codigo, nombre, Genero.CIENCIA_FICCION, 120,
                Clasificacion.MAYORES_12, formatos, LocalDate.now().getYear(), EstadoPelicula.DISPONIBLE);
    }
    public static LocalDate manana() {
        return LocalDate.now().plusDays(1);
    }


}
