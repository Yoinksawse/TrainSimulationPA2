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
import java.util.Collections;

public class visualLine {
    private String name;
    private String code;
    private double y_pos;
    private boolean colourise = false;

    Simulation simul;
    Train monitored;
    com.example.trainstation_pa2.Model.Line thisLine;
    Group currentLine = new Group();
    double interval;
    int timecounter;
    int totalPoints;

    private ArrayList<Station> stations = new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();
    private ArrayList<Integer> interStationCnt = new ArrayList<>();
    private ArrayList<Double> intervalUnitLength = new ArrayList<>(); //the unit length at each interval (after which station)
    private ArrayList<Double> preSumWeight = new ArrayList<>();
    private ArrayList<Integer> preSumInterStationCnt = new ArrayList<>();
    private ArrayList<Integer> visited = new ArrayList<>();
    private ArrayList<Train> trains = new ArrayList<>();
    //y_pos usually 150
    public visualLine(String name, String code, ArrayList<Station> stations, ArrayList<Integer> travelTime, double y_pos, int timecounter) {
        this.name = name;
        this.code = code;
        this.y_pos = y_pos;
        this.stations.addAll(stations);
        this.travelTime.addAll(travelTime);
    }

    public Group visualise(Simulation simul, Train monitored, int timecounter) {
        // prepparation
        currentLine.getChildren().clear();
        this.timecounter = timecounter;
        this.simul = simul;
        this.monitored = monitored;

        this.trains = this.simul.getTrainsARL();

        this.colourise = (this.monitored != null);
        this.thisLine = this.simul.getLine();
        _init();
        if (totalPoints != preSumWeight.size() - 1) {
            calculatePrefixWeights();
            calculatePrefixInterStationCnt();//TODO: deepseekedit
        }

        // Calculate total travel time (sum of all travel times between stations)
        double totalTravelTime = 0.0;
        //double totalStations = this.countStations();
        for (int i = 1; i < this.countStations(); i++) {
            int t = getTravelTime(i);
            totalTravelTime += t;
            //totalStations += calcInterstations(t);
        }

        // Calculate line length based on total travel time (scaled by a factor)
        double lineLength = totalPoints * 25;

        // Draw base line for the train's line
        Line baseline = new Line(50, y_pos, 50 + lineLength, y_pos);
        baseline.setStroke(Color.web("#6B3143"));
        baseline.setStrokeWidth(11);
        baseline.setStrokeLineCap(StrokeLineCap.ROUND);
        currentLine.getChildren().add(baseline);

        //store this, will be used for coordinating later.
        interval = lineLength / (totalPoints - 1);

        //int totalPoints = this.countStations() + totInterStationCnt;
        //double actualIntervalLength = (double) totalPoints / (double) totInterStationCnt;
        //so when i take the current position (in visited) the train is at, * by actualIntervalLength,
        //that is the "energy level"

        //energy level needs to pass threshold (pre_sum_weight[cnt + 1])
        //cnt: current inter station number (during visualisation)
        //cnt + 1: number of the next interstation it will reach
        //eventually, cnt will == visited.size()

        //so, create a while loop, each time incrementing the current "energy level" by actualIntervalLength
        //then binary search where this is at inside the "energy level" array
        //"energy level array": pre_sum of the weight.
        //needs to be initialised
        // = accumulate (no of interstations between every 2 stations, multiply by intervalUnitLength[i])

        //arrays: interStationCnt, intervalUnitLength, preSumWeight, preSumInterStationCnt, visited


        //System.out.println(preSumWeight.getLast()); //TODO

        // Display first point
        Circle firstStation = new Circle(50, y_pos, 10); // x=50 matches baseline start
        firstStation.setStroke(Color.BLACK);
        firstStation.setStrokeWidth(2);
        if (colourise) setCircleColour(firstStation, visited.get(0));
        else firstStation.setFill(Color.WHITE);
        currentLine.getChildren().add(firstStation);

        System.out.println(visited);

        double unitsPassed = 0;
        int timeUnit = 0;
        int interStationsPassed = 0;
        int stationsPassed = 0;
        while (timeUnit < visited.size() && interStationsPassed + 1 < this.totalPoints) {
            //1 jump. in visited (no. of time units)
            timeUnit++; //actual time unit that matches the interstations and visited array
            unitsPassed += intervalUnitLength.get(stationsPassed);

            //System.out.println("curunit: " + timeUnit);
            //System.out.println(intervalUnitLength);
            //System.out.println(stationsPassed);

            //i just moved past the next interStation!
            if (Double.compare(unitsPassed, preSumWeight.get(interStationsPassed + 1)) >= 0) {
                interStationsPassed++;


                //i am currently on a station, make big circle
                Circle point;
                if (isStationPoint(interStationsPassed)) { //TODO: DEEPSEEKEDIT
                    stationsPassed++;
                    double curX = 50 + interStationsPassed * (interval);
                    point = new Circle(curX, y_pos, 10);
                    point.setStroke(Color.BLACK);
                    point.setStrokeWidth(2);
                }
                //i am only on an interstation, make small circle
                else {
                    double curX = 50 + interStationsPassed * (interval);
                    point = new Circle(curX, y_pos, 5.5);
                    point.setStroke(Color.BLACK);
                    point.setStrokeWidth(2);
                }

                //find out the circle colour
                if (colourise && timeUnit - 1 < visited.size()) {
                    setCircleColour(point, visited.get(Math.min((int) unitsPassed, visited.size() - 1)));
                    System.out.println("A" + unitsPassed);

                }
                else point.setFill(Color.WHITE);
                currentLine.getChildren().add(point);
            }
        }

        //for (Integer i : interStationCnt) System.out.printf("%d\n", i);
        return currentLine;
    }

