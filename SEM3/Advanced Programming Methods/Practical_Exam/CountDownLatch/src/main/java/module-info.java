module com.example.a7_gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.a7_gui to javafx.fxml;
    exports com.example.a7_gui;
}