package com.mygarden.app.controllers;

import com.mygarden.app.controllers.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;


public class GardenController extends AbstractController {


    private static final int COLS = 10;
    private static final int ROWS = 10;
    private static final double TILE = 96;
    private static final double HALF = TILE / 2.0;


    @FXML private ScrollPane scroll;
    @FXML private AnchorPane canvas;
    @FXML private Group isoLayer;

    @Override
    public void onUserIsSet() {
        System.out.println("Garden owner: " + (getUser() != null ? getUser().getName() : "Who?"));
    }

    @FXML
    public void initialize() {
        buildIsoGrid();
        centerScroll();
    }

    // === Navegação ===
    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @FXML
    private void onGoToShop(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/shop-view.fxml", getUser());
    }


    private void buildIsoGrid() {
        isoLayer.getChildren().clear();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {


                StackPane tile = new StackPane();
                tile.setRotate(45);
                tile.setScaleY(0.5);
                Rectangle ground = new Rectangle(TILE, TILE);
                ground.setArcWidth(18);
                ground.setArcHeight(18);
                ground.setFill(Color.web("#bfae8c"));
                ground.setStroke(Color.web("#8d7b56"));
                ground.setStrokeWidth(2.0);

                tile.getChildren().add(ground);


                Label label = new Label("+");
                label.setStyle("-fx-font-size: 16; -fx-text-fill: rgba(0,0,0,0.55); -fx-font-weight: bold;");
                tile.getChildren().add(label);


                double x = (c - r) * HALF;
                double y = (c + r) * (HALF / 2);

                tile.setLayoutX(x);
                tile.setLayoutY(y);


                tile.setOnDragOver(this::handleDragOver);
                tile.setOnDragExited(this::handleDragExited);
                tile.setOnDragDropped(e -> handleDragDropped(e, label, ground));

                isoLayer.getChildren().add(tile);
            }
        }
    }

    private void centerScroll() {
        // centraliza a visão aproximadamente
        scroll.setHvalue(0.5);
        scroll.setVvalue(0.0);
    }

    // =============================================================================
    // Drag & Drop
    // =============================================================================


    public void handlePlantDragDetected(MouseEvent event, String plantName) {
        Node source = (Node) event.getSource();
        Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(plantName);
        db.setContent(content);
        event.consume();
    }

    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

            Node n = (Node) event.getSource();
            n.setScaleX(0.52);
            n.setScaleY(0.52);
        }
        event.consume();
    }

    private void handleDragExited(DragEvent event) {
        Node n = (Node) event.getSource();
        n.setScaleX(0.5);
        n.setScaleY(0.5);
        event.consume();
    }

    private void handleDragDropped(DragEvent event, Label label, Rectangle ground) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
            String plant = db.getString();
            label.setText(plant);


            ground.setFill(Color.web("#9ccc65"));

            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }
}
