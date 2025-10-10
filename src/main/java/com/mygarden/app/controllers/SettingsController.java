package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.AudioManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

public class SettingsController extends AbstractController{
    /*@FXML
    private Label welcomeText;*/

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeLabel;

    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @Override
    public void onUserIsSet()
    {
        // Called when the page is loaded to update all the UI with the user data

        // Initialize volume slider
        AudioManager audio = AudioManager.getInstance();
        double volPercent = audio.getVolume() * 100.0;
        volumeSlider.setValue(volPercent);
        volumeLabel.setText(String.format("%d%%", Math.round(volPercent)));

        // Update AudioManager when slider moves
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double v = newVal.doubleValue();
            audio.setVolume(v / 100.0); // AudioManager expects 0.0..1.0
            volumeLabel.setText(String.format("%d%%", Math.round(v)));
        });
    }

}