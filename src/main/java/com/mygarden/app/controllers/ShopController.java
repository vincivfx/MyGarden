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

import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.Shop;
import com.mygarden.app.models.ShopItem;
import com.mygarden.app.repositories.ShopItemsRepository;
import com.mygarden.app.repositories.TransferRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class ShopController extends AbstractController implements Initializable  {


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
                

                indexInShop++;
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
        loadShopFromDatabase();
        showShopItemsFromCategorie(currentCategorie);
    }

    // --- End Methods ---

    // --- FXML UI elements ---
    @FXML
    private Label UserCoins;

    @FXML
    private GridPane ShopGrid;
    // --- END FXML UI elements ---
    
    @FXML
    private void buyPlant(MouseEvent event) throws IOException {
        
        //Get the index of the item in the shop
        AnchorPane cell = (AnchorPane) event.getSource();
        int indexInShop = ShopGrid.getChildren().indexOf(cell);

        ShopItem shopItem = shop.getShopItemFromCategorie(indexInShop, currentCategorie);

        if(shopItem.getPrice() <= getUser().getCoins()) //Enough Money
        {
            if(SceneUtils.showConfirmationPopup(String.format("Are you sure to buy %s ?", shopItem.getName())))
            {
                System.out.println("Buy");

                TransferRepository tr = new TransferRepository();
                try {
                    tr.buy(getUser(), shopItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // getUser().spendCoins(shopItem.getPrice());
                updateUICoins();
                

                //Create the plant with the name and the image of the shop item
                //getUser().addPlantInInventory(new Plant()); 
                SceneUtils.showPopup("Plant is bought");
            }
            
        }
        else  //Not Enough money
        {
            System.out.println("Not Enough money");
        }

        
    }

    @FXML
    private void changeCategorie(ActionEvent event)
    {
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
    }

    @FXML
    private void goToMainPage(ActionEvent event) throws IOException {
        SceneUtils.changeScene(event, "/com/mygarden/app/main-page-view.fxml", getUser());
    }


    
}
