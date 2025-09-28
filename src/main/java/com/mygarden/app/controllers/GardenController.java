package com.mygarden.app.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;

public class GardenController extends AbstractController {

    @FXML
    private GridPane gardenGrid;

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

    @FXML
    public void initialize() {
        // Set up drag-and-drop for each cell in the grid
        for (javafx.scene.Node node : gardenGrid.getChildren()) {
            if (node instanceof Label) {
                Label cell = (Label) node;
                cell.setOnDragOver(this::handleDragOver);
                cell.setOnDragDropped(this::handleDragDropped);
            }
        }
    }

    // Eg: call this when a plant is dragged from inventory/shop
    public void handlePlantDragDetected(MouseEvent event, String plantName) {
        Label source = (Label) event.getSource();
        Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(plantName);
        db.setContent(content);
        event.consume();
    }

    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getSource() && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            Label target = (Label) event.getSource();
            target.setText(db.getString());
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }
}
