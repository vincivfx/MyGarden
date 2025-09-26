package com.mygarden.app.models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "mg_user_plants")
public class UserItem {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private ShopItem shopItem;

    @DatabaseField
    private Integer position_x;

    @DatabaseField
    private Integer position_y;

    @DatabaseField
    private Date created_at;

    public UserItem() {
        created_at = new Date();
    }

}
