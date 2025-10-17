package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="mg_gardens")
public class Garden {
    
    @DatabaseField
    private int garden_id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User user;

    public Garden() {}

}
