package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cliente;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.EstadoVenta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Sala;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Venta;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.DatoInvalidoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de la pantalla Gestionar funciones (administrador).
 */
public class GestionFuncionesController {

    private final Cine cine;

    public GestionFuncionesController() {
        cine = ModelFactoryController.getInstance().getCine();
    }

    public void programarFuncion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha,
                                 String horaTexto, String valorBaseTexto) throws CinemaException {
        if (pelicula == null) {
            throw new DatoInvalidoException("Debe seleccionar la película.");
        }
        if (sala == null) {
            throw new DatoInvalidoException("Debe seleccionar la sala.");
        }
        if (fecha == null) {
            throw new DatoInvalidoException("Debe seleccionar la fecha.");
        }
        LocalTime hora = Conversor.aHora(horaTexto);
        double valorBase = Conversor.aDecimalOpcional(valorBaseTexto, "valor base");   // vacío = 0
        cine.programarFuncion(codigo.trim(), pelicula.getCodigo(), sala.getCodigo(), fecha, hora, valorBase);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Cuántas entradas vendidas (activas) tiene la función: se usa para pedir confirmación. */
    public int contarEntradasVendidas(Funcion funcion) {
        return (funcion == null) ? 0 : funcion.contarEntradasVendidas();
    }

    /**
     * Cancela la función (y con ella todas sus ventas activas).
     * Devuelve las ventas que se cancelaron, para saber a quién hay que devolverle el dinero.
     */
    public List<Venta> cancelarFuncion(Funcion funcion) throws CinemaException {
        if (funcion == null) {
            throw new DatoInvalidoException("Seleccione en la tabla la función que desea cancelar.");
        }
        // Se guardan ANTES de cancelar, porque después ya no estarán activas
        List<Venta> afectadas = new ArrayList<>();
        for (Venta venta : funcion.getListVentas()) {
            if (venta.getEstado() == EstadoVenta.ACTIVA) {
                afectadas.add(venta);
            }
        }
        cine.cancelarFuncion(funcion.getCodigo());
        ModelFactoryController.getInstance().guardar();   // persistencia
        return afectadas;
    }

    /**
     * Texto con los reembolsos, agrupado por cliente: datos de contacto, asientos y cuánto devolverle.
     * El valor pagado incluye los combos.
     */
    public String generarReembolsos(Funcion funcion, List<Venta> canceladas) {
        double totalGeneral = 0;
        for (Venta venta : canceladas) {
            totalGeneral += venta.getValorPagado();
        }

        String texto = "Función " + funcion.getCodigo() + " - " + funcion.getPelicula().getNombre()
                + " - " + funcion.getFecha() + " " + funcion.getHora() + " - " + funcion.getSala().getNombre() + "\n"
                + "Entradas canceladas: " + canceladas.size()
                + "      Total a devolver: " + Conversor.aPesos(totalGeneral) + "\n";

        // Se recorren los clientes uno por uno (sin repetir) y se juntan sus ventas
        List<Cliente> yaListados = new ArrayList<>();
        for (Venta venta : canceladas) {
            Cliente cliente = venta.getCliente();
            if (yaListados.contains(cliente)) {
                continue;
            }
            yaListados.add(cliente);

            String asientos = "";
            String codigos = "";
            double aDevolver = 0;
            for (Venta otra : canceladas) {
                if (otra.getCliente() == cliente) {
                    asientos += (asientos.isEmpty() ? "" : ", ") + otra.getAsiento().getCodigo();
                    codigos += (codigos.isEmpty() ? "" : ", ") + otra.getCodigo();
                    aDevolver += otra.getValorPagado();
                }
            }

            texto += "\n" + cliente.getNombre() + "  (identificación " + cliente.getIdentificacion() + ")\n"
                    + "   Teléfono: " + (cliente.getTelefono().isEmpty() ? "sin registrar" : cliente.getTelefono())
                    + "     Correo: " + (cliente.getCorreo().isEmpty() ? "sin registrar" : cliente.getCorreo()) + "\n"
                    + "   Asientos: " + asientos + "   (ventas " + codigos + ")\n"
                    + "   A devolver: " + Conversor.aPesos(aDevolver) + "\n";
        }
        return texto;
    }

    /** Modifica la función con los datos del formulario (solo si no tiene ventas). */
    public void modificarFuncion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha,
                                 String horaTexto, String valorBaseTexto) throws CinemaException {
        if (codigo.trim().isEmpty()) {
            throw new DatoInvalidoException("Seleccione en la tabla la función que desea modificar.");
        }
        if (pelicula == null) {
            throw new DatoInvalidoException("Debe seleccionar la película.");
        }
        if (sala == null) {
            throw new DatoInvalidoException("Debe seleccionar la sala.");
        }
        if (fecha == null) {
            throw new DatoInvalidoException("Debe seleccionar la fecha.");
        }
        LocalTime hora = Conversor.aHora(horaTexto);
        double valorBase = Conversor.aDecimalOpcional(valorBaseTexto, "valor base");
        cine.modificarFuncion(codigo.trim(), pelicula.getCodigo(), sala.getCodigo(), fecha, hora, valorBase);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    /** Cambia el valor base (recargo) de la función seleccionada en la tabla. */
    public void modificarValorBase(Funcion funcion, String valorBaseTexto) throws CinemaException {
        if (funcion == null) {
            throw new DatoInvalidoException("Seleccione en la tabla la función a la que le quiere cambiar el valor base.");
        }
        double valorBase = Conversor.aDecimalOpcional(valorBaseTexto, "valor base");
        cine.modificarValorBase(funcion.getCodigo(), valorBase);
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public void eliminarFuncion(Funcion funcion) throws CinemaException {
        if (funcion == null) {
            throw new DatoInvalidoException("Seleccione en la tabla la función que desea eliminar.");
        }
        cine.eliminarFuncion(funcion.getCodigo());
        ModelFactoryController.getInstance().guardar();   // persistencia
    }

    public List<Funcion> obtenerFunciones() {
        return cine.consultarFunciones();
    }

    /** Todas las películas (si se elige una deshabilitada, el modelo lanza la excepción). */
    public List<Pelicula> obtenerPeliculas() {
        return cine.consultarPeliculas();
    }

    /** Todas las salas (si se elige una fuera de servicio, el modelo lanza la excepción). */
    public List<Sala> obtenerSalas() {
        return cine.consultarSalas();
    }
}
