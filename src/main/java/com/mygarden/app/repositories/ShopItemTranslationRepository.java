package com.mygarden.app.repositories;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.ShopItem;
import com.mygarden.app.models.ShopItemTranslation;

import java.sql.SQLException;

public class ShopItemTranslationRepository {

    private final Dao<ShopItemTranslation, Integer> translationDao;

    public ShopItemTranslationRepository() {
        this.translationDao = DatabaseManager.getInstance().getShopItemTranslationDao();
    }

    /**
     * Get the translation for a given ShopItem and language.
     * If no translation is found, returns null.
     */
    public ShopItemTranslation getTranslation(ShopItem item, String lang) {
        try {
            return translationDao.queryBuilder()
                    .where()
                    .eq("shop_item_id", item.getId())
                    .and()
                    .eq("lang", lang)
                    .queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
