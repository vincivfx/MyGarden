package com.mygarden.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Helper for reloading FXML with the current ResourceBundle.
 */
public final class UIHelper {
    private UIHelper() {}

    /**
     * Replace the root of the given stage's scene by reloading the provided fxmlPath.
     * fxmlPath should be absolute resource path, e.g. "/fxml/login.fxml".
     */
    public static void reloadFXML(Stage stage, String fxmlPath) throws IOException {
        ResourceBundle bundle = LanguageManager.getBundle();
        FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource(fxmlPath), bundle);
        Parent root = loader.load();

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
    }
}
