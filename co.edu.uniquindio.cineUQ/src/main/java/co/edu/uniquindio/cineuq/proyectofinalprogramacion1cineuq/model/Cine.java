package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.*;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal del sistema: agrupa salas, películas, funciones, clientes, empleados y ventas,
 * e implementa todas las operaciones de negocio.
 */
public class Cine implements GestionPeliculas, GestionSalas, GestionFunciones,
        GestionPersonas, GestionVentas, GestionTarifas, GestionCombos, Reportes, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

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
    private List<Empleado> listEmpleados;   // operadores y administradores
    private List<Venta> listVentas;
    private List<Tarifa> listTarifas;
    private List<Membresia> listMembresias;
    private List<Combo> listCombos;

    // Contador para generar los códigos de venta: V1, V2, V3...
    private int consecutivoVentas;

    public Cine(String nombre, String nit, String correo, String telefono, String direccion) {
        this.nombre = nombre;
        this.nit = nit;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.listSalas = new ArrayList<>();
        this.listPeliculas = new ArrayList<>();
        this.listFunciones = new ArrayList<>();
        this.listClientes = new ArrayList<>();
        this.listEmpleados = new ArrayList<>();
        this.listVentas = new ArrayList<>();
        this.consecutivoVentas = 0;

        // Una tarifa por cada tipo de entrada, con su valor por defecto
        this.listTarifas = new ArrayList<>();
        for (TipoEntrada tipo : TipoEntrada.values()) {
            listTarifas.add(new Tarifa(tipo, tipo.getValorPorDefecto()));
        }
        crearMembresiasPorDefecto();
        crearCombosPorDefecto();
    }

    // =====================================================================
    // Validaciones de apoyo
    // =====================================================================

    private void validarTexto(String texto, String campo) throws DatoInvalidoException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DatoInvalidoException("El campo " + campo + " es obligatorio.");
        }
    }

    /** El año debe estar entre 1895 (primera proyección de cine) y dos años después del actual (preventas). */
    private void validarAnioEstreno(int anio) throws DatoInvalidoException {
        int maximo = LocalDate.now().getYear() + 2;
        if (anio < 1895 || anio > maximo) {
            throw new DatoInvalidoException("El año de estreno debe estar entre 1895 y " + maximo + ".");
        }
    }

    private boolean salaTieneFuncionesProgramadas(Sala sala) {
        for (Funcion funcion : listFunciones) {
            if (funcion.getSala() == sala && funcion.getEstado() == EstadoFuncion.PROGRAMADA) {
                return true;
            }
        }
        return false;
    }

    // =====================================================================
    // Películas
    // =====================================================================

    @Override
    public void registrarPelicula(Pelicula pelicula) throws PeliculaDuplicadaException, DatoInvalidoException {
        validarTexto(pelicula.getCodigo(), "código");
        validarTexto(pelicula.getNombre(), "nombre");
        if (pelicula.getDuracionMinutos() <= 0) {
            throw new DatoInvalidoException("La duración debe ser mayor que cero.");
        }
        if (pelicula.getFormatos().isEmpty()) {
            throw new DatoInvalidoException("Debe seleccionar al menos un formato para la película.");
        }
        validarAnioEstreno(pelicula.getAnioEstreno());
        for (Pelicula p : listPeliculas) {
            if (p.getCodigo().equals(pelicula.getCodigo())) {
                throw new PeliculaDuplicadaException(pelicula.getCodigo());
            }
        }
        listPeliculas.add(pelicula);
    }

    @Override
    public Pelicula buscarPelicula(String codigo) throws PeliculaNoEncontradaException {
        for (Pelicula pelicula : listPeliculas) {
            if (pelicula.getCodigo().equals(codigo)) {
                return pelicula;
            }
        }
        throw new PeliculaNoEncontradaException(codigo);
    }

    @Override
    public List<Pelicula> consultarPeliculas() {
        return listPeliculas;
    }

    @Override
    public List<Pelicula> consultarPeliculasDisponibles() {
        List<Pelicula> disponibles = new ArrayList<>();
        for (Pelicula pelicula : listPeliculas) {
            if (pelicula.estaDisponible()) {
                disponibles.add(pelicula);
            }
        }
        return disponibles;
    }

    @Override
    public void modificarPelicula(String codigo, String nombre, Genero genero, int duracionMinutos,
                                  Clasificacion clasificacion, List<Formato> formatos, int anioEstreno)
            throws PeliculaNoEncontradaException, DatoInvalidoException, ModificacionNoPermitidaException {
        Pelicula pelicula = buscarPelicula(codigo);
        validarTexto(nombre, "nombre");
        if (duracionMinutos <= 0) {
            throw new DatoInvalidoException("La duración debe ser mayor que cero.");
        }
        if (formatos == null || formatos.isEmpty()) {
            throw new DatoInvalidoException("Debe seleccionar al menos un formato para la película.");
        }
        validarAnioEstreno(anioEstreno);
        // No se puede quitar un formato que una función programada está usando
        for (Funcion funcion : listFunciones) {
            if (funcion.getPelicula() == pelicula && funcion.getEstado() == EstadoFuncion.PROGRAMADA
                    && !formatos.contains(funcion.getFormato())) {
                throw new ModificacionNoPermitidaException("No se puede quitar el formato " + funcion.getFormato()
                        + " porque la función " + funcion.getCodigo() + " está programada en ese formato.");
            }
        }
        pelicula.setNombre(nombre);
        pelicula.setGenero(genero);
        pelicula.setDuracionMinutos(duracionMinutos);
        pelicula.setClasificacion(clasificacion);
        pelicula.setFormatos(formatos);
        pelicula.setAnioEstreno(anioEstreno);
    }

    @Override
    public void asignarImagenesPelicula(String codigo, String rutaCaratula, String rutaBanner)
            throws PeliculaNoEncontradaException {
        Pelicula pelicula = buscarPelicula(codigo);
        pelicula.setRutaCaratula(rutaCaratula);
        pelicula.setRutaBanner(rutaBanner);
    }

    /** Sinopsis, elenco principal y estudio de la película (son opcionales: pueden quedar vacíos). */
    public void asignarInformacionPelicula(String codigo, String sinopsis, String elenco, String estudio)
            throws PeliculaNoEncontradaException {
        Pelicula pelicula = buscarPelicula(codigo);
        pelicula.setSinopsis(sinopsis == null ? "" : sinopsis.trim());
        pelicula.setElenco(elenco == null ? "" : elenco.trim());
        pelicula.setEstudio(estudio == null ? "" : estudio.trim());
    }

    // ---------------- Secciones del detalle de la película ----------------

    /** Una sección necesita al menos título o texto (la etiqueta y la foto son opcionales). */
    private void validarSeccion(String titulo, String texto) throws DatoInvalidoException {
        boolean sinTitulo = (titulo == null || titulo.isBlank());
        boolean sinTexto = (texto == null || texto.isBlank());
        if (sinTitulo && sinTexto) {
            throw new DatoInvalidoException("La sección necesita al menos un título o un texto.");
        }
    }

    /** Revisa que la posición exista en la lista de secciones de la película (0 = la primera). */
    private void validarPosicionSeccion(Pelicula pelicula, int posicion) throws DatoInvalidoException {
        if (posicion < 0 || posicion >= pelicula.getListSecciones().size()) {
            throw new DatoInvalidoException("Seleccione una sección de la tabla.");
        }
    }

    private String limpiar(String texto) {
        return (texto == null) ? "" : texto.trim();
    }

    /** Agrega la sección al final de la lista de la película. */
    public void agregarSeccionPelicula(String codigoPelicula, String etiqueta, String titulo, String texto,
                                       String rutaImagen) throws PeliculaNoEncontradaException, DatoInvalidoException {
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        validarSeccion(titulo, texto);
        pelicula.getListSecciones().add(new SeccionPelicula(limpiar(etiqueta), limpiar(titulo), limpiar(texto), rutaImagen));
    }

    public void modificarSeccionPelicula(String codigoPelicula, int posicion, String etiqueta, String titulo,
                                         String texto, String rutaImagen)
            throws PeliculaNoEncontradaException, DatoInvalidoException {
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        validarPosicionSeccion(pelicula, posicion);
        validarSeccion(titulo, texto);
        SeccionPelicula seccion = pelicula.getListSecciones().get(posicion);
        seccion.setEtiqueta(limpiar(etiqueta));
        seccion.setTitulo(limpiar(titulo));
        seccion.setTexto(limpiar(texto));
        seccion.setRutaImagen(rutaImagen);
    }

    public void eliminarSeccionPelicula(String codigoPelicula, int posicion)
            throws PeliculaNoEncontradaException, DatoInvalidoException {
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        validarPosicionSeccion(pelicula, posicion);
        pelicula.getListSecciones().remove(posicion);
    }

    /**
     * Cambia el orden de una sección: desplazamiento -1 = la sube un puesto, +1 = la baja.
     * Devuelve la nueva posición.
     */
    public int moverSeccionPelicula(String codigoPelicula, int posicion, int desplazamiento)
            throws PeliculaNoEncontradaException, DatoInvalidoException {
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        validarPosicionSeccion(pelicula, posicion);
        List<SeccionPelicula> secciones = pelicula.getListSecciones();
        int nueva = posicion + desplazamiento;
        if (nueva < 0 || nueva >= secciones.size()) {
            throw new DatoInvalidoException(desplazamiento < 0 ? "La sección ya es la primera." : "La sección ya es la última.");
        }
        SeccionPelicula seccion = secciones.remove(posicion);
        secciones.add(nueva, seccion);
        return nueva;
    }

    /** Solo se puede eliminar si nunca se le programaron funciones (si no, se debe deshabilitar). */
    @Override
    public void eliminarPelicula(String codigo) throws PeliculaNoEncontradaException, EliminacionNoPermitidaException {
        Pelicula pelicula = buscarPelicula(codigo);
        if (!pelicula.getListFunciones().isEmpty()) {
            throw new EliminacionNoPermitidaException("No se puede eliminar la película " + codigo
                    + " porque tiene funciones registradas. Puede deshabilitarla.");
        }
        listPeliculas.remove(pelicula);
    }

    @Override
    public void habilitarPelicula(String codigo) throws PeliculaNoEncontradaException {
        buscarPelicula(codigo).habilitar();
    }

    @Override
    public void deshabilitarPelicula(String codigo) throws PeliculaNoEncontradaException {
        buscarPelicula(codigo).deshabilitar();
    }

    // =====================================================================
    // Salas
    // =====================================================================

    @Override
    public void registrarSala(Sala sala) throws SalaDuplicadaException, DatoInvalidoException {
        validarTexto(sala.getCodigo(), "código");
        validarTexto(sala.getNombre(), "nombre");
        validarCapacidad(sala.getCapacidad());
        for (Sala s : listSalas) {
            if (s.getCodigo().equals(sala.getCodigo())) {
                throw new SalaDuplicadaException(sala.getCodigo());
            }
        }
        listSalas.add(sala);
    }

    /** La capacidad va de 1 a 1000 asientos. */
    private void validarCapacidad(int capacidad) throws DatoInvalidoException {
        if (capacidad <= 0) {
            throw new DatoInvalidoException("La capacidad debe ser mayor que cero.");
        }
        if (capacidad > Sala.CAPACIDAD_MAXIMA) {
            throw new DatoInvalidoException("La capacidad máxima de una sala es " + Sala.CAPACIDAD_MAXIMA + " asientos.");
        }
    }

    /**
     * Para datos guardados con versiones anteriores: las salas de más de 260 asientos
     * se reorganizan con filas más anchas, y sus funciones SIN ventas copian los asientos nuevos.
     * Devuelve true si cambió algo (para guardar).
     */
    public boolean actualizarDistribucionSalas() {
        boolean cambio = false;
        for (Sala sala : listSalas) {
            if (sala.getCapacidad() <= Sala.CAPACIDAD_MAXIMA && !sala.tieneDistribucionActual()) {
                sala.reorganizarAsientos();
                cambio = true;
            }
        }
        for (Funcion funcion : listFunciones) {
            Sala sala = funcion.getSala();
            if (funcion.getAsientos().size() == sala.getAsientos().size() && !mismaDistribucion(funcion, sala)) {
                if (funcion.reconstruirAsientos()) {
                    cambio = true;
                }
            }
        }
        return cambio;
    }

    private boolean mismaDistribucion(Funcion funcion, Sala sala) {
        for (int i = 0; i < sala.getAsientos().size(); i++) {
            if (!funcion.getAsientos().get(i).getCodigo().equals(sala.getAsientos().get(i).getCodigo())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Sala buscarSala(String codigo) throws SalaNoEncontradaException {
        for (Sala sala : listSalas) {
            if (sala.getCodigo().equals(codigo)) {
                return sala;
            }
        }
        throw new SalaNoEncontradaException(codigo);
    }

    @Override
    public List<Sala> consultarSalas() {
        return listSalas;
    }

    @Override
    public void modificarSala(String codigo, String nombre, int capacidad)
            throws SalaNoEncontradaException, DatoInvalidoException {
        Sala sala = buscarSala(codigo);
        validarTexto(nombre, "nombre");
        validarCapacidad(capacidad);
        if (capacidad != sala.getCapacidad() && salaTieneFuncionesProgramadas(sala)) {
            throw new DatoInvalidoException(
                    "No se puede cambiar la capacidad de una sala que tiene funciones programadas.");
        }
        sala.setNombre(nombre);
        if (capacidad != sala.getCapacidad()) {
            sala.setCapacidad(capacidad);   // vuelve a generar los asientos
        }
    }

    /** Solo se puede eliminar si nunca se programaron funciones en ella (si no, se debe dejar fuera de servicio). */
    @Override
    public void eliminarSala(String codigo) throws SalaNoEncontradaException, EliminacionNoPermitidaException {
        Sala sala = buscarSala(codigo);
        for (Funcion funcion : listFunciones) {
            if (funcion.getSala() == sala) {
                throw new EliminacionNoPermitidaException("No se puede eliminar la sala " + codigo
                        + " porque tiene funciones registradas. Puede dejarla fuera de servicio.");
            }
        }
        listSalas.remove(sala);
    }

    @Override
    public void habilitarSala(String codigo) throws SalaNoEncontradaException {
        buscarSala(codigo).habilitar();
    }

    @Override
    public void deshabilitarSala(String codigo) throws SalaNoEncontradaException {
        buscarSala(codigo).deshabilitar();
    }

    // =====================================================================
    // Funciones
    // =====================================================================

    @Override
    public Funcion programarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha,
                                    LocalTime hora, double valorBase, Formato formato, Idioma idioma)
            throws FuncionDuplicadaException, PeliculaNoEncontradaException, PeliculaNoDisponibleException,
                   SalaNoEncontradaException, SalaNoDisponibleException, ConflictoHorarioException,
                   DatoInvalidoException {

        validarTexto(codigo, "código");
        if (fecha == null || hora == null) {
            throw new DatoInvalidoException("Debe indicar la fecha y la hora de la función.");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new DatoInvalidoException("No se pueden programar funciones en fechas pasadas.");
        }
        // El valor base es un recargo opcional (estrenos o formatos especiales): puede ser 0
        if (valorBase < 0) {
            throw new DatoInvalidoException("El valor base no puede ser negativo (déjelo vacío o en 0 si no aplica).");
        }
        for (Funcion f : listFunciones) {
            if (f.getCodigo().equals(codigo)) {
                throw new FuncionDuplicadaException(codigo);
            }
        }

        Pelicula pelicula = buscarPelicula(codigoPelicula);
        if (!pelicula.estaDisponible()) {
            throw new PeliculaNoDisponibleException(codigoPelicula);
        }

        // El formato debe ser uno de los formatos en que está disponible la película
        if (formato == null) {
            throw new DatoInvalidoException("Debe seleccionar el formato de la función.");
        }
        if (!pelicula.tieneFormato(formato)) {
            throw new DatoInvalidoException("La película " + pelicula.getNombre() + " no está disponible en "
                    + formato + ". Formatos de la película: " + pelicula.describirFormatos() + ".");
        }
        if (idioma == null) {
            throw new DatoInvalidoException("Debe indicar si la función es doblada o subtitulada.");
        }

        Sala sala = buscarSala(codigoSala);
        if (!sala.estaDisponible()) {
            throw new SalaNoDisponibleException(codigoSala);
        }

        Funcion nueva = new Funcion(codigo, pelicula, sala, fecha, hora, valorBase, formato, idioma);

        // Verificar que no se cruce con otra función programada en la misma sala.
        // Cada función ocupa la sala desde su inicio hasta el fin de la película + 15 min de limpieza.
        // Dos horarios se cruzan si cada uno empieza antes de que el otro deje libre la sala.
        // Se compara con fecha Y hora, así se detectan también las funciones que pasan de medianoche.
        for (Funcion existente : listFunciones) {
            boolean mismaSala = existente.getSala() == sala;
            boolean programada = existente.getEstado() == EstadoFuncion.PROGRAMADA;
            if (mismaSala && programada) {
                boolean seCruzan = nueva.getInicio().isBefore(existente.getSalaLibreDesde())
                        && existente.getInicio().isBefore(nueva.getSalaLibreDesde());
                if (seCruzan) {
                    throw new ConflictoHorarioException(codigoSala, existente);
                }
            }
        }

        listFunciones.add(nueva);
        pelicula.agregarFuncion(nueva);
        return nueva;
    }

    @Override
    public Funcion buscarFuncion(String codigo) throws FuncionNoEncontradaException {
        for (Funcion funcion : listFunciones) {
            if (funcion.getCodigo().equals(codigo)) {
                return funcion;
            }
        }
        throw new FuncionNoEncontradaException(codigo);
    }

    @Override
    public List<Funcion> consultarFunciones() {
        return listFunciones;
    }

    @Override
    public List<Funcion> consultarFuncionesProgramadas() {
        List<Funcion> programadas = new ArrayList<>();
        for (Funcion funcion : listFunciones) {
            if (funcion.getEstado() == EstadoFuncion.PROGRAMADA) {
                programadas.add(funcion);
            }
        }
        return programadas;
    }

    @Override
    public List<Funcion> consultarFuncionesProgramadas(String codigoPelicula) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion funcion : consultarFuncionesProgramadas()) {
            if (funcion.getPelicula().getCodigo().equals(codigoPelicula)) {
                resultado.add(funcion);
            }
        }
        return resultado;
    }

    @Override
    public List<Funcion> consultarFuncionesPorFecha(LocalDate fecha) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion funcion : listFunciones) {
            if (funcion.getFecha().equals(fecha)) {
                resultado.add(funcion);
            }
        }
        return resultado;
    }

    @Override
    public List<Asiento> consultarAsientos(String codigoFuncion) throws FuncionNoEncontradaException {
        return buscarFuncion(codigoFuncion).getAsientos();
    }

    /**
     * Modifica película, sala, fecha, hora y valor base de una función, solo si está programada
     * y NO tiene ventas (para no cambiarle la función a quien ya compró).
     * Se quita la función vieja y se programa de nuevo con los datos nuevos (con todas las validaciones
     * de programarFuncion: sala disponible, cruces de horario, etc.). Si algo falla, queda la vieja.
     */
    @Override
    public Funcion modificarFuncion(String codigo, String codigoPelicula, String codigoSala, LocalDate fecha,
                                    LocalTime hora, double valorBase, Formato formato, Idioma idioma)
            throws CinemaException {
        Funcion vieja = buscarFuncion(codigo);
        if (vieja.getEstado() != EstadoFuncion.PROGRAMADA) {
            throw new ModificacionNoPermitidaException("Solo se pueden modificar funciones programadas.");
        }
        // Solo cuentan las ventas ACTIVAS: si todas se cancelaron, la función se puede modificar
        if (vieja.contarEntradasVendidas() > 0) {
            throw new ModificacionNoPermitidaException("No se puede modificar la función " + codigo
                    + " porque tiene entradas vendidas activas. Solo puede actualizar su valor base o cancelarla.");
        }

        listFunciones.remove(vieja);
        vieja.getPelicula().removerFuncion(vieja);
        try {
            return programarFuncion(codigo, codigoPelicula, codigoSala, fecha, hora, valorBase, formato, idioma);
        } catch (CinemaException e) {
            // Los datos nuevos no son válidos: se deja la función como estaba
            listFunciones.add(vieja);
            vieja.getPelicula().agregarFuncion(vieja);
            throw e;
        }
    }

    /** Cambia el valor base (recargo) de una función. Las ventas ya hechas conservan lo que se pagó. */
    @Override
    public void modificarValorBase(String codigoFuncion, double nuevoValor)
            throws FuncionNoEncontradaException, DatoInvalidoException {
        if (nuevoValor < 0) {
            throw new DatoInvalidoException("El valor base no puede ser negativo (déjelo vacío o en 0 si no aplica).");
        }
        buscarFuncion(codigoFuncion).setValorBase(nuevoValor);
    }

    /**
     * Cancela la función. Las funciones no se eliminan: quedan en el historial con estado CANCELADA
     * (como en un cine real, por contabilidad y reclamos).
     * Solo se puede cancelar si NO tiene entradas activas: si las tiene, primero se cancelan esas
     * ventas en "Cancelar venta" (así cada cliente recibe su devolución).
     */
    @Override
    public void cancelarFuncion(String codigo)
            throws FuncionNoEncontradaException, FuncionNoDisponibleException, CancelacionNoPermitidaException {
        Funcion funcion = buscarFuncion(codigo);
        int activas = funcion.contarEntradasVendidas();
        if (activas > 0) {
            throw new CancelacionNoPermitidaException("La función " + codigo + " tiene " + activas
                    + " entrada(s) vendida(s) activa(s). Primero cancele esas ventas en \"Cancelar venta\""
                    + " y luego cancele la función.");
        }
        funcion.cancelar();
    }


    // =====================================================================
    // Personas (clientes y empleados)
    // =====================================================================

    @Override
    public void registrarCliente(Cliente cliente) throws ClienteDuplicadoException, DatoInvalidoException {
        validarDatosCliente(cliente);
        if (cliente.getPlan() == null) {
            throw new DatoInvalidoException("Debe seleccionar el plan de membresía.");
        }
        for (Cliente c : listClientes) {
            if (c.getIdentificacion().equals(cliente.getIdentificacion())) {
                throw new ClienteDuplicadoException(cliente.getIdentificacion());
            }
        }
        listClientes.add(cliente);
    }

    /**
     * Revisa los datos del cliente (sin el plan). También la usa Sign Up en el paso 1,
     * para avisar de los errores antes de pasar a la pantalla de planes.
     */
    @Override
    public void validarDatosCliente(Cliente cliente) throws DatoInvalidoException {
        validarTexto(cliente.getIdentificacion(), "identificación");
        validarTexto(cliente.getNombre(), "nombre");
        validarTexto(cliente.getTelefono(), "teléfono");
        validarTexto(cliente.getCorreo(), "correo");

        // Teléfono: solo números, entre 7 y 10 dígitos
        String telefono = cliente.getTelefono().trim();
        if (telefono.length() < 7 || telefono.length() > 10) {
            throw new DatoInvalidoException("El teléfono debe tener entre 7 y 10 dígitos.");
        }
        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                throw new DatoInvalidoException("El teléfono solo puede tener números.");
            }
        }

        // Correo: debe tener una @ y un punto después de ella (ej.: nombre@correo.com)
        String correo = cliente.getCorreo().trim();
        int arroba = correo.indexOf('@');
        if (arroba <= 0 || correo.indexOf('.', arroba) < arroba + 2 || correo.endsWith(".") || correo.contains(" ")) {
            throw new DatoInvalidoException("El correo no es válido (ejemplo: nombre@correo.com).");
        }

        if (cliente.getTipoCliente() == null) {
            throw new DatoInvalidoException("Debe seleccionar el tipo de cliente.");
        }
    }

    @Override
    public Cliente buscarCliente(String identificacion) throws ClienteNoEncontradoException {
        for (Cliente cliente : listClientes) {
            if (cliente.getIdentificacion().equals(identificacion)) {
                return cliente;
            }
        }
        throw new ClienteNoEncontradoException(identificacion);
    }

    @Override
    public List<Cliente> consultarClientes() {
        return listClientes;
    }

    /** Cambia los datos del cliente (la identificación no cambia porque es la que lo identifica). */
    @Override
    public void modificarCliente(String identificacion, String nombre, String telefono, String correo,
                                 TipoCliente tipoCliente, PlanMembresia plan)
            throws ClienteNoEncontradoException, DatoInvalidoException {
        Cliente cliente = buscarCliente(identificacion);
        // Se validan los datos nuevos antes de cambiar nada
        validarDatosCliente(new Cliente(identificacion, nombre, telefono, correo, tipoCliente, plan));
        if (plan == null) {
            throw new DatoInvalidoException("Debe seleccionar el plan de membresía.");
        }
        cliente.setNombre(nombre.trim());
        cliente.setTelefono(telefono.trim());
        cliente.setCorreo(correo.trim());
        cliente.setTipoCliente(tipoCliente);
        cliente.setPlan(plan);
    }

    /** Solo se puede eliminar un cliente que no tenga compras (se necesitan para el historial y los reportes). */
    @Override
    public void eliminarCliente(String identificacion)
            throws ClienteNoEncontradoException, EliminacionNoPermitidaException {
        Cliente cliente = buscarCliente(identificacion);
        if (!cliente.getListVentas().isEmpty()) {
            throw new EliminacionNoPermitidaException("No se puede eliminar el cliente " + identificacion
                    + " porque tiene compras registradas (se necesitan para el historial y los reportes).");
        }
        listClientes.remove(cliente);
    }

    @Override
    public void registrarEmpleado(Empleado empleado) throws UsuarioDuplicadoException, DatoInvalidoException {
        validarTexto(empleado.getIdentificacion(), "identificación");
        validarTexto(empleado.getNombre(), "nombre");
        validarTexto(empleado.getUsuario(), "usuario");
        validarTexto(empleado.getContrasena(), "contraseña");
        for (Empleado e : listEmpleados) {
            if (e.getUsuario().equals(empleado.getUsuario())) {
                throw new UsuarioDuplicadoException("el usuario " + empleado.getUsuario());
            }
            if (e.getIdentificacion().equals(empleado.getIdentificacion())) {
                throw new UsuarioDuplicadoException("la identificación " + empleado.getIdentificacion());
            }
        }
        listEmpleados.add(empleado);
    }

    @Override
    public Empleado autenticarEmpleado(String usuario, String contrasena) throws CredencialesInvalidasException {
        for (Empleado empleado : listEmpleados) {
            if (empleado.validarCredenciales(usuario, contrasena)) {
                return empleado;
            }
        }
        throw new CredencialesInvalidasException(usuario);
    }

    @Override
    public List<Empleado> consultarEmpleados() {
        return listEmpleados;
    }

    @Override
    public Empleado buscarEmpleado(String identificacion) throws EmpleadoNoEncontradoException {
        for (Empleado empleado : listEmpleados) {
            if (empleado.getIdentificacion().equals(identificacion)) {
                return empleado;
            }
        }
        throw new EmpleadoNoEncontradoException(identificacion);
    }

    /**
     * Cambia nombre, usuario y contraseña del empleado.
     * Si la contraseña llega vacía, se deja la que tenía.
     */
    @Override
    public void modificarEmpleado(String identificacion, String nombre, String usuario, String contrasena)
            throws EmpleadoNoEncontradoException, UsuarioDuplicadoException, DatoInvalidoException {
        Empleado empleado = buscarEmpleado(identificacion);
        validarTexto(nombre, "nombre");
        validarTexto(usuario, "usuario");
        for (Empleado otro : listEmpleados) {
            if (otro != empleado && otro.getUsuario().equals(usuario.trim())) {
                throw new UsuarioDuplicadoException("el usuario " + usuario.trim());
            }
        }
        empleado.setNombre(nombre.trim());
        empleado.setUsuario(usuario.trim());
        if (contrasena != null && !contrasena.isEmpty()) {
            empleado.setContrasena(contrasena);
        }
    }

    /**
     * Elimina un empleado, salvo que:
     *  - sea el mismo que tiene la sesión iniciada,
     *  - sea el último administrador (siempre debe quedar uno),
     *  - sea un operador con ventas registradas (se necesitan para el historial).
     */
    @Override
    public void eliminarEmpleado(String identificacion, Empleado empleadoActual)
            throws EmpleadoNoEncontradoException, EliminacionNoPermitidaException {
        Empleado empleado = buscarEmpleado(identificacion);
        if (empleado == empleadoActual) {
            throw new EliminacionNoPermitidaException("No puede eliminar su propia cuenta mientras tiene la sesión iniciada.");
        }
        if (empleado instanceof Administrador) {
            int administradores = 0;
            for (Empleado e : listEmpleados) {
                if (e instanceof Administrador) {
                    administradores++;
                }
            }
            if (administradores == 1) {
                throw new EliminacionNoPermitidaException("No se puede eliminar el último administrador del sistema.");
            }
        }
        if (empleado instanceof Operador && !((Operador) empleado).getListVentas().isEmpty()) {
            throw new EliminacionNoPermitidaException("No se puede eliminar el operador " + identificacion
                    + " porque tiene ventas registradas (se necesitan para el historial).");
        }
        listEmpleados.remove(empleado);
    }

    // =====================================================================
    // Ventas
    // =====================================================================

    @Override
    public Venta venderEntrada(String codigoFuncion, String codigoAsiento, String identificacionCliente,
                               TipoEntrada tipoEntrada, List<Combo> combos, Operador operador)
            throws FuncionNoEncontradaException, FuncionNoDisponibleException, ClienteNoEncontradoException,
                   SinAsientosDisponiblesException, AsientoNoEncontradoException, AsientoOcupadoException,
                   DatoInvalidoException {

        if (tipoEntrada == null) {
            throw new DatoInvalidoException("Debe seleccionar el tipo de entrada.");
        }

        Funcion funcion = buscarFuncion(codigoFuncion);
        if (funcion.getEstado() != EstadoFuncion.PROGRAMADA) {
            throw new FuncionNoDisponibleException(codigoFuncion);
        }

        Cliente cliente = buscarCliente(identificacionCliente);
        if (!esTipoEntradaPermitido(cliente, tipoEntrada)) {
            throw new DatoInvalidoException("El cliente " + cliente.getNombre() + " está registrado como "
                    + cliente.getTipoCliente().getDescripcion() + ". Solo puede comprar entrada General o "
                    + cliente.getTipoCliente().getDescripcion() + ". Si cambió su condición, actualice el tipo de cliente.");
        }

        if (funcion.contarAsientosDisponibles() == 0) {
            throw new SinAsientosDisponiblesException(codigoFuncion);
        }

        if (combos != null) {
            for (Combo combo : combos) {
                if (!combo.estaDisponible()) {
                    throw new DatoInvalidoException("El combo " + combo.getNombre() + " ya no está disponible.");
                }
                if (!combo.sePuedeVenderEn(funcion.getFormato())) {
                    throw new DatoInvalidoException("El combo " + combo.getNombre()
                            + " es exclusivo de las funciones VIP Bistró.");
                }
            }
        }

        Asiento asiento = funcion.buscarAsiento(codigoAsiento);
        asiento.ocupar();   // lanza AsientoOcupadoException si ya está ocupado

        // Precios con el descuento del plan de membresía del cliente
        if (combos == null) {
            combos = new ArrayList<>();
        }
        double tarifa = calcularValorEntrada(tipoEntrada);
        double valorEntrada = calcularValorEntrada(tipoEntrada, cliente.getPlan(), funcion.getValorBase());
        double valorCombos = calcularValorCombos(combos, cliente.getPlan());

        consecutivoVentas++;
        String codigoVenta = "V" + consecutivoVentas;

        Venta venta = new Venta(codigoVenta, cliente, funcion, asiento, tipoEntrada,
                operador, LocalDateTime.now(), tarifa, funcion.getValorBase(), valorEntrada, combos, valorCombos);

        listVentas.add(venta);
        funcion.agregarVenta(venta);
        cliente.agregarVenta(venta);
        if (operador != null) {
            operador.agregarVenta(venta);
        }
        return venta;
    }

    // ---------------- Pago con tarjeta (simulado) ----------------

    /**
     * Revisa los datos de la tarjeta y el monto ingresado, y devuelve el pago aprobado.
     * No se conecta con ningún banco: el "saldo" es el monto que se escribe.
     *  - Monto menor al total  -> SaldoInsuficienteException.
     *  - Monto mayor al total  -> se aprueba y el restante queda como valor devuelto.
     */
    public Pago procesarPago(TipoTarjeta tipoTarjeta, String numeroTarjeta, String titular,
                             String documentoTitular, int mesVencimiento, int anioVencimiento,
                             String cvv, int cuotas, double total, double montoIngresado)
            throws DatoInvalidoException, SaldoInsuficienteException {

        if (tipoTarjeta == null) {
            throw new DatoInvalidoException("Seleccione si la tarjeta es débito o crédito.");
        }

        String numero = (numeroTarjeta == null) ? "" : numeroTarjeta.replace(" ", "");
        if (numero.isEmpty() || !esSoloDigitos(numero)) {
            throw new DatoInvalidoException("El número de la tarjeta solo puede tener dígitos.");
        }
        Franquicia franquicia = Franquicia.detectar(numero);
        if (franquicia == null) {
            throw new DatoInvalidoException("Tarjeta no reconocida. Se aceptan Visa, Mastercard, American Express y Diners Club.");
        }
        if (numero.length() != franquicia.getCantidadDigitos()) {
            throw new DatoInvalidoException("Una tarjeta " + franquicia.getNombre() + " tiene "
                    + franquicia.getCantidadDigitos() + " dígitos y se ingresaron " + numero.length() + ".");
        }

        if (titular == null || titular.isBlank()) {
            throw new DatoInvalidoException("Ingrese el nombre del titular como aparece en la tarjeta.");
        }
        if (documentoTitular == null || documentoTitular.isBlank()) {
            throw new DatoInvalidoException("Ingrese el documento del titular.");
        }

        if (mesVencimiento < 1 || mesVencimiento > 12 || anioVencimiento <= 0) {
            throw new DatoInvalidoException("Seleccione el mes y el año de vencimiento.");
        }
        // La tarjeta sirve hasta el último día de su mes de vencimiento
        LocalDate hoy = LocalDate.now();
        if (anioVencimiento < hoy.getYear()
                || (anioVencimiento == hoy.getYear() && mesVencimiento < hoy.getMonthValue())) {
            throw new DatoInvalidoException("La tarjeta está vencida.");
        }

        if (cvv == null || cvv.length() != franquicia.getDigitosCvv() || !esSoloDigitos(cvv)) {
            throw new DatoInvalidoException("El código de seguridad (CVV) de " + franquicia.getNombre()
                    + " tiene " + franquicia.getDigitosCvv() + " dígitos.");
        }

        if (tipoTarjeta == TipoTarjeta.DEBITO) {
            cuotas = 1;   // el débito siempre es de contado
        } else if (cuotas < 1) {
            throw new DatoInvalidoException("Seleccione el número de cuotas.");
        }

        if (total <= 0) {
            throw new DatoInvalidoException("No hay nada que pagar.");
        }
        if (montoIngresado <= 0) {
            throw new DatoInvalidoException("Ingrese el monto a pagar.");
        }
        if (montoIngresado < total) {
            throw new SaldoInsuficienteException(total, montoIngresado);
        }

        String ultimos = numero.substring(numero.length() - 4);
        return new Pago(tipoTarjeta, franquicia, ultimos, titular.trim(), documentoTitular.trim(),
                cuotas, total, montoIngresado);
    }

    /** true si el texto tiene solo dígitos (0-9). */
    private boolean esSoloDigitos(String texto) {
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isDigit(texto.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /** Suma el precio de los combos y le aplica el descuento de combos del plan. */
    /**
     * Regla del tipo de entrada: cualquier cliente puede comprar GENERAL;
     * los demás tipos solo si coinciden con su tipo de cliente (ej.: NIÑO -> entrada NIÑO).
     */
    public boolean esTipoEntradaPermitido(Cliente cliente, TipoEntrada tipoEntrada) {
        if (cliente == null || tipoEntrada == null) {
            return true;   // todavía no hay nada que comparar
        }
        return tipoEntrada == TipoEntrada.GENERAL
                || tipoEntrada.name().equals(cliente.getTipoCliente().name());
    }

    /** Valor de los combos SIN descuento (precio de lista). */
    public double calcularValorCombosSinDescuento(List<Combo> combos) {
        double total = 0;
        for (Combo combo : combos) {
            total += combo.getPrecio();
        }
        return total;
    }

    @Override
    public double calcularValorCombos(List<Combo> combos, PlanMembresia plan) {
        double total = 0;
        for (Combo combo : combos) {
            total += combo.getPrecio();
        }
        return Math.round(total * (1 - buscarMembresia(plan).getDescuentoCombos()));   // redondeado a pesos
    }

    @Override
    public void cancelarVenta(String codigoVenta) throws VentaNoEncontradaException, VentaYaCanceladaException {
        buscarVenta(codigoVenta).cancelar();   // también libera el asiento
    }

    @Override
    public Venta buscarVenta(String codigoVenta) throws VentaNoEncontradaException {
        for (Venta venta : listVentas) {
            if (venta.getCodigo().equals(codigoVenta)) {
                return venta;
            }
        }
        throw new VentaNoEncontradaException(codigoVenta);
    }

    @Override
    public List<Venta> consultarVentas() {
        return listVentas;
    }

    // =====================================================================
    // Tarifas
    // =====================================================================

    @Override
    public List<Tarifa> consultarTarifas() {
        return listTarifas;
    }

    @Override
    public Tarifa buscarTarifa(TipoEntrada tipoEntrada) {
        for (Tarifa tarifa : listTarifas) {
            if (tarifa.getTipoEntrada() == tipoEntrada) {
                return tarifa;
            }
        }
        return null;   // no pasa: en el constructor se crea una tarifa por cada tipo
    }

    @Override
    public void modificarTarifa(TipoEntrada tipoEntrada, double nuevoValor) throws DatoInvalidoException {
        if (tipoEntrada == null) {
            throw new DatoInvalidoException("Debe seleccionar el tipo de entrada.");
        }
        if (nuevoValor <= 0) {
            throw new DatoInvalidoException("El valor de la tarifa debe ser mayor que cero.");
        }
        buscarTarifa(tipoEntrada).setValor(nuevoValor);
    }

    @Override
    public double calcularValorEntrada(TipoEntrada tipoEntrada) {
        return buscarTarifa(tipoEntrada).getValor();
    }

    /**
     * Valor de UNA entrada:
     *     (tarifa del tipo de entrada + valor base de la función) - descuento de entradas del plan
     * El valor base es un recargo opcional por estreno o formato especial (puede ser 0).
     */
    @Override
    public double calcularValorEntrada(TipoEntrada tipoEntrada, PlanMembresia plan, double valorBaseFuncion) {
        double sinDescuento = calcularValorEntrada(tipoEntrada) + valorBaseFuncion;
        return Math.round(sinDescuento * (1 - buscarMembresia(plan).getDescuentoEntradas()));   // redondeado a pesos
    }

    // =====================================================================
    // Combos (CRUD del administrador)
    // =====================================================================

    /** Los 6 combos con los que empieza el cine. El administrador los puede cambiar o borrar. */
    private void crearCombosPorDefecto() {
        listCombos = new ArrayList<>();
        listCombos.add(new Combo("C1", "Combo Personal", "Crispetas medianas + gaseosa mediana", 18000));
        listCombos.add(new Combo("C2", "Combo Pareja", "Crispetas grandes + 2 gaseosas medianas", 32000));
        listCombos.add(new Combo("C3", "Combo Familiar", "2 crispetas grandes + 4 gaseosas + chocolatina", 52000));
        listCombos.add(new Combo("C4", "Combo Nachos", "Nachos con queso cheddar + gaseosa grande", 24000));
        listCombos.add(new Combo("C5", "Combo Perro Caliente", "Perro caliente + crispetas pequeñas + gaseosa", 26000));
        listCombos.add(new Combo("C6", "Combo Infantil", "Crispetas pequeñas + jugo + juguete sorpresa", 16000));

        // Exclusivo de las salas VIP Bistró (comida servida en la silla)
        Combo bistro = new Combo("C7", "Combo Bistró Gourmet", "Hamburguesa gourmet + papas a la francesa + limonada de coco", 48000);
        bistro.setExclusivoVipBistro(true);
        listCombos.add(bistro);
    }

    private void validarDatosCombo(String codigo, String nombre, String descripcion, double precio)
            throws DatoInvalidoException {
        validarTexto(codigo, "código");
        validarTexto(nombre, "nombre");
        validarTexto(descripcion, "contenido del combo");
        if (precio <= 0) {
            throw new DatoInvalidoException("El precio del combo debe ser mayor que cero.");
        }
    }

    @Override
    public void registrarCombo(Combo combo) throws ComboDuplicadoException, DatoInvalidoException {
        validarDatosCombo(combo.getCodigo(), combo.getNombre(), combo.getDescripcion(), combo.getPrecio());
        for (Combo c : consultarCombos()) {
            if (c.getCodigo().equalsIgnoreCase(combo.getCodigo())) {
                throw new ComboDuplicadoException(combo.getCodigo());
            }
        }
        listCombos.add(combo);
    }

    @Override
    public Combo buscarCombo(String codigo) throws ComboNoEncontradoException {
        for (Combo combo : consultarCombos()) {
            if (combo.getCodigo().equalsIgnoreCase(codigo)) {
                return combo;
            }
        }
        throw new ComboNoEncontradoException(codigo);
    }

    @Override
    public List<Combo> consultarCombos() {
        // Datos guardados antes de existir el CRUD de combos: se crean los 6 combos iniciales
        if (listCombos == null) {
            crearCombosPorDefecto();
        }
        return listCombos;
    }

    /** Solo los combos habilitados: son los que el operador puede vender. */
    @Override
    public List<Combo> consultarCombosDisponibles() {
        List<Combo> disponibles = new ArrayList<>();
        for (Combo combo : consultarCombos()) {
            if (combo.estaDisponible()) {
                disponibles.add(combo);
            }
        }
        return disponibles;
    }

    /**
     * Combos que se pueden ofrecer en una función: los habilitados, y los exclusivos de VIP Bistró
     * solo si la función es VIP Bistró. Sin función elegida se muestran solo los combos normales.
     */
    public List<Combo> consultarCombosParaFuncion(Funcion funcion) {
        Formato formato = (funcion == null) ? null : funcion.getFormato();
        List<Combo> combos = new ArrayList<>();
        for (Combo combo : consultarCombosDisponibles()) {
            if (combo.sePuedeVenderEn(formato)) {
                combos.add(combo);
            }
        }
        return combos;
    }

    /** Cambia los datos del combo. Las ventas ya hechas conservan el valor que se pagó. */
    @Override
    public void modificarCombo(String codigo, String nombre, String descripcion, double precio,
                               boolean exclusivoVipBistro)
            throws ComboNoEncontradoException, DatoInvalidoException {
        Combo combo = buscarCombo(codigo);
        validarDatosCombo(codigo, nombre, descripcion, precio);
        combo.setNombre(nombre.trim());
        combo.setDescripcion(descripcion.trim());
        combo.setPrecio(precio);
        combo.setExclusivoVipBistro(exclusivoVipBistro);
    }

    public void asignarImagenCombo(String codigo, String rutaImagen) throws ComboNoEncontradoException {
        buscarCombo(codigo).setRutaImagen(rutaImagen);
    }

    @Override
    public void habilitarCombo(String codigo) throws ComboNoEncontradoException {
        buscarCombo(codigo).habilitar();
    }

    @Override
    public void deshabilitarCombo(String codigo) throws ComboNoEncontradoException {
        buscarCombo(codigo).deshabilitar();
    }

    /** true si el combo aparece en alguna venta. */
    public boolean comboFueVendido(Combo combo) {
        for (Venta venta : listVentas) {
            if (venta.contarCombo(combo) > 0) {
                return true;
            }
        }
        return false;
    }

    /** Solo se elimina si nunca se ha vendido (para no dañar el historial); si no, se deshabilita. */
    @Override
    public void eliminarCombo(String codigo) throws ComboNoEncontradoException, EliminacionNoPermitidaException {
        Combo combo = buscarCombo(codigo);
        if (comboFueVendido(combo)) {
            throw new EliminacionNoPermitidaException("No se puede eliminar el combo " + codigo
                    + " porque ya se ha vendido. Puede deshabilitarlo para que no aparezca en la venta.");
        }
        listCombos.remove(combo);
    }

    /**
     * Para datos guardados con la versión anterior, donde los combos eran fijos (enum):
     * cada combo viejo de las ventas se cambia por el combo nuevo que tiene el mismo nombre.
     * Devuelve true si cambió algo (para guardar).
     */
    public boolean actualizarCombosAnteriores() {
        boolean cambio = false;
        for (Venta venta : listVentas) {
            if (venta.actualizarCombosAnteriores(consultarCombos())) {
                cambio = true;
            }
        }
        return cambio;
    }

    // =====================================================================
    // Membresías (valores vigentes de cada plan)
    // =====================================================================

    /** Una membresía por cada plan, con los valores por defecto del enum PlanMembresia. */
    private void crearMembresiasPorDefecto() {
        listMembresias = new ArrayList<>();
        for (PlanMembresia plan : PlanMembresia.values()) {
            listMembresias.add(new Membresia(plan));
        }
    }

    @Override
    public List<Membresia> consultarMembresias() {
        // Datos guardados antes de existir las membresías: se crean con los valores por defecto
        if (listMembresias == null) {
            crearMembresiasPorDefecto();
        }
        return listMembresias;
    }

    @Override
    public Membresia buscarMembresia(PlanMembresia plan) {
        for (Membresia membresia : consultarMembresias()) {
            if (membresia.getPlan() == plan) {
                return membresia;
            }
        }
        return null;   // no pasa: hay una membresía por cada plan
    }

    /**
     * Cambia los valores de un plan. Los descuentos van de 0 a 1 (0.25 = 25 %).
     * Las ventas ya hechas conservan lo que se pagó.
     */
    @Override
    public void modificarMembresia(PlanMembresia plan, double precioMensual, double descuentoEntradas,
                                   double descuentoCombos, String beneficioExtra) throws DatoInvalidoException {
        if (plan == null) {
            throw new DatoInvalidoException("Debe seleccionar el plan.");
        }
        if (precioMensual < 0) {
            throw new DatoInvalidoException("El precio mensual no puede ser negativo.");
        }
        if (descuentoEntradas < 0 || descuentoEntradas > 1 || descuentoCombos < 0 || descuentoCombos > 1) {
            throw new DatoInvalidoException("Los descuentos deben estar entre 0 % y 100 %.");
        }
        validarTexto(beneficioExtra, "beneficio extra");
        Membresia membresia = buscarMembresia(plan);
        membresia.setPrecioMensual(precioMensual);
        membresia.setDescuentoEntradas(descuentoEntradas);
        membresia.setDescuentoCombos(descuentoCombos);
        membresia.setBeneficioExtra(beneficioExtra.trim());
    }

    // =====================================================================
    // Reportes (las ventas canceladas no cuentan)
    // =====================================================================

    @Override
    public int calcularTotalEntradasVendidas() {
        int total = 0;
        for (Venta venta : listVentas) {
            if (venta.getEstado() == EstadoVenta.ACTIVA) {
                total++;
            }
        }
        return total;
    }

    @Override
    public double calcularTotalIngresos() {
        double total = 0;
        for (Venta venta : listVentas) {
            if (venta.getEstado() == EstadoVenta.ACTIVA) {
                total += venta.getValorPagado();
            }
        }
        return total;
    }

    @Override
    public Pelicula obtenerPeliculaMasVendida() {
        Pelicula masVendida = null;
        int maximo = 0;
        for (Pelicula pelicula : listPeliculas) {
            int vendidas = pelicula.contarEntradasVendidas();
            if (vendidas > maximo) {
                maximo = vendidas;
                masVendida = pelicula;
            }
        }
        return masVendida;
    }

    @Override
    public double obtenerOcupacionFuncion(String codigoFuncion) throws FuncionNoEncontradaException {
        Funcion funcion = buscarFuncion(codigoFuncion);
        int total = funcion.getAsientos().size();
        if (total == 0) {
            return 0;
        }
        int ocupados = total - funcion.contarAsientosDisponibles();
        return ocupados * 100.0 / total;
    }

    @Override
    public List<Funcion> obtenerFuncionesProgramadas(LocalDate fecha) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion funcion : consultarFuncionesPorFecha(fecha)) {
            if (funcion.getEstado() == EstadoFuncion.PROGRAMADA) {
                resultado.add(funcion);
            }
        }
        return resultado;
    }

    // =====================================================================
    // Getters y setters de los datos del cine
    // =====================================================================

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Cine{nombre='" + nombre + "', salas=" + listSalas.size()
                + ", peliculas=" + listPeliculas.size() + ", funciones=" + listFunciones.size()
                + ", clientes=" + listClientes.size() + ", empleados=" + listEmpleados.size()
                + ", ventas=" + listVentas.size() + "}";
    }
}
