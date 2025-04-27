package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.HelloApplication;
import com.example.trainstation_pa2.Model.visualStation;
import com.example.trainstation_pa2.VisualApplication;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class LineMapController extends HelloApplication {

    @FXML
    private StackPane mapContainer; //map displayed here

    public void showMap() {
        // Assuming VisualApplication is a class with a method to return a Group or Node to be added
        VisualApplication visualApp = new VisualApplication();
        Group mapGroup = visualApp.visualise();  // The visual representation of the map
        mapContainer.getChildren().clear();  // Clear previous content if any
        mapContainer.getChildren().add(mapGroup);  // Add the new map to the container
    }

    @FXML
    protected void initialize(Stage stage) {
        //TODO: a function that updates the stuff on the map
        // to be used whenever map is changed
        Pane root = new Pane();

        // Create a station circle
        visualStation station = new visualStation(100, 100, 15, "dummy, TODO change later");

        // Add the circle to the scene
        root.getChildren().add(station.getCircle());

        // Example: after 2 seconds, mark it "reached" (pink)
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
        pause.setOnFinished(e -> station.updateColor(2));
        pause.play();

        Scene scene = new Scene(root, 500, 1000);
        stage.setScene(scene);
        stage.setTitle("MRT Visualizer");
        stage.show();
        return;
    }

    @FXML
    protected static void update() {
        //TODO: a function that updates the stuff on the map
        // to be used whenever map is changed
        return;

        //使用中国地铁车厢门上方的站头显示牌的格式
    }
}
