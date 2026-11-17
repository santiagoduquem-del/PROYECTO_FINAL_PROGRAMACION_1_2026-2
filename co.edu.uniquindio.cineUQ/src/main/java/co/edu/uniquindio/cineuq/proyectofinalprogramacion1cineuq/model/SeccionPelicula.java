package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model;

import java.io.Serializable;

/**
 * Sección informativa que aparece debajo del detalle de una película, como en la página de AMC:
 * un texto pequeño arriba (opcional), un título, un texto y una foto (opcional).
 * El administrador decide cuántas tiene cada película (pueden ser cero).
 * En pantalla se intercalan: texto | foto, luego foto | texto, y así sucesivamente.
 */
public class SeccionPelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    private String etiqueta;     // texto pequeño sobre el título, ej.: "EXCLUSIVO PARA MIEMBROS" (puede estar vacío)
    private String titulo;
    private String texto;
    private String rutaImagen;   // foto de la sección (null = sin foto)

    public SeccionPelicula(String etiqueta, String titulo, String texto, String rutaImagen) {
        this.etiqueta = etiqueta;
        this.titulo = titulo;
        this.texto = texto;
        this.rutaImagen = rutaImagen;
    }

    public boolean tieneImagen() {
        return rutaImagen != null;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
