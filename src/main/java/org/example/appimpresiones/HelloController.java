package org.example.appimpresiones;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloController extends Stage {
    private Stage currentStage;
    private BorderPane borderPane;
    public HelloController() {
        crearUI();

        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(getClass().getResource("/org/example/appimpresiones/estilos/styles.css").toExternalForm());
        this.setScene(scene);
    }

    private void crearUI() {
        TableViewExample tableViewExample = new TableViewExample();
        borderPane = new BorderPane();

        // Agregando la tabla al top de la app
        borderPane.setTop(tableViewExample.table);
    }
}