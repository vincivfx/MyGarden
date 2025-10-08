package com.mygarden.app.repositories;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.Challenge;
import com.mygarden.app.models.ShopItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ChallengeRepository implements BaseRepository<Challenge, Integer> {
    private final Dao<Challenge, Integer> challengeDao = DatabaseManager.getInstance().getChallengeDao();

    @Override
    public List<Challenge> findAll() throws SQLException {
        return challengeDao.queryForAll();
    }

    @Override
    public Optional<Challenge> findById(Integer id) throws SQLException {
        return Optional.of(challengeDao.queryForId(id));
    }

    @Override
    public Challenge save(Challenge entity) throws SQLException {
        challengeDao.createOrUpdate(entity);
        return entity;
    }
}
