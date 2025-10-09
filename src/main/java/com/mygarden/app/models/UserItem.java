package com.mygarden.app.models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "mg_user_plants")
public class UserItem {

    public final static String ID = "id";
    public final static String USER_ID = "user_id";
    public final static String SHOP_ITEM_ID = "plant_id";
    public final static String POSITION_X = "position_x";
    public final static String POSITION_Y = "position_y";
    public final static String CREATED_AT = "created_at";

    @DatabaseField(generatedId = true, columnName = ID)
    private Integer id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = SHOP_ITEM_ID)
    private ShopItem shopItem;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = USER_ID)
    private User user;

    @DatabaseField(columnName = POSITION_X)
    private Integer position_x;

    @DatabaseField(columnName = POSITION_Y)
    private Integer position_y;

    @DatabaseField(columnName = CREATED_AT)
    private Date created_at;

    public UserItem() {
        created_at = new Date();
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void move(int position_x, int position_y) {
        this.position_x = position_x;
        this.position_y = position_y;
    }

}
