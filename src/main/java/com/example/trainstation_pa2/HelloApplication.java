package com.example.trainstation_pa2;

import com.example.trainstation_pa2.Controller.HelloController;
import com.example.trainstation_pa2.Controller.LineMapController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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

    public static LineMapController showVisualisation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/trainstation_pa2/View/line-map.fxml"));
            //setup scene
            Scene scene = new Scene(fxmlLoader.load(), 1400, 300);
            scene.getStylesheets().add(HelloApplication.class.getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setResizable(false); //TODO
            stage.setTitle("MRT Visualizer");
            stage.setScene(scene);
            stage.getIcons().add(new Image(HelloApplication.class.getResource("/com/example/trainstation_pa2/View/train.png").toExternalForm()));
            stage.setOnCloseRequest(event -> {
                HelloController.resetMap();
            });
            stage.show();

            //export the controller
            return fxmlLoader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}