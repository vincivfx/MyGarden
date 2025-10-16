package com.mygarden.app.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "mg_users")
public class User extends TimeStampAbstractModel {

    @DatabaseField(id = true)
    private String username;

    @DatabaseField
    private String name;

    @DatabaseField
    private String password;

    @DatabaseField
    private int coins;

    @DatabaseField
    private Date lastChallengeGenerationDate;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "current_daily_challenge_id")
    private Challenge currentDailyChallenge;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "current_weekly_challenge_id")
    private Challenge currentWeeklyChallenge;

    @ForeignCollectionField
    private ForeignCollection<UserChallenge> completedChallenges;

    @ForeignCollectionField
    private ForeignCollection<Transfer> transfers;

    public User() {
        // needed by ORMLite
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return this.name;
    }

    public int getCoins() {
        coins = 100;

        if (this.transfers == null) {
            return coins;
        }

        for (Transfer transfer : this.transfers) {
            coins += transfer.getAmount();
        }
        return coins;
    }

    public void spendCoins(int price) {
        this.coins -= price;
    }

    public void earnCoins(int coins) {
        this.coins += coins;
    }

    public LocalDate getLastChallengeGenerationDate() {
        if (lastChallengeGenerationDate == null) return null;
        return lastChallengeGenerationDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public void setLastChallengeGenerationDate(LocalDate localDate) {
        if (localDate == null) {
            this.lastChallengeGenerationDate = null;
        } else {
            this.lastChallengeGenerationDate = Date.from(
                    localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
            );
        }
    }

    public void setCurrentDailyChallenge(Challenge challenge) {
        this.currentDailyChallenge = challenge;
    }

    public Challenge getCurrentDailyChallenge() {
        return this.currentDailyChallenge;
    }

    public void setCurrentWeeklyChallenge(Challenge challenge) {
        this.currentWeeklyChallenge = challenge;
    }

    public Challenge getCurrentWeeklyChallenge() {
        return this.currentWeeklyChallenge;
    }

    public Boolean verifyPassword(String password) {
        return true;
    }

    public static User createUser(String username, String password, String name) {
        User user = new User();

        user.username = username;
        user.password = password;
        user.name = name;

        return user;
    }


}