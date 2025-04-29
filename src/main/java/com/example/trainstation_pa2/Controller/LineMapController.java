package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.Model.*;
import com.example.trainstation_pa2.Model.Line;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class LineMapController extends HelloController {
    @FXML
    Simulation simulation;
    @FXML
    private StackPane mapContainer; // Map displayed here
    ArrayList<visualTrain> visualTrains = new ArrayList<>();

    @FXML
    public void initialize() {
        simulation = HelloController.getSimulation();
        // 1. Draw the line (representing the railway track) on the stage
        Line line = simulation.getLine();
        String lnName = line.getName();
        String lnCode = line.getCode();
        ArrayList<Station> arl_stations = line.getStationsARL();
        ArrayList<Integer> arl_traveltime = line.getTravelTimeARL();
        visualLine vline = new visualLine(lnName, lnCode, arl_stations, arl_traveltime, 150);

        mapContainer.getChildren().add(vline.visualise(this.simulation, HelloController.getMonitored()));
        //Line trackLine = new Line(50, 100, 800, 100); //scene.Line, not the line class!
        //trackLine.setStroke(Color.DARKRED);
        //trackLine.setStrokeWidth(15);

        //2. add all the trains there currently are in the simulation, and add it a) according to their positions + b) states. (using addvisualtrain 2nd overload)
    }

    @FXML
    public void initData(Simulation other) {
        this.simulation = other;
    }

    @FXML
    protected void addVisualTrain(Train toAdd) {
        visualTrain train = new visualTrain(toAdd, 50, 100);
        visualTrains.add(train);
        mapContainer.getChildren().add(train.getTrainShape());
    }

    @FXML
    protected void addVisualTrain(Train toAdd, double startX, double startY) {
        //add new visualtrain
        visualTrain train = new visualTrain(toAdd, startX, startY);
        //add styles
        train.getTrainShape().getStyleClass().add("trainShape");

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
        // 1. Update positions (move the trains along the line based on their speed/state).
        // Change colors/sizes to represent state (e.g., green = moving, red = stopped).
        // Add animations (like smooth movement).
    }
        /*
        for (visualTrain vt : visualTrains) {
            //new position (movement)
            double newX = calcNewX(vt);

            //bold border? blinking visualLine? (monitoring)

            //colour of border? train state


            vt.getTrainShape().setTranslateX(newX);
        }
    }

    private double calcNewX(visualTrain t) {

    }
    */
}






