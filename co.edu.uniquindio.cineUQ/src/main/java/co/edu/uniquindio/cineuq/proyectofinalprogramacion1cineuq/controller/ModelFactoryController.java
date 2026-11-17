package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda el ÚNICO objeto Cine de toda la aplicación y los datos que comparten las pantallas:
 *  - el empleado que inició sesión
 *  - el cliente que se está registrando (entre Sign Up y la pantalla de planes)
 *  - los combos elegidos para la venta en curso (entre la venta y la ventana de combos)
 *  - el formato que se eligió para ver su información (entre la barra / Más formatos y el detalle)
 *
 * Usa el patrón Singleton: solo existe una instancia, que se obtiene con getInstance().
 * Los datos se guardan en un archivo (clase Persistencia), así que no se pierden al cerrar el programa.
 */
public class ModelFactoryController {

    private static ModelFactoryController instancia;

    private Cine cine;
    private Empleado empleadoActual;
    private Cliente clienteEnRegistro;
    private List<Combo> combosSeleccionados;
    private Formato formatoSeleccionado;
    private Pelicula peliculaSeleccionada;   // la que se eligió en la cartelera
    private Pelicula peliculaHorarios;       // película cuyos horarios se muestran (null = todas)
    private Funcion funcionParaVenta;        // función elegida en Horarios: Generar venta la trae seleccionada
    private Funcion funcionEnCombos;         // función de la venta, para mostrar los combos que aplican
    private double totalAPagar;              // total de la compra que se va a pagar con tarjeta
    private Cliente clienteEnPago;           // cliente de la compra (para sugerir el titular)
    private Pago pagoRealizado;              // pago aprobado en la ventana de pago (null = no se pagó)

    // Constructor privado: nadie puede hacer "new ModelFactoryController()" desde afuera
    private ModelFactoryController() {
        combosSeleccionados = new ArrayList<>();

        // PERSISTENCIA: se cargan los datos guardados la última vez que se usó el programa
        cine = Persistencia.cargarCine();

        // Si no hay datos guardados (primera vez), se crean los datos de prueba y se guardan
        if (cine == null) {
            cine = new Cine("CinemaUQ", "900123456-7", "contacto@cinemauq.co", "6067000000", "Armenia, Quindío");
            cargarDatosDePrueba();
            guardar();
        } else {
            // Datos guardados con versiones anteriores: se ponen al día y se guardan
            boolean salasActualizadas = cine.actualizarDistribucionSalas();   // salas grandes con filas más anchas
            boolean combosActualizados = cine.actualizarCombosAnteriores();   // combos fijos -> combos del CRUD
            if (salasActualizadas || combosActualizados) {
                guardar();
            }
        }
    }

    /** Guarda en el archivo todos los datos del cine. Los controllers lo llaman después de cada cambio. */
    public void guardar() {
        Persistencia.guardarCine(cine);
    }

    public static ModelFactoryController getInstance() {
        if (instancia == null) {
            instancia = new ModelFactoryController();
        }
        return instancia;
    }

    public Cine getCine() {
        return cine;
    }

    public Empleado getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleado empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public Cliente getClienteEnRegistro() {
        return clienteEnRegistro;
    }

    public void setClienteEnRegistro(Cliente clienteEnRegistro) {
        this.clienteEnRegistro = clienteEnRegistro;
    }

    public List<Combo> getCombosSeleccionados() {
        return combosSeleccionados;
    }

    public Formato getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(Formato formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
    }

    public Pelicula getPeliculaSeleccionada() {
        return peliculaSeleccionada;
    }

    public void setPeliculaSeleccionada(Pelicula peliculaSeleccionada) {
        this.peliculaSeleccionada = peliculaSeleccionada;
    }

    public Pelicula getPeliculaHorarios() {
        return peliculaHorarios;
    }

    public void setPeliculaHorarios(Pelicula peliculaHorarios) {
        this.peliculaHorarios = peliculaHorarios;
    }

    public Funcion getFuncionParaVenta() {
        return funcionParaVenta;
    }

