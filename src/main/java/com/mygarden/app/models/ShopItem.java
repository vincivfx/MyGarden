package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mg_shop_items")
public class ShopItem {
    @DatabaseField(id = true)
    private String shop_item_id;

    @DatabaseField
    private String name;

    @DatabaseField
    private int price;

    @DatabaseField
    private int category_id;

    public ShopItem() {
        // no-arg constructor for ORMLite
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setId(String id) {
        this.shop_item_id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCategoryId(int category_id) {
        this.category_id = category_id;
    }

    /**
     * Static method that enables developers to get a ShopItem without constructor
     *
     * @param name       Name of the ShopItem
     * @param price      Price in coins
     * @param categoryId Category Id related to the ShopItem
     * @return the generated ShopItem
     */
    public static ShopItem create(String id, String name, int price, int categoryId) {
        ShopItem shopItem = new ShopItem();
        shopItem.setId(id);
        shopItem.setName(name);
        shopItem.setPrice(price);
        shopItem.setCategoryId(categoryId);
        return shopItem;
    }
}
