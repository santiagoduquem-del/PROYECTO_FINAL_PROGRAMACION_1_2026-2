module co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq to javafx.fxml;
    opens co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.viewController to javafx.fxml;
    exports co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq;

    // Para que JUnit pueda ejecutar las pruebas de estos paquetes
    opens co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.model to org.junit.platform.commons;
    opens co.edu.uniquindio.cineuq.proyectofinalprogramacion1cineuq.controller to org.junit.platform.commons;
}