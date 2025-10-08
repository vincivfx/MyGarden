package com.mygarden.app.controllers;
import java.io.IOException;

import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ChallengeController extends AbstractController{
    // --- FXML UI elements ---
    @FXML
    private Label UserCoins;

    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d", getUser().getCoins()));
    }

    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
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