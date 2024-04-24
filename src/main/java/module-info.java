module com.example.javafxversion2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxversion2 to javafx.fxml;
    exports com.example.javafxversion2;
}