    private boolean isStationPoint(int pointIndex) {
        int accumulated = 0;
        for (int i = 0; i < interStationCnt.size(); i++) {
            if (pointIndex == accumulated) return true;
            accumulated += interStationCnt.get(i) + 1;
        }
        return pointIndex == accumulated;
    }

    private void setCircleColour(Circle circle, int state) {
        if (state == 1) {
            circle.setFill(Color.web("#692F45")); // reached
        } else if (state == 2) { // Blinking
            circle.setFill(Color.web("#4D8D6A"));
            FillTransition ft = new FillTransition(Duration.seconds(0.5), circle);
            ft.setFromValue(Color.web("#4D8D6A"));
            ft.setToValue(Color.WHITE);
            ft.setCycleCount(Animation.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        } else {
            circle.setFill(Color.web("#4D8D6A")); // not reached
        }
    }

    //colourise
    private void _init() {
        this.visited.clear();
        this.interStationCnt.clear();

        //init interstationcnt
        int totInterStationCnt = 0;
        for (int i = 0; i < this.travelTime.size(); i++) { //travelTime[i] = travel time before station i + 1 (between i and i + 1)
            int x = calcInterstations(this.travelTime.get(i));
            interStationCnt.add(x); //number of interstations before a station (0 indexed)
            totInterStationCnt += x;
        }

        //init totPoints
        this.totalPoints = totInterStationCnt + countStations();

        //init presum arrays
        if (preSumWeight.isEmpty()) calculatePrefixWeights();
        //System.out.println(preSumWeight);
        if (preSumInterStationCnt.isEmpty()) calculatePrefixInterStationCnt();
        //System.out.println(preSumInterStationCnt);

        //init visited array
        for (int i = 0; i < this.timecounter; i++) visited.add(1); // visited
        if (this.monitored == null);
        else if (this.monitored.isStopped()) visited.add(1);
        else visited.add(2); //moving
        while (visited.size() < totInterStationCnt) visited.add(0); //not visited
    }

    private void calculatePrefixInterStationCnt() { //at each station
        preSumInterStationCnt.clear();

        int accumulateStationCnt = 0;
        preSumInterStationCnt.add(0);

        for (int i = 0; i < interStationCnt.size(); i++) {
            accumulateStationCnt += interStationCnt.get(i);
            preSumInterStationCnt.add(accumulateStationCnt);
        }
    }

    private void calculatePrefixWeights() {
        preSumWeight.clear();
        intervalUnitLength.clear();

        double accumulateWeight = 0;
        preSumWeight.add(0.0); // Starting point

        for (int i = 0; i < countStations() - 1; i++) {
            int interSNTCnt = interStationCnt.get(i);
            int totalIntervalPoints = travelTime.get(i);

            //weight (traveltime) of each interval between 2 stations
            double unitWeight = (interSNTCnt > 0) ? ((double) totalIntervalPoints / (double) interSNTCnt + 1) : totalIntervalPoints;
            intervalUnitLength.add(unitWeight);

            //add weights for intermediate points
            for (int j = 0; j < interSNTCnt; j++) {
                accumulateWeight += unitWeight;
                preSumWeight.add(accumulateWeight);
            }

            //add weight for station
            accumulateWeight += unitWeight;
            preSumWeight.add(accumulateWeight);
        }
        System.out.println(intervalUnitLength);
    }




    //==================================================

    public void monitorTrain (Train train) {
        this.monitored = train;

        this.visualise(this.simul, this.monitored, this.timecounter);
    }

    public void deMonitorTrain () {
        //TODO: initialise without colour
        this.monitored = null; //dereference cur monitored
        this.visualise(this.simul, null, this.timecounter);
    }

    //=========================================================================

    // Helper method to calculate number of intermediate points
    private int calcInterstations(int travelTime) {
        if (travelTime < 4) {
            return travelTime - 1;
        }
        return (int) (travelTime / Math.log(travelTime)) - 1;
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

    public double getIntervalLength() {
        return this.interval;
    }
}