package com.mygarden.app.repositories;

import com.j256.ormlite.dao.Dao;
import com.mygarden.app.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, X> {
    public List<T> findAll() throws SQLException;
    public Optional<T> findById(X id) throws SQLException;
    public T save(T entity) throws SQLException;
}
