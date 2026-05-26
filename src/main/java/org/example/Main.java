package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main application class that initializes and starts the JavaFX user interface.
 * This class handles loading the persistent data from the storage file on startup
 * and setting up the primary graphical window (Stage) using the FXML layout configuration.
 * It also performs an initial save sequence to ensure file connectivity.
 */
public class Main extends Application {

    /**
     * Initializes the JavaFX stage, loads data, and sets up the user interface scene.
     *
     * @param primaryStage the primary window container provided by the JavaFX runtime
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            MediaLibrary.loadFromFile();

            Parent root = FXMLLoader.load(getClass().getResource("/org/example/main_window.fxml"));
            primaryStage.setTitle("Media Library");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setResizable(false);
            primaryStage.show();

            MediaLibrary.saveToFile();

        } catch (Exception e) {
            System.out.println("Error starting JavaFX application:");
            e.printStackTrace();
        }
    }
    /**
     * The standard main method used to launch the standalone JavaFX application runtime.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}