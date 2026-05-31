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

/**
 * Controller class responsible for handling the interactive media creation wizard form.
 * This class captures user input from the addition modal view, coordinates the data orchestration
 * flow with the AI and cloud repository layer {@link ApiService} to sanitize title strings and extract
 * rich metadata, maps the structural response into polymorphic subclass entities, and logs the items
 * into the main collection file persistent database.
 *
 */
public class AddController {


    @FXML private ComboBox<String> typeComboBox;

    @FXML private TextField titleField;

    @FXML private Label statusLabel;

    /**
     * Initializes the entry view state configuration settings automatically after FXML structural loading completes.
     * Populates the selection dropdown components with supported categories paired with illustrative emojis
     * and triggers the baseline interactive prompt text notification layout.
     */
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

    /**
     * Orchestrates validation checks, initiates cloud serialization cycles, and logs newly created media items.
     * Evaluates missing inputs defensively. If inputs pass validation, it pushes strings into the AI layer
     * for cleaning, triggers HTTP request handlers targeting external databases to map attributes, maps data fields into
     * concrete child records using polymorphic assignments, and invokes storage synchronization updates.
     */
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
            System.out.println("🔍 MOVIE SEARCH - Sending sanitized title to OMDb: " + cleanTitle);
            JSONObject apiData = ApiService.fetchMovieOrSeries(cleanTitle, "movie");

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
            System.out.println("🔍 BOOK SEARCH - Sending full title to API: " + cleanTitle);

            JSONObject apiData = ApiService.fetchBook(cleanTitle);

            String author = "Unknown Author";
            int pageCount = 250;
            String publisher = "Global Publisher";
            String isbn = "000-00-000";

