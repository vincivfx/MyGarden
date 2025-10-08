package com.mygarden.app.models;

import java.util.Optional;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Transfer {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "user_id")
    private User user;

    @DatabaseField(canBeNull = false)
    private int amount;

    @DatabaseField(canBeNull = true, foreign = true)
    private ShopItem shopItemId;

    @DatabaseField(canBeNull = true, foreign = true)
    private Challenge challengeId;

    public int getAmount() {return amount;}

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
