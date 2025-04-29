package com.example.trainstation_pa2.Model;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;

public class visualTrain extends Group {
    private Train train;
    private SVGPath trainShape;
    private static ImagePattern imagePattern;
    private double x_pos, y_pos;

    //constructor
    public visualTrain(Train train, double startX, double startY) {
        this.train = train;
        //1. draw: outline of the train, changes colour
        this.trainShape = new SVGPath();
        this.trainShape.setContent(
                "M 0.039999961853027344 24.64000129699707 L 1.0399999618530273 19.64000129699707 L 3.0399999618530273 " +
                        "14.64000129699707 L 7.039999961853027 10.64000129699707 L 11.039999961853027 6.64000129699707 " +
                        "L 15.039999961853027 3.6400012969970703 L 19.039999961853027 2.6400012969970703 L 23.039999961853027 " +
                        "1.6400012969970703 L 849.4666662216187 -0.5000076293945312 L 858.0666809082031 0.9666595458984375 " +
                        "L 866.0666809082031 2.9666595458984375 L 874.0666809082031 5.633331298828125 L 884.0666809082031 " +
                        "8.633331298828125 L 892.0666809082031 13.633331298828125 L 901.0666809082031 18.633331298828125 " +
                        "L 908.0666809082031 22.633331298828125 L 915.0666809082031 29.633331298828125 L 922.0666809082031 " +
                        "36.633331298828125 L 929.0666809082031 44.633331298828125 L 934.0666809082031 50.633331298828125 " +
                        "L 938.0666809082031 56.633331298828125 L 981.0666809082031 182.23333740234375 L 982.0666809082031 " +
                        "191.23333740234375 L 983.0666809082031 198.23333740234375 L 982.0666809082031 204.23333740234375 " +
                        "L 981.0666809082031 211.23333740234375 L 977.0666809082031 218.23333740234375 L 975.0666809082031 " +
                        "222.23333740234375 L 971.0666809082031 227.23333740234375 L 967.0666809082031 230.23333740234375 " +
                        "L 960.0666809082031 235.23333740234375 L 956.0666809082031 237.23333740234375 L 950.0666809082031 " +
                        "239.23333740234375 L 944.0666809082031 239.23333740234375 L 938.0666809082031 240.23333740234375 " +
                        "L 933.0666809082031 246.23333740234375 L 929.0666809082031 248.23333740234375 L 924.0666809082031 " +
                        "250.23333740234375 L 918.0666809082031 252.23333740234375 L 914.0666809082031 253.23333740234375 " +
                        "L 50.08571434020996 252.94285583496094 L 42.08571434020996 249.94285583496094 L 36.08571434020996 " +
                        "246.94285583496094 L 30.08571434020996 242.94285583496094 L 23.08571434020996 238.94285583496094 " +
                        "L 14.085714340209961 236.94285583496094 L 7.085714340209961 229.94285583496094 L 3.085714340209961 " +
                        "222.94285583496094 L 0.08571434020996094 214.94285583496094 L -0.8000001907348633 27.433326721191406 Z"
        );

        //2. set positions
        this.x_pos = startX;
        this.y_pos = startY;
        this.trainShape.setTranslateX(x_pos);
        this.trainShape.setTranslateY(y_pos);

        //3. set the border style according to state
        Color stateColour;
            //states of movement (Part 1 stuff)
        if (train.isDelayed()) stateColour = Color.RED;
        else if (!train.isStopped()) stateColour = Color.GREEN;
        else stateColour = Color.BLACK; //train is stopped falls under here

        this.setBorderColour(stateColour); //stroke colour (state)
        this.setBorderWidth(4); //stroke width

            //monitored (Part 2 stuff)
        if (train.isMonitored()) setMonitored(true);

        //4. set the fill (the train image)
        Image _img = new Image(getClass().getResource("/com/example/trainstation_pa2/View/train2.png").toExternalForm());
        imagePattern = new ImagePattern(_img);
        this.trainShape.setFill(imagePattern);
    }

    //accessors
    public void setBorderColour(Color col) {
        this.trainShape.setStroke(col);
    }
    public void setBorderWidth(double width) {
        this.trainShape.setStrokeWidth(width);
    }
    public void setMonitored(boolean isMonitored) {
        trainShape.setEffect(new Glow(0.8));
        trainShape.setStroke(Color.YELLOW);
    }

    public Train getTrain() {return this.train;}
    public double getY() {
        return this.y_pos;
    }
    public double getX() {
        return this.x_pos;
    }
    //mutators
    public SVGPath getTrainShape() {return this.trainShape;}

    public void moveTo(double dx, double dy) {
        x_pos += dx;
        y_pos += dy;

        TranslateTransition transition = new TranslateTransition();
        transition.setNode(this.trainShape);
        transition.setDuration(Duration.seconds(Math.abs(dx) * 0.5)); // Adjust speed
        transition.setByX(dx);
        transition.setInterpolator(Interpolator.LINEAR);
        trainShape.setTranslateX(x_pos);
        trainShape.setTranslateY(y_pos);
    }



}