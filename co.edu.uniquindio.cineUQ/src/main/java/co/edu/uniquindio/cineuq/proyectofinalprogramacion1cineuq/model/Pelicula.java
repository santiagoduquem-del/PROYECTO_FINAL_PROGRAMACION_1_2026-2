package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;


/**
 * Película disponible en la cartelera del cinema.
 */
public class Pelicula {

    private String codigo;
    private String nombre;
    private Genero genero;
    private int duracionMinutos;
    private Clasificacion clasificacion;
    private EstadoPelicula estado;

    public Pelicula(String codigo, String nombre, Genero genero, int duracionMinutos, Clasificacion clasificacion, EstadoPelicula estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.genero = genero;
        this.duracionMinutos = duracionMinutos;
        this.clasificacion = clasificacion;
        this.estado = estado;
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

    public EstadoPelicula getEstado() {
        return estado;
    }

    public void setEstado(EstadoPelicula estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pelicula{"
                + "codigo='" + codigo + "'"
                + ", nombre='" + nombre + "'"
                + ", genero=" + genero
                + ", duracionMinutos=" + duracionMinutos
                + ", clasificacion=" + clasificacion
                + ", estado=" + estado
                + "}";
    }
}
