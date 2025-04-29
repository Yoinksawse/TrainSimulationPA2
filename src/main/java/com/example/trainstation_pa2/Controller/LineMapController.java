package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.Model.*;
import com.example.trainstation_pa2.Model.Line;
import com.sun.source.tree.LineMap;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

public class LineMapController extends HelloController {
    @FXML
    Simulation simulation;
    @FXML
    visualLine vline;
    @FXML
    private StackPane mapContainer;
    //ArrayList<Train> trains;
    ArrayList<visualTrain> visualTrains = new ArrayList<>();
    int timecounter;

    @FXML
    public void initialize() {
        simulation = HelloController.getSimulation();
        //1. draw line
        double y_pos = 150;

        Line line = simulation.getLine();
        String lnName = line.getName();
        String lnCode = line.getCode();
        ArrayList<Station> arl_stations = line.getStationsARL();
        ArrayList<Integer> arl_traveltime = line.getTravelTimeARL();
        this.vline = new visualLine(lnName, lnCode, arl_stations, arl_traveltime, y_pos, timecounter);

        mapContainer.getChildren().add(vline.visualise(this.simulation, HelloController.getMonitored(), this.timecounter));

        //2. add all the trains there currently are in the simulation, and add it a) according to their positions + b) states. (using addvisualtrain 2nd overload)
        ArrayList<Train> trains = simulation.getTrainsARL();
        for (Train t : trains) {
            visualTrain vt = new visualTrain(t, 50, y_pos);
            mapContainer.getChildren().add(vt);
        }
    }

    @FXML
    public void initData(Simulation other, int timecounter) {
        this.simulation = other;
        this.timecounter = timecounter;
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

    protected void monitorTrain(Train toMonitor) {
        this.vline.monitorTrain(toMonitor);
    }

    protected void deMonitorTrain() {
        this.vline.deMonitorTrain();
    }

    public void closeWindow() {
        Stage stage = (Stage) mapContainer.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void nexttick() {
        //TODO: a function that updates the stuff on the map
        // to be used whenever map is changed
        // basically does the same thing as initialize, just that it goes through every train + station status if monitored
        // 1. Update positions (move the trains along the line based on their speed/state).
        // Change colors/sizes to represent state (e.g., green = moving, red = stopped).
        // Add animations (like smooth movement).
        for (visualTrain vt : visualTrains) {
            Train t = vt.getTrain();

            //1. find new xpos, move train
            double newX = vt.getX() + this.vline.getIntervalLength();
            vt.moveTo(newX, vt.getY());

            //3. update styling (train state)
            Color borderColor;
            if (t.isDelayed()) borderColor = Color.RED;
            else if (!t.isStopped()) borderColor = Color.GREEN;
            else borderColor = Color.BLACK;
            vt.setBorderColour(borderColor);

            //2. update monitoring
            vt.getTrainShape().getStyleClass().removeAll("glossy-border", "normal-border"); //updating styles before vt setmonitored

            if (t.isMonitored()) this.vline.monitorTrain(t);
            vt.setMonitored(t.isMonitored());
        }
        if (!getMonitored().isServiceEnded()) {
            vline.visualise(this.simulation, getMonitored(), this.timecounter);
            //System.out.println("timecounter: " + this.timecounter);
        }
    }
}






