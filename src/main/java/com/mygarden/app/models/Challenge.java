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
    private String tip;

    

    @DatabaseField
    private int points;

    @DatabaseField
    private Date date;
    
    @DatabaseField
    private String type;// daily or weekly

    public int getPoints() {
        return points;
    }

    public Challenge() {
        // empty constructor required by ORMLite
    }


    public Challenge(int challenge_id, String description, String type, String tip) {
        this.challenge_id = challenge_id;
        this.description = description;
        this.type = type;
        this.tip=tip;
    }

    public void setChallenge_id(int challenge_id) {
        this.challenge_id = challenge_id;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTip() {
        return this.tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
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
    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
    return type;
    }

    public String getDescription() {
        return description;
    }

    public int getChallengeId() {
    return challenge_id;
    }


}
