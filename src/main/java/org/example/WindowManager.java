package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class WindowManager {
    public static void changeWindow(ActionEvent event, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(WindowManager.class.getResource("/org/example/" + fxmlFile));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            stage.show();
        } catch (Exception e) {
            System.out.println("Could not open window: " + fxmlFile);
            e.printStackTrace();
        }
    }
}