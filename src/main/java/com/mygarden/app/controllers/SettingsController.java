package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.AudioManager;
import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SettingsController extends AbstractController{

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeLabel;

    @FXML
    private Slider sfxVolumeSlider;

    @FXML
    private Label sfxVolumeLabel;

    @FXML
    private VBox rootPane;

    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @Override
    public void onUserIsSet()
    {
        // Called when the page is loaded to update all the UI with the user data
        AudioManager audio = AudioManager.getInstance();

        // --- Music slider init & listener ---
        double musicVolPercent = audio.getMusicVolume() * 100.0;
        volumeSlider.setValue(musicVolPercent);
        volumeLabel.setText(String.format("%d%%", Math.round(musicVolPercent)));

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double v = newVal.doubleValue();
            audio.setMusicVolume(v / 100.0); 
            volumeLabel.setText(String.format("%d%%", Math.round(v)));
        });

        // --- SFX slider init & listener ---
        double sfxVolPercent = audio.getSfxVolume() * 100.0;
        sfxVolumeSlider.setValue(sfxVolPercent);
        sfxVolumeLabel.setText(String.format("%d%%", Math.round(sfxVolPercent)));

        sfxVolumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double v = newVal.doubleValue();
            audio.setSfxVolume(v / 100.0);
            sfxVolumeLabel.setText(String.format("%d%%", Math.round(v)));
        });

        // Play a short test click when the user releases the slider thumb 
        sfxVolumeSlider.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> SoundManager.getInstance().playClick());

        // Settings Stylesheet
        if (rootPane.getScene() != null) {
        rootPane.getScene().getStylesheets().add(
            getClass().getResource("/styles/settings.css").toExternalForm()
        );
    }
    }

}
