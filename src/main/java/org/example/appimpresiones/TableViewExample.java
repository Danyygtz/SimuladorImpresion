package org.example.appimpresiones;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TableViewExample{
    public TableView<DataItem> table;
    public TableViewExample() {
        // Crear la tabla
        table = new TableView<>();
        // Configurar tabla
        // Configurar el ajuste automático de las columnas
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Crear las columnas
        TableColumn<DataItem, String> column1 = new TableColumn<>("No. Archivo");
        column1.setCellValueFactory(cellData -> cellData.getValue().string1Property());

        TableColumn<DataItem, String> column2 = new TableColumn<>("Nombre");
        column2.setCellValueFactory(cellData -> cellData.getValue().string2Property());

        TableColumn<DataItem, Integer> column3 = new TableColumn<>("Hojas a imprimir");
        column3.setCellValueFactory(cellData -> cellData.getValue().intProperty().asObject());

        TableColumn<DataItem, String> column4 = new TableColumn<>("Fecha de impresión");
        column4.setCellValueFactory(cellData -> cellData.getValue().dateAsStringProperty());

        TableColumn<DataItem, ProgressIndicator> progressColumn = new TableColumn<>("Barra de Progreso");
        progressColumn.setCellValueFactory(cellData -> cellData.getValue().progressProperty());
        progressColumn.getStyleClass().add("table-cell-centered");

        TableColumn<DataItem, Double> progressColumnBar = new TableColumn<>("Progreso");
        progressColumnBar.setCellValueFactory(cellData -> cellData.getValue().getProgressBar());

        // Configurar la fábrica de celdas para la columna de progreso
        progressColumnBar.setCellFactory(new Callback<TableColumn<DataItem, Double>, TableCell<DataItem, Double>>() {
            @Override
            public ProgressBarCell call(TableColumn<DataItem, Double> param) {
                return new ProgressBarCell();
            }
        });

        // Agregar las columnas a la tabla
        table.getColumns().addAll(column1, column2, column3, column4, progressColumn, progressColumnBar);

        // Agregar datos de ejemplo
        for (int i = 0; i < 10; i++) {
            table.getItems().add(new DataItem("Dato1", "Dato2", i * 10, LocalDate.now().plusDays(i), new ProgressIndicator(), i * 0.1));
        }
    }

    // Clase para representar los datos en la tabla
    public static class DataItem {
        private final DoubleProperty progressBar;
        private final SimpleStringProperty string1;
        private final SimpleStringProperty string2;
        private final SimpleIntegerProperty intProperty;
        private final SimpleObjectProperty<LocalDate> dateProperty;
        private final SimpleObjectProperty<ProgressIndicator> progressProperty;

        public DataItem(String string1, String string2, int intValue, LocalDate dateValue, ProgressIndicator progressValue, Double progress) {
            this.string1 = new SimpleStringProperty(string1);
            this.string2 = new SimpleStringProperty(string2);
            this.intProperty = new SimpleIntegerProperty(intValue);
            this.dateProperty = new SimpleObjectProperty<>(dateValue);
            this.progressProperty = new SimpleObjectProperty<>(progressValue);
            this.progressBar = new SimpleDoubleProperty(progress);
        }

        public ObservableValue<Double> getProgressBar() {
            return progressBar.asObject();
        }

        public SimpleStringProperty string1Property() {
            return string1;
        }

        public SimpleStringProperty string2Property() {
            return string2;
        }

        public SimpleIntegerProperty intProperty() {
            return intProperty;
        }

        public SimpleObjectProperty<LocalDate> dateProperty() {
            return dateProperty;
        }

        public SimpleObjectProperty<ProgressIndicator> progressProperty() {
            return progressProperty;
        }

        public SimpleStringProperty dateAsStringProperty() {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = dateProperty.get().format(dateFormat);
            return new SimpleStringProperty(formattedDate);
        }

        // Método para animar la ProgressBar automáticamente
        private void animateProgressBar() {
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar, 0.0)),
                    new KeyFrame(Duration.seconds(10), new KeyValue(progressBar, 1.0))
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    // Clase de celda personalizada con ProgressBar
    public static class ProgressBarCell extends javafx.scene.control.TableCell<DataItem, Double> {
        private final ProgressBar progressBar = new ProgressBar();

        @Override
        protected void updateItem(Double progress, boolean empty) {
            super.updateItem(progress, empty);

            if (empty || progress == null) {
                setGraphic(null);
            } else {
                progressBar.setProgress(progress);
                setGraphic(progressBar);
            }
        }
    }
}