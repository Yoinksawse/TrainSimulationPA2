package com.example.trainstation_pa2.Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class visualTrain {
    private Train train;
    private SVGPath trainShape;
    private double x_pos, y_pos;

    // Constructor for VisualTrain
    public visualTrain(Train train, double startX, double startY, int state) {
        this.train = train;
        this.x_pos = startX;
        this.y_pos = startY;

        //draw
        this.trainShape = new SVGPath();
        this.trainShape.setContent(
                "M10,10 L90,10 L90,30 L80,40 L10,40 L10,10 Z" +
                        "M30,30 L30,50 L70,50 L70,30 Z" +
                        "M15,50 C20,60 20,80 15,90 L75,90 C70,80 70,60 65,50 Z"
        );
        //set positions
        this.trainShape.setTranslateX(x_pos);
        this.trainShape.setTranslateY(y_pos);

        // Set the fill color for the train
        this.trainShape.setFill(Color.BLUE); // Change this to any color you like
    }

    // Change the fill color of the train
    public void changeColor(Color c) {
        this.trainShape.setFill(c);
    }

    public SVGPath getTrainShape() {
        return trainShape;
    }

    public void updatePosition(double dx, double dy) {
        x_pos += dx;
        y_pos += dy;
        trainShape.setTranslateX(x_pos);
        trainShape.setTranslateY(y_pos);
    }

    public void updateFromTrain() {
        // Update position logic based on Train's state
    }
}
