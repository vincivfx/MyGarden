package com.mygarden.app;

import java.io.IOException;

import com.mygarden.app.controllers.AbstractController;
import com.mygarden.app.models.User;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyGardenApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyGardenApplication.class.getResource("main-page-view.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load(), 335, 600);

        AbstractController controller = fxmlLoader.getController();
        
        controller.setUser(new User());

        stage.setResizable(false);
        stage.setTitle("MyGarden");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}