module com.example.trainstation_pa2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens com.example.trainstation_pa2 to javafx.fxml;
    exports com.example.trainstation_pa2;
    exports com.example.trainstation_pa2.Controller;
    opens com.example.trainstation_pa2.Controller to javafx.fxml;
    exports com.example.trainstation_pa2.Model;
    opens com.example.trainstation_pa2.Model to javafx.fxml;
    exports com.example.trainstation_pa2.View;
    opens com.example.trainstation_pa2.View to javafx.fxml;
}