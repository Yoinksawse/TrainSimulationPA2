package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.Model.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.ArrayList;

import static com.example.trainstation_pa2.Controller.MRTMainController.getMonitored;
import static com.example.trainstation_pa2.Controller.MRTMainController.setIsMapShowed;

public class LineMapController {
    @FXML
    VisualLine vline;
    @FXML
    private StackPane mapContainer;

    Simulation simulation;
    int timecounter;

    int currentTrainTimecounter = 0;

    public void initData(Simulation other, int timecounter) {
        this.simulation = other;
        this.timecounter = timecounter;
    }


    @FXML
    public void initialize() {
        setIsMapShowed();
        simulation = MRTMainController.getSimulation();

        //0. add button
        StackPane tickButtonPane = new StackPane();
        String title = MRTMainController.getLineName();
        Button tickButton;
        if (!Character.isLetter(title.charAt(0)) && !Character.isLetter(title.charAt(title.length() - 1))) {
            tickButton = new Button("下一站");
        }
        else tickButton = new Button("Next Tick");
        tickButton.setOnAction(event -> MRTMainController.externalTick());
        tickButtonPane.getChildren().add(tickButton);

        //1. draw line
        double y_pos = 150;

        Line line = simulation.getLine();
        String lnName = line.getName();
        String lnCode = line.getCode();
        ArrayList<Station> arl_stations = line.getStationsARL();
        ArrayList<Integer> arl_traveltime = line.getTravelTimeARL();

        int currentTrainTimecounter = 0;
        if (getMonitored() != null) {
            for (TrainTicker t : MRTMainController.getTrainTickers()) {
                if (t.getTrain().getTrainID().equals(getMonitored().getTrainID())) {
                    currentTrainTimecounter = t.getTrainTicker();
                    break;
                }
            }
        }
        this.vline = new VisualLine(lnName, lnCode, arl_stations, arl_traveltime, y_pos, currentTrainTimecounter);

        //2. set up container
        //Image background = new Image("com/example/trainstation_pa2/View/singaporemap_unmarked_edited.jpg");
        //TODO: make repeating background
       // mapContainer.setBackground(new Background());

        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(vline.visualise(this.simulation, getMonitored(), currentTrainTimecounter));

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
    public void closeWindow() {
        Stage stage = (Stage) mapContainer.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void nextTick() {
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

    @FXML
    public void resetStations() {
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(vline.visualise(this.simulation, null, 0));
    }
}