package org.example.appimpresiones;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HelloController helloController = new HelloController();
        stage.setTitle("simulador de impresiones");
        stage.setScene(helloController.getScene());
        stage.setResizable(true);
        stage.setHeight(600);
        stage.setWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}