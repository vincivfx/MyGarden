package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<ShopItem> ShopItems;


    public Shop() {
        ShopItems = new ArrayList<>();
        ShopItems.add(new ShopItem("Tulip", 15, "/images/shopImg/tulip.png", 0));
        ShopItems.add(new ShopItem("Rose", 25, "/images/shopImg/rose.png", 0));
        ShopItems.add(new ShopItem("Sunflower", 20, "/images/shopImg/sunflower.png", 0));
        ShopItems.add(new ShopItem("Daisy", 40, "/images/shopImg/daisy.png", 0));
         
        ShopItems.add(new ShopItem("Boxwood", 35, "/images/shopImg/boxwood.png", 1));
        ShopItems.add(new ShopItem("Hydrangea", 30, "/images/shopImg/hydrangea.png", 1));
        ShopItems.add(new ShopItem("Lavender", 35, "/images/shopImg/lavander.png", 1));
        ShopItems.add(new ShopItem("Rhododendron", 30, "/images/shopImg/rhododendron.png", 1));

        ShopItems.add(new ShopItem("Maple", 200, "/images/shopImg/maple.png", 2));
        ShopItems.add(new ShopItem("Oak", 120, "/images/shopImg/oak.png", 2));
        ShopItems.add(new ShopItem("Pine", 80, "/images/shopImg/pine.png", 2));
        ShopItems.add(new ShopItem("Willow", 150, "/images/shopImg/willow.png", 2));
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
