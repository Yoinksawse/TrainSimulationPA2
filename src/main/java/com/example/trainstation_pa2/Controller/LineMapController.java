package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.HelloApplication;
import com.example.trainstation_pa2.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.example.trainstation_pa2.Controller.HelloController.getMonitored;

public class LineMapController extends HelloApplication {
    @FXML
    Simulation simulation;
    @FXML
    VisualLine vline;
    @FXML
    private StackPane mapContainer;
    //ArrayList<Train> trains;
    ArrayList<VisualTrain> visualTrains = new ArrayList<>();
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
        this.vline = new VisualLine(lnName, lnCode, arl_stations, arl_traveltime, y_pos, timecounter);

        //2. set up container
        //Image background = new Image("com/example/trainstation_pa2/View/singaporemap_unmarked_edited.jpg");
        //TODO: make repeating background
       // mapContainer.setBackground(new Background());

        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(vline.visualise(this.simulation, getMonitored(), timecounter));

        final double[] mouseAnchor = new double[2];
        final double[] initialTranslate = new double[2];

        mapContainer.setOnMousePressed(e -> {
            mouseAnchor[0] = e.getSceneX();
            mouseAnchor[1] = e.getSceneY();
            initialTranslate[0] = mapContainer.getTranslateX();
            initialTranslate[1] = mapContainer.getTranslateY();
        });

        mapContainer.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseAnchor[0];
            double dy = e.getSceneY() - mouseAnchor[1];
            mapContainer.setTranslateX(initialTranslate[0] + dx);
            mapContainer.setTranslateY(initialTranslate[1] + dy);
        });

        /*
        //2. add all the trains there currently are in the simulation, and add it a) according to their positions + b) states. (using addvisualtrain 2nd overload)
        ArrayList<Train> trains = new ArrayList<>(List.of(simulation.getTrains()));
        for (Train t : trains) {
            VisualTrain vt = this.addVisualTrain(t, 50, y_pos);
            mapContainer.getChildren().add(vt);
        }

        //display all the trains
        for (VisualTrain vt : visualTrains) {
            Train t = vt.getTrain();
            //0. adjust dimensions
            vt.getTrainShape().setScaleX(0.1);
            vt.getTrainShape().setScaleY(0.1);

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

            if (t.isMonitored()) {
                this.vline.monitorTrain(t);
                vt.setMonitored(t.isMonitored());
            }
        }
        if (getMonitored() != null && !getMonitored().isServiceEnded()) {
            vline.visualise(this.simulation, getMonitored(), this.timecounter);
        }


         */
    }

    @FXML
    public void initData(Simulation other, int timecounter) {
        this.simulation = other;
        this.timecounter = timecounter;
    }

    /*
    @FXML
    protected VisualTrain addVisualTrain(Train toAdd, double startX, double startY) {
        //add new visualtrain
        VisualTrain vt = new VisualTrain(toAdd, startX, startY);

        //1. find new xpos, move train
        double newX = vt.getX() + this.vline.getIntervalLength();
        vt.moveTo(newX, vt.getY());

        //add styles
        vt.getTrainShape().getStyleClass().add("trainShape");
        vt.setScaleX(0.1);
        vt.setScaleY(0.1);

        visualTrains.add(vt);
        mapContainer.getChildren().add(vt.getTrainShape());
        return vt;
    }

    @FXML
    protected void removeVisualTrain(Train toRemove) {
        for (int i = 0; i < visualTrains.size(); i++) {
            VisualTrain vt = visualTrains.get(i);
            if (vt.getTrain().equals(toRemove)) {
                visualTrains.remove(vt);
                mapContainer.getChildren().remove(vt.getTrainShape());

                vline.deMonitorTrain();

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


     */
    public void resetStations() {
        for (Node node : mapContainer.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                circle.setFill(Color.WHITE); // Default color
                // Or use a predefined method if available:
                // setCircleColour(circle, DEFAULT_COLOR);
            }
        }
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
        /*
        for (VisualTrain vt : visualTrains) {
            Train t = vt.getTrain();
            //0. adjust dimensions
            vt.getTrainShape().setScaleX(0.1);
            vt.getTrainShape().setScaleY(0.1);

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

            if (t.isMonitored()) {
                this.vline.monitorTrain(t);
                vt.setMonitored(t.isMonitored());
            }
        }

         */
        if (getMonitored() != null ) {
            int currentTrainTimecounter = 0;
            for (TrainTicker t: HelloController.getTrainTickers()) {
                if (t.getTrain().getTrainID().equals(getMonitored().getTrainID())){
                    currentTrainTimecounter = t.getTrainTicker();
                    break;
                }
            }
            mapContainer.getChildren().clear();
            mapContainer.getChildren().add(vline.visualise(this.simulation, getMonitored(), currentTrainTimecounter));
        }
    }
}