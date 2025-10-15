package com.mygarden.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyGardenApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyGardenApplication.class.getResource("login-page-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 335, 600);

        stage.setResizable(false);
        stage.setTitle("MyGarden");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // connect to the SQLite database
        try {
            boolean shouldSpawnDatabase = false;
            File dbFile = new File("./persistence.db");

            if (!dbFile.exists()) {
                shouldSpawnDatabase = true;
            }

            DatabaseManager.connect();

            if (shouldSpawnDatabase || args.length > 0 && args[0].equals("--spawn")) {
                DatabaseManager.getInstance().spawnDatabase();
            }


        } catch (SQLException exception) {
            // if connection fails, just exit printing errors
            exception.printStackTrace();
            System.exit(1);
        }


        // launch JavaFX application
        launch();
    }
}