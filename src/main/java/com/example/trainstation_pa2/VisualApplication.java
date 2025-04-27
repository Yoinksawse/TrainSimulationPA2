package com.example.trainstation_pa2;

import com.example.trainstation_pa2.Controller.HelloController;
import com.example.trainstation_pa2.Model.Line;
import com.example.trainstation_pa2.Model.Station;
import com.example.trainstation_pa2.Model.visualLine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

public class VisualApplication extends Application {
    Line thisLine;
    ArrayList<Station> stations = new ArrayList<>();
    ArrayList<Integer> travelTime = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.thisLine = this.importLine();
        Station[] stationsArr = thisLine.getStations();
        stations.addAll(Arrays.asList(stationsArr));

        // create canvas
        Canvas canvas = new Canvas(1000, 620);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // create visualLine (to be drawn on canvas)
        //String name, String code, ArrayList<Station> stations, ArrayList<Integer> travelTime, double y_pos
        visualLine line = new visualLine("Line 1", "L1", stations, travelTime, y_pos);

        // Draw the visual line on the canvas
        line.draw(gc);  // Add drawing logic in the visualLine class

        // Set up the scene
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, 1000, 620);
        scene.getStylesheets().add(VisualApplication.class.getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());

        // Set up the stage
        primaryStage.setTitle("Train Station Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void newLine(GraphicsContext gc, String lineName, String lineCode, ArrayList<Station> stations, ArrayList<Integer> travelTimes) {
        visualLine line = new visualLine(lineName, lineCode, stations, travelTimes, y_pos);
        line.draw(gc);
    }

    private Line importLine() {
        return HelloController.
    }

    public static void main(String[] args) {
        launch(args);
    }
}