package org.example;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;
import module.Media;
import module.Film;
import module.Book;
import module.Serial;
import module.Music;
import module.MusicType;

import java.util.List;

public class AddController {

    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField titleField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(
                "🎬 FILM",
                "📚 BOOK",
                "📺 SERIES",
                "🎵 MUSIC"
        ));
        if (statusLabel != null) {
            statusLabel.setText("Ready. Enter title and let Gemini AI do the magic!");
        }
    }

    @FXML
    void onSaveClick() {
        String selectedType = typeComboBox.getValue();
        String rawTitle = titleField.getText();

        if (selectedType == null || rawTitle == null || rawTitle.trim().isEmpty()) {
            System.out.println("⚠️ Error: Form incomplete!");
            if (statusLabel != null) {
                statusLabel.setText("❌ Error: Please select type and enter a title!");
                statusLabel.setStyle("-fx-text-fill: #f72585;");
            }
            return;
        }

        rawTitle = rawTitle.trim();

        if (statusLabel != null) {
            statusLabel.setText("🤖 Gemini AI is cleaning and organizing the title...");
            statusLabel.setStyle("-fx-text-fill: #4cc9f0;");
        }

        String cleanTitle = ApiService.cleanTitleWithGemini(rawTitle);
        int newId = generateUniqueId();

        if (statusLabel != null) {
            statusLabel.setText("🌐 Querying global cloud databases for artwork & metadata...");
            statusLabel.setStyle("-fx-text-fill: #4361ee;");
        }

        String genre = "General";
        int year = 2026;
        String imageUrl = "https://example.com/default-poster.jpg";
        String description = "No description available.";

        Media newMediaItem = null;

        if (selectedType.contains("FILM")) {
            JSONObject apiData = ApiService.fetchMovieOrSeries(cleanTitle);

            String director = "Unknown Director";
            int duration = 120;

            if (apiData != null) {
                cleanTitle = apiData.optString("Title", cleanTitle);
                genre = apiData.optString("Genre", genre);
                year = parseYear(apiData.optString("Year", "2026"));
                imageUrl = apiData.optString("Poster", imageUrl);
                description = apiData.optString("Plot", description);
                director = apiData.optString("Director", director);
                duration = parseDuration(apiData.optString("Runtime", "120 min"));
            }
            newMediaItem = new Film(newId, cleanTitle, genre, year, imageUrl, description, duration, director, "Production Studio");
        }

        else if (selectedType.contains("BOOK")) {
            System.out.println("🔍 VYHLEDÁVÁNÍ KNIHY - Posílám do API plný název: " + cleanTitle);

            JSONObject apiData = ApiService.fetchBook(cleanTitle);

            String author = "Neznámý autor";
            int pageCount = 250;
            genre = "Literatura";
            imageUrl = "https://example.com/default-poster.jpg";

            if (apiData != null) {
                System.out.println("✅ DATA DORAZILA: Knižní API vrátilo platný objekt!");
                cleanTitle = apiData.optString("title", cleanTitle);

                if (apiData.has("categories")) {
                    genre = apiData.getJSONArray("categories").optString(0, genre);
                }
                if (apiData.has("authors")) {
                    author = apiData.getJSONArray("authors").optString(0, author);
                }

                String publishedDate = apiData.optString("publishedDate", "2026");
                year = parseYear(publishedDate);
                pageCount = apiData.optInt("pageCount", pageCount);
                description = apiData.optString("description", description);
                imageUrl = apiData.optString("thumbnailUrl", imageUrl);

            } else {
                System.out.println("❌ DATA NEDORAZILA: apiData je stále null!");
            }
            newMediaItem = new Book(newId, cleanTitle, genre, year, imageUrl, description, author, pageCount, "Globální nakladatel", "000-00-000");
        }

        else if (selectedType.contains("SERIES")) {
            JSONObject apiData = ApiService.fetchMovieOrSeries(cleanTitle);

            int totalSeasons = 1;
            int totalEpisodes = 12;

            if (apiData != null) {
                cleanTitle = apiData.optString("Title", cleanTitle);
                genre = apiData.optString("Genre", genre);
                year = parseYear(apiData.optString("Year", "2026"));
                imageUrl = apiData.optString("Poster", imageUrl);
                description = apiData.optString("Plot", description);
                totalSeasons = parseInteger(apiData.optString("totalSeasons", "1"));
                System.out.println("🔍 Querying sub-seasons to calculate exact episode count for: " + cleanTitle);
            } else {
                totalEpisodes = 12;
            }
            newMediaItem = new Serial(newId, cleanTitle, genre, year, imageUrl, description, "Showrunner", totalSeasons, totalEpisodes, "Active");
        }

        else if (selectedType.contains("MUSIC")) {
            System.out.println("🔍 VYHLEDÁVÁNÍ HUDBY - Posílám do API plný název: " + cleanTitle);

            JSONObject musicData = ApiService.fetchMusic(cleanTitle);

            String artist = "Various Artists";
            String recordLabel = "Record Label";
            genre = "Music";

            if (musicData != null) {
                System.out.println("✅ DATA DORAZILA: Internet Archive vrátil hudební objekt!");
                cleanTitle = musicData.optString("title", cleanTitle);
                artist = musicData.optString("artist", artist);
                genre = musicData.optString("genre", genre);
                year = parseYear(musicData.optString("year", "2026"));
                description = musicData.optString("description", description);
                recordLabel = musicData.optString("publisher", recordLabel);
            } else {
                System.out.println("❌ DATA NEDORAZILA: Hudební apiData je null!");
            }

            newMediaItem = new Music(newId, cleanTitle, genre, year, imageUrl, description, artist, recordLabel, 10, 2400, MusicType.ALBUM);
        }

        if (newMediaItem != null) {
            MediaLibrary.addItem(newMediaItem);
            System.out.println("🚀 Smarter Item Saved: " + cleanTitle);

            // Zavřeme toto dialogové okno formuláře
            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        } else {
            if (statusLabel != null) {
                statusLabel.setText("❌ Error: Failed to generate media asset.");
                statusLabel.setStyle("-fx-text-fill: #f72585;");
            }
        }
    }

    private int generateUniqueId() {
        List<Media> currentList = MediaLibrary.getMediaList();
        int maxId = 0;
        for (Media m : currentList) {
            if (m.getId() > maxId) maxId = m.getId();
        }
        return maxId + 1;
    }

    private int parseYear(String yearStr) {
        try {
            return Integer.parseInt(yearStr.substring(0, 4));
        } catch (Exception e) {
            return 2026;
        }
    }

    private int parseDuration(String durationStr) {
        try {
            return Integer.parseInt(durationStr.replace(" min", "").trim());
        } catch (Exception e) {
            return 120;
        }
    }

    private int parseInteger(String intStr) {
        try {
            return Integer.parseInt(intStr.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    @FXML
    void onBackClick() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}