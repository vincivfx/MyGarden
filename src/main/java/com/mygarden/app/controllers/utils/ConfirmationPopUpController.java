package com.mygarden.app.controllers.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmationPopUpController {
    private boolean confirmed = false;
    
    @FXML
    private Label question;

    public void setQuestion(String questionText)
    {
        question.setText(questionText);
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
