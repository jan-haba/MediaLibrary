package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddController {
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField titleField;

    @FXML
    void onSaveClick(ActionEvent event) {
        System.out.println("Save clicked! (Here we will connect AI and API tomorrow)");
    }

    @FXML
    void onBackClick(ActionEvent event) {
        WindowManager.changeWindow(event, "main_window.fxml", "My Personal Media Library");
    }
}