package com.mygarden.app.repositories;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.Transfer;
import com.mygarden.app.models.UserItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserItemRepository implements BaseRepository<UserItem, Integer> {
    public final Dao<UserItem, Integer> userItemsDao;

    public UserItemRepository() throws SQLException {
        this.userItemsDao = DatabaseManager.getInstance().getUserItemDao();
    }

    @Override
    public List<UserItem> findAll() throws SQLException {
        return userItemsDao.queryForAll();
    }

    @Override
    public Optional<UserItem> findById(Integer id) throws SQLException {
        return Optional.ofNullable(userItemsDao.queryForId(id));
    }

    @Override
    public UserItem save(UserItem entity) throws SQLException {
        userItemsDao.createOrUpdate(entity);

        Optional<UserItem> currentUserItem = findById(entity.getId());

        if (currentUserItem.isEmpty()) {throw new SQLException("Transfer not found");}

        return currentUserItem.get();
    }


    /**
     * Move a userItem inside the garden, changing the position
     * @param entity
     * @param position_x
     * @param position_y
     * @throws SQLException
     */
    public boolean move(UserItem entity, int position_x, int position_y) throws SQLException {
        return true;

    }
}
