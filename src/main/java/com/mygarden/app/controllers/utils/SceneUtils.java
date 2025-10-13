package com.mygarden.app.controllers.utils;

import java.io.IOException;

import com.mygarden.app.controllers.AbstractController;
import com.mygarden.app.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneUtils {

    public static void changeScene(ActionEvent event, String fxmlFile, User currentUser) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource(fxmlFile));

        Scene newScene = new Scene(fxmlLoader.load());

        if (currentUser != null) {
            AbstractController controller = fxmlLoader.getController();
            controller.setUser(currentUser);
        }

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
    }

    public static boolean showConfirmationPopup(String questionText) throws IOException 
    {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL); // Bloque la fenêtre principale

        popup.initStyle(StageStyle.UNDECORATED); // enlève la barre de titre
        popup.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/mygarden/app/utils/confirmation-popup-view.fxml"));
        Scene popUpScene = new Scene(fxmlLoader.load());

        ConfirmationPopUpController controller = fxmlLoader.getController();
        controller.setQuestion(questionText);

        popup.setScene(popUpScene);
        popup.showAndWait();

        return controller.isConfirmed();
    }

    public static void showPopup(String text) throws IOException 
    {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL); // Bloque la fenêtre principale

        popup.initStyle(StageStyle.UNDECORATED); // enlève la barre de titre
        popup.setResizable(false);

        FXMLLoader fxmlLoader = new FXMLLoader(SceneUtils.class.getResource("/com/mygarden/app/utils/popup-view.fxml"));
        Scene popUpScene = new Scene(fxmlLoader.load());

        PopUpController controller = fxmlLoader.getController();
        controller.setText(text);

        popup.setScene(popUpScene);
        popup.showAndWait();

    }
}
