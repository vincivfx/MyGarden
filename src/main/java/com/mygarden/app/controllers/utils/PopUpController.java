package com.mygarden.app.controllers.utils;

import java.util.ResourceBundle;

import com.mygarden.app.LanguageManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PopUpController {
    @FXML
    private Label label;

    @FXML
    private void initialize() {
        try {
            ResourceBundle bundle = LanguageManager.getBundle();
            if (label != null && bundle != null && bundle.containsKey("popup.message")) {
                label.setText(bundle.getString("popup.message"));
            }
        } catch (Exception e) {
            // Do not fail if bundle lookup fails
            e.printStackTrace();
        }
    }

    public void setText(String text)
    {
        if (label != null && text != null) {
            label.setText(text);
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
