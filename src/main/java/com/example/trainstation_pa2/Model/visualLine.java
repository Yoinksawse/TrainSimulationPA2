package com.example.trainstation_pa2.Model;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class visualLine {
    private String name;
    private String code;
    private ArrayList<Station> stations = new ArrayList<>();
    private ArrayList<Integer> travelTime = new ArrayList<>();
    private double y_pos;
    Group currentLine = new Group();


    public visualLine(String name, String code, ArrayList<Station> stations, ArrayList<Integer> travelTime, double y_pos) {
        this.name = name;
        this.code = code;
        this.y_pos = y_pos;
        this.stations.addAll(stations);
        this.travelTime.addAll(travelTime);
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

    // Method to generate the visual line and circles on the scene
    public Group visualise() {
        //draw base line for a train's line
        double linelen = 700;
        Line baseline = new Line(50, 100, 50 + linelen, 100);
        baseline.setStroke(Color.RED);
        baseline.setStrokeWidth(4);
        currentLine.getChildren().add(baseline);

        //create class with visualStation (circles) objects, 1 circle for station & inter-points
        double x_pos = 50;
        double linesize = this.countStations();
        for (int i = 0; i < linesize; i++) {
            Circle stationCircle = new Circle(x_pos, y_pos, 20); //large circle for Station
            stationCircle.setStroke(Color.DARKRED);
            stationCircle.getStyleClass().add((i > 0) ? "reached-colour" : "notreached-colour");
            //group
            currentLine.getChildren().add(stationCircle);

            //if there are points in between
            if (i < linesize - 1) {
                //get no of points (interstations)
                int t = getTravelTime(i + 1);
                int interStationsCount = t < 4 ? t : (int)(t / Math.log(t));

                for (int j = 0; j < interStationsCount; j++) {
                    //mark out position of interstation
                    double i_intervallength = linelen / (linesize - 1);
                    double coarsetune_x = i * i_intervallength;
                    double j_intervallength = i_intervallength / (interStationsCount + 1);
                    double finetune_x = (j + 1) * j_intervallength;
                    double interStationPos = x_pos + coarsetune_x + finetune_x;

                    //create interstation
                    Circle interStationCircle = new Circle(interStationPos, y_pos, 10);
                    interStationCircle.setStroke(Color.DARKRED);
                    stationCircle.getStyleClass().add("notreached-colour");

                    //append to group
                    currentLine.getChildren().add(interStationCircle);
                }
            }

        }
        return currentLine;
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

    public void draw(GraphicsContext gc) {

    }
}
