package com.mygarden.app.models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "mg_user_challenges")
public class UserChallenge {

    @DatabaseField(generatedId = true)
    private Integer id;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "user_id")
    private User user;

    @DatabaseField(foreign = true, canBeNull = false, columnName = "challenge_id")
    private Challenge challenge;

    @DatabaseField
    private Date completed_at;

    public UserChallenge() {
        completed_at = new Date();
    }


    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Date getCompletedAt() {
        return completed_at;
    }

    public void setCompletedAt(Date completedAt) {
        this.completed_at = completedAt;
    }

}
