package com.mygarden.app.repositories;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.ChallengeTranslation;

public class ChallengeTranslationRepository {

    private final Dao<ChallengeTranslation, Integer> dao;

    public ChallengeTranslationRepository() throws SQLException {
        this.dao = DatabaseManager.getInstance().getChallengeTranslationDao();
    }

    /**
     * Get the translation for a given Challenge and language.
     * If no translation is found, returns null.
     */
    public ChallengeTranslation getTranslation(Challenge challenge, String lang) throws SQLException {
        return dao.queryBuilder()
                  .where()
                  .eq("challenge_id", challenge.getId())
                  .and()
                  .eq("lang", lang)
                  .queryForFirst();
    }
}
