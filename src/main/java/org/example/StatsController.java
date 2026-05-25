package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatsController {
    @FXML private Label movieHoursLabel;
    @FXML private Label episodesLabel;
    @FXML private Label pagesLabel;
    @FXML private Label musicTracksLabel;

    @FXML
    void onBackClick(ActionEvent event) {
        WindowManager.changeWindow(event, "main_window.fxml", "My Personal Media Library");
    }
}