package com.mygarden.app.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="mg_challenges")
public class Challenge {
    

    @DatabaseField(generatedId = true)
    private int challenge_id;

    @DatabaseField
    private String description;

    @DatabaseField
    private int points;

    @DatabaseField
    private Date date;

    public int getPoints() {
        return points;
    }

    public Challenge() {
        // needed by ORMLite
    }



}
