package com.mygarden.app.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="mg_challenges_translations")
public class ChallengeTranslation extends TimeStampAbstractModel {
    

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true, columnName = "challenge_id", canBeNull = false)
    private Challenge challenge;

    @DatabaseField(canBeNull = false)
    private String lang; // "en", "sv"

    @DatabaseField(canBeNull = false)
    private String description;

    @DatabaseField(canBeNull = false)
    private String tip;

    public ChallengeTranslation() {
        // ORMLite needs a no-arg constructor
    }

    public ChallengeTranslation(Challenge challenge, String lang, String description, String tip) {
        this.challenge = challenge;
        this.lang = lang;
        this.description = description;
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public String getLang() {
        return lang;
    }

    public String getDescription() {
        return description;
    }

    public String getTip() {
        return tip;
    }

    

}
