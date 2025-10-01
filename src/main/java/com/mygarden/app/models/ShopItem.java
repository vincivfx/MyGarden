package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mg_shop_items")
public class ShopItem {
    @DatabaseField(id = true, generatedId = true)
    private int shop_item_id;

    @DatabaseField
    private String name;

    @DatabaseField
    private int price;

    @DatabaseField
    private String image_path;

    @DatabaseField
    private int categorie;

    public ShopItem(String name, int price, String image_path, int categorie) {
        this.name = name;
        this.price = price;
        this.image_path = image_path;
        this.categorie = categorie;
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
    public void setPrice(int price) {
        this.price = price;
    }
    public String getImagePath() {
        return image_path;
    }
    public void setImagePath(String imagePath) {
        this.image_path = imagePath;
    }

    public int getCategorie() {
        return categorie;
    }
    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }
    
}
