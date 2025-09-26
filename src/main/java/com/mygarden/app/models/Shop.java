package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<ShopItem> ShopItems;


    public Shop() {
    }

    public List<ShopItem> getShopItems() {
        return ShopItems;
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.ShopItems = shopItems;
    }
    
}
