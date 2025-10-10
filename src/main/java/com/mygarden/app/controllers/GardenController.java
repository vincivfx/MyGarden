package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class GardenController extends AbstractController {

    // Layout
    private static final int COLS = 5;
    private static final int ROWS = 5;

    // Tile size
    private static final double TILE_W = 72;
    private static final double TILE_H = 40;
    private static final double HALF_W = TILE_W / 2.0;
    private static final double HALF_H = TILE_H / 2.0;

    // Zoom
    private double scale = 1.0;
    private static final double MIN_SCALE = 0.6;
    private static final double MAX_SCALE = 2.0;
    private static final double ZOOM_STEP = 1.1;

    // Drag Screen
    private double panStartX;
    private double panStartY;
    private double baseTranslateX;
    private double baseTranslateY;

    @FXML private AnchorPane canvas;
    @FXML private Group isoLayer;

    // Images
    private Image tileSoilImg;

    @Override
    public void onUserIsSet() {
        System.out.println("Garden owner: " + (getUser() != null ? getUser().getName(): "Who?")); // we can remove this
    }

    @FXML
    public void initialize() {
        setupBackground();

        var soilUrl = getClass().getResource("/images/tile_soil.png");
        if (soilUrl == null) {
            System.err.println("Wrong path on tile image"); // We can remove this, is just a safe thing
            tileSoilImg = null;
        } else {
            tileSoilImg = new Image(soilUrl.toExternalForm(), TILE_W, TILE_H, true, true);
        }

        buildIsoGrid();
        centerIsoLayer();
        clampIsoLayer();

        // Resize canvaz
        canvas.widthProperty().addListener((o, a, b) -> { centerIsoLayer(); clampIsoLayer(); });
        canvas.heightProperty().addListener((o, a, b) -> { centerIsoLayer(); clampIsoLayer(); });

        // Scroll zoom
        canvas.addEventFilter(ScrollEvent.SCROLL, this::handleZoomAtCursor);

        // Reset position with 2 clicks
        canvas.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) resetCamera();
        });
    }

    @FXML
    private void onGoToMainPage(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @FXML
    private void onGoToShop(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/shop-view.fxml", getUser());
    }

    private void setupBackground() {
        var url = getClass().getResource("/images/bg_grass.png");
        if (url == null) {
            System.err.println("Wrong path on bg image"); // We can remove this, is just a safe thing
            return;
        }
        Image img = new Image(url.toExternalForm());
        ImageView bg = new ImageView(img);

        bg.setPreserveRatio(false);
        bg.setSmooth(true);
        bg.setMouseTransparent(true);
        bg.setManaged(false);

        bg.fitWidthProperty().bind(canvas.widthProperty());
        bg.fitHeightProperty().bind(canvas.heightProperty());

        canvas.getChildren().add(0, bg);
    }


    // Build 5v5 grid with bg image
    private void buildIsoGrid() {
        isoLayer.getChildren().clear();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane tile = new StackPane();

                ImageView ground = new ImageView(tileSoilImg);
                ground.setFitWidth(TILE_W);
                ground.setFitHeight(TILE_H);
                ground.setPreserveRatio(false);
                ground.setSmooth(true);

                Label label = new Label("+");
                label.setStyle("-fx-font-size: 13; -fx-text-fill: rgba(0,0,0,0.55); -fx-font-weight: bold;");

                tile.getChildren().addAll(ground, label);

                // Isometric position
                double x = (c - r) * HALF_W;
                double y = (c + r) * HALF_H;
                tile.setLayoutX(x);
                tile.setLayoutY(y);

                // Hover highlight
                tile.setOnMouseEntered(ev -> {
                    tile.setCursor(Cursor.HAND);
                    tile.setScaleX(1.04);
                    tile.setScaleY(1.04);
                });
                tile.setOnMouseExited(ev -> {
                    tile.setCursor(Cursor.DEFAULT);
                    tile.setScaleX(1.0);
                    tile.setScaleY(1.0);
                });

                // Drag and Drop, I just created the method we still need the inventory system
                tile.setOnDragOver(this::handleDragOver);
                tile.setOnDragExited(this::handleDragExited);
                tile.setOnDragDropped(e -> handleDragDropped(e, label, ground));

                isoLayer.getChildren().add(tile);
            }
        }
    }


    // Centralize the screen
    private void centerIsoLayer() {
        Bounds b = isoLayer.getBoundsInParent();
        double isoCenterX = (b.getMinX() + b.getMaxX()) / 2.0;
        double isoCenterY = (b.getMinY() + b.getMaxY()) / 2.0;

        double targetX = canvas.getWidth() / 2.0;
        double targetY = canvas.getHeight() / 2.0;

        isoLayer.setTranslateX(isoLayer.getTranslateX() + (targetX - isoCenterX));
        isoLayer.setTranslateY(isoLayer.getTranslateY() + (targetY - isoCenterY));
    }

    // Clamp
    private void clampIsoLayer() {
        final double margin = 12;
        Bounds b = isoLayer.getBoundsInParent();
        double cw = canvas.getWidth(), ch = canvas.getHeight();

        double dx = 0, dy = 0;

        if (b.getWidth() <= cw - 2 * margin) {
            dx = (cw / 2.0) - (b.getMinX() + b.getMaxX()) / 2.0;
        } else {
            if (b.getMinX() > margin) dx = margin - b.getMinX();
            if (b.getMaxX() < cw - margin) dx = (cw - margin) - b.getMaxX();
        }

        if (b.getHeight() <= ch - 2 * margin) {
            dy = (ch / 2.0) - (b.getMinY() + b.getMaxY()) / 2.0;
        } else {
            if (b.getMinY() > margin) dy = margin - b.getMinY();
            if (b.getMaxY() < ch - margin) dy = (ch - margin) - b.getMaxY();
        }

        if (dx != 0 || dy != 0) {
            isoLayer.setTranslateX(isoLayer.getTranslateX() + dx);
            isoLayer.setTranslateY(isoLayer.getTranslateY() + dy);
        }
    }

    private void resetCamera() {
        scale = 1.0;
        isoLayer.setScaleX(scale);
        isoLayer.setScaleY(scale);
        centerIsoLayer();
        clampIsoLayer();
    }

    // Mouse Zoom on Scroll
    private void handleZoomAtCursor(ScrollEvent e) {
        if (e.getDeltaY() == 0) return;

        double factor = (e.getDeltaY() > 0) ? ZOOM_STEP : (1.0 / ZOOM_STEP);
        double newScale = clamp(scale * factor, MIN_SCALE, MAX_SCALE);
        if (newScale == scale) { e.consume(); return; }

        Point2D mouseInIsoLocal = isoLayer.sceneToLocal(e.getSceneX(), e.getSceneY());
        Point2D before = isoLayer.localToParent(mouseInIsoLocal);

        // rescale
        scale = newScale;
        isoLayer.setScaleX(scale);
        isoLayer.setScaleY(scale);

        Point2D after = isoLayer.localToParent(mouseInIsoLocal);
        isoLayer.setTranslateX(isoLayer.getTranslateX() + (before.getX() - after.getX()));
        isoLayer.setTranslateY(isoLayer.getTranslateY() + (before.getY() - after.getY()));

        clampIsoLayer();
        e.consume();
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

   // Mouse drag the screen
    @FXML
    private void onPanStart(MouseEvent e) {
        panStartX = e.getSceneX();
        panStartY = e.getSceneY();
        baseTranslateX = isoLayer.getTranslateX();
        baseTranslateY = isoLayer.getTranslateY();
    }

    @FXML
    private void onPanDrag(MouseEvent e) {
        double dx = e.getSceneX() - panStartX;
        double dy = e.getSceneY() - panStartY;
        isoLayer.setTranslateX(baseTranslateX + dx);
        isoLayer.setTranslateY(baseTranslateY + dy);
        clampIsoLayer();
    }


    // Drag and Drop

    public void handlePlantDragDetected(MouseEvent event, String plantName) {
        Node src = (Node) event.getSource();
        Dragboard db = src.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString(plantName); // todo: change here later when inventory is done
        db.setContent(content);
        event.consume();
    }

    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            Node n = (Node) event.getSource();
            n.setScaleX(1.04);
            n.setScaleY(1.04);
        }
        event.consume();
    }

    private void handleDragExited(DragEvent event) {
        Node n = (Node) event.getSource();
        n.setScaleX(1.0);
        n.setScaleY(1.0);
        event.consume();
    }

    private void handleDragDropped(DragEvent event, Label label, ImageView ground) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasString()) {
            String plant = db.getString(); // todo: change here later when inventory is done
            label.setText(plant);
            ground.setOpacity(0.98);
            success = true;
        }

        event.setDropCompleted(success);
        event.consume();
    }
}
