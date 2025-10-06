package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<ShopItem> ShopItems;


    public Shop() {
        ShopItems = new ArrayList<>();
    }

    public void addShopItem(ShopItem item) {
        ShopItems.add(item);
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
            if(item.getCategory() == categorie)
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