    public void setFuncionParaVenta(Funcion funcionParaVenta) {
        this.funcionParaVenta = funcionParaVenta;
    }

    public Funcion getFuncionEnCombos() {
        return funcionEnCombos;
    }

    public void setFuncionEnCombos(Funcion funcionEnCombos) {
        this.funcionEnCombos = funcionEnCombos;
    }

    public double getTotalAPagar() {
        return totalAPagar;
    }

    public void setTotalAPagar(double totalAPagar) {
        this.totalAPagar = totalAPagar;
    }

    public Cliente getClienteEnPago() {
        return clienteEnPago;
    }

    public void setClienteEnPago(Cliente clienteEnPago) {
        this.clienteEnPago = clienteEnPago;
    }

    public Pago getPagoRealizado() {
        return pagoRealizado;
    }

    public void setPagoRealizado(Pago pagoRealizado) {
        this.pagoRealizado = pagoRealizado;
    }

    /** Datos iniciales para poder usar la aplicación desde el primer momento. */
    private void cargarDatosDePrueba() {
        try {
            cine.registrarEmpleado(new Administrador("1001", "Administrador", "administrador", "admin123"));
            cine.registrarEmpleado(new Operador("1002", "Operador", "operador", "operador123"));

            cine.registrarSala(new Sala("S1", "Sala 1 Dolby", 50, EstadoSala.DISPONIBLE));
            cine.registrarSala(new Sala("S2", "Sala 2 IMAX", 80, EstadoSala.DISPONIBLE));
            cine.registrarSala(new Sala("S3", "Sala 3", 40, EstadoSala.FUERA_DE_SERVICIO));

            cine.registrarPelicula(new Pelicula("P1", "El Último Algoritmo", Genero.CIENCIA_FICCION, 125,
                    Clasificacion.MAYORES_12, List.of(Formato.DOS_D, Formato.IMAX, Formato.INFINITY_VISION), 2026, EstadoPelicula.DISPONIBLE));
            cine.registrarPelicula(new Pelicula("P2", "Aventuras en el Quindío", Genero.ANIMACION, 95,
                    Clasificacion.TODOS, List.of(Formato.DOS_D, Formato.TRES_D), 2025, EstadoPelicula.DISPONIBLE));
            cine.registrarPelicula(new Pelicula("P3", "Noche de Terror", Genero.TERROR, 110,
                    Clasificacion.MAYORES_18, List.of(Formato.CUATRO_DX), 1998, EstadoPelicula.DISPONIBLE));

            cine.registrarCliente(new Cliente("2001", "Laura Gómez", "3104567890", "laura.gomez@correo.com",
                    TipoCliente.ESTUDIANTE, PlanMembresia.ESTANDAR));
            cine.registrarCliente(new Cliente("2002", "Carlos Pérez", "3159876543", "carlos.perez@correo.com",
                    TipoCliente.GENERAL, PlanMembresia.FREE));
            cine.registrarCliente(new Cliente("2003", "Martha Ríos", "3201234567", "martha.rios@correo.com",
                    TipoCliente.ADULTO_MAYOR, PlanMembresia.PREMIUM));

            LocalDate hoy = LocalDate.now();
            cine.programarFuncion("F1", "P1", "S1", hoy, LocalTime.of(14, 0), 0,
                    Formato.DOS_D, Idioma.DOBLADA);                                      // 2D doblada
            cine.programarFuncion("F2", "P1", "S2", hoy, LocalTime.of(18, 30), 8000,
                    Formato.IMAX, Idioma.SUBTITULADA);                                   // estreno IMAX
            cine.programarFuncion("F3", "P2", "S1", hoy, LocalTime.of(17, 0), 3000,
                    Formato.TRES_D, Idioma.DOBLADA);                                     // 3D
            cine.programarFuncion("F4", "P3", "S2", hoy.plusDays(1), LocalTime.of(21, 0), 6000,
                    Formato.CUATRO_DX, Idioma.SUBTITULADA);                              // 4DX
        } catch (CinemaException e) {
            System.err.println("Error cargando datos de prueba: " + e.getMessage());
        }
    }
}
