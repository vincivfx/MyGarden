package com.mygarden.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtils {

    static void changeScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));
        Scene newScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }
}
