package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.Model.Train;
import com.example.trainstation_pa2.Model.visualStation;
import com.example.trainstation_pa2.Model.visualTrain;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class LineMapController extends HelloController {
    @FXML
    private StackPane mapContainer; // Map displayed here
    ArrayList<visualTrain> visualTrains = new ArrayList<>();

    @FXML
    public void initialize() {
        // 1. Draw the line (representing the railway track) on the stage
        Line trackLine = new Line(50, 150, 850, 150); //scene.Line, not the line class!
        trackLine.setStroke(Color.DARKRED);
        trackLine.setStrokeWidth(15);
        mapContainer.getChildren().add(trackLine);

        //2. add all the trains there currently are in the simulation, and add it a) according to their positions + b) states. (using addvisualtrain 2nd overload)
    }



    @FXML
    protected void addVisualTrain(Train toAdd) {
        visualTrain train = new visualTrain(toAdd, 50, 150);
        visualTrains.add(train);
        mapContainer.getChildren().add(train.getTrainShape());
    }

    @FXML
    protected void addVisualTrain(Train toAdd, double startX, double startY) {
        visualTrain train = new visualTrain(toAdd, startX, startY);
        visualTrains.add(train);
        mapContainer.getChildren().add(train.getTrainShape());
    }

    @FXML
    protected void removeVisualTrain(Train toRemove) {
        for (int i = 0; i < visualTrains.size(); i++) {
            visualTrain vt = visualTrains.get(i);
            if (vt.getTrain().equals(toRemove)) {
                visualTrains.remove(vt);
                mapContainer.getChildren().remove(vt.getTrainShape());
                return;
            }
        }
    }

    @FXML
    protected void update() {
        //TODO: a function that updates the stuff on the map
        // to be used whenever map is changed
        // basically does the same thing as initialize, just that it goes through every train + station status if monitored
        return;
    }
}