            if (apiData != null) {
                System.out.println("✅ DATA RECEIVED: Book API returned a valid object!");
                cleanTitle = apiData.optString("title", cleanTitle);
                description = apiData.optString("description", description);
                pageCount = apiData.optInt("pageCount", pageCount);
                publisher = apiData.optString("publisher", publisher);
                imageUrl = apiData.optString("imageUrl", imageUrl);
                year = parseYear(apiData.optString("publishedDate", "2026"));

                if (apiData.has("categories")) {
                    genre = apiData.getJSONArray("categories").optString(0, genre);
                }
                if (apiData.has("authors")) {
                    author = apiData.getJSONArray("authors").optString(0, author);
                }
                if (apiData.has("industryIdentifiers")) {
                    isbn = apiData.getJSONArray("industryIdentifiers").getJSONObject(0).optString("identifier", isbn);
                }
            } else {
                System.out.println("❌ DATA NOT RECEIVED: Using default fallback values.");
            }
            newMediaItem = new Book(newId, cleanTitle, genre, year, imageUrl, description, author, pageCount, publisher, isbn);
        }

        else if (selectedType.contains("SERIES")) {
            System.out.println("🔍 SERIES SEARCH - Sending sanitized title to OMDb: " + cleanTitle);
            JSONObject apiData = ApiService.fetchMovieOrSeries(cleanTitle, "series");

            int totalSeasons = 1;
            int totalEpisodes = 0;

            if (apiData != null) {
                cleanTitle = apiData.optString("Title", cleanTitle);
                genre = apiData.optString("Genre", genre);
                year = parseYear(apiData.optString("Year", "2026"));
                imageUrl = apiData.optString("Poster", imageUrl);
                description = apiData.optString("Plot", description);
                totalSeasons = parseInteger(apiData.optString("totalSeasons", "1"));


                System.out.println("🔍 Seasons detected: " + totalSeasons + ". Iterating through all seasons for precise episode count...");

                for (int i = 1; i <= totalSeasons; i++) {
                    int episodesInSeason = ApiService.fetchEpisodeCountForSeason(cleanTitle, i);
                    totalEpisodes += episodesInSeason;
                }

                System.out.println("✅ Calculation complete! Total verified episode count is: " + totalEpisodes);

                if (totalEpisodes == 0) {
                    totalEpisodes = totalSeasons * 10;
                }
            } else {
                totalEpisodes = 12;
            }

            newMediaItem = new Serial(newId, cleanTitle, genre, year, imageUrl, description, "Showrunner", totalSeasons, totalEpisodes, "Active");
        }

        else if (selectedType.contains("MUSIC")) {
            System.out.println("🔍 MUSIC SEARCH - Sending full title to API: " + cleanTitle);

            JSONObject musicData = ApiService.fetchMusic(cleanTitle);

            String artist = "Various Artists";
            String recordLabel = "Record Label";
            int totalTracks = 10;
            int durationSeconds = 2400;
            MusicType releaseType = MusicType.ALBUM;

            if (musicData != null) {
                System.out.println("✅ DATA RECEIVED: iTunes API returned a valid music object!");
                cleanTitle = musicData.optString("title", cleanTitle);
                artist = musicData.optString("artist", artist);
                genre = musicData.optString("genre", genre);
                year = musicData.optInt("year", 2026);
                description = musicData.optString("description", description);
                recordLabel = musicData.optString("publisher", recordLabel);
                totalTracks = musicData.optInt("totalTracks", totalTracks);
                durationSeconds = musicData.optInt("durationSeconds", durationSeconds);
                imageUrl = musicData.optString("imageUrl", imageUrl);

                try {
                    releaseType = MusicType.valueOf(musicData.optString("releaseType", "ALBUM"));
                } catch (IllegalArgumentException e) {
                    releaseType = MusicType.ALBUM;
                }
            }

            newMediaItem = new Music(newId, cleanTitle, genre, year, imageUrl, description, artist, recordLabel, totalTracks, durationSeconds, releaseType);
        }

        if (newMediaItem != null) {
            MediaLibrary.addItem(newMediaItem);
            System.out.println("🚀 Smarter Item Saved: " + cleanTitle);

            Stage stage = (Stage) titleField.getScene().getWindow();
            stage.close();
        } else {
            if (statusLabel != null) {
                statusLabel.setText("❌ Error: Failed to generate media asset.");
                statusLabel.setStyle("-fx-text-fill: #f72585;");
            }
        }
    }

    /**
     * Inspects the global collections system list to generate a non-conflicting incremental primary key index.
     *
     * @return an integer representing the newly computed unique identification key
     */
    private int generateUniqueId() {
        List<Media> currentList = MediaLibrary.getMediaList();
        int maxId = 0;
        for (Media m : currentList) {
            if (m.getId() > maxId) maxId = m.getId();
        }
        return maxId + 1;
    }

    /**
     * Parses the calendar year integer parameter from raw API text representations using defensive fallback routines.
     *
     * @param yearStr the raw textual representation extracted from the cloud API mapping response
     * @return the extracted 4-digit calendar year integer, or 2026 as a fallback value
     */
    private int parseYear(String yearStr) {
        try {
            return Integer.parseInt(yearStr.substring(0, 4));
        } catch (Exception e) {
            return 2026;
        }
    }

    /**
     * Sanitizes string indicators representing runtimes and maps them into numeric integer values.
     *
     * @param durationStr the raw duration text snippet parsed from the response structure (e.g., "142 min")
     * @return an integer indicating the track duration scale mapped directly in minutes, or 120 as a fallback value
     */
    private int parseDuration(String durationStr) {
        try {
            return Integer.parseInt(durationStr.replace(" min", "").trim());
        } catch (Exception e) {
            return 120;
        }
    }

    /**
     * Converts unstructured numerical strings cleanly into usable primitives with safe exception boundaries.
     *
     * @param intStr the text metric sequence to parse safely
     * @return the integer representation of the text parameter sequence, or 1 as a fallback value
     */
    private int parseInteger(String intStr) {
        try {
            return Integer.parseInt(intStr.trim());
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Dismisses the active modal scene context window layout sequence without submitting changes.
     */
    @FXML
    void onBackClick() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}