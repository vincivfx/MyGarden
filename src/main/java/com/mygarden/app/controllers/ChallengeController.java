package com.mygarden.app.controllers;
import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.Challenge;

import java.io.IOException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
//import javafx.scene.control.Label;
import javafx.scene.control.Label;

public class ChallengeController extends AbstractController{
    // --- FXML UI elements ---
    @FXML
    private Label UserCoins;

    @FXML
    private Label challengeDescription;

    @FXML
    private Button completedBtn;

    private Challenge currentChallenge;

    private void loadRandomChallenge(String type){
        //call a list of all the items in table ddbb
        //if all is empty " no available challenges"

        // take random challenge 
        Random rnd= new Random();
        currentChallenge=

    }

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