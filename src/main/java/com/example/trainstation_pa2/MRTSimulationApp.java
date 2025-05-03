package com.example.trainstation_pa2;

import com.example.trainstation_pa2.Controller.LineMapController;
import com.example.trainstation_pa2.Controller.MRTMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MRTSimulationApp extends Application {
    private String lineName;
    //private static FXMLLoader mainLoader;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/trainstation_pa2/View/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        scene.getStylesheets().add(getClass().getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());

        stage.setTitle("MRT Status Simulator");
        stage.getIcons().add(new Image(getClass().getResource("/com/example/trainstation_pa2/View/train.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();

        //mainLoader = fxmlLoader;
    }

    public static void showIntro() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MRTSimulationApp.class.getResource("/com/example/trainstation_pa2/View/about-programmer.fxml"));
            Scene myScene = new Scene(fxmlLoader.load(), 320, 480);
            myScene.getStylesheets().add(MRTSimulationApp.class.getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("About Programmer");
            stage.getIcons().add(new Image(MRTSimulationApp.class.getResource("/com/example/trainstation_pa2/View/train.png").toExternalForm()));
            stage.setScene(myScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LineMapController showVisualisation() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MRTSimulationApp.class.getResource("/com/example/trainstation_pa2/View/line-map.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1400, 300);
            scene.getStylesheets().add(MRTSimulationApp.class.getResource("/com/example/trainstation_pa2/View/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setResizable(false);

            //title and icon
            String title = MRTMainController.getLineName();
            stage.setTitle(title);
            stage.getIcons().add(new Image(MRTSimulationApp.class.getResource("/com/example/trainstation_pa2/View/train.png").toExternalForm()));

            stage.setScene(scene);
            stage.setOnCloseRequest(event -> MRTMainController.resetMap());
            stage.show();

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
