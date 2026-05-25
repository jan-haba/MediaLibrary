package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import module.Media;

public class MainController {

    @FXML private TextField searchField;
    @FXML private ListView<Media> mediaListView;

    @FXML
    void onAddWindowClick(ActionEvent event) {
        WindowManager.changeWindow(event, "add_window.fxml", "Add New Media");
    }

    @FXML
    void onStatsWindowClick(ActionEvent event) {
        WindowManager.changeWindow(event, "stats_window.fxml", "Library Statistics");
    }
}