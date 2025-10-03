package com.mygarden.app.repositories;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.DatabaseManager;
import com.mygarden.app.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepository implements BaseRepository<User, String> {

    public final Dao<User, String> userDao;

    public UserRepository() {
        this.userDao = DatabaseManager.getInstance().getUserDao();
    }


    @Override
    public List<User> findAll() throws SQLException {
        return userDao.queryForAll();
    }

    @Override
    public Optional<User> findById(String id) throws SQLException {
        return Optional.ofNullable(userDao.queryForId(id));
    }

    @Override
    public User save(User entity) throws SQLException {
        userDao.createOrUpdate(entity);
        return entity;
    }

    public Optional<User> login(String username, String password) {
        Optional<User> currentUser;

        try {
            currentUser = this.findById(username);
        } catch (SQLException sqlException) {
            return Optional.empty();
        }

        if (currentUser.isEmpty() || !currentUser.get().verifyPassword(password)) {
            return Optional.empty();
        }

        return currentUser;
    }

    public Optional<User> register(String username, String password, String name) {
        User user = User.createUser(username, password, name);
        try {
            this.userDao.createOrUpdate(user);
        } catch (SQLException sqlException) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
