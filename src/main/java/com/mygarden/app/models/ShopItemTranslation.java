package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mg_shop_item_translations")
public class ShopItemTranslation {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, columnName = "shop_item_id", canBeNull = false)
    private ShopItem shopItem;

    @DatabaseField(canBeNull = false)
    private String lang; // ex. "en", "sv"

    @DatabaseField(canBeNull = false)
    private String name;

    public ShopItemTranslation() {}

    public ShopItemTranslation(ShopItem shopItem, String lang, String name) {
        this.shopItem = shopItem;
        this.lang = lang;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ShopItem getShopItem() {
        return shopItem;
    }

    public String getLang() {
        return lang;
    }

    public String getName() {
        return name;
    }
}
