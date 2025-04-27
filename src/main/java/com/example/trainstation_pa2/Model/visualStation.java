package com.example.trainstation_pa2.Model;
import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class visualStation {
    private Timeline timeline;
    private String name;
    private Circle circle;
    private boolean reached;  // glossy red if reached
    private boolean reachNext; //if reachnext, flash glossy green and white
    private boolean betweenStations;

    public visualStation(double centerX, double centerY, double radius, String name) {
        betweenStations = radius < 10;
        this.name = name;
        circle = new Circle(centerX, centerY, radius);
        circle.setStroke(Color.DARKRED); // dark red border
        updateColor(0); // initially not reached
    }

    public void updateColor(int status) {
        // Stop previous flashing if any
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }

        if (status == 0) {
            this.reached = false;
            this.reachNext = false;
            //set col to pure green
            circle.getStyleClass().add("notreached-colour");
        }
        else if (status == 1) {
            this.reached = true;
            this.reachNext = false;
            //set col to pink
            circle.getStyleClass().add("reached-colour");
        }
        else if (status == 2) {
            this.reached = false;
            this.reachNext = true;
            //set col to pure green
            circle.getStyleClass().add("reaching-colour");
            /*
            FillTransition fillTransition = new FillTransition();
            //styles
            fillTransition.setShape(circle);
            fillTransition.setFromValue(circle.getStyleClass().add("reachnext-colour"));
            fillTransition.setToValue(Color.WHITE);
            //loop controls
            fillTransition.setDuration(Duration.seconds(1));
            fillTransition.setCycleCount(Timeline.INDEFINITE);
            fillTransition.setAutoReverse(true);
            //start!!
            fillTransition.play();
             */
        }
    }

    public Circle getCircle() {
        return circle;
    }
}
