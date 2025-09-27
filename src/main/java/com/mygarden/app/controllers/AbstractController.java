package com.mygarden.app.controllers;

import com.mygarden.app.models.User;

public abstract class AbstractController {

    User user;

    public User getUser()
    {
        return user;
    }

    public void setUser(User newUser)
    {
        user = newUser;
        onUserIsSet();
    }

    public void onUserIsSet()
    {

    }
    
}
