package com.mygarden.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mygarden.app.LanguageManager;
import com.mygarden.app.SoundManager;
import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.Shop;
import com.mygarden.app.models.ShopItem;
import com.mygarden.app.models.ShopItemTranslation;
import com.mygarden.app.repositories.ShopItemTranslationRepository;
import com.mygarden.app.repositories.ShopItemsRepository;
import com.mygarden.app.repositories.TransferRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


public class ShopController extends AbstractController implements Initializable {



    int currentCategorie = -1;
    private final Map<String, Image> imageCache = new HashMap<>();


    // --- Models ---

    Shop shop = new Shop();

    // --- End Models ---


    // --- Methods ---
    private void initialzeGrid(int rows, int columnss)
    {
        for (int row = 0; row < rows; row++) 
        {

            for (int col = 0; col < columnss; col++) 
            {
                // Créer la cellule
                AnchorPane cell = new AnchorPane();
                cell.setPrefSize(100, 100); // taille de la cellule, ajuste comme tu veux
                
                // Ajouter l'image
                ImageView imageView = new ImageView();
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                AnchorPane.setTopAnchor(imageView, 10.0); // distance du haut
                AnchorPane.setLeftAnchor(imageView, 10.0); // distance de la gauche
                
                // Ajouter le texte
                Label label = new Label("Texte");
                AnchorPane.setBottomAnchor(label, 10.0); // distance du bas
                AnchorPane.setLeftAnchor(label, 10.0); // distance de la gauche
                
                // Ajouter image et texte au AnchorPane
                cell.getChildren().addAll(imageView, label);
                
                // Ajouter la cellule à la grille
                ShopGrid.add(cell, col, row);
            }
        }
    }

