package com.example.trainstation_pa2;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    static Stage exportStage;

    @Override
    public void start(Stage stage) throws IOException {
        exportStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/trainstation_pa2/View/hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(getClass().getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());
        stage.setTitle("MRT Status Simulator");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/com/example/trainstation_pa2/View/train.png").toExternalForm()));
        stage.show();
    }

    public static void showIntro() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/trainstation_pa2/View/about-programmer.fxml"));
            Scene myScene = new Scene(fxmlLoader.load(), 320, 480);
            myScene.getStylesheets().add(HelloApplication.class.getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("About Programmer");
            stage.setScene(myScene);
            stage.getIcons().add(new Image(HelloApplication.class.getResource("/com/example/trainstation_pa2/View/train.png").toExternalForm()));
            stage.show();
        } catch (IOException e) {
            return;
        }
    }

    public static Stage importStage() {
        return exportStage;
    }

    public static void main(String[] args) {
        launch();
    }
}