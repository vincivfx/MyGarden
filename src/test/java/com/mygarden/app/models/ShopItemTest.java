package com.mygarden.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ShopItemTest {

    @Test
    void testEmptyConstructorAndSettersGetters() {
        ShopItem item = new ShopItem();

        item.setId("Id1");
        item.setName("Rose");
        item.setPrice(10);
        item.setCategoryId(1);

        assertEquals("Id1", item.getId());
        assertEquals("Rose", item.getName());
        assertEquals(10, item.getPrice());
        assertEquals(1, item.getCategory());
    }

    @Test
    void testStaticCreateMethod() {
        ShopItem item = ShopItem.create("Id2", "Tulip", 15, 2);

        assertEquals("Id2", item.getId());
        assertEquals("Tulip", item.getName());
        assertEquals(15, item.getPrice());
        assertEquals(2, item.getCategory());
    }

    @Test
    void testModifyPropertiesAfterCreate() {
        ShopItem item = ShopItem.create("Id3", "Lily", 20, 3);

        // Modifier les propriétés après création
        item.setName("Daisy");
        item.setPrice(25);
        item.setCategoryId(4);

        assertEquals("Daisy", item.getName());
        assertEquals(25, item.getPrice());
        assertEquals(4, item.getCategory());
    }
}
