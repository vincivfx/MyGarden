package com.mygarden.app.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.Challenge;

public class ChallengesRepository implements BaseRepository<Challenge, String>{
    private final Dao<Challenge, String> challengeDao = DatabaseManager.getInstance().getChallengeDao();

    @Override
    public List<Challenge> findAll() throws SQLException {
        return challengeDao.queryForAll();
    }

    @Override
    public Optional<Challenge> findById(String id) throws SQLException {
        return Optional.ofNullable(challengeDao.queryForId(id));
    }

    public List<Challenge> findByType(String type) throws SQLException {
        return challengeDao.queryBuilder()
                .where()
                .eq("type", type)
                .query();
    }


    @Override
    public Challenge save(Challenge entity) throws SQLException {
        challengeDao.createOrUpdate(entity);
        return entity;
    }
}
