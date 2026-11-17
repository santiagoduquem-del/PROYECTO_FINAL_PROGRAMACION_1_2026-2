package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Clasificacion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Genero;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.exceptions.CinemaException;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.GestionPeliculasController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class GestionPeliculasViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<Genero> cbGenero;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtAnioEstreno;
    @FXML private TextArea txtSinopsis;
    @FXML private TextField txtElenco;
    @FXML private TextField txtEstudio;
    @FXML private ComboBox<Clasificacion> cbClasificacion;
    @FXML private FlowPane contenedorFormatos;
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, String> colCodigo;
    @FXML private TableColumn<Pelicula, String> colNombre;
    @FXML private TableColumn<Pelicula, String> colAnio;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, String> colDuracion;
    @FXML private TableColumn<Pelicula, String> colClasificacion;
    @FXML private TableColumn<Pelicula, String> colFormato;
    @FXML private TableColumn<Pelicula, String> colEstado;
    @FXML private ImageView imgCaratula;
    @FXML private ImageView imgBanner;

    private final GestionPeliculasController gestionPeliculasController = new GestionPeliculasController();

    // Imágenes elegidas en el formulario (null si no se ha subido ninguna)
    private String rutaCaratula;
    private String rutaBanner;

    // Casillas de los formatos (se crean en initialize)
    private final List<CheckBox> casillasFormato = new ArrayList<>();

    @FXML
    public void initialize() {
        cbGenero.getItems().setAll(Genero.values());
        cbClasificacion.getItems().setAll(Clasificacion.values());
        // Una casilla por cada formato; en userData queda el formato que representa
        for (Formato formato : Formato.values()) {
            CheckBox casilla = new CheckBox(formato.getNombre());
            casilla.setUserData(formato);
            casilla.setStyle("-fx-text-fill: white; -fx-font-size: 15px;");
            casillasFormato.add(casilla);
        }
        contenedorFormatos.getChildren().setAll(casillasFormato);

        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colAnio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().describirEstreno()));
        colGenero.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGenero().toString()));
        colDuracion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDuracionMinutos() + " min"));
        colClasificacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getClasificacion().getDescripcion()));
        colFormato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().describirFormatos()));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado().toString()));

        refrescarTabla();
    }

    // Al hacer clic en una fila, sus datos pasan al formulario
    @FXML
    private void onSeleccionarPelicula() {
        Pelicula pelicula = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (pelicula != null) {
            txtCodigo.setText(pelicula.getCodigo());
            txtNombre.setText(pelicula.getNombre());
            txtSinopsis.setText(pelicula.getSinopsis());
            txtElenco.setText(pelicula.getElenco());
            txtEstudio.setText(pelicula.getEstudio());
            cbGenero.setValue(pelicula.getGenero());
            txtDuracion.setText(String.valueOf(pelicula.getDuracionMinutos()));
            txtAnioEstreno.setText(pelicula.getAnioEstreno() == 0 ? "" : String.valueOf(pelicula.getAnioEstreno()));
            cbClasificacion.setValue(pelicula.getClasificacion());
            for (CheckBox casilla : casillasFormato) {
                casilla.setSelected(pelicula.tieneFormato((Formato) casilla.getUserData()));
            }

            rutaCaratula = pelicula.getRutaCaratula();
            rutaBanner = pelicula.getRutaBanner();
            Imagenes.mostrar(imgCaratula, rutaCaratula);
            Imagenes.mostrar(imgBanner, rutaBanner);
        }
    }

    @FXML
    private void onSubirCaratula() {
        String ruta = Imagenes.elegirImagen(rootPane.getScene().getWindow(), "Seleccione la carátula de la película");
        if (ruta != null) {
            rutaCaratula = ruta;
            Imagenes.mostrar(imgCaratula, rutaCaratula);
        }
    }

    @FXML
    private void onSubirBanner() {
        String ruta = Imagenes.elegirImagen(rootPane.getScene().getWindow(), "Seleccione el banner de la película");
        if (ruta != null) {
            rutaBanner = ruta;
            Imagenes.mostrar(imgBanner, rutaBanner);
        }
    }

    @FXML
    private void onRegistrar() {
        try {
            gestionPeliculasController.registrarPelicula(txtCodigo.getText(), txtNombre.getText(), cbGenero.getValue(),
                    txtDuracion.getText(), txtAnioEstreno.getText(), cbClasificacion.getValue(), obtenerFormatosMarcados(),
                    txtSinopsis.getText(), txtElenco.getText(), txtEstudio.getText(), rutaCaratula, rutaBanner);
            Alertas.mostrarInfo("Película registrada", "La película se registró correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo registrar", e.getMessage());
        }
    }

    @FXML
    private void onModificar() {
        try {
            gestionPeliculasController.modificarPelicula(txtCodigo.getText(), txtNombre.getText(), cbGenero.getValue(),
                    txtDuracion.getText(), txtAnioEstreno.getText(), cbClasificacion.getValue(), obtenerFormatosMarcados(),
                    txtSinopsis.getText(), txtElenco.getText(), txtEstudio.getText(), rutaCaratula, rutaBanner);
            Alertas.mostrarInfo("Película modificada", "Los datos de la película se actualizaron.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo modificar", e.getMessage());
        }
    }

    @FXML
    private void onDeshabilitar() {
        try {
            gestionPeliculasController.deshabilitarPelicula(txtCodigo.getText());
            Alertas.mostrarInfo("Película deshabilitada", "La película quedó NO_DISPONIBLE.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo deshabilitar", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        if (!Alertas.confirmar("Eliminar película",
                "¿Seguro que desea eliminar la película " + txtCodigo.getText() + "? Esta acción no se puede deshacer.")) {
            return;
        }
        try {
            gestionPeliculasController.eliminarPelicula(txtCodigo.getText());
            Alertas.mostrarInfo("Película eliminada", "La película se eliminó correctamente.");
            onLimpiar();
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo eliminar", e.getMessage());
        }
    }

    @FXML
    private void onHabilitar() {
        try {
            gestionPeliculasController.habilitarPelicula(txtCodigo.getText());
            Alertas.mostrarInfo("Película habilitada", "La película quedó DISPONIBLE.");
            refrescarTabla();
        } catch (CinemaException e) {
            Alertas.mostrarError("No se pudo habilitar", e.getMessage());
        }
    }

    @FXML
    private void onLimpiar() {
        txtCodigo.clear();
        txtNombre.clear();
        txtSinopsis.clear();
        txtElenco.clear();
        txtEstudio.clear();
        cbGenero.setValue(null);
        txtDuracion.clear();
        txtAnioEstreno.clear();
        cbClasificacion.setValue(null);
        for (CheckBox casilla : casillasFormato) {
            casilla.setSelected(false);
        }
        rutaCaratula = null;
        rutaBanner = null;
        Imagenes.mostrar(imgCaratula, null);
        Imagenes.mostrar(imgBanner, null);
        tablaPeliculas.getSelectionModel().clearSelection();
    }

    /** Abre las secciones del detalle de la película seleccionada en la tabla. */
    @FXML
    private void onGestionarSecciones() {
        try {
            gestionPeliculasController.prepararSecciones(txtCodigo.getText());
            Navegador.irA(rootPane, "GestionSecciones.fxml");
        } catch (CinemaException e) {
            Alertas.mostrarError("Secciones del detalle", e.getMessage());
        }
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuAdministrador.fxml");
    }

    private void refrescarTabla() {
        tablaPeliculas.getItems().setAll(gestionPeliculasController.obtenerPeliculas());
        tablaPeliculas.refresh();
    }

    /** Formatos marcados en las casillas. */
    private List<Formato> obtenerFormatosMarcados() {
        List<Formato> marcados = new ArrayList<>();
        for (CheckBox casilla : casillasFormato) {
            if (casilla.isSelected()) {
                marcados.add((Formato) casilla.getUserData());
            }
        }
        return marcados;
    }
}
