package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtils {

    static void changeScene(ActionEvent event, String fxmlFile, User currentUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));

        Scene newScene = new Scene(fxmlLoader.load());

        AbstractController controller = fxmlLoader.getController();
        controller.setUser(currentUser);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }
}
