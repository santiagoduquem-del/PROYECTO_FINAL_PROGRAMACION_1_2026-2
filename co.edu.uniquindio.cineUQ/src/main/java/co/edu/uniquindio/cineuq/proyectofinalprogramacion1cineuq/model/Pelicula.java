package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.ContadorEntradas;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.interfaces.Habilitable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Película de la cartelera del cinema.
 */
public class Pelicula implements Habilitable, ContadorEntradas, Serializable {

    // Versión de la clase para guardarla en archivo (persistencia)
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private Genero genero;
    private int duracionMinutos;
    private Clasificacion clasificacion;
    private List<Formato> formatos;   // formatos en que se puede proyectar (2D, IMAX...)
    private Formato formato;          // SOLO de la versión anterior (un formato por película); ver getFormatos()
    private int anioEstreno;   // 0 = sin dato (películas guardadas antes de existir este atributo)
    private EstadoPelicula estado;
    private List<Funcion> listFunciones;

    // Ubicación de las imágenes que sube el administrador (null si no tiene).
    // Se guarda como texto para que el modelo no dependa de JavaFX.
    private String rutaCaratula;
    private String rutaBanner;

    // Información que escribe el administrador (se muestra en el detalle de la película)
    private String sinopsis;
    private String elenco;     // actores principales, ej.: "Robert Pattinson, Zoë Kravitz"
    private String estudio;    // ej.: "Warner Bros. Pictures"
    private List<SeccionPelicula> listSecciones;   // secciones de abajo del detalle (texto + foto intercalados)

    public Pelicula(String codigo, String nombre, Genero genero, int duracionMinutos,
                    Clasificacion clasificacion, List<Formato> formatos, int anioEstreno, EstadoPelicula estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
        this.formatos = new ArrayList<>(formatos);
        this.anioEstreno = anioEstreno;
        this.estado = estado;
        this.listFunciones = new ArrayList<>();
    }

    public void agregarFuncion(Funcion funcion) {
        listFunciones.add(funcion);
    }

    public void removerFuncion(Funcion funcion) {
        listFunciones.remove(funcion);
    }

    @Override
    public int contarEntradasVendidas() {
        int total = 0;
        for (Funcion funcion : listFunciones) {
            total += funcion.contarEntradasVendidas();
        }
        return total;
    }

    @Override
    public boolean estaDisponible() {
        return estado == EstadoPelicula.DISPONIBLE;
    }

    @Override
    public void habilitar() {
        estado = EstadoPelicula.DISPONIBLE;
    }

    @Override
    public void deshabilitar() {
        estado = EstadoPelicula.NO_DISPONIBLE;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(Clasificacion clasificacion) {
        this.clasificacion = clasificacion;
    }

    /**
     * Formatos en que está disponible la película.
     * Datos guardados con la versión anterior (un solo formato): se pasa ese formato a la lista.
     */
    public List<Formato> getFormatos() {
        if (formatos == null) {
            formatos = new ArrayList<>();
            if (formato != null) {
                formatos.add(formato);
            }
        }
        return formatos;
    }

    public void setFormatos(List<Formato> formatos) {
        this.formatos = new ArrayList<>(formatos);
    }

    public boolean tieneFormato(Formato formato) {
        return getFormatos().contains(formato);
    }

    /** Texto con todos los formatos, ej.: "2D · IMAX · 4DX". */
    public String describirFormatos() {
        String texto = "";
        for (Formato f : getFormatos()) {
            if (!texto.isEmpty()) {
                texto += " · ";
            }
            texto += f.getNombre();
        }
        return texto.isEmpty() ? "Sin formato" : texto;
    }

    public int getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(int anioEstreno) {
        this.anioEstreno = anioEstreno;
    }

    /** Es reestreno si se estrenó antes del año actual. */
    public boolean esReestreno() {
        return anioEstreno > 0 && anioEstreno < LocalDate.now().getYear();
    }

    /** Texto para mostrar, ej.: "2014 · Reestreno", "2026" o "Sin dato". */
    public String describirEstreno() {
        if (anioEstreno == 0) {
            return "Sin dato";
        }
        if (esReestreno()) {
            return anioEstreno + " · Reestreno";
        }
        return String.valueOf(anioEstreno);
    }

    public EstadoPelicula getEstado() {
        return estado;
    }

    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }

    public List<Funcion> getListFunciones() {
        return listFunciones;
    }

    public String getRutaCaratula() {
        return rutaCaratula;
    }

    public void setRutaCaratula(String rutaCaratula) {
        this.rutaCaratula = rutaCaratula;
    }

    public String getRutaBanner() {
        return rutaBanner;
    }

    public void setRutaBanner(String rutaBanner) {
        this.rutaBanner = rutaBanner;
    }

    // Así se muestra en los ComboBox de la interfaz
    @Override
    public String toString() {
        return nombre + " (" + codigo + ")";
    }

    // Las películas guardadas antes de existir estos datos los tienen en null: se devuelve texto vacío
    public String getSinopsis() {
        return (sinopsis == null) ? "" : sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getElenco() {
        return (elenco == null) ? "" : elenco;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public String getEstudio() {
        return (estudio == null) ? "" : estudio;
    }

    /** Secciones del detalle. Las películas guardadas antes de esta versión no tenían lista: se crea vacía. */
    public List<SeccionPelicula> getListSecciones() {
        if (listSecciones == null) {
            listSecciones = new ArrayList<>();
        }
        return listSecciones;
    }

    public void setEstudio(String estudio) {
        this.estudio = estudio;
    }
}
