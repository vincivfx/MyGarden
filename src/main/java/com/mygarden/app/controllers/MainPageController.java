package com.mygarden.app.controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import com.mygarden.app.AudioManager;
import com.mygarden.app.LanguageManager;
import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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

        ResourceBundle bundle = LanguageManager.getBundle();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mygarden/app/settings-view.fxml"), bundle);
        Parent root = loader.load();


        Object controller = loader.getController();
        if (controller instanceof AbstractController) {
            ((AbstractController) controller).setUser(getUser());
        }

        // Replace the scene root on the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
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