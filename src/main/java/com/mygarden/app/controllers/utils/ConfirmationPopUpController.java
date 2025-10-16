package com.mygarden.app.controllers.utils;

import java.util.ResourceBundle;

import com.mygarden.app.LanguageManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmationPopUpController {
    private boolean confirmed = false;
    
    @FXML
    private Label question;

    @FXML
    private void initialize() {
        try {
            ResourceBundle bundle = LanguageManager.getBundle();
            if (question != null && bundle != null && bundle.containsKey("popup.confirm.question")) {
                question.setText(bundle.getString("popup.confirm.question"));
            }
        } catch (Exception e) {
            // do not fail initialization for i18n issues
            e.printStackTrace();
        }
    }

    public void setQuestion(String questionText)
    {
        if (question != null && questionText != null) {
            question.setText(questionText);
        }
    }

    @FXML
    private void onYesClicked(ActionEvent event) {
        confirmed = true;
        closeWindow(event);
    }

    @FXML
    private void onNoClicked(ActionEvent event) {
        confirmed = false;
        closeWindow(event);
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
