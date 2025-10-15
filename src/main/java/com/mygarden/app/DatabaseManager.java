package com.mygarden.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.ChallengeTranslation;
import com.mygarden.app.models.Garden;
import com.mygarden.app.models.ShopItem;
import com.mygarden.app.models.ShopItemTranslation;
import com.mygarden.app.models.Transfer;
import com.mygarden.app.models.User;
import com.mygarden.app.models.UserChallenge;
import com.mygarden.app.models.UserItem;

public final class DatabaseManager {

    private static DatabaseManager INSTANCE;

    private final ConnectionSource connectionSource;
    private final Dao<User, String> userDao;
    private final Dao<Challenge, Integer> challengeDao;
    private final Dao<ChallengeTranslation, Integer> challengeTranslationDao;
    private final Dao<UserChallenge, Integer> userChallengeDao;
    private final Dao<Garden, String> gardenDao;
    private final Dao<UserItem, Integer> userItemsDao;
    private final Dao<ShopItem, String> shopItemDao;
    private final Dao<ShopItemTranslation, Integer> shopItemTranslationDao;
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
        challengeTranslationDao = DaoManager.createDao(connectionSource, ChallengeTranslation.class);
        shopItemDao = DaoManager.createDao(connectionSource, ShopItem.class);
        shopItemTranslationDao = DaoManager.createDao(connectionSource, ShopItemTranslation.class);
        gardenDao = DaoManager.createDao(connectionSource, Garden.class);
        userItemsDao = DaoManager.createDao(connectionSource, UserItem.class);
        userChallengeDao = DaoManager.createDao(connectionSource, UserChallenge.class);
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
        TableUtils.createTableIfNotExists(cs, ChallengeTranslation.class);
        TableUtils.createTableIfNotExists(cs, UserChallenge.class);
        TableUtils.createTableIfNotExists(cs, ShopItem.class);
        TableUtils.createTableIfNotExists(cs, ShopItemTranslation.class);
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

    public Dao<UserChallenge, Integer> getUserChallengeDao() {
        return userChallengeDao;
    }

    public Dao<ChallengeTranslation, Integer> getChallengeTranslationDao() {
        return challengeTranslationDao;
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

    public Dao<ShopItemTranslation, Integer> getShopItemTranslationDao() {
        return shopItemTranslationDao;
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

    public void resetApplication() throws SQLException {
        TableUtils.clearTable(connectionSource, UserItem.class);
        TableUtils.clearTable(connectionSource, UserChallenge.class);
        TableUtils.clearTable(connectionSource, Garden.class);
        TableUtils.clearTable(connectionSource, Transfer.class);
        TableUtils.clearTable(connectionSource, User.class);
    }

    private void loadChallengesFromFile(String resourcePath) throws IOException, SQLException {

        // Get language from file name (ex. challenges_en.txt → "en")
        String filename = resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
        String lang = filename.substring(filename.indexOf("_") + 1, filename.indexOf(".txt"));

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(resourcePath)))) {

            String line;
            int index = 0;
            List<Challenge> challenges = null;

            // If not English, load existing challenges to match them in order
            if (!lang.equals("en")) {
                challenges = challengeDao.queryForAll();
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (lang.equals("en")) {
                    // English: type;description;tip
                    if (parts.length >= 3) {
                        String type = parts[0].trim();
                        String description = parts[1].trim();
                        String tip = parts[2].trim();

                        Challenge ch = new Challenge(type);
                        challengeDao.create(ch);

                        ChallengeTranslation tr = new ChallengeTranslation(ch, lang, description, tip);
                        challengeTranslationDao.create(tr);
                    }
                } else {
                    // Other languages: description;tip (in same order as English challenges)
                    if (parts.length >= 2 && challenges != null && index < challenges.size()) {
                        String description = parts[0].trim();
                        String tip = parts[1].trim();

                        Challenge ch = challenges.get(index);
                        ChallengeTranslation tr = new ChallengeTranslation(ch, lang, description, tip);
                        challengeTranslationDao.createOrUpdate(tr);
                    }
                    index++;
                }
            }
        }
    }

    private void loadShopItemsFromFile(String resourcePath) throws IOException, SQLException {
        // Get language from file name (ex. shopitems_en.txt → "en")
        String filename = resourcePath.substring(resourcePath.lastIndexOf("/") + 1);
        String lang = filename.substring(filename.indexOf("_") + 1, filename.indexOf(".txt"));

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(resourcePath)))) {

            String line;
            int index = 0;
            List<ShopItem> items = null;

            if (!lang.equals("en")) {
                items = shopItemDao.queryForAll();
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (lang.equals("en")) {
                    // English: id;price;categoryId;name
                    if (parts.length >= 4) {
                        String id = parts[0].trim();
                        int price = Integer.parseInt(parts[1].trim());
                        int categoryId = Integer.parseInt(parts[2].trim());
                        String name = parts[3].trim();

                        ShopItem item = ShopItem.create(id, price, categoryId);
                        shopItemDao.create(item);

                        ShopItemTranslation tr = new ShopItemTranslation(item, "en", name);
                        shopItemTranslationDao.create(tr);
                    }
                } else {
                    // Other languages: name (in same order as English shop items)
                    if (items != null && index < items.size()) {
                        String name = line.trim();
                        ShopItem item = items.get(index);
                        ShopItemTranslation tr = new ShopItemTranslation(item, lang, name);
                        shopItemTranslationDao.createOrUpdate(tr);
                    }
                    index++;
                }
            }
        }
    }



    public void spawnDatabase() throws SQLException, IOException {
        System.out.println("Spawning database");
        
        // Load English (creates shopitems and en translations)
        loadShopItemsFromFile("/languages/shopitems_en.txt");

        // Load other languages (only translations, matches existing shopitems)
        loadShopItemsFromFile("/languages/shopitems_sv.txt");
        loadShopItemsFromFile("/languages/shopitems_de.txt");
        loadShopItemsFromFile("/languages/shopitems_es.txt");
        loadShopItemsFromFile("/languages/shopitems_it.txt");
        loadShopItemsFromFile("/languages/shopitems_fr.txt");
        loadShopItemsFromFile("/languages/shopitems_pt.txt");


        // Load English (creates challenges and en translations)
        loadChallengesFromFile("/languages/challenges_en.txt");

        // Load other languages (only translations, matches existing challenges)
        loadChallengesFromFile("/languages/challenges_sv.txt");
        loadChallengesFromFile("/languages/challenges_de.txt");
        loadChallengesFromFile("/languages/challenges_es.txt");
        loadChallengesFromFile("/languages/challenges_it.txt");
        loadChallengesFromFile("/languages/challenges_fr.txt");
        loadChallengesFromFile("/languages/challenges_pt.txt");
        


    }

}
