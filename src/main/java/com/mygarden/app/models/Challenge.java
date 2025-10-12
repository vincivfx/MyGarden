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
    private String type; // daily or weekly

    @DatabaseField
    private String tip;

    

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

    

    public Challenge(int challenge_id, String description, String type, String tip) {
        this.challenge_id = challenge_id;
        this.type = type;
        this.description = description;
        this.tip=tip;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

    public int getPoints() {
        return points;
    }
}
