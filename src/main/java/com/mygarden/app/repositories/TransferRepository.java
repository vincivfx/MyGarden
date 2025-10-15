package com.mygarden.app.repositories;

import java.sql.SQLException;
import java.util.*;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.ShopItem;
import com.mygarden.app.models.Transfer;
import com.mygarden.app.models.User;
import com.mygarden.app.models.UserItem;
import javafx.util.Pair;

public class TransferRepository implements BaseRepository<Transfer, Integer> {
    public final Dao<Transfer, Integer> transferDao;

    public TransferRepository() {
        this.transferDao = DatabaseManager.getInstance().getTransferDao();
    }

    @Override
    public List<Transfer> findAll() throws SQLException {
        return transferDao.queryForAll();
    }

    @Override
    public Optional<Transfer> findById(Integer id) throws SQLException {
        return Optional.ofNullable(transferDao.queryForId(id));
    }

    @Override
    public Transfer save(Transfer entity) throws SQLException {
        transferDao.create(entity);
        return entity;
    }

    /**
     * Register the bought of an item by the user, adding the transfer to the transfer list
     *
     * @param shopItem the item the user bought
     * @param user     the user who's buying the item
     * @return empty optional if no enough coins or in case of exception, otherwise returns the
     * transfer element if it worked.
     */
    public Optional<Transfer> buy(User user, ShopItem shopItem) throws SQLException {
        if (user.getCoins() < shopItem.getPrice()) {
            return Optional.empty();
        }

        Transfer transfer = Transfer.createTransferBought(user, shopItem);
        UserItem boughtUserItem = new UserItem();

        // initialize user item fields and persist
        boughtUserItem.setUser(user);
        boughtUserItem.setShopItem(shopItem);

        UserItemRepository userItemRepository = new UserItemRepository();
    UserItem saved = userItemRepository.save(boughtUserItem);
    System.out.println("TransferRepository.buy: created UserItem id=" + saved.getId() + " for user=" + user.getUsername() + " shopItem=" + shopItem.getId());

        this.save(transfer);
        return Optional.of(transfer);
    }

    /**
     * Register the completion of a challenge by the user
     *
     * @param challenge the challenge the user completed
     * @param user      the user who completed the challenge
     * @return the transfer who's registering the challenge
     */
    public Transfer registerChallenge(User user, Challenge challenge) throws SQLException {
        Transfer transfer = Transfer.createTransferChallenge(user, challenge);
        this.save(transfer);

        Optional<Transfer> currentTransfer = findById(transfer.getId());

        if (currentTransfer.isEmpty()) {throw new SQLException("Transfer not found");}

        return currentTransfer.get();
    }

    public List<Pair<ShopItem, Integer>> getPlantStatistics(User user) throws SQLException {
        HashMap<String, Pair<ShopItem, Integer>> plants = new HashMap<>();
        ShopItemsRepository shopItemsRepository = new ShopItemsRepository();

        shopItemsRepository.findAll().forEach(shopItem -> {
            plants.put(shopItem.getId(), new Pair<>(shopItem, 0));
        });

        List<Transfer> transfers = transferDao.queryBuilder()
                .where().eq(Transfer.USER_ID, user)
                .and().isNotNull(Transfer.SHOP_ITEM_ID)
                .query();

        transfers.forEach(transfer -> {
            plants.computeIfPresent(transfer.getShopItemId().getId(), (k, t) -> new Pair<>(t.getKey(), t.getValue() + 1));
        });

        List<Pair<ShopItem, Integer>> results = new ArrayList<>();

        plants.forEach((k, v) -> {
            results.add(v);
        });

        return results;
    }
}
