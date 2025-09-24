package com.mygarden.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mygarden.app.models.Shop;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;


public class ShopController implements Initializable {
    Shop shop = new Shop();

    @FXML
    private GridPane ShopGrid;

    private void InitialzeGrid(int rows, int columnss)
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

    @Override
    public void initialize (URL url, ResourceBundle resbundle){

           InitialzeGrid(3,4);

            for (int i = 0; i < shop.getShopItems().size(); i++)
            {
                AnchorPane anchor = (AnchorPane)(ShopGrid.getChildren().get(i));
                ImageView iv = (ImageView)anchor.getChildren().get(0);

                iv.setImage(new Image(getClass().getResourceAsStream(shop.getShopItems().get(i).getImagePath())));
            } 
            
        }

    
}
