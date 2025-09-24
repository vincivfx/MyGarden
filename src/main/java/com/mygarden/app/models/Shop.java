package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<ShopItem> ShopItems;
    

    public Shop() {
        ShopItems = new ArrayList<>();
        ShopItems.add(new ShopItem("tulip",3,"/images/MainPage.jpg"));
    }

    public List<ShopItem> getShopItems() {
        return ShopItems;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.ShopItems = shopItems;
    }
    
}
