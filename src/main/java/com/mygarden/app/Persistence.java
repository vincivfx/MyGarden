package com.mygarden.app;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mygarden.app.models.User;

import java.sql.SQLException;

public class Persistence {

    public Dao<User, String> userDao;

    public Persistence() throws SQLException {

        String connectionString = "jdbc:sqlite:persistence.db";

        /* connect to SQLite database */
        ConnectionSource connectionSource = new JdbcConnectionSource(connectionString);

        /* define DAOs */
        userDao = DaoManager.createDao(connectionSource, User.class);

        /* create tables if they don't exist */
        TableUtils.createTableIfNotExists(connectionSource, User.class);

    }


}
