package com.mygarden.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GardenController extends AbstractController {


    @Override
    public void onUserIsSet() {
        System.out.println("Garden owner: " + (getUser() != null ? getUser().getName(): "Who?"));
    }

    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @FXML
    private void onGoToShop(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/shop-view.fxml", getUser());
    }
}
