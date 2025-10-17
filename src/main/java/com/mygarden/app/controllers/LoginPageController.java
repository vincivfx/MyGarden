package com.mygarden.app.controllers;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import com.mygarden.app.LanguageManager;
import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.User;
import com.mygarden.app.repositories.UserRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Robust LoginPageController that forces language controls into the scene so the user can
 * always change language on the login screen.
 */
public class LoginPageController {

    private boolean loginMode = true;

    // FXML fields
    @FXML private Parent rootPane; 
    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private TextField name;
    @FXML private Label informations;
    @FXML private Button changeModeButton;
    @FXML private Button connection;
    @FXML private ComboBox<String> languageSelector;

    @FXML
    private void initialize() {
        try {
            ResourceBundle bundle = LanguageManager.getBundle();

            if (login != null) {
                String p = (bundle != null && bundle.containsKey("login.prompt.username")) ? bundle.getString("login.prompt.username") : "Login";
                login.setPromptText(p);
            }
            if (password != null) {
                String p = (bundle != null && bundle.containsKey("login.prompt.password")) ? bundle.getString("login.prompt.password") : "Password";
                password.setPromptText(p);
            }
            if (name != null) {
                String p = (bundle != null && bundle.containsKey("login.prompt.name")) ? bundle.getString("login.prompt.name") : "Name";
                name.setPromptText(p);
            }
            if (connection != null) {
                String txt = (bundle != null && bundle.containsKey("login.connection.start")) ? bundle.getString("login.connection.start") : "Start!";
                connection.setText(txt);
                connection.setFocusTraversable(false);
            }
            if (changeModeButton != null) {
                String txt = (bundle != null && bundle.containsKey("login.changeMode.noAccount")) ? bundle.getString("login.changeMode.noAccount") : "I don't have an account";
                changeModeButton.setText(txt);
            }
            if (informations != null) {
                informations.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Popolate language selector
        languageSelector.getItems().setAll("en","sv","de","es","it","fr","pt");

        // Show flags in the selector
        languageSelector.setCellFactory(cb -> new ListCell<>() {
            private final ImageView imageView = new ImageView();
            {
                setStyle("-fx-padding: 2 0 2 0;"); // padding setting
                setOnMouseEntered(e -> setCursor(Cursor.HAND));
                setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
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
                setText(null); // no text
            }
        });
        languageSelector.setButtonCell(languageSelector.getCellFactory().call(null));

        // Set current language
        languageSelector.setValue(LanguageManager.getCurrentLang());

        // Change language listener
        languageSelector.valueProperty().addListener((obs, oldLang, newLang) -> {
            if (newLang != null) {
                setLanguageAndReload(new Locale(newLang));
            }
        });
    }

    
    

    @FXML
    private void tryToConnect(ActionEvent e) throws IOException {
        try {
            UserRepository ur = new UserRepository();
            Optional<User> optionalUser;
            if (loginMode) {
                optionalUser = ur.login(login.getText(), password.getText());
            } else {
                optionalUser = ur.register(login.getText(), password.getText(), name.getText());
            }

            if (optionalUser.isPresent()) {
                SceneUtils.changeScene(e, "/com/mygarden/app/main-page-view.fxml", optionalUser.get());
            } else {
                ResourceBundle bundle = LanguageManager.getBundle();
                String failed = (bundle != null && bundle.containsKey("login.failed")) ? bundle.getString("login.failed") : "Login failed";
                if (informations != null) informations.setText(failed);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ResourceBundle bundle = LanguageManager.getBundle();
            String err = (bundle != null && bundle.containsKey("login.error")) ? bundle.getString("login.error") : "An error occurred";
            if (informations != null) informations.setText(err);
        }
    }

    @FXML
    private void changeMode(ActionEvent e) {
        loginMode = !loginMode;
        if (name != null) name.setVisible(!loginMode);
        ResourceBundle bundle = LanguageManager.getBundle();

        if (loginMode) {
            String conn = (bundle != null && bundle.containsKey("login.connection.start")) ? bundle.getString("login.connection.start") : "Start!";
            if (connection != null) connection.setText(conn);
            String changeBtn = (bundle != null && bundle.containsKey("login.changeMode.noAccount")) ? bundle.getString("login.changeMode.noAccount") : "I don't have an account";
            if (changeModeButton != null) changeModeButton.setText(changeBtn);
        } else {
            String conn = (bundle != null && bundle.containsKey("login.connection.create")) ? bundle.getString("login.connection.create") : "Create";
            if (connection != null) connection.setText(conn);
            String changeBtn = (bundle != null && bundle.containsKey("login.changeMode.haveAccount")) ? bundle.getString("login.changeMode.haveAccount") : "I already have an account";
            if (changeModeButton != null) changeModeButton.setText(changeBtn);
        }
    }

    @FXML
    private void onSetEnglish(MouseEvent event) { setLanguageAndReload(Locale.ENGLISH); }

    @FXML
    private void onSetSwedish(MouseEvent event) { setLanguageAndReload(new Locale("sv")); }

    private void setLanguageAndReload(Locale locale) {
        // store current inputs so they can be restored after reload
        String curLogin = login != null ? login.getText() : "";
        String curPassword = password != null ? password.getText() : "";
        String curName = name != null ? name.getText() : "";
        boolean curNameVisible = name != null && name.isVisible();
        boolean curLoginMode = loginMode;

        // set locale globally
        LanguageManager.setLocale(locale);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mygarden/app/login-page-view.fxml"), LanguageManager.getBundle());
            Parent root = loader.load();

            // swap root
            Stage stage = (Stage) ((Node) (rootPane != null ? (Node) rootPane : login)).getScene().getWindow();
            stage.getScene().setRoot(root);

            // restore inputs on the new scene
            Object ctrl = loader.getController();
            if (ctrl instanceof LoginPageController) {
                LoginPageController newCtrl = (LoginPageController) ctrl;
                if (newCtrl.login != null) newCtrl.login.setText(curLogin);
                if (newCtrl.password != null) newCtrl.password.setText(curPassword);
                if (newCtrl.name != null) {
                    newCtrl.name.setText(curName);
                    newCtrl.name.setVisible(curNameVisible);
                }
                newCtrl.loginMode = curLoginMode;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
