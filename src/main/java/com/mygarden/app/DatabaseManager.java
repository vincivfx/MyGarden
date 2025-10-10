package com.mygarden.app;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.Garden;
import com.mygarden.app.models.ShopItem;
import com.mygarden.app.models.Transfer;
import com.mygarden.app.models.User;
import com.mygarden.app.models.UserItem;

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

        challengeDao.createOrUpdate(new Challenge(1,  "Bring a reusable water bottle",               "daily", "Cuts down on single-use plastic"));
        challengeDao.createOrUpdate(new Challenge(2,  "Take the stairs instead of the elevator",    "daily", "Saves electricity and promotes health"));
        challengeDao.createOrUpdate(new Challenge(3,  "Turn off lights when leaving a room",        "daily", "Reduces unnecessary energy use"));
        challengeDao.createOrUpdate(new Challenge(4,  "Use public transport or bike instead of car","daily", "Lowers CO2 emissions"));
        challengeDao.createOrUpdate(new Challenge(5,  "Carry a reusable shopping bag",              "daily", "Avoids plastic waste"));
        challengeDao.createOrUpdate(new Challenge(6,  "Unplug chargers when not in use",           "daily", "Prevents phantom energy consumption"));
        challengeDao.createOrUpdate(new Challenge(7,  "Print only if necessary",                    "daily", "Saves paper and ink"));
        challengeDao.createOrUpdate(new Challenge(8,  "Take a shorter shower (max 5 min)",         "daily", "Conserves water and energy"));
        challengeDao.createOrUpdate(new Challenge(9,  "Recycle paper, plastic, or glass",          "daily", "Keeps materials in circulation"));
        challengeDao.createOrUpdate(new Challenge(10, "Eat a vegetarian meal",                     "daily", "Reduces carbon footprint from meat"));
        challengeDao.createOrUpdate(new Challenge(11, "Use a reusable coffee cup",                 "daily", "Cuts disposable cup waste"));
        challengeDao.createOrUpdate(new Challenge(12, "Share leftover food with a friend",         "daily", "Reduces food waste"));
        challengeDao.createOrUpdate(new Challenge(13, "Turn off tap while brushing teeth",         "daily", "Saves liters of water"));
        challengeDao.createOrUpdate(new Challenge(14, "Reuse scrap paper for notes",               "daily", "Extends material life"));
        challengeDao.createOrUpdate(new Challenge(15, "Pick up 3 pieces of litter",                "daily", "Keeps environment clean"));
        challengeDao.createOrUpdate(new Challenge(16, "Open windows instead of using AC",         "daily", "Saves electricity"));
        challengeDao.createOrUpdate(new Challenge(17, "Donate or swap an unused item",            "daily", "Extends product lifecycle"));
        challengeDao.createOrUpdate(new Challenge(18, "Use a cloth napkin instead of paper",      "daily", "Reduces disposable waste"));
        challengeDao.createOrUpdate(new Challenge(19, "Carry your own cutlery set",               "daily", "Avoids single-use plastics"));
        challengeDao.createOrUpdate(new Challenge(20, "Plan your meals for the day",              "daily", "Prevents impulse food waste"));

      
        challengeDao.createOrUpdate(new Challenge(21, "Go a full week without eating red meat",           "weekly", "Significantly lowers carbon footprint"));
        challengeDao.createOrUpdate(new Challenge(22, "Avoid buying any single-use plastic items",         "weekly", "Reduces long-term waste"));
        challengeDao.createOrUpdate(new Challenge(23, "Volunteer for a local clean-up",                    "weekly", "Improves community environment"));
        challengeDao.createOrUpdate(new Challenge(24, "Track your energy consumption for 7 days",          "weekly", "Builds awareness of usage patterns"));
        challengeDao.createOrUpdate(new Challenge(25, "Commit to biking/walking to uni all week",         "weekly", "Cuts transport emissions"));
        challengeDao.createOrUpdate(new Challenge(26, "Repair or upcycle one old item",                    "weekly", "Extends product life, avoids waste"));
        challengeDao.createOrUpdate(new Challenge(27, "Buy only second-hand clothes this week",            "weekly", "Reduces fast fashion impact"));
        challengeDao.createOrUpdate(new Challenge(28, "Cook all meals at home for a week",                 "weekly", "Cuts packaging waste and transport"));
        challengeDao.createOrUpdate(new Challenge(29, "Collect and recycle e-waste properly",              "weekly", "Prevents toxic landfill pollution"));
        challengeDao.createOrUpdate(new Challenge(30, "Keep a sustainability journal",                     "weekly", "Reflects on habits and improvements"));

        

    }

}
