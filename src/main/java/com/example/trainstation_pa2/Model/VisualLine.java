package com.example.trainstation_pa2.Model;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.util.Duration;

import java.util.ArrayList;

public class VisualLine {
    private String name;
    private String code;
    private double y_pos;
    private boolean colourise = false;
    private ArrayList<Station> stations = new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();
    private ArrayList<Integer> interStationCnt = new ArrayList<>();
    private ArrayList<Integer> visited = new ArrayList<>();
    private ArrayList<Train> trains = new ArrayList<>();

    com.example.trainstation_pa2.Model.Line thisLine;
    Simulation simul;
    Train monitored;
    Group currentLine = new Group();
    double interval;
    int timeCounter;
    int totalPoints;

    //y_pos usually 150
    public VisualLine(String name, String code, ArrayList<Station> stations, ArrayList<Integer> travelTime, double y_pos, int timeCounter) {
        this.name = name;
        this.code = code;
        this.y_pos = y_pos;
        this.stations.addAll(stations);
        this.travelTime.addAll(travelTime);
        this.timeCounter = timeCounter;
    }

    public Group visualise(Simulation simul, Train monitored, int timeCounter) {
        //Clean UI
        currentLine.getChildren().clear();

        //setup core datafields
        this.simul = simul;
        this.thisLine = this.simul.getLine();
        this.timeCounter = timeCounter;

        //monitor
        this.monitored = monitored;
        this.colourise = (this.monitored != null);
        prepareData(); //start prepare data for drawing

        //draw baseline
        double lineLength = (totalPoints) * 25;
        interval = lineLength / (totalPoints - 1); //store this, will be used for coordinating later.

        Line baseline = new Line(50, y_pos, 50 + lineLength, y_pos);
        baseline.setStroke(Color.web("#6B3143"));
        baseline.setStrokeWidth(11);
        currentLine.getChildren().add(baseline);

        int pointsPassed = 0;
        for (int i = 0; i < this.stations.size() - 1; i++) {
            // show station
            drawStation(pointsPassed, true, i);

            for (int j = 0; j < interStationCnt.get(i); j++) {
                pointsPassed++;
                //show interstation
                drawStation(pointsPassed, false, -1);
            }
            pointsPassed++;
        }
        //show last station
        drawStation(pointsPassed, true, this.stations.size() - 1);
        return currentLine;
    }

    private void prepareData() {
        //clear visited
        this.visited.clear();
        this.interStationCnt.clear();

        //init totPoints, interStationCnt
        int totInterStationCnt = 0;
        for (int i = 0; i < this.travelTime.size(); i++) { //travelTime[i] = travel time before station i + 1 (between i and i + 1)
            int x = this.travelTime.get(i) - 1;
            interStationCnt.add(x); //number of interstations before a station (0 indexed)
            totInterStationCnt += x;
        }
        this.totalPoints = totInterStationCnt + this.stations.size();

        //init visited array
        if (monitored != null) {
            int totalTicks = 0;
            int stopCnt = 1;
            for (int i=0; i < travelTime.size(); i++){
                totalTicks += travelTime.get(i) + 1;
                if (totalTicks < this.timeCounter) {
                    stopCnt++;
                }
            }

            for (int i = 0; i <= timeCounter - stopCnt; i++) visited.add(1);
            if (!monitored.isStopped() && !monitored.isServiceEnded()) visited.add(2);
            for (int i = visited.size(); i < totalPoints; i++) visited.add(0);

            if (timeCounter == 0 || timeCounter == 1) visited.set(0, 1);
        }
    }

    private void drawStation(int pointsPassed, boolean atStation, int stationNo) {
        double radius = (atStation) ? 10 : 5.5;

        //draw station circle
        Circle stn = new Circle((50 + ((pointsPassed) * interval)), y_pos, radius);
        stn.setStroke(Color.BLACK);
        stn.setStrokeWidth(2);
        if (colourise) setCircleColour(stn, visited.get(pointsPassed));
        else stn.setFill(Color.WHITE);
        currentLine.getChildren().add(stn);

        // show station name
        if (atStation) {
            String stationName = stations.get(stationNo).getName();
            double adjustment = stationName.length();
            if (stationName.length() <= 5) {
                adjustment += stationName.length();
            }
            if (stationName.length() >= 10) {
                adjustment *= -(0.01 * stationName.length());
            }

            javafx.scene.text.Text stationLabel = new javafx.scene.text.Text(
                    (pointsPassed* interval) + (adjustment * 0.17),
                    y_pos + 50,
                    stationName
            );
            stationLabel.setFill(Color.BLACK);
            stationLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
            stationLabel.setRotate(-60);
            currentLine.getChildren().add(stationLabel);
        }
    }

    private void setCircleColour(Circle circle, int state) {
        if (state == 1) {
            circle.setFill(Color.web("#692F45")); // reached
            circle.getStyleClass().add("reached_colour");
        } else if (state == 2) { // Blinking
            circle.setFill(Color.web("#4D8D6A"));
            FillTransition ft = new FillTransition(Duration.seconds(0.5), circle);
            ft.setFromValue(Color.web("#4D8D6A"));
            ft.setToValue(Color.WHITE);
            ft.setCycleCount(Animation.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
            circle.getStyleClass().add("reachnext_colour");
        } else if (state == 0){
            circle.setFill(Color.web("#4D8D6A")); // not reached
            circle.getStyleClass().add("notreached_colour");
        }
        else circle.setFill(Color.WHITE);
    }


//    public void resetStations() {
//        for (Node node : currentLine.getChildren()) {
//            if (node instanceof Circle) {
//                Circle circle = (Circle) node;
//                circle.setFill(Color.WHITE);
//            }
//        }
//    }

}