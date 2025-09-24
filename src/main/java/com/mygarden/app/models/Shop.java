package com.mygarden.app.models;

import java.util.List;

public class Shop {
    List<Plant> shopItems;
    

    public Shop() {
        shopItems.add(new Plant("tulip",3,"images/MainPage.jpg"));
    }

    public List<Plant> getShopItems() {
        return shopItems;
    }

    public void setShopItems(List<Plant> shopItems) {
        this.shopItems = shopItems;
    }
    
}
