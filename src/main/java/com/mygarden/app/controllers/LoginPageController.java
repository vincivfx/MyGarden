package com.mygarden.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import com.mygarden.app.LanguageManager;
import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.User;
import com.mygarden.app.repositories.UserRepository;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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

    // These may or may not be present in FXML. 
    @FXML private HBox flagBox;         
    @FXML private ImageView flagUk;     
    @FXML private ImageView flagSv;     

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

        // Ensure language controls are added after the Scene has been created.
        Platform.runLater(() -> injectLanguageControlsAlways());
    }

    
    private void injectLanguageControlsAlways() {
        try {
            Node sceneRoot = null;
            if (rootPane instanceof Node) {
                sceneRoot = (Node) rootPane;
            } else if (login instanceof Node && login.getScene() != null) {
                sceneRoot = login.getScene().getRoot();
            } else if (connection instanceof Node && connection.getScene() != null) {
                sceneRoot = connection.getScene().getRoot();
            } else {
                if (login != null && login.getScene() != null) sceneRoot = login.getScene().getRoot();
                else if (connection != null && connection.getScene() != null) sceneRoot = connection.getScene().getRoot();
            }

            if (sceneRoot == null) {
                System.out.println("LoginPageController: could not find scene root to attach language controls.");
                return;
            }

            HBox hbox = new HBox(8);
            hbox.setPadding(new Insets(4));
            hbox.setAlignment(Pos.TOP_RIGHT);
            hbox.setId("login-lang-box");

            URL ukUrl = getClass().getResource("/images/settingsImg/flag_uk.png");
            URL svUrl = getClass().getResource("/images/settingsImg/flag_sv.png");
            boolean ukExists = ukUrl != null;
            boolean svExists = svUrl != null;

            if (ukExists) {
                ImageView ivUk = new ImageView(new Image(ukUrl.toExternalForm()));
                ivUk.setFitWidth(40);
                ivUk.setFitHeight(30);
                ivUk.setPreserveRatio(true);
                ivUk.setPickOnBounds(true);
                ivUk.setOnMouseClicked(e -> setLanguageAndReload(Locale.ENGLISH));
                Tooltip.install(ivUk, new Tooltip("English"));
                hbox.getChildren().add(ivUk);
                System.out.println("LoginPageController: flag_uk.png loaded and added.");
            } else {
                Button enBtn = new Button("EN");
                enBtn.setOnAction(a -> setLanguageAndReload(Locale.ENGLISH));
                Tooltip.install(enBtn, new Tooltip("English"));
                hbox.getChildren().add(enBtn);
                System.out.println("LoginPageController: flag_uk.png missing — EN fallback button added.");
            }

            if (svExists) {
                ImageView ivSv = new ImageView(new Image(svUrl.toExternalForm()));
                ivSv.setFitWidth(40);
                ivSv.setFitHeight(30);
                ivSv.setPreserveRatio(true);
                ivSv.setPickOnBounds(true);
                ivSv.setOnMouseClicked(e -> setLanguageAndReload(new Locale("sv")));
                Tooltip.install(ivSv, new Tooltip("Svenska"));
                hbox.getChildren().add(ivSv);
                System.out.println("LoginPageController: flag_sv.png loaded and added.");
            } else {
                Button svBtn = new Button("SV");
                svBtn.setOnAction(a -> setLanguageAndReload(new Locale("sv")));
                Tooltip.install(svBtn, new Tooltip("Svenska"));
                hbox.getChildren().add(svBtn);
                System.out.println("LoginPageController: flag_sv.png missing — SV fallback button added.");
            }

            if (sceneRoot instanceof Pane) {
                Pane paneRoot = (Pane) sceneRoot;
                if (paneRoot instanceof AnchorPane) {
                    AnchorPane.setTopAnchor(hbox, 10.0);
                    AnchorPane.setRightAnchor(hbox, 10.0);
                }
                paneRoot.getChildren().add(hbox);
                System.out.println("LoginPageController: language controls added to Pane root.");
            } else {
                Parent originalRoot = (Parent) sceneRoot;
                StackPane wrapper = new StackPane();
                wrapper.getChildren().add(originalRoot);
                wrapper.getChildren().add(hbox);
                StackPane.setAlignment(hbox, Pos.TOP_RIGHT);
                Stage stage = getStageFromNode(originalRoot);
                if (stage != null && stage.getScene() != null) {
                    stage.getScene().setRoot(wrapper);
                    System.out.println("LoginPageController: scene root wrapped in StackPane and language controls added.");
                } else {
                    System.out.println("LoginPageController: unable to replace scene root; language controls not shown.");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Stage getStageFromNode(Node node) {
        if (node == null) return null;
        if (node.getScene() != null) return (Stage) node.getScene().getWindow();
        return null;
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
