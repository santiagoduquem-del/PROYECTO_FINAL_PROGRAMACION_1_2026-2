package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.DetallePeliculaController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Clasificacion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Pos;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.SeccionPelicula;
import java.util.List;
import javafx.scene.control.Button;

/**
 * Detalle de una película de la cartelera: banner, carátula, todos sus datos,
 * sinopsis, elenco y estudio, y el botón "Comprar entradas". Se abre al hacer clic en una carátula de Consultar películas.
 */
public class DetallePeliculaViewController extends BarraSuperiorViewController {

    @FXML private AnchorPane rootPane;
    @FXML private ImageView imgBanner;
    @FXML private ImageView imgCaratula;
    @FXML private Label lblNombre;
    @FXML private Label lblAnio;
    @FXML private Label lblGenero;
    @FXML private Label lblDuracion;
    @FXML private Label lblClasificacion;
    @FXML private FlowPane contenedorFormatos;
    @FXML private Label lblSinopsis;
    @FXML private Label lblElenco;
    @FXML private Label lblEstudio;
    @FXML private VBox contenedorSecciones;

    private final DetallePeliculaController detallePeliculaController = new DetallePeliculaController();

    @FXML
    public void initialize() {
        Pelicula pelicula = detallePeliculaController.obtenerPeliculaSeleccionada();
        if (pelicula == null) {
            return;
        }
        lblNombre.setText(pelicula.getNombre());
        lblAnio.setText(pelicula.describirEstreno());
        lblGenero.setText(pelicula.getGenero().toString());
        lblDuracion.setText(pelicula.getDuracionMinutos() + " minutos");
        lblClasificacion.setText(pelicula.getClasificacion().getDescripcion());
        lblClasificacion.setStyle(estiloClasificacion(pelicula.getClasificacion()));
        // Formatos: un logo por cada uno. Si un logo no está en recursosGraficos, se muestra su nombre.
        for (Formato formato : pelicula.getFormatos()) {
            contenedorFormatos.getChildren().add(crearLogoFormato(formato));
        }

        Imagenes.mostrar(imgBanner, pelicula.getRutaBanner());   // llena todo el recuadro
        Imagenes.mostrar(imgCaratula, pelicula.getRutaCaratula());

        // Información que escribe el administrador (si está vacía, se avisa)
        lblSinopsis.setText(textoOAviso(pelicula.getSinopsis()));
        lblElenco.setText(textoOAviso(pelicula.getElenco()));
        lblEstudio.setText(textoOAviso(pelicula.getEstudio()));

        dibujarSecciones(pelicula);
    }

    /**
     * Secciones que puso el administrador, una debajo de otra e intercaladas como en AMC:
     * la 1.ª texto | foto, la 2.ª foto | texto, la 3.ª texto | foto... Sin secciones no se muestra nada.
     */
    private void dibujarSecciones(Pelicula pelicula) {
        List<SeccionPelicula> secciones = pelicula.getListSecciones();
        for (int i = 0; i < secciones.size(); i++) {
            boolean fotoALaDerecha = (i % 2 == 0);   // posiciones pares: foto a la derecha
            contenedorSecciones.getChildren().add(crearSeccion(secciones.get(i), fotoALaDerecha));
        }
    }

