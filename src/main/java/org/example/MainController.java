package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import module.Media;
import module.Film;
import module.Book;
import module.Serial;
import module.Music;

import java.util.List;

public class MainController {

    @FXML private TextField searchField;
    @FXML private ListView<Media> mediaListView;

    // Special JavaFX list that automatically updates the UI when changed
    private final ObservableList<Media> observableMediaList = FXCollections.observableArrayList();

    /**
     * The initialize() method runs AUTOMATICALLY as soon as the FXML layout is loaded,
     * right before the user actually sees the window.
     */
    @FXML
    public void initialize() {
        setupListViewFormatting();

        refreshData();

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMedia(newValue);
        });
    }


    private void refreshData() {
        List<Media> allData = MediaLibrary.getMediaList();
        observableMediaList.setAll(allData);
        mediaListView.setItems(observableMediaList);
    }


    private void filterMedia(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            mediaListView.setItems(observableMediaList);
            return;
        }

        String lowerCaseFilter = searchText.toLowerCase().trim();

        ObservableList<Media> filteredList = FXCollections.observableArrayList();

        for (Media m : observableMediaList) {
            if (m.getTitle().toLowerCase().contains(lowerCaseFilter) ||
                    m.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                filteredList.add(m);
            }
        }

        mediaListView.setItems(filteredList);
    }


    private void setupListViewFormatting() {
        mediaListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Media item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    String prefix = "📦 [MEDIA]";
                    String details = "";

                    if (item instanceof Film) {
                        prefix = "🎬 [FILM]";
                        details = " (" + ((Film) item).getDuration() + " min, Director: " + ((Film) item).getDirector() + ")";
                    } else if (item instanceof Book) {
                        prefix = "📚 [BOOK]";
                        details = " (" + ((Book) item).getPageCount() + " pages, Author: " + ((Book) item).getAuthor() + ")";
                    } else if (item instanceof Serial) {
                        prefix = "📺 [SERIES]";
                        details = " (" + ((Serial) item).getTotalSeasons() + " seasons)";
                    } else if (item instanceof Music) {
                        prefix = "🎵 [MUSIC]";
                        details = " (Artist: " + ((Music) item).getArtist() + ")";
                    }

                    setText(prefix + " " + item.getTitle() + " [" + item.getGenre() + "]" + details);
                }
            }
        });
    }

    @FXML
    void onAddWindowClick(ActionEvent event) {
        WindowManager.changeWindow(event, "add_window.fxml", "Add New Media");
    }

    @FXML
    void onStatsWindowClick(ActionEvent event) {
        WindowManager.changeWindow(event, "stats_window.fxml", "Library Statistics");
    }
}