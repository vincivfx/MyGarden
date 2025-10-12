package com.mygarden.app.repositories;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.User;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.UserChallenge;

public class UserChallengeRepository {

    private final Dao<UserChallenge, Integer> userChallengeDao = DatabaseManager.getInstance().getUserChallengeDao();

    /**
     * Save or update a UserChallenge entity
     */
    public UserChallenge save(UserChallenge entity) throws SQLException {
        userChallengeDao.createOrUpdate(entity);
        return entity;
    }

    /**
     * Mark a challenge as completed for a user
     */
    public void markAsCompleted(User user, Challenge challenge) throws SQLException {
        // Controlla se esiste già una riga
        UserChallenge existing = userChallengeDao.queryBuilder()
                .where()
                .eq("user_id", user.getUsername())
                .and()
                .eq("challenge_id", challenge.getChallengeId())
                .queryForFirst();

        if (existing == null) {
            // Creo nuova entry
            UserChallenge uc = new UserChallenge();
            uc.setUser(user);
            uc.setChallenge(challenge);
            userChallengeDao.create(uc);
        }
    }

    public boolean isChallengeCompletedByUser(User user, Challenge challenge) throws SQLException {
        UserChallenge existing = userChallengeDao.queryBuilder()
                .where()
                .eq("user_id", user.getUsername())
                .and()
                .eq("challenge_id", challenge.getChallengeId())
                .queryForFirst();

        return existing != null;
    }

    /**
     * Find all challenges completed by a user
     */
    public List<UserChallenge> findCompletedByUser(User user) throws SQLException {
        return userChallengeDao.queryBuilder()
                .where()
                .eq("user_id", user.getUsername())
                .query();
    }

    /**
     * Find all challenges completed by a user (returning Challenge objects)
     */
    public List<Challenge> findCompletedChallenges(User user) throws SQLException {
        List<UserChallenge> userChallenges = findCompletedByUser(user);
        return userChallenges.stream()
                .map(UserChallenge::getChallenge)
                .toList();
    }

    public List<Challenge> findNotCompletedByUserAndType(User user, String type) throws SQLException {
        // All challenges of the specified type
        ChallengesRepository cr = new ChallengesRepository();
        List<Challenge> allOfType = cr.findByType(type);

        // Challenges already completed by the user
        List<UserChallenge> completed = findCompletedByUser(user);

        List<Integer> completedIds = completed.stream()
                .map(uc -> uc.getChallenge().getChallengeId())
                .toList();

        // Filter out uncompleted challenges
        return allOfType.stream()
                .filter(c -> !completedIds.contains(c.getChallengeId()))
                .toList();
    }
}
