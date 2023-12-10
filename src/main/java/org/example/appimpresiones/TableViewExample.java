package org.example.appimpresiones;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TableViewExample extends Thread {
    // Variable estática para manejar las animaciones
    public static boolean flagAnimation = false;
    // Variable para manejar la tabla
    public static TableView<DataItem> table;
    // Variable para manejar los elementos de la tabla
    public static Queue<DataItem> progressQueue;
    // Variable para manejar el flujo (Stop/Start)
    private boolean flagStop;

    // Método implementado para manejar el Thread
    @Override
    public void run() {
        // Ciclo infinito
        while (true) {
            // Si no la cola de procesos es vacia
            // Si no Existe una animación actualmente en proceso
            // Si no se ha presionado el botón stop
            // Rntonces inicia el if
            if(!progressQueue.isEmpty() && !flagAnimation && !flagStop) {
                // método para ejecutar procesos fuera del Thread
                Platform.runLater(() -> {
                    // Método peek para traer el elemento cabeza sin eliminarlo
                    // Se está obteniendo el primer proceso e iniciando su proceso (animación)
                    progressQueue.peek().animateProgressBar();
                });
            }
            try {
                // Deten el programa 1 segundo
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public TableViewExample() {
        // Crear la tabla
        table = new TableView<>();
        // Crea el arrayList para almacenar los elementos
        progressQueue = new ConcurrentLinkedQueue<>();
        // Inicializando la variable en falso (No se ha presioando el botón)
        this.flagStop = false;
        // Configurar tabla
        // Configurar el ajuste automático (tamaño) de las columnas
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Crear las columnas
        TableColumn<DataItem, Integer> column1 = new TableColumn<>("No. Archivo");
        column1.setCellValueFactory(cellData -> cellData.getValue().intProperty1().asObject());

        TableColumn<DataItem, String> column2 = new TableColumn<>("Nombre");
        column2.setCellValueFactory(cellData -> cellData.getValue().string2Property());

        TableColumn<DataItem, Integer> column3 = new TableColumn<>("Hojas a imprimir");
        column3.setCellValueFactory(cellData -> cellData.getValue().intProperty2().asObject());

        TableColumn<DataItem, String> column4 = new TableColumn<>("Fecha de impresión");
        column4.setCellValueFactory(cellData -> cellData.getValue().dateAsStringProperty());

        //TableColumn<DataItem, ProgressIndicator> progressColumn = new TableColumn<>("Barra de Progreso");
        //progressColumn.setCellValueFactory(cellData -> cellData.getValue().progressProperty());
        //progressColumn.getStyleClass().add("table-cell-centered");

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
        table.getColumns().addAll(column1, column2, column3, column4, progressColumnBar);
    }

    public boolean isFlag() {
        return flagStop;
    }

    public void setFlag(boolean flag) {
        this.flagStop = flag;
    }

    public void on() {
        this.flagStop = !flagStop;
    }

    // Clase para representar los datos en la tabla
    public static class DataItem {
        private final DoubleProperty progressBar;
        private final SimpleIntegerProperty string1;
        private final SimpleStringProperty string2;
        private final SimpleIntegerProperty intProperty;
        private final SimpleObjectProperty<LocalDate> dateProperty;
        //private final SimpleObjectProperty<ProgressIndicator> progressProperty;

        public DataItem(int string1, String string2, int intValue, LocalDate dateValue, Double progress) {
            this.string1 = new SimpleIntegerProperty(string1);
            this.string2 = new SimpleStringProperty(string2);
            this.intProperty = new SimpleIntegerProperty(intValue);
            this.dateProperty = new SimpleObjectProperty<>(dateValue);
            //this.progressProperty = new SimpleObjectProperty<>(progressValue);
            this.progressBar = new SimpleDoubleProperty(progress);
        }

        public ObservableValue<Double> getProgressBar() {
            return progressBar.asObject();
        }

        public void addProgressBar() {
            animateProgressBar();
        }

        public SimpleIntegerProperty intProperty1() {
            return string1;
        }

        public SimpleStringProperty string2Property() {
            return string2;
        }

        public SimpleIntegerProperty intProperty2() {
            return intProperty;
        }

        public SimpleObjectProperty<LocalDate> dateProperty() {
            return dateProperty;
        }

        //public SimpleObjectProperty<ProgressIndicator> progressProperty() {
            //return progressProperty;
        //}

        // método para obtener la fecha
        public SimpleStringProperty dateAsStringProperty() {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = dateProperty.get().format(dateFormat);
            return new SimpleStringProperty(formattedDate);
        }

        // Método para animar la ProgressBar automáticamente
        private void animateProgressBar() {
            // Se cambia el proceso a que ya existe una animación en proceso
            flagAnimation = true;
            // Se crea la animacion con 10s
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar, 0.0)),
                    new KeyFrame(Duration.seconds(10), new KeyValue(progressBar, 1.0))
            );
            // Se configura el proceso final cuando termina la animación
            timeline.setOnFinished(event -> {
                // se cambia la variable para saber que no hay ninguna animación actualmente
                flagAnimation = false;
                // método poll para traer el elemento cabeza y eliminarlo
                // Se elimina el elemento de la tabla
                TableViewExample.table.getItems().remove(progressQueue.poll());
            });
            // se configura para que la animación nada más se realice 1 vez
            timeline.setCycleCount(1);
            // se inicia la animación
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