module org.example.appimpresiones {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.appimpresiones to javafx.fxml;
    exports org.example.appimpresiones;
}