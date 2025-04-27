package com.example.trainstation_pa2.Model;

import com.example.trainstation_pa2.Controller.HelloController;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class visualSimulation {
    Line thisLine;
    ArrayList<Station> stations = new ArrayList<>();
    ArrayList<Integer> travelTime = new ArrayList<>();
    private double y_pos = 200.0;

    public Group visualise() {
        //setup Line and Stations arraylist
        this.thisLine = this.importLine();
        assert thisLine != null;
        Station[] stationsArr = thisLine.getStations();
        stations.addAll(Arrays.asList(stationsArr));

        // create canvas
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 620);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //TODO: some setup stuff

        newLine(gc, this.thisLine.getName(), this.thisLine.getCode(), stations, travelTime, y_pos);

        //setup setup scene (return group)
        root.getChildren().add(canvas);
        return root;
    }

    public void newLine(GraphicsContext gc, String lineName, String lineCode, ArrayList<Station> stations, ArrayList<Integer> travelTimes, double y_pos) {
        visualLine line = new visualLine(lineName, lineCode, stations, travelTimes, y_pos);
        line.draw(gc);
    }

    private Line importLine() {
        try {
            return new Line(HelloController.linefilename);
        } catch (IOException e) {
            return null;
        }
    }
}