package com.mygarden.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("MyGarden");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Persistence persistence;

        try {
            persistence = new Persistence();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.exit(1);
        }

        launch();
    }
}