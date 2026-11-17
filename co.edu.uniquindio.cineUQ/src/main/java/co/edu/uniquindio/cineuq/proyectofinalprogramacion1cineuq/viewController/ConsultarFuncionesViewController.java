package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController;

import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller.ConsultarFuncionesController;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Formato;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Funcion;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Idioma;
import co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model.Pelicula;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Horarios de proyección, como en las páginas de cine:
 *   Película
 *     Lunes, 16 de noviembre
 *       [logo IMAX]  SUBTITULADA
 *       ( 14:00 )  ( 18:30 )      <- botones; debajo: sala y asientos libres
 * Al hacer clic en una hora se abre Generar venta con esa función ya elegida.
 */
public class ConsultarFuncionesViewController extends BarraSuperiorViewController {

    // Estilo de los botones de hora: borde blanco redondeado; al pasar el mouse se rellenan
    private static final String ESTILO_HORA =
            "-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2;"
            + " -fx-border-radius: 30; -fx-background-radius: 30; -fx-text-fill: white;"
            + " -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;";
    private static final String ESTILO_HORA_ENCIMA =
            "-fx-background-color: white; -fx-border-color: white; -fx-border-width: 2;"
            + " -fx-border-radius: 30; -fx-background-radius: 30; -fx-text-fill: black;"
            + " -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;";

