package com.mygarden.app.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.mygarden.app.controllers.utils.SceneUtils;
import com.mygarden.app.models.User;
import com.mygarden.app.repositories.UserRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginPageController {
    

    private boolean loginMode = true;
    //FXML 

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private TextField name;

    @FXML
    private Label informations;

    @FXML
    private Button changeModeButton;

    @FXML
    private Button connection;

    @FXML
    private void tryToConnect(ActionEvent e) throws IOException, SQLException
    {
        UserRepository ur = new UserRepository();
        Optional<User> optionalUser;
        if(loginMode)
        {
            optionalUser = ur.login(login.getText(), password.getText());
        }
        else
        {
            optionalUser = ur.register(login.getText(), password.getText(), name.getText());
        }
        
        if (optionalUser.isPresent()) {
            SceneUtils.changeScene(e, "/com/mygarden/app/main-page-view.fxml", optionalUser.get());
        } else {
            informations.setText("Login failed");
        }
    }

    @FXML
    private void changeMode(ActionEvent e)
    {
        loginMode = !loginMode;
        name.setVisible(!loginMode);
        if(loginMode)
        {
            connection.setText("Connect");
            changeModeButton.setText("Don't have an account?");
            
        }
        else
        {
            connection.setText("Create");
            changeModeButton.setText("You have an account");
        }
    }



}
