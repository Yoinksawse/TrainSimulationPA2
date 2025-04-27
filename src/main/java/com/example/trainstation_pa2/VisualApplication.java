package com.example.trainstation_pa2;

import com.example.trainstation_pa2.Controller.HelloController;
import com.example.trainstation_pa2.Model.Line;
import com.example.trainstation_pa2.Model.Station;
import com.example.trainstation_pa2.Model.visualLine;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VisualApplication {
    Stage stage = HelloApplication.importStage();

    Line thisLine;
    ArrayList<Station> stations = new ArrayList<>();
    ArrayList<Integer> travelTime = new ArrayList<>();
    private double y_pos = 200.0;

    public void visualise() {
        //setup Line and Stations arraylist
        this.thisLine = this.importLine();
        assert thisLine != null;
        Station[] stationsArr = thisLine.getStations();
        stations.addAll(Arrays.asList(stationsArr));

        // create canvas
        Canvas canvas = new Canvas(1000, 620);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //TODO: some setup stuff

        newLine(gc, this.thisLine.getName(), this.thisLine.getCode(), stations, travelTime, y_pos);

        //setup scene + get stage
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 1000, 620);
        scene.getStylesheets().add(VisualApplication.class.getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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