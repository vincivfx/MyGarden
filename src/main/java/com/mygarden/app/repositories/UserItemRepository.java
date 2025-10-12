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
     * Move a userItem inside the garden, changing the position, checking if the position is actually
     * free or not.
     * @param entity the UserItem to move
     * @param position_x new X coordinate of the element
     * @param position_y new Y coordinate of the element
     * @throws SQLException
     * @return TRUE if the move has been completed, FALSE if not, i.e. occupied position
     */
    public boolean move(UserItem entity, int position_x, int position_y) throws SQLException {
        long occupied = userItemsDao.queryBuilder().where()
                .eq(UserItem.USER_ID, entity.getUser().getUsername())
                .and().eq(UserItem.POSITION_X, position_x)
                .and().eq(UserItem.POSITION_Y, position_y)
                .countOf();

        if (occupied == 0) {
            return false;
        }

        entity.move(position_x, position_y);
        this.save(entity);

        return true;
    }
}
