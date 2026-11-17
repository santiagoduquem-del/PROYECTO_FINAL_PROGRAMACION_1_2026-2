package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionSeccionesController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.SeccionPelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Pantalla Secciones del detalle (administrador): agrega, modifica, elimina y ordena las secciones
 * de texto y foto que aparecen debajo del detalle de la película elegida en Gestionar películas.
 */
public class GestionSeccionesViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private Label lblPelicula;
    @FXML private TextField txtEtiqueta;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtTexto;
    @FXML private ImageView imgSeccion;
    @FXML private TableView<SeccionPelicula> tablaSecciones;
    @FXML private TableColumn<SeccionPelicula, String> colPosicion;
    @FXML private TableColumn<SeccionPelicula, String> colEtiqueta;
    @FXML private TableColumn<SeccionPelicula, String> colTitulo;
    @FXML private TableColumn<SeccionPelicula, String> colTexto;
    @FXML private TableColumn<SeccionPelicula, String> colFoto;

    private final GestionSeccionesController gestionSeccionesController = new GestionSeccionesController();

    // Foto elegida en el formulario (null = sin foto)
    private String rutaFoto;

    @FXML
    public void initialize() {
        lblPelicula.setText("Secciones de " + gestionSeccionesController.obtenerPelicula().getNombre());

        // La posición se calcula con el lugar de la sección en la lista (1, 2, 3...)
        colPosicion.setCellValueFactory(c -> new SimpleStringProperty(
                String.valueOf(tablaSecciones.getItems().indexOf(c.getValue()) + 1)));
        colEtiqueta.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEtiqueta()));
        colTitulo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitulo()));
        colTexto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTexto()));
        colFoto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().tieneImagen() ? "Sí" : "No"));
        refrescarTabla();
    }

    // Al hacer clic en una fila, sus datos pasan al formulario
    @FXML
    private void onSeleccionarSeccion() {
        SeccionPelicula seccion = tablaSecciones.getSelectionModel().getSelectedItem();
        if (seccion != null) {
            txtEtiqueta.setText(seccion.getEtiqueta());
            txtTitulo.setText(seccion.getTitulo());
            txtTexto.setText(seccion.getTexto());
            rutaFoto = seccion.getRutaImagen();
            Imagenes.mostrar(imgSeccion, rutaFoto);
        }
    }

    @FXML
    private void onSubirFoto() {
        String ruta = Imagenes.elegirImagen(rootPane.getScene().getWindow(), "Seleccione la foto de la sección");
        if (ruta != null) {
            rutaFoto = ruta;
            Imagenes.mostrar(imgSeccion, rutaFoto);
        }
    }

    @FXML
    private void onQuitarFoto() {
        rutaFoto = null;   // la sección queda solo con texto (se aplica al pulsar Agregar o Modificar)
        Imagenes.mostrar(imgSeccion, null);
    }

    @FXML
    private void onAgregar() {
        try {
            gestionSeccionesController.agregarSeccion(txtEtiqueta.getText(), txtTitulo.getText(),
                    txtTexto.getText(), rutaFoto);
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo agregar", e.getMessage());
        }
    }

    @FXML
    private void onModificar() {
        try {
            int posicion = tablaSecciones.getSelectionModel().getSelectedIndex();
            gestionSeccionesController.modificarSeccion(posicion, txtEtiqueta.getText(), txtTitulo.getText(),
                    txtTexto.getText(), rutaFoto);
            refrescarTabla();
            tablaSecciones.getSelectionModel().select(posicion);
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        int posicion = tablaSecciones.getSelectionModel().getSelectedIndex();
        if (posicion < 0) {
            Alertas.mostrarError("No se pudo eliminar", "Seleccione una sección de la tabla.");
            return;
        }
        if (!Alertas.confirmar("Eliminar sección", "¿Seguro que desea eliminar la sección " + (posicion + 1) + "?")) {
            return;
        }
        try {
            gestionSeccionesController.eliminarSeccion(posicion);
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo eliminar", e.getMessage());
        }
    }

    @FXML
    private void onSubir() {
        mover(-1);
    }

    @FXML
    private void onBajar() {
        mover(1);
    }

    /** Mueve la sección seleccionada y la deja seleccionada en su nuevo lugar. */
    private void mover(int desplazamiento) {
        try {
            int nueva = gestionSeccionesController.moverSeccion(
                    tablaSecciones.getSelectionModel().getSelectedIndex(), desplazamiento);
            refrescarTabla();
            tablaSecciones.getSelectionModel().select(nueva);
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo mover", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        txtEtiqueta.clear();
        txtTitulo.clear();
        txtTexto.clear();
        rutaFoto = null;
        Imagenes.mostrar(imgSeccion, null);
        tablaSecciones.getSelectionModel().clearSelection();
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "GestionPeliculas.fxml");
    }

    private void refrescarTabla() {
        tablaSecciones.getItems().setAll(gestionSeccionesController.obtenerSecciones());
        tablaSecciones.refresh();
    }
}
