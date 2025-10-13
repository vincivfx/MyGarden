package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.AudioManager;
import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private void onGoToChallenge(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/challenge-view.fxml",getUser());
    }

    @FXML
    private void onGoToGarden(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/garden-view.fxml",getUser());

    }

    @FXML
    private void onGoToShop(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/shop-view.fxml", getUser());
    }

    @FXML
    private void onGoToSettings(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/settings-view.fxml", getUser());
    }


    @Override
    public void onUserIsSet(){
    // Call when the page is loaded to update all the UI with the user data
    updateUICoins();

    // Background music
    AudioManager audio = AudioManager.getInstance();
    if (!audio.isMusicPlaying()) {
        audio.playMusic("/audio/background.mp3", true);
        }
    }
}