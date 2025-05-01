package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.example.trainstation_pa2.Controller.MRTMainController.getMonitored;

public class LineMapController {
    @FXML
    VisualLine vline;
    @FXML
    private StackPane mapContainer;

    Simulation simulation;
    int timecounter;

    public void initData(Simulation other, int timecounter) {
        this.simulation = other;
        this.timecounter = timecounter;
    }


    @FXML
    public void initialize() {
        simulation = MRTMainController.getSimulation();

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
    }


    @FXML
    public void resetStations() {
        for (Node node : mapContainer.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                circle.setFill(Color.WHITE);
            }
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) mapContainer.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void nexttick() {
        if (getMonitored() != null ) {
            int currentTrainTimecounter = 0;
            for (TrainTicker t: MRTMainController.getTrainTickers()) {
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