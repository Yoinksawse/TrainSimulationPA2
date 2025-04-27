package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.Model.visualStation;
import com.example.trainstation_pa2.Model.visualSimulation;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class LineMapController extends HelloController {
    @FXML
    private StackPane mapContainer; //map displayed here

    public void showMap() {
        visualSimulation visualApp = new visualSimulation();
        Group mapGroup = visualApp.visualise();
        mapContainer.getChildren().setAll(mapGroup);  // Replace contents
    }

    @FXML
    public void initialize() {
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
    }

    @FXML
    protected static void update() {
        //TODO: a function that updates the stuff on the map
        // to be used whenever map is changed
        return;

        //使用中国地铁车厢门上方的站头显示牌的格式
    }
}
