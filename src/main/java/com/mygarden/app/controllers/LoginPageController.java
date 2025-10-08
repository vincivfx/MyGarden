package com.mygarden.app.controllers;

import java.util.Optional;

import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.User;
import com.mygarden.app.repositories.UserRepository;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginPageController {
    
    //FXML 

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private void tryToLogin(Event e)
    {
        UserRepository ur = new UserRepository();

        Optional<User> optionalUser = ur.register("Nguiard", "1234", "Nathan GM");
        if (optionalUser.isPresent()) {
            SceneUtils.changeScene(event, fxmlFile, currentUser);
        } else {
            System.out.println("Login failed");
        }
        //controller.setUser(user);

    }

}
