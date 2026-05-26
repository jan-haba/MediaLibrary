package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import module.Media;
import module.Film;
import module.Book;
import module.Serial;
import module.Music;

import java.util.List;

public class StatsController {

    @FXML private Label movieHoursLabel;
    @FXML private Label episodesLabel;
    @FXML private Label pagesLabel;
    @FXML private Label musicTracksLabel;


    @FXML
    public void initialize() {
        calculateAndDisplayStatistics();
    }


    private void calculateAndDisplayStatistics() {
        List<Media> currentList = MediaLibrary.getMediaList();

        int totalMovieMinutes = 0;
        int totalEpisodes = 0;
        int totalBookPages = 0;
        int totalMusicTracks = 0;

        for (Media media : currentList) {
            if (media instanceof Film) {
                totalMovieMinutes += ((Film) media).getDuration();
            } else if (media instanceof Book) {
                totalBookPages += ((Book) media).getPageCount();
            } else if (media instanceof Serial) {
                totalEpisodes += ((Serial) media).getTotalEpisodes();
            } else if (media instanceof Music) {
                totalMusicTracks += ((Music) media).getTotalTracks();
            }
        }

        int hours = totalMovieMinutes / 60;
        int minutes = totalMovieMinutes % 60;
        String formattedWatchTime = hours + " hrs " + minutes + " mins";

        movieHoursLabel.setText("Total Movie Hours: " + formattedWatchTime);
        episodesLabel.setText("Total Series Episodes Watched: " + totalEpisodes);
        pagesLabel.setText("Total Book Pages Read: " + totalBookPages);
        musicTracksLabel.setText("Total Music Tracks: " + totalMusicTracks);
    }


    @FXML
    void onBackClick(ActionEvent event) {
        WindowManager.changeWindow(event, "main_window.fxml", "My Personal Media Library");
    }
}