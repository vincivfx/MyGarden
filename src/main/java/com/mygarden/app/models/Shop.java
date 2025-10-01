package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<ShopItem> ShopItems;


    public Shop() {
        ShopItems = new ArrayList<>();
        ShopItems.add(new ShopItem("Tulip", 10, "/images/shopImg/tulip.png", 0));
        ShopItems.add(new ShopItem("Rose", 20, "/images/shopImg/rose.png", 0));
        ShopItems.add(new ShopItem("Sunflower", 30, "/images/shopImg/sunflower.png", 1));
        ShopItems.add(new ShopItem("Daisy", 40, "/images/shopImg/daisy.png", 2));
    }

    public ShopItem getShopItem(int i) {
        return ShopItems.get(i);
    }

    public ShopItem getShopItemFromCategorie(int i, int categorie) 
    {
        if(categorie == -1)
        {
            return getShopItem(i);
        }

        int currentIndexInCategorie = 0;
        for(int j = 0; j < getNumberOfItems(); j++)
        {
            ShopItem item = getShopItem(j);
            if(item.getCategorie() == categorie)
            {
                if(currentIndexInCategorie == i)
                {
                    return item;
                }
                currentIndexInCategorie++;
            }
        }
        return null;
    }

    public int getNumberOfItems()
    {
        return ShopItems.size();
    }

    public void setShopItems(List<ShopItem> shopItems) {
        this.ShopItems = shopItems;
    }
    
}
