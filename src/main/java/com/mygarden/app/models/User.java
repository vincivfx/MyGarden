package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.mygarden.app.repositories.TransferRepository;

@DatabaseTable(tableName = "mg_users")
public class User {

    @DatabaseField(id = true)
    private String username;

    @DatabaseField
    private String name;

    @DatabaseField
    private String password;

    @DatabaseField
    private int coins;

    @ForeignCollectionField
    private ForeignCollection<Transfer> transfers;


    private List<Plant> inventory;

    public User() {
        this.name = "Arthur";
        // needed by ORMLite
        this.coins = 50;
        inventory = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public int getCoins() {
        int coins = 0;

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
