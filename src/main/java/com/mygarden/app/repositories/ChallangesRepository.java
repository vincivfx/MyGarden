package com.mygarden.app.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.Challenge;

public class ChallangesRepository implements BaseRepository<Challenge, String>{
    private final Dao<Challenge, String> challengeDao = DatabaseManager.getInstance().getChallengeDao();

    @Override
    public List<Challenge> findAll() throws SQLException {
        return challengeDao.queryForAll();
    }

    @Override
    public Optional<Challenge> findById(String id) throws SQLException {
        return Optional.of(challengeDao.queryForId(id));
    }

    @Override
    public Challenge save(Challenge entity) throws SQLException {
        challengeDao.createOrUpdate(entity);
        return entity;
    }
}
