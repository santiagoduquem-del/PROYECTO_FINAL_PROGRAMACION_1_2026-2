package co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // La primera pantalla es la intro; al terminar, ella misma abre el Login
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ventanaBienvenida.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1200);
        stage.setTitle("CinemaUQ");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}