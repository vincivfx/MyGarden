package com.mygarden.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
//import javafx.scene.control.Label;

public class SettingsController {
    /*@FXML
    private Label welcomeText;*/

    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml");
    }

}