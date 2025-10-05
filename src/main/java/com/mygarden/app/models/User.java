package com.mygarden.app.models;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

    @DatabaseField
    private List<Plant> inventory;

    public User() {
        this.name = "Arthur"; // Remove this when it's working
        // needed by ORMLite
        this.coins = 50;
        inventory = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }

    public int getCoins()
    {
        return this.coins;
    }

    public void spendCoins(int price)
    {
        this.coins -= price;
    }

    public void earnCoins(int coins)
    {
        this.coins += coins;
    }

    public void addPlantInInventory(Plant plant)
    {
        inventory.add(plant);
    }

}
