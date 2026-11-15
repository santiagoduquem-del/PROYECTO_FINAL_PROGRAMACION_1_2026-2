package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Cine;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Combo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Guarda y carga todos los datos del cine en un archivo, usando SERIALIZACIÓN de Java.
 *
 * Todo queda en la carpeta "CinemaUQ" dentro de la carpeta personal del usuario:
 *   CinemaUQ/cinemauq.dat   -> el objeto Cine completo (películas, salas, funciones, ventas...)
 *   CinemaUQ/imagenes/      -> copia de las carátulas y banners que sube el administrador
 *
 * Para volver a empezar con los datos de prueba basta con borrar el archivo cinemauq.dat.
 */
public class Persistencia {

    private static final String CARPETA = System.getProperty("user.home") + File.separator + "CinemaUQ";
    private static final String ARCHIVO_DATOS = CARPETA + File.separator + "cinemauq.dat";
    private static final String CARPETA_IMAGENES = CARPETA + File.separator + "imagenes";

    private Persistencia() { }

    /** Guarda el cine completo en el archivo. */
    public static void guardarCine(Cine cine) {
        new File(CARPETA).mkdirs();   // crea la carpeta si no existe
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            salida.writeObject(cine);
        } catch (IOException e) {
            System.err.println("No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    /**
     * Carga el cine desde el archivo.
     * Devuelve null si el archivo no existe (primera vez) o si no se pudo leer.
     */
    public static Cine cargarCine() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            return null;
        }
        try (ObjectInputStream entrada = new LectorCompatible(new FileInputStream(archivo))) {
            return (Cine) entrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Antes de empezar de cero se guarda una copia del archivo, para no perder los datos
            String respaldo = CARPETA + File.separator + "cinemauq_respaldo_" + System.currentTimeMillis() + ".dat";
            boolean copiado = archivo.renameTo(new File(respaldo));
            System.err.println("No se pudieron cargar los datos guardados (" + e.getMessage() + ").");
            System.err.println(copiado ? "Se guardó una copia en " + respaldo + ". Se empieza con los datos de prueba."
                                       : "Se empieza con los datos de prueba.");
            return null;
        }
    }

    /**
     * Lector de objetos que también entiende los archivos de la versión anterior.
     * Allí los combos eran un enum (los enum se guardan con serialVersionUID 0);
     * ahora Combo es una clase. Cuando el archivo trae el enum viejo, se lee con la copia
     * que quedó en model.legado, y luego Cine.actualizarCombosAnteriores() los convierte.
     */
    private static class LectorCompatible extends ObjectInputStream {

        LectorCompatible(InputStream entrada) throws IOException {
            super(entrada);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass descripcion) throws IOException, ClassNotFoundException {
            if (descripcion.getName().equals(Combo.class.getName()) && descripcion.getSerialVersionUID() == 0L) {
                return co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.legado.Combo.class;
            }
            return super.resolveClass(descripcion);
        }
    }

    /**
     * Copia una imagen elegida por el administrador a la carpeta CinemaUQ/imagenes
     * y devuelve la ruta de la copia. Así la imagen no se pierde si se mueve o borra el original.
     *
     * @param rutaOriginal ruta de la imagen (como la entrega el explorador de archivos), o null
     * @param nombreNuevo  nombre para la copia, sin extensión (ej.: "P1_caratula")
     */
    public static String copiarImagen(String rutaOriginal, String nombreNuevo) {
        if (rutaOriginal == null) {
            return null;
        }
        try {
            Path origen = Paths.get(URI.create(rutaOriginal));
            Path carpeta = Paths.get(CARPETA_IMAGENES);

            // Si la imagen ya está en la carpeta del programa, no hay que copiarla otra vez
            if (origen.getParent() != null && origen.getParent().equals(carpeta)) {
                return rutaOriginal;
            }

            Files.createDirectories(carpeta);
            String nombreArchivo = origen.getFileName().toString();
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.'));   // ej.: ".jpg"
            Path destino = carpeta.resolve(nombreNuevo + extension);

            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
            return destino.toUri().toString();
        } catch (IOException | RuntimeException e) {
            System.err.println("No se pudo copiar la imagen: " + e.getMessage());
            return rutaOriginal;   // se deja la ruta original para que al menos se vea ahora
        }
    }
}
