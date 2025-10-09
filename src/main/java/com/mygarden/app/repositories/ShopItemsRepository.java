package com.mygarden.app.repositories;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.ShopItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShopItemsRepository implements BaseRepository<ShopItem, String>{
    private final Dao<ShopItem, String> shopItemDao = DatabaseManager.getInstance().getShopItemDao();

    @Override
    public List<ShopItem> findAll() throws SQLException {
        return shopItemDao.queryForAll();
    }

    @Override
    public Optional<ShopItem> findById(String id) throws SQLException {
        return Optional.of(shopItemDao.queryForId(id));
    }

    @Override
    public ShopItem save(ShopItem entity) throws SQLException {
        shopItemDao.createOrUpdate(entity);

        Optional<ShopItem> shopItem = findById(entity.getId());

        if (shopItem.isEmpty()) {
            throw new SQLException("Shop item not found");
        }

        return shopItem.get();
    }
}
