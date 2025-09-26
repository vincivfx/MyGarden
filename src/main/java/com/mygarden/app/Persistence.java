package com.mygarden.app;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mygarden.app.models.*;

public class Persistence {

    public Dao<User, String> userDao;
    public Dao<Challenge, String> challengeDao;
    public Dao<Garden, String> gardenDao;
    public Dao<UserItem, String> userPlantDao;
    public Dao<ShopItem, String> shopItemDao;


    public Persistence() throws SQLException {

        String connectionString = "jdbc:sqlite:persistence.db";

        /* connect to SQLite database */
        ConnectionSource connectionSource = new JdbcConnectionSource(connectionString);

        /* define DAOs */
        userDao = DaoManager.createDao(connectionSource, User.class);
        challengeDao = DaoManager.createDao(connectionSource, Challenge.class);
        shopItemDao = DaoManager.createDao(connectionSource, ShopItem.class);
        gardenDao = DaoManager.createDao(connectionSource, Garden.class);
        userPlantDao = DaoManager.createDao(connectionSource, UserItem.class);


        /* create tables if they don't exist */
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, Challenge.class);
        TableUtils.createTableIfNotExists(connectionSource, ShopItem.class);
        TableUtils.createTableIfNotExists(connectionSource, Garden.class);
        TableUtils.createTableIfNotExists(connectionSource, UserItem.class);

    }


}
