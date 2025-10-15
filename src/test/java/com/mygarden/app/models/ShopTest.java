package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopTest {

    Shop shop;

    @BeforeEach
    void setUp() {
        shop = new Shop();
    }

    @Test
    void testAddShopItem() {
        ShopItem item = ShopItem.create("Id","Rose", 1, 0);
        shop.addShopItem(item);

        assertEquals(1, shop.getNumberOfItems());
        assertEquals(item, shop.getShopItem(0));
    }

    @Test
    void testGetShopItemFromCategorie() {
    ShopItem item1 = ShopItem.create("Id1", "Rose", 10, 1); // prix 1
    ShopItem item2 = ShopItem.create("Id2", "Tulip", 120, 2); // prix 2
    ShopItem item3 = ShopItem.create("Id3", "Lily", 15, 1); // prix 1

    shop.addShopItem(item1);
    shop.addShopItem(item2);
    shop.addShopItem(item3);

    // Comme les items sont triés par prix croissant :
    // Prix 1 : item1, item3
    // Prix 2 : item2

    // Vérifie récupération par catégorie 1 (catégorie = 1)
    assertEquals(item1, shop.getShopItemFromCategorie(0, 1));
    assertEquals(item3, shop.getShopItemFromCategorie(1, 1));

    // Vérifie récupération par catégorie 2 (catégorie = 2)
    assertEquals(item2, shop.getShopItemFromCategorie(0, 2));

    // Vérifie récupération avec catégorie -1 (ignore catégorie)
    // Ici l’ordre est par prix croissant
    assertEquals(item1, shop.getShopItemFromCategorie(0, -1));
    assertEquals(item3, shop.getShopItemFromCategorie(1, -1));
    assertEquals(item2, shop.getShopItemFromCategorie(2, -1));

    // Vérifie index hors catégorie -> doit retourner null
    assertNull(shop.getShopItemFromCategorie(2, 1));
}


    @Test
    void testSetShopItems() {
        List<ShopItem> newItems = new ArrayList<>();
        ShopItem item = ShopItem.create("Id1","Rose", 1, 1);
        newItems.add(item);

        shop.setShopItems(newItems);

        // Vérifie que la taille du shop a bien été mise à jour
        assertEquals(1, shop.getNumberOfItems());

        // Vérifie que l’item ajouté est bien celui récupéré
        assertEquals(item, shop.getShopItem(0));
    }

    @Test
    void testGetNumberOfItemsEmptyShop() {
        assertEquals(0, shop.getNumberOfItems());
    }
}
