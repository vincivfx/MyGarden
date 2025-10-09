package com.mygarden.app;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mygarden.app.models.*;

public final class DatabaseManager {

    private static DatabaseManager INSTANCE;

    private final ConnectionSource connectionSource;
    private final Dao<User, String> userDao;
    private final Dao<Challenge, Integer> challengeDao;
    private final Dao<Garden, String> gardenDao;
    private final Dao<UserItem, Integer> userItemsDao;
    private final Dao<ShopItem, String> shopItemDao;
    private final Dao<Transfer, Integer> transferDao;

    /**
     * @param cs
     * @throws SQLException
     */
    public DatabaseManager(ConnectionSource cs) throws SQLException {
        this.connectionSource = cs;

        /* define DAOs */
        userDao = DaoManager.createDao(connectionSource, User.class);
        challengeDao = DaoManager.createDao(connectionSource, Challenge.class);
        shopItemDao = DaoManager.createDao(connectionSource, ShopItem.class);
        gardenDao = DaoManager.createDao(connectionSource, Garden.class);
        userItemsDao = DaoManager.createDao(connectionSource, UserItem.class);
        transferDao = DaoManager.createDao(connectionSource, Transfer.class);
    }

    /**
     * @param jdbcUrl
     * @throws SQLException
     */
    public static synchronized void init(String jdbcUrl) throws SQLException {
        JdbcPooledConnectionSource cs = new JdbcPooledConnectionSource(jdbcUrl);

        TableUtils.createTableIfNotExists(cs, User.class);
        TableUtils.createTableIfNotExists(cs, Challenge.class);
        TableUtils.createTableIfNotExists(cs, ShopItem.class);
        TableUtils.createTableIfNotExists(cs, Garden.class);
        TableUtils.createTableIfNotExists(cs, UserItem.class);
        TableUtils.createTableIfNotExists(cs, Transfer.class);

        INSTANCE = new DatabaseManager(cs);
    }

    public static synchronized DatabaseManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Database manager not initialized");
        }
        return INSTANCE;
    }

    public Dao<User, String> getUserDao() {
        return userDao;
    }

    public Dao<Challenge, Integer> getChallengeDao() {
        return challengeDao;
    }

    public Dao<Garden, String> getGardenDao() {
        return gardenDao;
    }

    public Dao<UserItem, Integer> getUserItemDao() {
        return userItemsDao;
    }

    public Dao<ShopItem, String> getShopItemDao() {
        return shopItemDao;
    }

    public Dao<Transfer, Integer> getTransferDao() {
        return transferDao;
    }

    public static synchronized void shutdown() {
        if (INSTANCE != null && INSTANCE.connectionSource != null) {
            try {
                INSTANCE.connectionSource.close();
            } catch (Exception ignored) {
            }
            INSTANCE = null;
        }
    }

    public static void connect() throws SQLException {
        DatabaseManager.init("jdbc:sqlite:persistence.db");
    }

    public static void stop() {
        DatabaseManager.shutdown();
    }

    public void spawnDatabase() throws SQLException {
        System.out.println("Spawning database");
        shopItemDao.createOrUpdate(ShopItem.create("rose", "Rose", 25, 0));
        shopItemDao.createOrUpdate(ShopItem.create("tulip", "Tulip", 15, 0));
        shopItemDao.createOrUpdate(ShopItem.create("sunflower", "Sunflower", 20, 0));
        shopItemDao.createOrUpdate(ShopItem.create("daisy", "Daisy", 40, 0));
        shopItemDao.createOrUpdate(ShopItem.create("boxwood", "Boxwood", 35, 1));
        shopItemDao.createOrUpdate(ShopItem.create("hydrangea", "Hydrangea", 30, 1));
        shopItemDao.createOrUpdate(ShopItem.create("lavender", "Lavender", 18, 1));
        shopItemDao.createOrUpdate(ShopItem.create("rhododendron", "Rhododendron", 45, 1));
        shopItemDao.createOrUpdate(ShopItem.create("maple", "Maple", 120, 2));
        shopItemDao.createOrUpdate(ShopItem.create("oak", "Oak", 200, 2));
        shopItemDao.createOrUpdate(ShopItem.create("pine", "Pine", 80, 2));
        shopItemDao.createOrUpdate(ShopItem.create("willow", "Willow", 150, 2));
    }

}
