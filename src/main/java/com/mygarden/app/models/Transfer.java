package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Transfer {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false, foreign = true)
    private User userId;

    @DatabaseField(canBeNull = false)
    private int amount;

    @DatabaseField(canBeNull = true, foreign = true)
    private ShopItem shopItemId;

    @DatabaseField(canBeNull = false, foreign = true)
    private Challenge challengeId;

    public Transfer() {
        /* required by ORMLite */
    }

}
