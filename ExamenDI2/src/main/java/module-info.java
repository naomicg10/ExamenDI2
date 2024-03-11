module com.example.examendi2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires javafx.swing;


    opens com.example.examendi2 to javafx.fxml;
    exports com.example.examendi2;
}