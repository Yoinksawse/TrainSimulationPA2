package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.HelloApplication;
import javafx.scene.input.KeyEvent;

import com.example.trainstation_pa2.Model.Line;
import com.example.trainstation_pa2.Model.Simulation;
import com.example.trainstation_pa2.Model.Train;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloController {
    @FXML
    //frontend fields for hello-view
    public AnchorPane title_text;
    public Button Monitor_button;
    public Button Remove_button;
    public Button addtrain_button;
    public Button resetsimulation_button;
    public Button tick_button;
    public Button loadline_button;
    public Button x_button;
    public Button showMap_button;
    public TextField searchtrain_inputfield;
    public TextField trainstat_displaybox;
    public TextField trainnumber_inputfield;
    public TextField addtrainid_inputfield;
    public TextField loadlinename_inputfield;
    public TextArea displaytrains_list;
    public TextArea linemonitor_list;
    public TextArea displaystations_list;
    public Text timecounter_text;
    public Hyperlink aboutprogrammer_link;

    //backend fields
    private static Train monitored;
    private static int timecounter;
    public static String linefilename;
    ArrayList<Train> curDisplayedTrains = new ArrayList<>();
    public static Simulation simul;

    @FXML
    public void initialize() {
        // Disable and clear all text fields
        searchtrain_inputfield.clear();
        searchtrain_inputfield.setDisable(true);
        displaytrains_list.clear();
        displaytrains_list.setDisable(true);
        trainstat_displaybox.clear();
        trainstat_displaybox.setDisable(true);
        trainnumber_inputfield.clear();
        trainnumber_inputfield.setDisable(true);
        addtrainid_inputfield.clear();
        addtrainid_inputfield.setDisable(true);
        linemonitor_list.clear();
        linemonitor_list.setDisable(true);
        displaystations_list.clear();
        displaystations_list.setDisable(true);
        loadlinename_inputfield.clear();
        loadlinename_inputfield.setDisable(false);

        Monitor_button.setDisable(true);
        Remove_button.setDisable(true);
        addtrain_button.setDisable(true);
        resetsimulation_button.setDisable(true);
        tick_button.setDisable(true);
        loadline_button.setDisable(false);
        x_button.setDisable(true);
        showMap_button.setDisable(true);

        //load headers etc
        Label title = new Label("MRT Status Monitor");
        title.setStyle("-fx-font-style: italic;");
        title_text.getChildren().add(title);

        //load time counter
        timecounter = 0;
        timecounter_text.setText(String.format("Time: %d min", timecounter));

        //about programmer link input
        aboutprogrammer_link.setOnAction(event -> showIntro());
    }

    @FXML
    protected void resetSimulation () {
        Alert resetConfirmation = new Alert(Alert.AlertType.INFORMATION);
        resetConfirmation.setTitle("Reset Simulation");
        resetConfirmation.setHeaderText("Are you sure you want to reset the simulation?");

        ButtonType _reset = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType _cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        resetConfirmation.getButtonTypes().setAll(_reset, _cancel);

        ButtonType clicked = resetConfirmation.showAndWait().orElse(_cancel);

        if (clicked == _reset) {
            //reset time counter
            timecounter = 0;
            timecounter_text.setText(String.format("Time: %d min", timecounter));
            //stop monitoring
            if (monitored != null) stopMonitoring();
            //remove all trains
            for (Train t : simul.getTrains()) {
                simul.removeTrain(t.getTrainID());
            }
            displaytrains_list.clear();
            linemonitor_list.setText(simul.toString());

            //TODO: updatemap: close the window
            // #############################################################################################
        }
    }

    @FXML
    protected void onAddLine() {
        //load from csv, start simulation
        try {
            linefilename = String.format("%s.csv", loadlinename_inputfield.getText());
            Line newLine = new Line(linefilename);
            simul = new Simulation(newLine);

            displaystations_list.setText(newLine.toString());
            linemonitor_list.setText(simul.toString());
            //TODO: updatemap: line added, simulation start: list all trains, show the stack
            // #############################################################################################

            //preparations: enable io fields
            searchtrain_inputfield.setDisable(false);
            displaytrains_list.setDisable(false);
            addtrainid_inputfield.setDisable(false);
            linemonitor_list.setDisable(false);
            displaystations_list.setDisable(false);
            //loadlinename_inputfield.setDisable(false);

            Monitor_button.setDisable(false);
            Remove_button.setDisable(false);
            addtrain_button.setDisable(false);
            resetsimulation_button.setDisable(false);
            tick_button.setDisable(false);
            showMap_button.setDisable(false);
            //loadline_button.setDisable(false);
        }
        catch (IOException e) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("File Error");
            invalid.setHeaderText("There was a problem reading the file.");
            invalid.show();
        }
    }


    @FXML
    protected void addTrain()  {
        //get trainid from input field
        //line is this line
        String newTrainID = addtrainid_inputfield.getText();

        try {
            //exception 1: train already exists
            if (simul.getTrain(newTrainID) != null) {
                Alert invalid = new Alert(Alert.AlertType.ERROR);
                invalid.setTitle("Duplicate Train");
                invalid.setHeaderText("Trying to create duplicate train!");
                invalid.show();
                return;
            }
            //exception 2: there is a train in this station
            for (Train t : simul.getTrains()) {
                if (t.getStationIndex() == 0) {
                    Alert invalid = new Alert(Alert.AlertType.ERROR);
                    invalid.setTitle("Add Train Error");
                    invalid.setHeaderText("You have already added a train. Add the next train later in the simulation!");
                    invalid.show();
                    return;
                }
            }

            //all is well
            simul.addTrain(new Train(newTrainID, this.simul.getLine()));
        }
        //exception 3: train id is wrong (length/chars)
        catch(IllegalArgumentException e) {
            Alert invalid = new Alert(Alert.AlertType.INFORMATION);
            invalid.setTitle("Train ID Error");
            invalid.setHeaderText(e.toString().substring(36));
            invalid.show();
        }

        //display list of trains
        Train trains[] = this.simul.getTrains();
        String trainList = "";
        for (int i = 0; i < trains.length; i++) {
            curDisplayedTrains.add(trains[i]);
            trainList += String.format("%s\n", trains[i].getTrainID());
        }
        displaytrains_list.setText(trainList);
        addtrainid_inputfield.clear();

        //TODO: updatemap: train added: push the train to the "stack"
        // #############################################################################################
    }

    @FXML
    public void findTrain(KeyEvent keyEvent) {
        curDisplayedTrains.clear();
        //get trainid from input field
        //line is this line
        String curTargetTrainID = searchtrain_inputfield.getText();
        System.out.println(curTargetTrainID);

        try {
            String displayedTrains = "";
            for (Train t : simul.getTrains()) {
                if (t.getTrainID().contains(curTargetTrainID)) {
                    curDisplayedTrains.add(t);
                    displayedTrains += String.format("%s\n", t.getTrainID());
                }
            }

            //all is well
            displaytrains_list.setText(displayedTrains);

            //TODO: updatemap: tried to find train: make every train in curDisplayedTrains have green colour (match!) or something
            // #############################################################################################
        }
        //exception 3: train id is wrong (length/chars)
        catch(IllegalArgumentException e) {
            Alert invalid = new Alert(Alert.AlertType.INFORMATION);
            invalid.setTitle("Train ID Error");
            invalid.setHeaderText(e.toString().substring(36));
            invalid.show();
        }
    }

    @FXML
    protected void monitorTrain() {
        String searchedTrain = searchtrain_inputfield.getText();
        //exception 0: monitor field is empty()
        if (searchedTrain.isEmpty()) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Train ID Search Error");
            invalid.setHeaderText("Enter a target train ID.");
            invalid.show();
            return;
        }
        //exception 1: cannot find train
        else if (curDisplayedTrains.isEmpty()) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Train ID Search Error");
            invalid.setHeaderText("No matches found.");
            invalid.show();
            return;
        }
        //exception 2: more than 1 target train
        else if (curDisplayedTrains.size() > 1) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Train ID Search Error");
            invalid.setHeaderText("More than one ID matches the criteria. Please refine your search further.");
            invalid.show();
            return;
        }
        else {
            //set monitored train
            monitored = curDisplayedTrains.getFirst();
            //show monitored train
            trainnumber_inputfield.setText(monitored.getTrainID());
            trainstat_displaybox.setText(monitored.toString());
            //clear searchbox
            searchtrain_inputfield.clear();
            //reset displayed train list
            String displayedTrains = "";
            curDisplayedTrains.clear();
            for (Train t : simul.getTrains()) {
                curDisplayedTrains.add(t);
                displayedTrains += String.format("%s\n", t.getTrainID());
            }
            displaytrains_list.setText(displayedTrains);

            //enable x_button to allow cancelling of monitoring
            x_button.setDisable(false);
            trainstat_displaybox.setDisable(false);
            trainnumber_inputfield.setDisable(false);


            //TODO: updatemap: train under monitoring: make this train have red colour or something
            // #############################################################################################
        }
    }

    @FXML
    protected void removeTrain() {
        String searchedTrain = searchtrain_inputfield.getText();
        //exception 0: monitor field is empty()
        if (searchedTrain.isEmpty()) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Train ID Search Error");
            invalid.setHeaderText("Enter a target train ID.");
            invalid.show();
            return;
        }
        //exception 1: cannot find train
        else if (curDisplayedTrains.isEmpty()) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Train ID Search Error");
            invalid.setHeaderText("No matches found.");
            invalid.show();
            return;
        }
        //exception 2: more than 1 target train
        else if (curDisplayedTrains.size() > 1) {
            Alert invalid = new Alert(Alert.AlertType.ERROR);
            invalid.setTitle("Train ID Search Error");
            invalid.setHeaderText("More than one ID matches the criteria. Please refine your search further.");
            invalid.show();
            return;
        }
        else {
            System.out.println("removal");
            String toRemove = curDisplayedTrains.getFirst().getTrainID();
            //remove train
            simul.removeTrain(toRemove);

            //demonitor
            if (monitored != null && monitored.getTrainID().equals(toRemove)) stopMonitoring();
            linemonitor_list.setText(simul.toString());

            //reset displayedtrains
            String displayedTrains = "";
            curDisplayedTrains.clear();
            for (Train t : simul.getTrains()) {
                curDisplayedTrains.add(t);
                displayedTrains += String.format("%s\n", t.getTrainID());
            }
            displaytrains_list.setText(displayedTrains);

            //clear searchbox
            searchtrain_inputfield.clear();


            //TODO: updatemap: make this train disappear, play a sound file or something
            // #############################################################################################
        }
    }

    @FXML
    protected void stopMonitoring() {
        monitored = null;
        trainnumber_inputfield.clear();
        trainstat_displaybox.clear();
        x_button.setDisable(true);
        trainstat_displaybox.setDisable(true);
        trainnumber_inputfield.setDisable(true);

        //TODO: updatemap: monitoring cancelled: make the monitored (red) train have normal colour again
        // #############################################################################################
    }

    @FXML
    protected void nexttick()  {
        if (this.simul.getTrains().length > 0) {
            this.simul.tick();
            timecounter++;
            timecounter_text.setText(String.format("Time: %d min", timecounter));
            linemonitor_list.setText(this.simul.toString());
            if (monitored != null) trainstat_displaybox.setText(monitored.toString());
        }

        //TODO: updatemap: ticked: update positions of all trains
        // #############################################################################################
    }

    @FXML
    protected void showIntro() {
        HelloApplication.showIntro();
    }

    //=================Visualisation====================
    private LineMapController lineMapController;
    @FXML
    private void displayMap() {
        this.lineMapController = HelloApplication.showVisualisation();
        if (lineMapController != null) {
            lineMapController.initData(this.simul);
        }
    }

    @FXML
    protected void updateMap() {
        //TODO: a function that updates the stuff on the map
        // to be used whenever map is changed
        if (lineMapController != null) {
            lineMapController.update();
        }
        else {
            this.displayMap();
        }
    }

    public static Simulation getSimulation() {
        return simul;
    }

    public Line getLine() {
        return this.simul.getLine();
    }

    public Train getTrain(String trainID) {
        return this.simul.getTrain(trainID);
    }
}