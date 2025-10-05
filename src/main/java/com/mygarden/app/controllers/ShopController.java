package com.mygarden.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.Shop;
import com.mygarden.app.models.ShopItem;

import com.mygarden.app.repositories.ShopItemsRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class ShopController extends AbstractController implements Initializable  {

    // --- Models ---

    Shop shop = new Shop();

    // --- End Models ---


    // --- Methods ---
    private void initialzeGrid(int rows, int columnss)
    {
         for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columnss; col++) {
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

    private void updateUICoins()
    {
        UserCoins.setText(String.format("%d coins", getUser().getCoins()));
    }

    // --- End Methods ---

    // --- FXML UI elements ---
    @FXML
    private Label UserCoins;

    @FXML
    private GridPane ShopGrid;
    // --- END FXML UI elements ---
    
    @FXML
    private void BuyPlant(MouseEvent event) throws IOException {
        
        if(SceneUtils.showConfirmationPopup("Are you sure to buy this plant ?"))
        {
            //Get the index of the item in the shop
            AnchorPane cell = (AnchorPane) event.getSource();
            int indexInShop = ShopGrid.getChildren().indexOf(cell);

            // If the index is valid
            if(indexInShop >= 0 && indexInShop < shop.getNumberOfItems())
            {
                ShopItem shopItem = shop.getShopItem(indexInShop);

                if(shopItem.getPrice() <= getUser().getCoins()) //Enough Money
                {
                    System.out.println("Buy");
                    getUser().spendCoins(shopItem.getPrice());
                    updateUICoins();

                    //Create the plant with the name and the image of the shop item
                    // getUser().addPlantInInventory(new Plant());
                    
                }
                else  //Not Enough money
                {
                System.out.println("Not Enough money");
                }
            }
        }
    }

    @Override
    public void onUserIsSet()
    {
        //Call when the page is load to update all the UI with the user data
        updateUICoins();
    }

    @Override
    public void initialize (URL url, ResourceBundle resbundle)
    {

        ShopItemsRepository repository = new ShopItemsRepository();
        try {
            List<ShopItem> shopItemList = repository.findAll();

            for (int i = 0; i < shopItemList.size(); i++)
            {
                ShopItem item = shopItemList.get(i);

                AnchorPane anchor = (AnchorPane)(ShopGrid.getChildren().get(i));
                ImageView itemImage = (ImageView)anchor.getChildren().get(0);
                itemImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/shopImg/" + item.getId() + ".png"))));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }


    
}
