package com.mygarden.app.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import com.mygarden.app.AudioManager;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.LanguageManager;
import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private Button langButton; 

    @FXML
    private Button deleteDataButton; 

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

        // ensure language button shows correct localized label when the page loads (if present)
        try {
            if (langButton != null && LanguageManager.getBundle().containsKey("lang.button")) {
                langButton.setText(LanguageManager.getBundle().getString("lang.button"));
            }
        } catch (Exception ignored) {
            // ignore if bundle key not found
        }
    }

    @FXML
    public void resetApplication(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // These strings could be localized as well - see notes below.
        alert.setTitle("Reset Application");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete all your data from MyGarden? These settings will be lost forever! (A long time!)");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                try {
                    DatabaseManager.getInstance().resetApplication();
                    SceneUtils.changeScene(event, "/com/mygarden/app/login-page-view.fxml", null);

                } catch (SQLException | IOException exception) {
                    System.out.println(exception.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText(exception.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    // -------------------------------
    // Language selection handlers
    // -------------------------------

    /**
     * Set language to English and reload the settings view while preserving slider values and user.
     * Wire this from FXML: e.g. a Button with onAction="#onSetEnglish"
     */
    @FXML
    private void onSetEnglish(ActionEvent event) {
        setLanguageAndReload(Locale.ENGLISH);
    }

    /**
     * Set language to Swedish and reload the settings view while preserving slider values and user.
     * Wire this from FXML: e.g. a Button/ImageView with onAction="#onSetSwedish"
     */
    @FXML
    private void onSetSwedish(ActionEvent event) {
        setLanguageAndReload(new Locale("sv"));
    }

    /**
     * Central helper: set Locale in LanguageManager, then reload this settings FXML using the new bundle.
     * We capture the relevant UI state (slider percentages) and restore them after reloading.
     */
    private void setLanguageAndReload(Locale locale) {
        // capture UI state we want to preserve
        double musicVol = volumeSlider != null ? volumeSlider.getValue() : -1;
        double sfxVol = sfxVolumeSlider != null ? sfxVolumeSlider.getValue() : -1;

        // persist language
        LanguageManager.setLocale(locale);

        // reload the settings view using the new ResourceBundle
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mygarden/app/settings-view.fxml"), LanguageManager.getBundle());
            Parent root = loader.load();

            // attach the current user to the newly-created controller if possible
            Object controller = loader.getController();
            if (controller instanceof AbstractController) {
                ((AbstractController) controller).setUser(getUser());
            }

            // swap scene root
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.getScene().setRoot(root);

            // restore slider values on the new scene (lookup by fx:id)
            if (musicVol >= 0) {
                var newMusicSlider = (Slider) stage.getScene().getRoot().lookup("#volumeSlider");
                var newMusicLabel = (Label) stage.getScene().getRoot().lookup("#volumeLabel");
                if (newMusicSlider != null) {
                    newMusicSlider.setValue(musicVol);
                    if (newMusicLabel != null) newMusicLabel.setText(String.format("%d%%", Math.round(musicVol)));
                }
            }
            if (sfxVol >= 0) {
                var newSfxSlider = (Slider) stage.getScene().getRoot().lookup("#sfxVolumeSlider");
                var newSfxLabel = (Label) stage.getScene().getRoot().lookup("#sfxVolumeLabel");
                if (newSfxSlider != null) {
                    newSfxSlider.setValue(sfxVol);
                    if (newSfxLabel != null) newSfxLabel.setText(String.format("%d%%", Math.round(sfxVol)));
                }
            }

            // play click sound for feedback (optional)
            SoundManager.getInstance().playClick();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
