package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<Plant> ShopItems;
    

    public Shop() {
        ShopItems = new ArrayList<>();
        ShopItems.add(new Plant("tulip",3,"/images/MainPage.jpg"));
    }

    public List<Plant> getShopItems() {
        return ShopItems;
    }

    public void setShopItems(List<Plant> shopItems) {
        this.ShopItems = shopItems;
    }
    
}