    private static final DateTimeFormatter FORMATO_DIA =
            DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale.forLanguageTag("es-CO"));

    @FXML private AnchorPane rootPane;
    @FXML private Label lblTitulo;
    @FXML private Label lblSubtitulo;
    @FXML private VBox contenedorHorarios;

    private final ConsultarFuncionesController consultarFuncionesController = new ConsultarFuncionesController();

    @FXML
    public void initialize() {
        if (consultarFuncionesController.obtenerPeliculaElegida() == null) {
            lblSubtitulo.setText("Todas las películas en cartelera. Elija un horario para comprar sus entradas.");
        }

        List<Pelicula> peliculas = consultarFuncionesController.obtenerPeliculasAMostrar();
        if (peliculas.isEmpty()) {
            contenedorHorarios.getChildren().add(crearAviso("No hay funciones próximas programadas."));
        }
        for (Pelicula pelicula : peliculas) {
            contenedorHorarios.getChildren().add(crearBloquePelicula(pelicula));
        }
    }

    // ------------------------------------------------------------------ Película

    /** Encabezado de la película y, debajo, sus horarios agrupados por día. */
    private VBox crearBloquePelicula(Pelicula pelicula) {
        VBox bloque = new VBox(44);   // espacio entre el encabezado y cada día
        bloque.getStyleClass().add("panel");   // cada película en su propia tarjeta (estilo de gestion.css)
        bloque.getChildren().add(crearEncabezadoPelicula(pelicula));

        List<Funcion> funciones = consultarFuncionesController.obtenerFuncionesProximas(pelicula);
        if (funciones.isEmpty()) {
            bloque.getChildren().add(crearAviso("Esta película no tiene funciones próximas."));
            return bloque;
        }

        // Las funciones ya vienen ordenadas por fecha y hora: se arma un grupo por cada día
        List<Funcion> delDia = new ArrayList<>();
        LocalDate diaActual = null;
        for (Funcion funcion : funciones) {
            if (diaActual != null && !funcion.getFecha().equals(diaActual)) {
                bloque.getChildren().add(crearBloqueDia(diaActual, delDia));
                delDia = new ArrayList<>();
            }
            diaActual = funcion.getFecha();
            delDia.add(funcion);
        }
        bloque.getChildren().add(crearBloqueDia(diaActual, delDia));
        return bloque;
    }

    /** Carátula pequeña, nombre y datos cortos de la película. */
    private HBox crearEncabezadoPelicula(Pelicula pelicula) {
        Label sinCaratula = new Label("Sin carátula");
        sinCaratula.getStyleClass().add("sin-imagen");
        ImageView caratula = new ImageView();
        caratula.setFitWidth(90);
        caratula.setFitHeight(135);
        caratula.setPreserveRatio(true);
        Imagenes.mostrar(caratula, pelicula.getRutaCaratula());
        StackPane marco = new StackPane(sinCaratula, caratula);
        marco.getStyleClass().add("marco-imagen");
        marco.setPrefSize(90, 135);
        marco.setMaxSize(90, 135);

        Label nombre = new Label(pelicula.getNombre());
        nombre.getStyleClass().add("titulo-pelicula");
        nombre.setStyle("-fx-font-size: 32px;");

        Label datos = new Label(pelicula.getClasificacion().getDescripcion()
                + "   ·   " + pelicula.getDuracionMinutos() + " min"
                + "   ·   " + pelicula.getGenero());
        datos.getStyleClass().add("etiqueta");
        datos.setStyle("-fx-font-size: 17px;");

        VBox textos = new VBox(8, nombre, datos);
        textos.setAlignment(Pos.CENTER_LEFT);

        HBox encabezado = new HBox(22, marco, textos);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        return encabezado;
    }

    // ------------------------------------------------------------------ Día

    /** "Lunes, 16 de noviembre" y, debajo, un grupo por cada formato e idioma de ese día. */
    private VBox crearBloqueDia(LocalDate dia, List<Funcion> funcionesDelDia) {
        Label titulo = new Label(describirDia(dia));
        titulo.setMaxWidth(Double.MAX_VALUE);
        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;"
                + " -fx-border-color: transparent transparent #5a5a5a transparent; -fx-border-width: 0 0 2 0;"
                + " -fx-padding: 0 0 8 0;");

        VBox bloque = new VBox(30, titulo);   // espacio entre el título del día y cada formato

        // Se agrupan las funciones que tienen el mismo formato e idioma (ej.: IMAX · Subtitulada)
        List<String> gruposVistos = new ArrayList<>();
        for (Funcion funcion : funcionesDelDia) {
            String clave = funcion.getFormato().name() + "-" + funcion.getIdioma().name();
            if (gruposVistos.contains(clave)) {
                continue;
            }
            gruposVistos.add(clave);

            List<Funcion> delGrupo = new ArrayList<>();
            for (Funcion otra : funcionesDelDia) {
                if (otra.getFormato() == funcion.getFormato() && otra.getIdioma() == funcion.getIdioma()) {
                    delGrupo.add(otra);
                }
            }
            bloque.getChildren().add(crearGrupoFormato(funcion.getFormato(), funcion.getIdioma(), delGrupo));
        }
        return bloque;
    }

    /** "Hoy · lunes, 16 de noviembre", "Mañana · …" o solo la fecha. */
    private String describirDia(LocalDate dia) {
        String texto = dia.format(FORMATO_DIA);
        texto = texto.substring(0, 1).toUpperCase() + texto.substring(1);
        if (dia.equals(LocalDate.now())) {
            return "Hoy  ·  " + texto;
        }
        if (dia.equals(LocalDate.now().plusDays(1))) {
            return "Mañana  ·  " + texto;
        }
        return texto;
    }

    // ------------------------------------------------------------------ Formato e idioma

    /** Logo del formato + idioma, y debajo los botones con las horas. */
    private VBox crearGrupoFormato(Formato formato, Idioma idioma, List<Funcion> funciones) {
        Label lblIdioma = new Label(idioma.getNombre().toUpperCase());
        lblIdioma.setStyle("-fx-text-fill: #d0d0d0; -fx-font-size: 20px;");

        HBox encabezado = new HBox(18, crearLogoFormato(formato), lblIdioma);
        encabezado.setAlignment(Pos.CENTER_LEFT);

        FlowPane horas = new FlowPane(40, 24);   // (horizontal, vertical) entre botones de hora
        for (Funcion funcion : funciones) {
            horas.getChildren().add(crearBotonHora(funcion));
        }

        VBox grupo = new VBox(20, encabezado, horas);   // entre el logo y los botones
        grupo.setStyle("-fx-padding: 0 0 0 4;");
        return grupo;
    }

    /** Botón con la hora; debajo, la sala y cuántos asientos quedan libres. */
    private VBox crearBotonHora(Funcion funcion) {
        Button boton = new Button(funcion.getHora().toString());
        boton.setPrefSize(150, 52);
        boton.setStyle(ESTILO_HORA);
        boton.setOnMouseEntered(e -> boton.setStyle(ESTILO_HORA_ENCIMA));
        boton.setOnMouseExited(e -> boton.setStyle(ESTILO_HORA));

        int libres = funcion.contarAsientosDisponibles();
        Label detalle = new Label(funcion.getSala().getNombre() + "  ·  "
                + (libres == 0 ? "AGOTADO" : libres + " libres"));
        detalle.setStyle("-fx-text-fill: " + (libres == 0 ? "#e57373" : "#bdbdbd") + "; -fx-font-size: 13px;");

        if (libres == 0) {
            boton.setDisable(true);   // sin asientos no se puede comprar
        }

        // Clic en la hora: se abre Generar venta con esta función ya elegida
        boton.setOnAction(e -> {
            consultarFuncionesController.seleccionarFuncion(funcion);
            Navegador.irA(rootPane, "RegistrarVenta.fxml");
        });

        VBox caja = new VBox(8, boton, detalle);   // entre el botón y "Sala · libres"
        caja.setAlignment(Pos.CENTER);
        return caja;
    }

    /** Logo del formato (o su nombre en texto si el logo no está en recursosGraficos). */
    private Node crearLogoFormato(Formato formato) {
        Image logo = Imagenes.cargarRecurso("recursosGraficos/" + formato.getLogo());
        if (logo == null) {
            Label nombre = new Label(formato.getNombre());
            nombre.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
            return nombre;
        }
        ImageView vista = new ImageView(logo);
        vista.setPreserveRatio(true);
        vista.setFitHeight(36);
        vista.setFitWidth(150);
        return vista;
    }

    private Label crearAviso(String texto) {
        Label aviso = new Label(texto);
        aviso.getStyleClass().add("etiqueta");
        aviso.setStyle("-fx-font-size: 18px;");
        return aviso;
    }

    @FXML
    private void onVolver() {
        Navegador.volverAtras(rootPane);
    }
}
