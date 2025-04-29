package com.example.trainstation_pa2.Model;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

import java.util.ArrayList;

public class visualLine {
    private String name;
    private String code;
    private double y_pos;
    private boolean colourise = false;

    Simulation simul;
    Train monitored;
    com.example.trainstation_pa2.Model.Line thisLine;
    Group currentLine = new Group();

    private ArrayList<Station> stations = new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();
    private ArrayList<Integer> interStationCnt = new ArrayList<>();
    private ArrayList<Integer> visited = new ArrayList<>();
    private ArrayList<Train> trains = new ArrayList<>();

    public visualLine(String name, String code, ArrayList<Station> stations, ArrayList<Integer> travelTime, double y_pos) {
        this.name = name;
        this.code = code;
        this.y_pos = y_pos;
        this.stations.addAll(stations);
        this.travelTime.addAll(travelTime);
    }

    public Group visualise(Simulation simul, Train monitored) {
        // prepparation
        currentLine.getChildren().clear();
        this.simul = simul;
        this.monitored = monitored;
        this.trains = this.simul.getTrainsARL();
        this.colourise = (this.monitored != null);
        this.thisLine = this.simul.getLine();
        if (this.colourise) initVisited();


        int curpos = 0; //counter: which station/interstation currently on

        // Calculate total travel time (sum of all travel times between stations)
        double totalTravelTime = 0.0;
        double totalStations = this.countStations();
        for (int i = 1; i < this.countStations(); i++) {
            int t = getTravelTime(i);
            totalTravelTime += t;
            totalStations += calcInterstations(t);
        }

        // Calculate line length based on total travel time (scaled by a factor)
        double lineLength = totalTravelTime * 30;

        // Draw base line for the train's line
        Line baseline = new Line(50, y_pos, 50 + lineLength, y_pos);
        baseline.setStroke(Color.web("#6B3143"));
        baseline.setStrokeWidth(11);
        baseline.setStrokeLineCap(StrokeLineCap.ROUND);
        currentLine.getChildren().add(baseline);

        // Create stations and intermediate points
        double currentX = 50; // Starting X position
        double interval = lineLength / (totalStations - 2);
        for (int i = 1; i < this.countStations(); i++) {
            // Create main station circle
            Circle stationCircle = new Circle(currentX, y_pos, 10);
            stationCircle.setStroke(Color.BLACK);
            stationCircle.setStrokeWidth(2);
            curpos++;

            //find out the circle colour
            Color fillColour = Color.WHITE;
            if (colourise) {
                if (visited.get(curpos) == 1) fillColour = Color.web("#692F45"); //1 for reached
                else if (visited.get(curpos) == 0) fillColour = Color.web("#4D8D6A"); // 0 for not reached
                else if (visited.get(curpos) == 2) { //blinking
                    fillColour = Color.web("#4D8D6A");
                    FillTransition ft = new FillTransition(Duration.seconds(0.5), stationCircle);
                    ft.setFromValue(fillColour);
                    ft.setToValue(Color.WHITE);
                    ft.setCycleCount(Animation.INDEFINITE);
                    ft.setAutoReverse(true);
                    ft.play();
                }
            }
            stationCircle.setFill(fillColour);
            //stationCircle.getStyleClass().add((i > 0) ? "reached-colour" : "notreached-colour");
            currentLine.getChildren().add(stationCircle);

            // Calculate spacing to next station if not the last station
            if (i < this.countStations() - 1) {
                int t = getTravelTime(i + 1);

                // Calculate number of intermediate points
                int interStationsCount = calcInterstations(t);

                // Create intermediate points
                for (int j = 0; j < interStationsCount; j++) {
                    currentX += interval;

                    Circle interStationCircle = new Circle(currentX, y_pos, 5.5);
                    interStationCircle.setStroke(Color.BLACK);
                    interStationCircle.setStrokeWidth(2);
                    curpos++;

                    //find out the circle colour
                    Color interFillColour = Color.WHITE;
                    if (colourise) {
                        if (visited.get(curpos) == 1) interFillColour = Color.web("#692F45"); //1 for reached
                        else if (visited.get(curpos) == 0) interFillColour = Color.web("#4D8D6A"); // 0 for not reached
                        else if (visited.get(curpos) == 2) { //blinking
                            interFillColour = Color.web("#4D8D6A");
                            FillTransition ift = new FillTransition(Duration.seconds(0.5), stationCircle);
                            ift.setFromValue(interFillColour);
                            ift.setToValue(Color.WHITE);
                            ift.setCycleCount(Animation.INDEFINITE);
                            ift.setAutoReverse(true);
                            ift.play();
                        }
                    }

                    interStationCircle.setFill(interFillColour);
                    //interStationCircle.getStyleClass().add("notreached-colour");
                    currentLine.getChildren().add(interStationCircle);
                }

                // Update currentX for the next station (move by interval)
                if (i < this.countStations() - 1) currentX += interval;
            }
        }

        //for (Integer i : interStationCnt) System.out.printf("%d\n", i);
        return currentLine;
    }

    //colourise

    private void initVisited() {
        this.interStationCnt.clear();
        this.visited.clear();
        int monitoredStationIndex = this.monitored.getStationIndex();

        //init interstationcnt
        int totInterStationCnt = 0;
        for (int i = 0; i < this.travelTime.size(); i++) {
            int x = calcInterstations(this.travelTime.get(i));
            interStationCnt.add(x);
            totInterStationCnt += x;
        }

        //everything before current position
        int passedInterStationCnt = 1;
        for (int i = 0; i <= monitoredStationIndex; i++) {
            passedInterStationCnt += interStationCnt.get(i) + 1; //interstations leading to station + station
        }

        // moving, need to find out which interstation
        if (!this.monitored.isStopped()) {
            int timeToNext = this.travelTime.get(monitoredStationIndex);
            int minsPassed = timeToNext - this.monitored.getMinutesToNextStop();
            int intervalLen = interStationCnt.get(monitoredStationIndex);
            passedInterStationCnt += (int) ((double) minsPassed / timeToNext * intervalLen);
        }

        int totalPoints = this.countStations() + totInterStationCnt;

        for (int i = 0; i < passedInterStationCnt; i++) visited.add(1); //reached
        if (!monitored.isStopped()) visited.add(2); //moving to
        while (visited.size() < totalPoints) visited.add(0); //not reached
    }

    private void monitorTrain (Train train) {
        this.monitored = train;
        this.visualise(this.simul, this.monitored);
    }

    private void deMonitorTrain () {
        //TODO: initialise without colour
        this.monitored = null; //dereference cur monitored
        this.visualise(this.simul, this.monitored);
    }

    //=========================================================================

    // Helper method to calculate number of intermediate points
    private int calcInterstations(int travelTime) {
        if (travelTime < 4) {
            return travelTime;
        }
        // Logarithmic scaling for larger travel times
        return (int) (travelTime / Math.log(travelTime));
    }

    public ArrayList<Integer> getInterStationCnt() {
        return new ArrayList<>(interStationCnt);
    }

    public ArrayList<Circle> getAllCircles() {
        ArrayList<Circle> circles = new ArrayList<>();
        for (Node node : currentLine.getChildren()) {
            if (node instanceof Circle) {
                circles.add((Circle) node);  // Cast to Circle and add to the list
            }
        }
        return circles;
    }

    public int countStations() {
        return stations.size();
    }

    // Returns the station at the given index
    public Station getStation(int index) {
        return new Station(stations.get(index));
    }

    // Returns the travel time to the next station at the given index
    public int getTravelTime(int index) {
        return travelTime.get(index-1);
    }
}
