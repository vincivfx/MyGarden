package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<ShopItem> ShopItems;


    public Shop() {
        ShopItems = new ArrayList<>();
        ShopItems.add(new ShopItem("Tulip", 10, "/images/tulip.jpg"));
        ShopItems.add(new ShopItem("Rose", 20, "/images/rose.jpg"));
        ShopItems.add(new ShopItem("Sunflower", 30, "/images/sunflower.jpg"));
        ShopItems.add(new ShopItem("Daisy", 40, "/images/daisy.jpg"));
    }

    public ShopItem getShopItem(int i) {
        return ShopItems.get(i);
    }

    public int getNumberOfItems()
    {
        return ShopItems.size();
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.ShopItems = shopItems;
    }
    
}
