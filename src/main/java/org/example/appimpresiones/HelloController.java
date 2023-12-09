package org.example.appimpresiones;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HelloController extends Stage {
    private int id = 1;
    private BorderPane borderPane;
    private Label statusIndicator;
    public HelloController() {
        crearUI();
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(getClass().getResource("/org/example/appimpresiones/estilos/styles.css").toExternalForm());
        this.setScene(scene);
    }

    private void crearUI() {
        TableViewExample tableViewExample = new TableViewExample();
        borderPane = new BorderPane();

        borderPane.setCenter(TableViewExample.table);

        // Crear un botón para agregar nuevos elementos
        Button addButton = new Button("Agregar Nuevo Elemento");
        addButton.setOnAction(e -> {
            TableViewExample.DataItem dataItem = new TableViewExample.DataItem(id, generarNombre(), ((int)(Math.random() * 100)), LocalDate.now(), 0.0);
            TableViewExample.progressQueue.add(dataItem);
            TableViewExample.table.getItems().add(dataItem);
            id++;
        });

        // Crear un botón para pausar las barras de progreso
        Button pauseButton = new Button("Pausar/Reanudar");
        pauseButton.setOnAction(e -> {
            tableViewExample.on();
            if (tableViewExample.isFlag()) {
                statusIndicator.setText("●");
                statusIndicator.setTextFill(Color.RED);
            } else {
                statusIndicator.setText("●");
                statusIndicator.setTextFill(Color.GREEN);
            }
        });

        // Crear el layout para mostrar el estado del servicio
        HBox statusBox = new HBox();
        Label statusLabel = new Label("Estado: ");
        statusIndicator = new Label("●");
        statusIndicator.setTextFill(Color.GREEN);
        statusBox.getChildren().addAll(statusLabel, statusIndicator);

        // Crear el diseño principal
        VBox root = new VBox();
        root.setSpacing(10);

        root.getChildren().addAll(addButton, pauseButton, statusBox);

        borderPane.setBottom(root);
        tableViewExample.start();

    }

    private String generarNombre() {
        String[] extensiones = {"pdf", "jpeg", "jpg", "png", "jpe", "tiff", "tif", "docx", "xlsx", "pptx"};
        // generar numero random para seleccionar la extension
        int idExtension = (int) (Math.random() * extensiones.length);
        // generar fecha y hora
        // Obtener la fecha y hora actual
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Formatear la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime+extensiones[idExtension];
    }
}