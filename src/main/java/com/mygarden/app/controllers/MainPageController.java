package com.mygarden.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mygarden.app.AudioManager;
import com.mygarden.app.LanguageManager;
import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

public class MainPageController extends AbstractController implements Initializable {
    // --- FXML UI elements ---
    @FXML
    private Label UserCoins;

    @FXML
    private Button shopBtn;

    @FXML
    private Button gardenBtn;

    @FXML
    private Button challengesBtn;

    @Override
    public void initialize(URL url, ResourceBundle resbundle) {
        
        /*
         * Minimal i18n initialization:
         * Use the ResourceBundle provided by FXMLLoader if available (resbundle),
         * otherwise fall back to LanguageManager.getBundle().
         * We only set the UI strings here (do not change existing behaviour).
         */
        ResourceBundle bundle = (resbundle != null) ? resbundle : LanguageManager.getBundle();

        try {
            if (shopBtn != null && bundle.containsKey("mainpage.shop")) {
                shopBtn.setText(bundle.getString("mainpage.shop"));
            }
            if (gardenBtn != null && bundle.containsKey("mainpage.garden")) {
                gardenBtn.setText(bundle.getString("mainpage.garden"));
            }
            if (challengesBtn != null && bundle.containsKey("mainpage.challenges")) {
                challengesBtn.setText(bundle.getString("mainpage.challenges"));
            }

            // placeholder/fallback for coins before the real user is set
            if (UserCoins != null && bundle.containsKey("mainpage.coins")) {
                UserCoins.setText(bundle.getString("mainpage.coins"));
            }

        } catch (Exception e) {
            // be conservative: if bundle lookup fails, do not break initialization
            e.printStackTrace();
        }
    }

    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d", getUser().getCoins()));
    }

    @FXML
    private ImageView backgroundImageView;

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

    challengesBtn.getTransforms().add(new Rotate(4, challengesBtn.getWidth()/2, challengesBtn.getHeight()/2));


    // Background music
    AudioManager audio = AudioManager.getInstance();
    if (!audio.isMusicPlaying()) {
        audio.playMusic("/audio/background.mp3", true);
        }
    }

    
}