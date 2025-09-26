package com.mygarden.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainPageController {
    @FXML
    private Label welcomeText;

    @FXML
    private void onChallengeClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Challenge");
        alert.setHeaderText("Test Challenge Name");
        alert.setContentText("Do these things.");
        alert.showAndWait(); // Show and Wait for user to close
    }
    /* 
    private void changeScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene newScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }*/

    @FXML
    private void onGoToGarden(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Changing Scene");
        alert.setHeaderText("Garden Clicked!");
        alert.setContentText("Remove comment in .java to change scene.");
        alert.showAndWait(); // Show and Wait for user to close
        //changeScene(event, "garden-view.fxml");
    }

    @FXML
    private void onGoToShop(ActionEvent event) throws IOException {
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Changing Scene");
        alert.setHeaderText("Shop Clicked!");
        alert.setContentText("Remove comment in .java to change scene.");
        alert.showAndWait(); // Show and Wait for user to close*/
        SceneUtils.changeScene(event, "/com/mygarden/app/shop-view.fxml");
    }

        @FXML
    private void onGoToSettings(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Changing Scene");
        alert.setHeaderText("Settings Clicked!");
        alert.setContentText("Remove comment in .java to change scene.");
        alert.showAndWait(); // Show and Wait for user to close
        //changeScene(event, "settings-view.fxml");
    }

    /*@FXML
    private void onGoToBack(ActionEvent event) throws IOException {
        //changeScene(event, "main-page-view.fxml");
    }*/
}