    private HBox crearSeccion(SeccionPelicula seccion, boolean fotoALaDerecha) {
        // Texto: etiqueta pequeña (opcional), título grande y el texto
        VBox texto = new VBox(16);
        texto.setAlignment(Pos.CENTER_LEFT);
        if (!seccion.getEtiqueta().isEmpty()) {
            Label etiqueta = new Label(seccion.getEtiqueta().toUpperCase());
            etiqueta.setStyle("-fx-text-fill: white; -fx-font-size: 17px; -fx-font-weight: bold;");
            texto.getChildren().add(etiqueta);
        }
        if (!seccion.getTitulo().isEmpty()) {
            Label titulo = new Label(seccion.getTitulo());
            titulo.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold;");
            titulo.setWrapText(true);
            texto.getChildren().add(titulo);
        }
        if (!seccion.getTexto().isEmpty()) {
            Label parrafo = new Label(seccion.getTexto());
            parrafo.setStyle("-fx-text-fill: #dddddd; -fx-font-size: 22px; -fx-line-spacing: 6;");
            parrafo.setWrapText(true);
            texto.getChildren().add(parrafo);
        }

        // Botón pequeño para comprar, como en AMC: lleva a los horarios de esta película
        Button comprar = new Button("Comprar entradas");
        comprar.getStyleClass().add("boton-principal");   // rojo y redondeado (gestion.css)
        comprar.setStyle("-fx-padding: 0 28 0 28;");
        comprar.setOnAction(e -> onComprarEntradas());
        texto.getChildren().add(comprar);

        HBox fila = new HBox(70);
        fila.setAlignment(Pos.CENTER);
        if (!seccion.tieneImagen()) {
            texto.setMaxWidth(1200);   // sin foto, el texto ocupa más ancho
            fila.getChildren().add(texto);
            return fila;
        }

        texto.setPrefWidth(760);
        texto.setMaxWidth(760);

        // Foto: llena su recuadro (se recorta lo que sobre) con las esquinas redondeadas
        ImageView foto = new ImageView();
        foto.setFitWidth(860);
        foto.setFitHeight(540);
        foto.setPreserveRatio(true);
        Imagenes.mostrar(foto, seccion.getRutaImagen());
        Rectangle esquinas = new Rectangle(860, 540);
        esquinas.setArcWidth(30);
        esquinas.setArcHeight(30);
        foto.setClip(esquinas);

        if (fotoALaDerecha) {
            fila.getChildren().addAll(texto, foto);
        } else {
            fila.getChildren().addAll(foto, texto);
        }
        return fila;
    }

    /**
     * Insignia de la clasificación con un color según la edad, como en las carteleras:
     * verde = todo público, amarillo = +12, naranja = +15, rojo = +18.
     */
    private String estiloClasificacion(Clasificacion clasificacion) {
        String fondo;
        String letra = "white";
        switch (clasificacion) {
            case TODOS:
                fondo = "#2e7d32";
                break;
            case MAYORES_7:
                fondo = "#689f38";
                break;
            case MAYORES_12:
                fondo = "#f9a825";
                letra = "#1a1a1a";   // sobre amarillo se lee mejor la letra oscura
                break;
            case MAYORES_15:
                fondo = "#ef6c00";
                break;
            default:
                fondo = "#c62828";   // MAYORES_18
                break;
        }
        return "-fx-background-color: " + fondo + "; -fx-text-fill: " + letra + ";"
                + " -fx-background-radius: 6; -fx-padding: 2 10 2 10; -fx-font-weight: bold; -fx-font-size: 15px;";
    }

    /** Logo del formato (Dolby Cinema un poco más grande, porque es largo y delgado). */
    private Node crearLogoFormato(Formato formato) {
        Image logo = Imagenes.cargarRecurso("recursosGraficos/" + formato.getLogo());
        if (logo == null) {
            Label nombre = new Label(formato.getNombre());
            nombre.getStyleClass().add("dato");
            return nombre;
        }
        ImageView vista = new ImageView(logo);
        vista.setPreserveRatio(true);
        vista.setFitHeight(45);         // todos más o menos del alto del de VIP Bistró
        vista.setFitWidth(170);         // límite para los logos muy alargados

        if (formato == Formato.CUATRO_DX) {
            vista.setFitHeight(60);
            vista.setFitWidth(200);
        }

        if (formato == Formato.VIP_BISTRO) {
            vista.setFitHeight(58);
            vista.setFitWidth(200);
        }

        // IMAX se ve muy grande: se achica
        if (formato == Formato.IMAX) {
            vista.setFitHeight(32);
            vista.setFitWidth(120);
        }

        // Prime un poquito más pequeño que IMAX
        if (formato == Formato.PRIME) {
            vista.setFitHeight(28);
            vista.setFitWidth(100);
        }

        // El logo de ScreenX es muy delgado: se agranda
        if (formato == Formato.SCREENX) {
            vista.setFitHeight(90);
            vista.setFitWidth(320);
        }
        return vista;
    }

    private String textoOAviso(String texto) {
        return texto.isEmpty() ? "Sin información registrada." : texto;
    }

    /** "Comprar entradas": muestra los horarios de esta película (estilo cartelera de cine). */
    @FXML
    private void onComprarEntradas() {
        detallePeliculaController.prepararCompra(detallePeliculaController.obtenerPeliculaSeleccionada());
        Navegador.irA(rootPane, "ConsultarFunciones.fxml");
    }

    @FXML
    private void onVolver() {
        Navegador.volverAtras(rootPane);
    }
}
