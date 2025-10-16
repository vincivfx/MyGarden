package com.mygarden.app.models;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="mg_challenges")
public class Challenge extends TimeStampAbstractModel {
    

    @DatabaseField(generatedId = true)
    private int challenge_id;

    @DatabaseField
    private String type; // daily or weekly

    @DatabaseField
    private int points;

    @DatabaseField
    private Date date;


    public int getId() {
        return challenge_id;
    }

    //REQUIRED by ORMLite
    public Challenge() {
        // no-arg constructor for ORMLite
    }

    

    public Challenge(String type) {
        this.type = type;
        this.points = type.equals("daily") ? 20 : 60;
    }

    public void setChallenge_id(int challenge_id) {
        this.challenge_id = challenge_id;
    }
  
    public void setPoints(int points) {
        this.points = points;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public int getChallengeId() {
        return challenge_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
    return type;
    }

    public int getPoints() {
        return points;
    }
}
