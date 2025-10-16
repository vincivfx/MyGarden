package com.mygarden.app.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
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
    private ComboBox<String> languageSelector;

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

        // --- Language ComboBox ---
        if (languageSelector != null) {
            languageSelector.getItems().setAll("en","sv","de","es","it","fr","pt");

            // Mostra solo bandiere
            languageSelector.setCellFactory(cb -> new ListCell<>() {
                private final ImageView imageView = new ImageView();
                {
                    setStyle("-fx-padding: 2 0 2 0;"); // padding azzerato per ogni cella
                }
                @Override
                protected void updateItem(String lang, boolean empty) {
                    super.updateItem(lang, empty);
                    if (empty || lang == null) {
                        setGraphic(null);
                    } else {
                        imageView.setFitWidth(54);
                        imageView.setFitHeight(36);
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setImage(new Image(
                            Objects.requireNonNull(getClass().getResourceAsStream(
                                "/images/settingsImg/" + lang + ".png"))
                        ));
                        setGraphic(imageView);
                    }
                    setText(null); // niente testo
                }
            });
            languageSelector.setButtonCell(languageSelector.getCellFactory().call(null));

            // Imposta lingua corrente
            languageSelector.setValue(LanguageManager.getCurrentLang());

            // Listener cambio lingua
            languageSelector.valueProperty().addListener((obs, oldLang, newLang) -> {
                if (newLang != null) {
                    setLanguageAndReload(new Locale(newLang));
                }
            });
        }

    }

    @FXML
    public void resetApplication(ActionEvent event) throws IOException {
        ResourceBundle bundle = LanguageManager.getBundle();

        // Localized title / content / button labels with fallbacks
        String title = bundle != null && bundle.containsKey("reset.title") ? bundle.getString("reset.title") : "Reset Application";
        String content = bundle != null && bundle.containsKey("reset.confirmText")
            ? bundle.getString("reset.confirmText")
            : "Are you sure you want to delete all your data from MyGarden? These settings will be lost forever! (A long time!)";
        String confirmLabel = bundle != null && bundle.containsKey("reset.confirmButton") ? bundle.getString("reset.confirmButton") : "Delete";
        String cancelLabel  = bundle != null && bundle.containsKey("reset.cancelButton")  ? bundle.getString("reset.cancelButton")  : "Cancel";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setStyle(
            "-fx-background-color: #b1ccbb;" +
            "-fx-border-color: #a0ac97;" +
            "-fx-border-width: 3;"
        );

        ButtonType deleteBtn = new ButtonType(confirmLabel, ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType(cancelLabel, ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteBtn, cancelBtn);

        Button deleteButton = (Button) alert.getDialogPane().lookupButton(deleteBtn);
        deleteButton.setStyle(
            "-fx-background-color: red;" +
            "-fx-border-color: darkred;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(cancelBtn);
        cancelButton.setStyle(
            "-fx-background-color: #59a834;" +
            "-fx-border-color: #3b8133;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );

        // ensure dialog grows to fit long text
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait().ifPresent(rs -> {
            if (rs == deleteBtn) { 
                try {
                    DatabaseManager.getInstance().deleteCurrentUser(getUser());
                    SceneUtils.changeScene(event, "/com/mygarden/app/login-page-view.fxml", null);

                } catch (SQLException | IOException exception) {
                    exception.printStackTrace();

                    String errTitle = bundle != null && bundle.containsKey("error.title") ? bundle.getString("error.title") : "Error";
                    String errMsg = exception.getMessage() != null ? exception.getMessage() : (bundle != null && bundle.containsKey("error.generic") ? bundle.getString("error.generic") : "An error occurred");

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle(errTitle);
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText(errMsg);
                    errorAlert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    errorAlert.showAndWait();
                }
            }
        });
    }

    // -------------------------------
    // Language selection handlers
    // -------------------------------

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