    private void loadShopFromDatabase() {

        ShopItemsRepository repository = new ShopItemsRepository();
        try 
        {
            List<ShopItem> shopItemList = repository.findAll();

            for (int i = 0; i < shopItemList.size(); i++) 
            {
                ShopItem item = shopItemList.get(i);
                shop.addShopItem(item);
                imageCache.put(item.getId(),
                    new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/images/shopImg/" + item.getId() + ".png")
                    ))
                );  
            }
        } 
        catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
            
    }
    
    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d", getUser().getCoins()));
    }

    private void clearShop()
    {
       for(Node node : ShopGrid.getChildren())
       {
            AnchorPane anchor = (AnchorPane)node;
           
            ImageView itemImage = (ImageView)anchor.getChildren().get(0);
            itemImage.setImage(null);
       }
    }

    private void showShopItemsFromCategorie(int categorie)
    {
        clearShop();
        List<Node> anchors = new ArrayList<>(ShopGrid.getChildren());
        int indexInShop = 0;
        for (int i = 0; i < shop.getNumberOfItems(); i++)
        {
            ShopItem item = shop.getShopItem(i);
            if(item.getCategory() == categorie || categorie == -1)
            {
                AnchorPane anchor = (AnchorPane)(anchors.get(indexInShop));
                ImageView itemImage = (ImageView)anchor.getChildren().get(0);

                itemImage.setImage(
                    imageCache.get(item.getId())
                );

                if(item.getPrice() > getUser().getCoins())
                {
                    anchor.setOnMouseClicked(null);
                    anchor.setOnMouseEntered(null);
                    anchor.setOnMouseExited(null);
                    anchor.setCursor(Cursor.DEFAULT);

                    ColorAdjust grayEffect = new ColorAdjust();
                    grayEffect.setSaturation(-0.5);
                    itemImage.setEffect(grayEffect);
                }
                else
                {
                    anchor.setOnMouseClicked(e -> buyPlant(e));
                    anchor.setOnMouseEntered(e -> buttonIsHovered(e));
                    anchor.setOnMouseExited(e -> buttonIsNoLongerHovered(e));
                    anchor.setCursor(Cursor.HAND);

                    itemImage.setEffect(null);
                }

                indexInShop++;
            }
        } 
    }

    @Override
    public void onUserIsSet()
    {
        //Call when the page is load to update all the UI with the user data
        updateUICoins();
        showShopItemsFromCategorie(currentCategorie);
    }

    @Override
    public void initialize (URL url, ResourceBundle resbundle)
    {   
        loadShopFromDatabase();

        /*
         * Minimal i18n initialization:
         * Use the ResourceBundle provided by FXMLLoader if available (resbundle),
         * otherwise fall back to LanguageManager.getBundle().
         * We only set the UI strings here (do not change existing behaviour).
         */
        ResourceBundle bundle = (resbundle != null) ? resbundle : LanguageManager.getBundle();

        try {
            if (titleText != null && bundle.containsKey("shop.title")) {
                titleText.setText(bundle.getString("shop.title"));
            }
            if (mainPageButton != null && bundle.containsKey("shop.mainPage")) {
                mainPageButton.setText(bundle.getString("shop.mainPage"));
            }
            if (CategorieAll != null && bundle.containsKey("shop.categories.all")) {
                CategorieAll.setText(bundle.getString("shop.categories.all"));
            }
            if (Categorie0 != null && bundle.containsKey("shop.categories.flowers")) {
                Categorie0.setText(bundle.getString("shop.categories.flowers"));
            }
            if (Categorie1 != null && bundle.containsKey("shop.categories.shrubs")) {
                Categorie1.setText(bundle.getString("shop.categories.shrubs"));
            }
            if (Categorie2 != null && bundle.containsKey("shop.categories.trees")) {
                Categorie2.setText(bundle.getString("shop.categories.trees"));
            }

            // placeholder/fallback for coins before the real user is set
            if (UserCoins != null && bundle.containsKey("shop.coins")) {
                UserCoins.setText(bundle.getString("shop.coins"));
            }
        } catch (Exception e) {
            // be conservative: if bundle lookup fails, do not break initialization
            e.printStackTrace();
        }
    }
    // --- End Methods ---

    // --- FXML UI elements ---
    @FXML
    private Text titleText;
    
    @FXML
    private Label UserCoins;

    @FXML
    private GridPane ShopGrid;

    @FXML
    private Button mainPageButton;

    @FXML
    private Button CategorieAll;

    @FXML
    private Button Categorie0;

    @FXML
    private Button Categorie1;

    @FXML
    private Button Categorie2;

    @FXML
    private ScrollPane scrollPaneShop;
    // --- END FXML UI elements ---
    
    @FXML
    private void buyPlant(MouseEvent event) {
        
        //Get the index of the item in the shop
        AnchorPane cell = (AnchorPane) event.getSource();
        int indexInShop = ShopGrid.getChildren().indexOf(cell);

        ShopItem shopItem = shop.getShopItemFromCategorie(indexInShop, currentCategorie);

        // Get the translation for the current language
        ShopItemTranslationRepository sitRepo = new ShopItemTranslationRepository();
        String lang = LanguageManager.getCurrentLang(); // es. "it", "sv", "en"
        ShopItemTranslation sit = sitRepo.getTranslation(shopItem, lang);

        if (sit == null) {
            // english fallback
            sit = sitRepo.getTranslation(shopItem, "en");
        }

        if(shopItem.getPrice() <= getUser().getCoins()) //Enough Money
        {
            try {
                if(SceneUtils.showConfirmationPopupFromKey("popup.purchase.confirm", sit.getName()))
                {
                    System.out.println("Buy");

                    TransferRepository tr = new TransferRepository();
                    try {
                        var currentUser = getUser();
                        var result = tr.buy(currentUser, shopItem);
                        if (result.isPresent()) {
                            System.out.println("ShopController.buyPlant: purchase successful for " + currentUser.getUsername());
                            updateUICoins();
                            SoundManager.getInstance().playPurchase();
                            SceneUtils.showPopupFromKey("popup.plant.bought");
                            showShopItemsFromCategorie(currentCategorie);
                            scrollPaneShop.setVvalue(scrollPaneShop.getVmin());
                            //SceneUtils.showPopup("Plant is bought");
                        } else {
                            System.out.println("ShopController.buyPlant: not enough coins for " + currentUser.getUsername());
                            SceneUtils.showPopupFromKey("popup.not.enough.coins");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        else  //Not Enough money
        {
            System.out.println("Not Enough money");
        }

        
    }

    @FXML
    private void buttonIsHovered(MouseEvent event)
    {
        ColorAdjust hoverEffect = new ColorAdjust();
        hoverEffect.setSaturation(0.5);

        AnchorPane cell = (AnchorPane) event.getSource();
        ImageView image = (ImageView) cell.getChildren().get(0);
        image.setEffect(hoverEffect);

    }

    @FXML
    private void buttonIsNoLongerHovered(MouseEvent event)
    {
        AnchorPane cell = (AnchorPane) event.getSource();
        ImageView image = (ImageView) cell.getChildren().get(0);
        image.setEffect(null);

    }

    @FXML
    private void changeCategorie(ActionEvent event)
    {
        SoundManager.getInstance().playClick();
        Button source = (Button) event.getSource();
         
        Pattern pattern = Pattern.compile("Categorie(\\d+)");
        Matcher matcher = pattern.matcher(source.getId());
        
        int categorie;
        if (matcher.matches()) {
            categorie = Integer.parseInt(matcher.group(1)); 
        } else {
            categorie = -1;
        }
        currentCategorie = categorie;
        showShopItemsFromCategorie(categorie);
        scrollPaneShop.setVvalue(scrollPaneShop.getVmin());
    }

    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }

    @FXML
    private void goToGarden(ActionEvent event) throws IOException {
        SoundManager.getInstance().playClick();
        SceneUtils.changeScene(event, "/com/mygarden/app/garden-view.fxml", getUser());
    }



    
}
