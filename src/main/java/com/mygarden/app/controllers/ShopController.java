package com.mygarden.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;

import com.mygarden.app.models.Shop;

public class ShopController implements Initializable {
    Shop shop= new Shop();
    @FXML
    private ImageView img00;
    @FXML
    private ImageView img10;
    @FXML
    private ImageView img01;
    @FXML
    private ImageView img11;


    @Override
    public void initialize (URL url, ResourceBundle resbundle){
        Image image= new Image(shop.getShopItems().get(0).getImagePath());
        img00.setImage(image);
        
        
    }

    
}
