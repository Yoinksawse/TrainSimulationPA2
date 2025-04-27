package com.example.trainstation_pa2.Controller;

import com.example.trainstation_pa2.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutProgrammerController {
    @FXML
    //frontend fields for aboutprogrammer
    public Button closeintroduction_button;
    public Text myname_text;

    @FXML
    protected void close() {
        Stage stage = (Stage) closeintroduction_button.getScene().getWindow();
        stage.close();
    }
}
