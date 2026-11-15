package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import javafx.beans.property.SimpleStringProperty;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.ConsultarPeliculasController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;

/**
 * Cartelera del operador: muestra las películas disponibles con la carátula,
 * el banner y los datos que registró el administrador.
 */
public class ConsultarPeliculasViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Pelicula> tablaPeliculas;
    @FXML private TableColumn<Pelicula, String> colNombre;
    @FXML private TableColumn<Pelicula, String> colAnio;
    @FXML private TableColumn<Pelicula, String> colGenero;
    @FXML private TableColumn<Pelicula, String> colDuracion;
    @FXML private TableColumn<Pelicula, String> colClasificacion;
    @FXML private TableColumn<Pelicula, String> colFormato;
    @FXML private ImageView imgBanner;
    @FXML private ImageView imgCaratula;
    @FXML private Label lblNombre;
    @FXML private Label lblAnio;
    @FXML private Label lblGenero;
    @FXML private Label lblDuracion;
    @FXML private Label lblClasificacion;
    @FXML private Label lblFormato;

    private final ConsultarPeliculasController consultarPeliculasController = new ConsultarPeliculasController();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colAnio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().describirEstreno()));
        colGenero.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGenero().toString()));
        colDuracion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDuracionMinutos() + " min"));
        colClasificacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getClasificacion().getDescripcion()));

        colFormato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFormato().toString()));

        List<Pelicula> disponibles = consultarPeliculasController.obtenerPeliculasDisponibles();
        tablaPeliculas.getItems().setAll(disponibles);

        // Se muestra de una vez la primera película de la cartelera
        if (!disponibles.isEmpty()) {
            mostrarDetalle(disponibles.get(0));
        }
    }

    @FXML
    private void onSeleccionarPelicula() {
        Pelicula pelicula = tablaPeliculas.getSelectionModel().getSelectedItem();
        if (pelicula != null) {
            mostrarDetalle(pelicula);
        }
    }

    private void mostrarDetalle(Pelicula pelicula) {
        lblNombre.setText(pelicula.getNombre());
        lblAnio.setText(pelicula.describirEstreno());
        lblGenero.setText(pelicula.getGenero().toString());
        lblDuracion.setText(pelicula.getDuracionMinutos() + " minutos");
        lblClasificacion.setText(pelicula.getClasificacion().getDescripcion());
        lblFormato.setText(pelicula.getFormato().toString());
        Imagenes.mostrar(imgBanner, pelicula.getRutaBanner());
        Imagenes.mostrar(imgCaratula, pelicula.getRutaCaratula());
    }

    @FXML
    private void onVolver() {
        Navegador.irA(rootPane, "MenuOperador.fxml");
    }
}
