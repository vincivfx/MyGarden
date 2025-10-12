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
import com.mygarden.app.models.UserChallenge;
import com.mygarden.app.models.UserItem;

public final class DatabaseManager {

    private static DatabaseManager INSTANCE;

    private final ConnectionSource connectionSource;
    private final Dao<User, String> userDao;
    private final Dao<Challenge, Integer> challengeDao;
    private final Dao<UserChallenge, Integer> userChallengeDao;
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
        TableUtils.createTableIfNotExists(cs, UserChallenge.class);
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

    public Dao<UserChallenge, Integer> getUserChallengeDao() {
        return userChallengeDao;
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
        shopItemDao.createOrUpdate(ShopItem.create("maple", "Maple", 200, 2));
        shopItemDao.createOrUpdate(ShopItem.create("oak", "Oak", 120, 2));
        shopItemDao.createOrUpdate(ShopItem.create("pine", "Pine", 80, 2));
        shopItemDao.createOrUpdate(ShopItem.create("willow", "Willow", 150, 2));

        challengeDao.createOrUpdate(new Challenge(1,  "Bring a reusable water bottle.", "daily", "Cuts down on single-use plastic"));
        challengeDao.createOrUpdate(new Challenge(2,  "Take the stairs instead of the elevator.", "daily", "Saves electricity and promotes health"));
        challengeDao.createOrUpdate(new Challenge(3,  "Turn off lights when leaving a room.", "daily", "Reduces unnecessary energy use"));
        challengeDao.createOrUpdate(new Challenge(4,  "Use public transport or bike instead of car.", "daily", "Lowers CO2 emissions"));
        challengeDao.createOrUpdate(new Challenge(5,  "Carry a reusable shopping bag.", "daily", "Avoids plastic waste"));
        challengeDao.createOrUpdate(new Challenge(6,  "Unplug chargers when not in use.", "daily", "Prevents phantom energy consumption"));
        challengeDao.createOrUpdate(new Challenge(7,  "Print only if necessary.", "daily", "Saves paper and ink"));
        challengeDao.createOrUpdate(new Challenge(8,  "Take a shorter shower (max 5 min).", "daily", "Conserves water and energy"));
        challengeDao.createOrUpdate(new Challenge(9,  "Recycle paper, plastic, or glass.", "daily", "Keeps materials in circulation"));
        challengeDao.createOrUpdate(new Challenge(10, "Eat a vegetarian meal.", "daily", "Reduces carbon footprint from meat"));
        challengeDao.createOrUpdate(new Challenge(11, "Use a reusable coffee cup.", "daily", "Cuts disposable cup waste"));
        challengeDao.createOrUpdate(new Challenge(12, "Share leftover food with a friend.", "daily", "Reduces food waste"));
        challengeDao.createOrUpdate(new Challenge(13, "Turn off tap while brushing teeth.", "daily", "Saves liters of water"));
        challengeDao.createOrUpdate(new Challenge(14, "Reuse scrap paper for notes.", "daily", "Extends material life"));
        challengeDao.createOrUpdate(new Challenge(15, "Pick up 3 pieces of litter.", "daily", "Keeps environment clean"));
        challengeDao.createOrUpdate(new Challenge(16, "Open windows instead of using AC.", "daily", "Saves electricity"));
        challengeDao.createOrUpdate(new Challenge(17, "Donate or swap an unused item.", "daily", "Extends product lifecycle"));
        challengeDao.createOrUpdate(new Challenge(18, "Use a cloth napkin instead of paper.", "daily", "Reduces disposable waste"));
        challengeDao.createOrUpdate(new Challenge(19, "Carry your own cutlery set.", "daily", "Avoids single-use plastics"));
        challengeDao.createOrUpdate(new Challenge(20, "Plan your meals for the day.", "daily", "Prevents impulse food waste"));
        challengeDao.createOrUpdate(new Challenge(21, "Refill a reusable bottle instead of buying bottled water.", "daily", "Avoids plastic waste and saves money"));
        challengeDao.createOrUpdate(new Challenge(22, "Switch off your computer completely after use.", "daily", "Reduces standby power consumption"));
        challengeDao.createOrUpdate(new Challenge(23, "Bring lunch in a reusable container.", "daily", "Cuts down on packaging waste"));
        challengeDao.createOrUpdate(new Challenge(24, "Use both sides of a sheet of paper.", "daily", "Maximizes paper use"));
        challengeDao.createOrUpdate(new Challenge(25, "Pick up 5 pieces of litter outdoors.", "daily", "Keeps shared spaces clean"));
        challengeDao.createOrUpdate(new Challenge(26, "Turn down heating by 1°C.", "daily", "Saves energy and reduces emissions"));
        challengeDao.createOrUpdate(new Challenge(27, "Switch to a digital receipt instead of paper.", "daily", "Reduces paper waste"));
        challengeDao.createOrUpdate(new Challenge(28, "Reuse a glass jar for storage.", "daily", "Gives packaging a second life"));
        challengeDao.createOrUpdate(new Challenge(29, "Decline a plastic straw.", "daily", "Avoids unnecessary single-use plastic"));
        challengeDao.createOrUpdate(new Challenge(30, "Take a walk instead of a short car ride.", "daily", "Reduces emissions and promotes health"));
        challengeDao.createOrUpdate(new Challenge(31, "Switch off your Wi-Fi router overnight.", "daily", "Saves electricity"));
        challengeDao.createOrUpdate(new Challenge(32, "Compost fruit or vegetable scraps.", "daily", "Turns waste into useful soil"));
        challengeDao.createOrUpdate(new Challenge(33, "Carry a reusable face mask or cloth instead of disposable.", "daily", "Reduces single-use waste"));
        challengeDao.createOrUpdate(new Challenge(34, "Use a bar of soap instead of liquid soap in plastic bottles.", "daily", "Cuts packaging waste"));
        challengeDao.createOrUpdate(new Challenge(35, "Spend one hour without using your phone.", "daily", "Saves device energy and supports wellbeing"));
        challengeDao.createOrUpdate(new Challenge(36, "Bring your own container for takeaway food.", "daily", "Avoids disposable packaging"));
        challengeDao.createOrUpdate(new Challenge(37, "Air-dry clothes instead of using a dryer.", "daily", "Saves electricity"));
        challengeDao.createOrUpdate(new Challenge(38, "Use natural light instead of lamps during the day.", "daily", "Reduces electricity use"));
        challengeDao.createOrUpdate(new Challenge(39, "Sort your home recycling more carefully today.", "daily", "Improves recycling quality"));
        challengeDao.createOrUpdate(new Challenge(40, "Turn off your car engine while waiting.", "daily", "Reduces fuel waste and emissions"));
        challengeDao.createOrUpdate(new Challenge(41, "Bring your own reusable straw.", "daily", "Avoids single-use plastic"));
        challengeDao.createOrUpdate(new Challenge(42, "Choose tap water over bottled water.", "daily", "Reduces plastic and transport emissions"));
        challengeDao.createOrUpdate(new Challenge(43, "Switch to a reusable cloth bag for produce.", "daily", "Avoids plastic produce bags"));
        challengeDao.createOrUpdate(new Challenge(44, "Use a refillable pen instead of disposable ones.", "daily", "Reduces plastic waste"));
        challengeDao.createOrUpdate(new Challenge(45, "Switch your phone to battery saver mode for the day.", "daily", "Reduces energy consumption"));
        challengeDao.createOrUpdate(new Challenge(46, "Pick up cigarette butts from the ground.", "daily", "Prevents toxic litter"));
        challengeDao.createOrUpdate(new Challenge(47, "Donate leftover food to a food-sharing box.", "daily", "Helps reduce waste and supports others"));
        challengeDao.createOrUpdate(new Challenge(48, "Switch to cloth towels instead of paper towels.", "daily", "Reduces disposable waste"));
        challengeDao.createOrUpdate(new Challenge(49, "Use public transport for one trip today.", "daily", "Cuts emissions compared to driving"));
        challengeDao.createOrUpdate(new Challenge(50, "Use a power strip to easily turn off multiple devices.", "daily", "Eliminates standby power use"));
        challengeDao.createOrUpdate(new Challenge(51, "Bring your own reusable chopsticks or cutlery.", "daily", "Avoids disposable plastic utensils"));
        challengeDao.createOrUpdate(new Challenge(52, "Switch off lights during daylight hours.", "daily", "Saves electricity by using natural light"));
        challengeDao.createOrUpdate(new Challenge(53, "Write down one eco-habit you want to keep long-term.", "daily", "Encourages reflection and habit-building"));
        challengeDao.createOrUpdate(new Challenge(54, "Reuse a cardboard box for storage or shipping.", "daily", "Extends packaging life"));
        challengeDao.createOrUpdate(new Challenge(55, "Set your computer to sleep after 5 minutes of inactivity.", "daily", "Saves electricity automatically"));
        challengeDao.createOrUpdate(new Challenge(56, "Choose a reusable razor instead of disposable.", "daily", "Cuts down on plastic waste"));
        challengeDao.createOrUpdate(new Challenge(57, "Decline paper receipts when possible.", "daily", "Reduces paper waste"));
        challengeDao.createOrUpdate(new Challenge(58, "Borrow a book from a library instead of buying new.", "daily", "Reduces resource use"));
        challengeDao.createOrUpdate(new Challenge(59, "Switch to a bamboo toothbrush.", "daily", "Reduces plastic waste in landfills"));
        challengeDao.createOrUpdate(new Challenge(60, "Try a zero-waste snack (like fruit without packaging).", "daily", "Avoids unnecessary packaging"));
        challengeDao.createOrUpdate(new Challenge(61, "Reuse a glass bottle as a vase or container.", "daily", "Gives packaging a second life"));
        challengeDao.createOrUpdate(new Challenge(62, "Lower your screen brightness to save energy.", "daily", "Reduces electricity use"));
        challengeDao.createOrUpdate(new Challenge(63, "Take a cold shower for 2 minutes.", "daily", "Saves hot water and energy"));
        challengeDao.createOrUpdate(new Challenge(64, "Bring your own reusable food wrap (like beeswax).", "daily", "Avoids plastic cling film"));
        challengeDao.createOrUpdate(new Challenge(65, "Cook a meal using only local ingredients.", "daily", "Supports local farmers and reduces transport emissions"));
        challengeDao.createOrUpdate(new Challenge(66, "Donate old clothes instead of throwing them away.", "daily", "Extends product life"));
        challengeDao.createOrUpdate(new Challenge(67, "Switch to digital tickets instead of printed ones.", "daily", "Reduces paper waste"));
        challengeDao.createOrUpdate(new Challenge(68, "Spend 10 minutes learning about local recycling rules.", "daily", "Improves recycling accuracy"));
        challengeDao.createOrUpdate(new Challenge(69, "Turn off heating when leaving home.", "daily", "Saves energy and money"));
        challengeDao.createOrUpdate(new Challenge(70, "Try a zero-waste snack (like fruit without packaging).", "daily", "Avoids unnecessary packaging"));
        challengeDao.createOrUpdate(new Challenge(71, "Use a reusable lunchbox instead of plastic wrap.", "daily", "Avoids disposable packaging"));
        challengeDao.createOrUpdate(new Challenge(72, "Use a reusable water bottle cap instead of buying new ones.", "daily", "Extends product life"));
        challengeDao.createOrUpdate(new Challenge(73, "Delete 10 old emails to reduce digital storage footprint.", "daily", "Reduces server energy use"));
        challengeDao.createOrUpdate(new Challenge(74, "Spend 5 minutes caring for plants or a green space.", "daily", "Supports biodiversity"));
        challengeDao.createOrUpdate(new Challenge(75, "Plant a seed or care for a houseplant today.", "daily", "Encourages biodiversity and green spaces"));
        challengeDao.createOrUpdate(new Challenge(76, "Collect rainwater in a small container for plants.", "daily", "Introduces water harvesting"));
        challengeDao.createOrUpdate(new Challenge(77, "Reuse packaging material for shipping.", "daily", "Extends packaging life"));
        challengeDao.createOrUpdate(new Challenge(78, "Switch your default search engine to an eco-friendly one (like Ecosia).", "daily", "Supports reforestation through digital habits"));
        challengeDao.createOrUpdate(new Challenge(79, "Walk or bike for a short errand instead of driving.", "daily", "Reduces emissions and promotes health"));
        challengeDao.createOrUpdate(new Challenge(80, "Try a meat-free snack today.", "daily", "Reduces carbon footprint from food"));
        challengeDao.createOrUpdate(new Challenge(81, "Choose a vegetarian breakfast.", "daily", "Lowers carbon footprint from food"));
        challengeDao.createOrUpdate(new Challenge(82, "Pick up 5 cigarette butts and dispose properly.", "daily", "Prevents toxic litter"));
        challengeDao.createOrUpdate(new Challenge(83, "Reuse a cardboard package for storage.", "daily", "Extends packaging life"));
        challengeDao.createOrUpdate(new Challenge(84, "Switch off unused lights for the entire day.", "daily", "Reduces energy consumption"));
        challengeDao.createOrUpdate(new Challenge(85, "Use a reusable coffee filter instead of disposable.", "daily", "Cuts paper waste"));
        challengeDao.createOrUpdate(new Challenge(86, "Collect used batteries and store them for proper recycling.", "daily", "Prevents toxic waste"));
        challengeDao.createOrUpdate(new Challenge(87, "Decline disposable cutlery with takeaway food.", "daily", "Avoids plastic waste"));
        challengeDao.createOrUpdate(new Challenge(88, "Buy a product with eco-label certification (Fairtrade, organic, etc.).", "daily", "Supports sustainable production"));
        challengeDao.createOrUpdate(new Challenge(89, "Take a 3-minute shorter shower.", "daily", "Saves water and energy"));
        challengeDao.createOrUpdate(new Challenge(90, "Reuse a plastic container instead of throwing it away.", "daily", "Extends product life"));
        challengeDao.createOrUpdate(new Challenge(91, "Turn off heating when leaving your room.", "daily", "Saves energy"));
        challengeDao.createOrUpdate(new Challenge(92, "Share a sustainability tip with a friend.", "daily", "Spreads awareness socially"));
        challengeDao.createOrUpdate(new Challenge(93, "Switch to digital notes instead of paper.", "daily", "Reduces paper waste"));
        challengeDao.createOrUpdate(new Challenge(94, "Buy a product with minimal packaging.", "daily", "Reduces packaging waste"));
        challengeDao.createOrUpdate(new Challenge(95, "Switch off background apps on your phone.", "daily", "Saves battery and energy"));
        challengeDao.createOrUpdate(new Challenge(96, "Repair a small household item instead of discarding it.", "daily", "Extends product life"));
        challengeDao.createOrUpdate(new Challenge(97, "Skip sugary bottled drinks and choose homemade instead.", "daily", "Reduces plastic and sugar consumption"));
        challengeDao.createOrUpdate(new Challenge(98, "Use a reusable shopping list app instead of paper notes.", "daily", "Reduces paper waste"));
        challengeDao.createOrUpdate(new Challenge(99, "Spend 15 minutes outdoors without electronics.", "daily", "Supports wellbeing and saves energy"));
        challengeDao.createOrUpdate(new Challenge(100, "Spend 10 minutes cleaning your workspace sustainably.", "daily", "Encourages mindful organization"));
        
        challengeDao.createOrUpdate(new Challenge(101, "Go a full week without eating red meat.", "weekly", "Significantly lowers carbon footprint"));
        challengeDao.createOrUpdate(new Challenge(102, "Avoid buying any single-use plastic items.", "weekly", "Reduces long-term waste"));
        challengeDao.createOrUpdate(new Challenge(103, "Volunteer for a local clean-up.", "weekly", "Improves community environment"));
        challengeDao.createOrUpdate(new Challenge(104, "Track your energy consumption for 7 days.", "weekly", "Builds awareness of usage patterns"));
        challengeDao.createOrUpdate(new Challenge(105, "Commit to biking/walking to uni all week.", "weekly", "Cuts transport emissions"));
        challengeDao.createOrUpdate(new Challenge(106, "Repair or upcycle one old item.", "weekly", "Extends product life, avoids waste"));
        challengeDao.createOrUpdate(new Challenge(107, "Buy only second-hand clothes this week.", "weekly", "Reduces fast fashion impact"));
        challengeDao.createOrUpdate(new Challenge(108, "Cook all meals at home for a week.", "weekly", "Cuts packaging waste and transport"));
        challengeDao.createOrUpdate(new Challenge(109, "Collect and recycle e-waste properly.", "weekly", "Prevents toxic landfill pollution"));
        challengeDao.createOrUpdate(new Challenge(110, "Keep a sustainability journal.", "weekly", "Reflects on habits and improvements"));
        challengeDao.createOrUpdate(new Challenge(111, "Go a full week without using disposable coffee cups.", "weekly", "Avoids significant single-use waste"));
        challengeDao.createOrUpdate(new Challenge(112, "Cook only plant-based dinners for 7 days.", "weekly", "Reduces carbon footprint from food"));
        challengeDao.createOrUpdate(new Challenge(113, "Avoid using your car for the entire week.", "weekly", "Cuts transport emissions drastically"));
        challengeDao.createOrUpdate(new Challenge(114, "Collect rainwater and use it for plants.", "weekly", "Saves tap water resources"));
        challengeDao.createOrUpdate(new Challenge(115, "Track and reduce your household waste for 7 days.", "weekly", "Builds awareness and reduces trash"));
        challengeDao.createOrUpdate(new Challenge(116, "Spend 30 minutes outdoors cleaning a park or street.", "weekly", "Improves local environment"));
        challengeDao.createOrUpdate(new Challenge(117, "Buy only unpackaged or bulk food this week.", "weekly", "Reduces packaging waste"));
        challengeDao.createOrUpdate(new Challenge(118, "Repair two items instead of replacing them.", "weekly", "Extends product life and saves resources"));
        challengeDao.createOrUpdate(new Challenge(119, "Avoid fast fashion purchases for a week.", "weekly", "Reduces textile waste and emissions"));
        challengeDao.createOrUpdate(new Challenge(120, "Track your water usage daily for 7 days.", "weekly", "Raises awareness of consumption"));
        challengeDao.createOrUpdate(new Challenge(121, "Volunteer at a community garden or eco-project.", "weekly", "Supports sustainable initiatives"));
        challengeDao.createOrUpdate(new Challenge(122, "Use only reusable containers for all meals this week.", "weekly", "Eliminates disposable packaging"));
        challengeDao.createOrUpdate(new Challenge(123, "Commit to zero food waste for 7 days.", "weekly", "Encourages mindful cooking and storage"));
        challengeDao.createOrUpdate(new Challenge(124, "Avoid using any single-use plastic bags all week.", "weekly", "Reduces plastic pollution"));
        challengeDao.createOrUpdate(new Challenge(125, "Document your sustainable actions daily for a week.", "weekly", "Helps build long-term eco habits"));

    }

}
