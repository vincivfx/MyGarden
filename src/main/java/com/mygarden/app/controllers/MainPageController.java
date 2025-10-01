package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class MainPageController extends AbstractController {
    // --- FXML UI elements ---
    @FXML
    private Label UserCoins;

    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d", getUser().getCoins()));
    }

    @FXML
    private void onChallengeClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Challenge");
        alert.setHeaderText("Test Challenge Name");
        alert.setContentText("Do these things.");
        alert.showAndWait(); // Show and Wait for user to close
    }

    @FXML
    private void onGoToGarden(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/garden-view.fxml",getUser());

    }

    @FXML
    private void onGoToShop(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/shop-view.fxml", getUser());
    }

    @FXML
    private void onGoToSettings(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/settings-view.fxml", getUser());
    }


    @Override
    public void onUserIsSet()
    {
        //Call when the page is load to update all the UI with the user data
        updateUICoins();
    }


}