package com.mygarden.app.controllers;

import java.io.IOException;

import com.mygarden.app.LanguageManager;
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
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import com.mygarden.app.models.ShopItem;
import com.mygarden.app.models.ShopItemTranslation;
import com.mygarden.app.models.UserItem;
import com.mygarden.app.repositories.ShopItemTranslationRepository;
import com.mygarden.app.repositories.UserItemRepository;

public class GardenController extends AbstractController {

    // Layout
    private static final int COLS = 5;
    private static final int ROWS = 5;

    // Tile size
    private static final double TILE_W = 72;
    private static final double TILE_H = 40;
    private static final double HALF_W = TILE_W / 2.0;
    private static final double HALF_H = TILE_H / 2.0;

    // Placed plant image size (used for planted ImageView fit and drag ghost size)
    private static final double PLACED_IMG_SIZE = 48.0;

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
    @FXML private AnchorPane inventoryPane;
    @FXML private GridPane inventoryGrid;
    @FXML private Button inventoryButton;

    // Images
    private Image tileSoilImg;

    @Override
    public void onUserIsSet() {
        System.out.println("Garden owner: " + (getUser() != null ? getUser().getName(): "Who?"));
        // Now that user is available, load inventory and placed items
        try {
            loadInventoryItems();
            loadPlacedItems();
        } catch (Exception ex) {
            System.err.println("Could not load user items on setUser: " + ex.getMessage());
        }
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

        // Inventory defaults: hidden
        if (inventoryPane != null) {
            inventoryPane.setMaxHeight(0);
            inventoryPane.setManaged(false);
            inventoryPane.setVisible(false);
        }
        // Do not load user-specific items here; wait until onUserIsSet when the user is available

        // allow dropping placed items back to inventory
        if (inventoryGrid != null) {
            inventoryGrid.setOnDragOver(e -> {
                if (e.getDragboard().hasString() && e.getDragboard().getString().startsWith("useritem:")) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
                e.consume();
            });

            inventoryGrid.setOnDragDropped(e -> {
                Dragboard db = e.getDragboard();
                boolean ok = false;
                if (db.hasString()) {
                    String payload = db.getString();
                    if (payload.startsWith("useritem:")) {
                        String[] parts = payload.split(":", 3);
                        if (parts.length >= 2) {
                            Integer userItemId = Integer.valueOf(parts[1]);
                            try {
                                UserItemRepository repo = new UserItemRepository();
                                var opt = repo.findById(userItemId);
                                if (opt.isPresent()) {
                                    UserItem ui = opt.get();
                                    // clear position
                                    ui.move(null, null);
                                    repo.save(ui);
                                    // remove placed image
                                    removePlacedImageByUserItemId(ui.getId());
                                    // refresh inventory UI
                                    loadInventoryItems();
                                    ok = true;
                                }
                            } catch (Exception ex) {
                                System.err.println("Failed to move item back to inventory: " + ex.getMessage());
                            }
                        }
                    }
                }
                e.setDropCompleted(ok);
                e.consume();
            });
        }
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

    @FXML
    private void onToggleInventory(ActionEvent event) {
        System.out.println("onToggleInventory called");
        if (inventoryPane == null) return;

        boolean isHidden = !inventoryPane.isVisible() || !inventoryPane.isManaged() || inventoryPane.getPrefHeight() == 0;
        if (isHidden) {
            // show - do not remove/reattach from parent, just make it visible and allow layout
            inventoryPane.setManaged(true);
            inventoryPane.setVisible(true);
            inventoryPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
            inventoryPane.setPrefHeight(150);
            // refresh items
            try { loadInventoryItems(); } catch (Exception ignored) {}
        } else {
            // hide - collapse via sizing and managed/visible flags
            inventoryPane.setVisible(false);
            inventoryPane.setManaged(false);
            inventoryPane.setMaxHeight(0);
            inventoryPane.setPrefHeight(0);
            inventoryPane.setMinHeight(0);
        }
        // request re-layout to ensure BorderPane updates
        if (inventoryPane.getParent() != null) inventoryPane.getParent().requestLayout();
        // ensure the button stays enabled
        if (inventoryButton != null) inventoryButton.setDisable(false);
        System.out.println("onToggleInventory completed; visible=" + inventoryPane.isVisible());
    }

    private void loadInventoryItems() throws SQLException {
        if (inventoryGrid == null || getUser() == null) return;

        UserItemRepository repo = new UserItemRepository();
        List<UserItem> items = repo.findAll();

        System.out.println("loadInventoryItems: total items found=" + (items != null ? items.size() : 0));

        inventoryGrid.getChildren().clear();

        int cols = 3;
        int r = 0, c = 0;
        for (UserItem ui : items) {
            try {
                if (ui == null) continue;
                if (ui.getUser() == null) {
                    System.out.println("skip UserItem id=" + ui.getId() + " because user==null");
                    continue;
                }
                String owner = ui.getUser().getUsername();
                if (!Objects.equals(owner, getUser().getUsername())) {
                    System.out.println("skip UserItem id=" + ui.getId() + " owner=" + owner);
                    continue;
                }

                ShopItem shopItem = ui.getShopItem();
                if (shopItem == null) {
                    System.out.println("skip UserItem id=" + ui.getId() + " because shopItem==null");
                    continue;
                }

                // skip items that are already placed in the garden
                if (ui.getPositionX() != null || ui.getPositionY() != null) {
                    System.out.println("skip UserItem id=" + ui.getId() + " because already placed at (" + ui.getPositionX() + "," + ui.getPositionY() + ")");
                    continue;
                }

                javafx.scene.layout.AnchorPane cell = new javafx.scene.layout.AnchorPane();
                cell.setPrefSize(80, 80);

                ImageView iv = new ImageView();
                iv.setFitWidth(64);
                iv.setFitHeight(64);
                iv.setPreserveRatio(true);
                try {
                    var is = getClass().getResourceAsStream("/images/inventoryImg/" + shopItem.getId() + ".png");
                    if (is != null) iv.setImage(new Image(is));
                    else {
                        var is2 = getClass().getResourceAsStream("/images/shopImg/" + shopItem.getId() + ".png");
                        if (is2 != null) iv.setImage(new Image(is2));
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load image for shopItem=" + shopItem.getId() + " error=" + e.getMessage());
                }

                // Start drag when pressed
                final Integer userItemId = ui.getId();
                final String shopId = shopItem.getId();

                // get translated name
                ShopItemTranslationRepository sitRepo = new ShopItemTranslationRepository();
                String lang = LanguageManager.getCurrentLang();
                
                ShopItemTranslation sit = sitRepo.getTranslation(shopItem, lang);
                if (sit == null) {
                    sit = sitRepo.getTranslation(shopItem, "en");
                }
                final String itemName = sit != null ? sit.getName() : shopId;

                iv.setOnDragDetected(ev -> {
                    Node src = (Node) ev.getSource();
                    javafx.scene.input.Dragboard db = src.startDragAndDrop(TransferMode.MOVE);
                    javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                    // payload: useritem:<userItemId>:<shopItemId>:<name>
                    content.putString("useritem:" + userItemId + ":" + shopId + ":" + itemName);
                    db.setContent(content);
                    // ghost image (drag view) using images\gardenImg
                    try {
                        var gis = getClass().getResourceAsStream("/images/gardenImg/" + shopId + ".png");
                        if (gis != null) {
                            javafx.scene.image.Image dragImg = new javafx.scene.image.Image(gis, PLACED_IMG_SIZE*2, PLACED_IMG_SIZE*2, true, true);
                            db.setDragView(dragImg);
                        }
                    } catch (Exception ignore) {}
                    ev.consume();
                });

                javafx.scene.control.Label nameLabel = new javafx.scene.control.Label(itemName);
                javafx.scene.layout.AnchorPane.setLeftAnchor(iv, 8.0);
                javafx.scene.layout.AnchorPane.setTopAnchor(iv, 4.0);
                javafx.scene.layout.AnchorPane.setLeftAnchor(nameLabel, 8.0);
                // lower the name label so it doesn't overlap the plant image
                javafx.scene.layout.AnchorPane.setTopAnchor(nameLabel, 70.0);

                cell.getChildren().addAll(iv, nameLabel);
                // tag cell with userItem id for later removal
                cell.setUserData(ui.getId());

                inventoryGrid.add(cell, c, r);

                c++;
                if (c >= cols) { c = 0; r++; }
            } catch (Exception itemEx) {
                System.err.println("Error rendering UserItem id=" + (ui != null ? ui.getId() : "null") + ": " + itemEx.getMessage());
            }
        }

        // Ensure layout updates
        inventoryGrid.requestLayout();
    }

    
    // Load all persisted UserItems which have a set position and render them on the garden tiles
    private void loadPlacedItems() {
        try {
            UserItemRepository repo = new UserItemRepository();
            var all = repo.findAll();
            if (all == null) return;

            for (UserItem ui : all) {
                try {
                    if (ui == null) continue;
                    if (ui.getUser() == null) continue;
                    if (!Objects.equals(ui.getUser().getUsername(), getUser() != null ? getUser().getUsername() : null)) continue;
                    if (ui.getPositionX() == null || ui.getPositionY() == null) continue;

                    // stored as position_x = column, position_y = row
                    int cx = ui.getPositionX();
                    int rx = ui.getPositionY();

                    // Find the tile matching coords
                    for (Node n : isoLayer.getChildren()) {
                        Object ud = n.getUserData();
                        if (ud instanceof int[]) {
                            int[] coords = (int[]) ud;
                            if (coords[0] == rx && coords[1] == cx) {
                                // place image view on this tile
                                ImageView placed = createPlacedImageView(ui);
                                // ensure it can be dragged
                                n.setOnDragOver(this::handleDragOver);
                                n.setOnDragExited(this::handleDragExited);
                                // add to tile (it's a StackPane)
                                if (n instanceof StackPane) {
                                    ((StackPane) n).getChildren().add(placed);
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Failed to render placed UserItem id=" + ui.getId() + " -> " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading placed items: " + e.getMessage());
        }
    }

    private ImageView createPlacedImageView(UserItem ui) {
        ImageView iv = new ImageView();
        iv.setFitWidth(PLACED_IMG_SIZE);
        iv.setFitHeight(PLACED_IMG_SIZE);
        iv.setPreserveRatio(true);
        // move planted images slightly up
        iv.setTranslateY(-18.0);
        try {
            var shopItem = ui.getShopItem();
            if (shopItem != null) {
                var is = getClass().getResourceAsStream("/images/gardenImg/" + shopItem.getId() + ".png");
                if (is == null) is = getClass().getResourceAsStream("/images/shopImg/" + shopItem.getId() + ".png");
                if (is != null) iv.setImage(new Image(is));
            }
        } catch (Exception ignored) {}

    // tag with userItem id so we can persist movements
    iv.setUserData(ui.getId());
    System.out.println("createPlacedImageView: creating image for userItem=" + ui.getId() + " shopItem=" + (ui.getShopItem() != null ? ui.getShopItem().getId() : "null") );

    // start drag from placed image
    iv.setOnDragDetected(ev -> handleGardenImageDragDetected(ev, ui.getId()));

        return iv;
    }

    private void handleGardenImageDragDetected(MouseEvent event, Integer userItemId) {
        Node src = (Node) event.getSource();
        Dragboard db = src.startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        // payload indicates placed item
        content.putString("useritem:" + userItemId + ":placed");
        db.setContent(content);
        // set drag view (ghost) to the garden image for this user item
        try {
            UserItemRepository repo = new UserItemRepository();
            var opt = repo.findById(userItemId);
            if (opt.isPresent()) {
                var shop = opt.get().getShopItem();
                if (shop != null) {
                    var gis = getClass().getResourceAsStream("/images/gardenImg/" + shop.getId() + ".png");
                    if (gis != null) db.setDragView(new Image(gis, PLACED_IMG_SIZE*2, PLACED_IMG_SIZE*2, true, true));
                }
            }
        } catch (Exception ignore) {}

        event.consume();
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
                // store tile coordinates for drop handling
                tile.setUserData(new int[]{r, c});

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
        content.putString(plantName); // when coming from inventory we pass "useritem:<id>:<name>"
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

        if (!db.hasString()) {
            event.setDropCompleted(false);
            event.consume();
            return;
        }

        String payload = db.getString();
        if (payload == null || !payload.startsWith("useritem:")) {
            // do not write arbitrary payload text into the garden
            event.setDropCompleted(false);
            event.consume();
            return;
        }

        // payload can be useritem:<userItemId>:<shopId>:<name>
    String[] parts = payload.split(":");
    String idStr = parts.length >= 2 ? parts[1] : null;

        if (idStr == null) {
            event.setDropCompleted(false);
            event.consume();
            return;
        }

        Node tile = (Node) event.getSource();
        Object ud = tile.getUserData();
        if (!(ud instanceof int[])) {
            event.setDropCompleted(false);
            event.consume();
            return;
        }

        int[] coords = (int[]) ud;
        int rx = coords[0];
        int cx = coords[1];

        // rely on repository.move() to check DB occupancy (do not pre-check UI children which may be noisy)

        try {
            UserItemRepository repo = new UserItemRepository();
            var opt = repo.findById(Integer.valueOf(idStr));
            if (opt.isPresent()) {
                UserItem ui = opt.get();
                // use repository move() which checks occupancy
                boolean moved = repo.move(ui, cx, rx);
                if (!moved) {
                    System.out.println("Repo rejected move: position occupied");
                    success = false;
                } else {
                    var saved = repo.findById(ui.getId()).orElse(ui);
                    removePlacedImageByUserItemId(ui.getId());
                    ImageView placed = createPlacedImageView(saved);
                    if (tile instanceof StackPane) ((StackPane) tile).getChildren().add(placed);

                    // remove from inventory grid if present
                    javafx.scene.Node toRemove = null;
                    for (javafx.scene.Node n : inventoryGrid.getChildren()) {
                        if (n.getUserData() != null && n.getUserData().equals(ui.getId())) { toRemove = n; break; }
                    }
                    if (toRemove != null) inventoryGrid.getChildren().remove(toRemove);

                    // keep the tile label unchanged — only show the image when placing an item

                    success = true;
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to persist user item move: " + ex.getMessage());
            success = false;
        }

        event.setDropCompleted(success);
        event.consume();
    }

    private void removePlacedImageByUserItemId(Integer userItemId) {
        for (Node n : isoLayer.getChildren()) {
            if (n instanceof StackPane) {
                StackPane sp = (StackPane) n;
                javafx.scene.Node toRemove = null;
                for (javafx.scene.Node child : sp.getChildren()) {
                    if (child instanceof ImageView && child.getUserData() != null && child.getUserData().equals(userItemId)) {
                        toRemove = child;
                        break;
                    }
                }
                if (toRemove != null) {
                    sp.getChildren().remove(toRemove);
                    return;
                }
            }
        }
    }
}
