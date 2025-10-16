package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Optional;

@DatabaseTable(tableName = "mg_transfers")
public class Transfer extends TimeStampAbstractModel {

    public static final String SHOP_ITEM_ID = "shop_item_id";
    public static final String CHALLENGE_ID = "challenge_id";
    public static final String USER_ID = "user_id";


    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true, foreign = true, foreignAutoRefresh = true, columnName = USER_ID)
    private User user;

    @DatabaseField(canBeNull = false)
    private int amount;

    @DatabaseField(canBeNull = true, foreign = true, columnName = SHOP_ITEM_ID)
    private ShopItem shopItemId;

    @DatabaseField(canBeNull = true, foreign = true, columnName = CHALLENGE_ID)
    private Challenge challengeId;

    public int getAmount() {return amount;}


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ShopItem getShopItemId() {
        return this.shopItemId;
    }

    public void setShopItemId(ShopItem shopItemId) {
        this.shopItemId = shopItemId;
    }

    public Challenge getChallengeId() {
        return this.challengeId;
    }

    public void setChallengeId(Challenge challengeId) {
        this.challengeId = challengeId;
    }

    public Transfer() {
        /* required by ORMLite */
    }

    public Transfer(User user, int amount, Optional<ShopItem> shopItemId, Optional<Challenge> challenge) {
        this.user = user;
        this.amount = amount;
        this.shopItemId = shopItemId.orElse(null);
        this.challengeId = challenge.orElse(null);
    }

    public static Transfer createTransferChallenge(User user, Challenge challenge) {
        return new Transfer(user, challenge.getPoints(), Optional.empty(), Optional.of(challenge));
    }

    public static Transfer createTransferBought(User user, ShopItem shopItem) {
        return new Transfer(user, -shopItem.getPrice(), Optional.of(shopItem), Optional.empty());
    }

}
