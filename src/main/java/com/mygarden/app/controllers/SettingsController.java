package com.mygarden.app.controllers;
import com.mygarden.app.controllers.utils.SceneUtils;

import java.io.IOException;

import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
//import javafx.scene.control.Label;

public class SettingsController extends AbstractController{
    /*@FXML
    private Label welcomeText;*/

    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @Override
    public void onUserIsSet()
    {
        //Call when the page is load to update all the UI with the user data
    }

}