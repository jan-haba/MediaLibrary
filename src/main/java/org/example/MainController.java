package org.example;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import module.Book;
import module.Film;
import module.Media;
import module.Music;
import module.Serial;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class responsible for managing the primary multimedia catalog dashboard.
 * This core user interface driver handles building the interactive image poster matrix,
 * coordinates responsive window resizing properties, executes query string text filtering,
 * and handles the instantiation layout cycles for sub-views such as addition forms, asset metrics,
 * and media specification dialogs.
 *
 */
public class MainController {


    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane mediaTilePane;

    /**
     * Initializes the dashboard environment configuration values instantly after layout resource bindings resolve.
     * Binds resizable dimension subtraction listeners to automatically calculate content column wrapping margins
     * and maps an interactive event observation listener chain monitoring lookahead inputs key by key.
     */
    @FXML
    public void initialize() {
        System.out.println("🖼️ Inicializace hlavního menu s dlaždicemi plakátů...");

        mediaTilePane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(25));

        refreshGrid(MediaLibrary.getMediaList());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMedia(newValue);
        });
    }

    /**
     * Flushes the active catalog visualization and repopulates the dashboard tile matrix.
     * Loops across arrays using runtime polymorphism to encapsulate specific layout properties.
     * Implements image caching, dropshadow aesthetic modifiers, and conditional stack layer overlays
     * to highlight asset entities flagged as user favorites.
     *
     * @param items the dataset collection consisting of {@link Media} entries to map into cards
     */
    private void refreshGrid(List<Media> items) {
        mediaTilePane.getChildren().clear();

        for (Media item : items) {
            VBox card = new VBox();
            card.setSpacing(8);
            card.setStyle("-fx-alignment: center; -fx-cursor: hand;");

            javafx.scene.layout.StackPane imageContainer = new javafx.scene.layout.StackPane();
            imageContainer.setStyle("-fx-alignment: top-right;");

            ImageView imageView = new ImageView();
            imageView.setFitWidth(130);
            imageView.setFitHeight(180);
            imageView.setPreserveRatio(false);
            imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

            String url = item.getImageUrl();
            if (url == null || url.isEmpty() || url.contains("example.com")) {
                url = "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=150&auto=format&fit=crop&q=60";
            }

            Image img = new Image(url, 130, 180, false, true, true);
            imageView.setImage(img);

            imageContainer.getChildren().add(imageView);

            if (item.isFavorite()) {
                Label favTag = new Label("❤ FAV");
                favTag.setStyle(
                        "-fx-background-color: #ff4757;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;" +
                                "-fx-font-size: 10px;" +
                                "-fx-padding: 3 7 3 7;" +
                                "-fx-background-radius: 0 0 0 10;" + // Round only the inner bottom corner
                                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 4, 0, 0, 0);"
                );

                imageContainer.getChildren().add(favTag);
            }

            String shortTitle = item.getTitle();
            if (shortTitle.length() > 18) {
                shortTitle = shortTitle.substring(0, 15) + "...";
            }

            Label titleLabel = new Label(shortTitle);
            titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

            card.getChildren().addAll(imageContainer, titleLabel);
            card.setOnMouseClicked(event -> showDetailWindow(item));

            mediaTilePane.getChildren().add(card);
        }
    }

    /**
     * Screens out storage items failing to match lookahead strings typed inside the lookup input.
     * Filters the global item stream comparing lower-case variants of media titles or genre classifications.
     *
     * @param query the textual search term parsed to restrict displayed catalog cards
     */
    private void filterMedia(String query) {
        if (query == null || query.isEmpty()) {
            refreshGrid(MediaLibrary.getMediaList());
            return;
        }

        String lowerQuery = query.toLowerCase();
        List<Media> filtered = MediaLibrary.getMediaList().stream()
                .filter(m -> m.getTitle().toLowerCase().contains(lowerQuery) ||
                        m.getGenre().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());

        refreshGrid(filtered);
    }

    /**
     * Builds and launches the standalone focused view presentation window displaying deep asset attributes.
     * Resolves structural UI mappings via reflection lookup injections. Evaluates data classifications using
     * explicit inheritance instance routing, maps rating configurations, and provides interactive callback closures
     * that commit modification events directly to the file persistence system.
     *
     * @param item the baseline concrete {@link Media} parent element selected to fill out the form
     */
    @SuppressWarnings("unchecked")
    private void showDetailWindow(Media item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/detail_window.fxml"));
            Parent root = loader.load();

            ImageView detailImageView = (ImageView) root.lookup("#detailImageView");
            Label titleLabel = (Label) root.lookup("#titleLabel");
            Label typeLabel = (Label) root.lookup("#typeLabel");
            Label authorLabel = (Label) root.lookup("#authorLabel");
            Label genreLabel = (Label) root.lookup("#genreLabel");
            Label yearLabel = (Label) root.lookup("#yearLabel");
            Label extraLabel = (Label) root.lookup("#extraLabel");
            TextArea descriptionArea = (TextArea) root.lookup("#descriptionArea");

            Button favoriteButton = (Button) root.lookup("#favoriteButton");
            ComboBox<String> ratingComboBox = (ComboBox<String>) root.lookup("#ratingComboBox");

            Button deleteButton = (Button) root.lookup("#deleteButton");

            titleLabel.setText(item.getTitle());
            genreLabel.setText("Genre: " + item.getGenre());
            yearLabel.setText("Year: " + item.getYear());
            descriptionArea.setText(item.getDescription());

            String imgUrl = item.getImageUrl();
            if (imgUrl == null || imgUrl.isEmpty() || imgUrl.contains("example.com")) {
                imgUrl = "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=300&auto=format&fit=crop&q=60";
            }
            detailImageView.setImage(new Image(imgUrl));

            if (item instanceof Film) {
                typeLabel.setText("🎬 TYPE: MOVIE");
                authorLabel.setText("Director: " + ((Film) item).getDirector());
                extraLabel.setText("Duration: " + ((Film) item).getDuration() + " min");
            } else if (item instanceof Book) {
                typeLabel.setText("📚 TYPE: BOOK");
                authorLabel.setText("Author: " + ((Book) item).getAuthor());
                extraLabel.setText("Pages: " + ((Book) item).getPageCount());
            } else if (item instanceof Serial) {
                typeLabel.setText("📺 TYPE: TV SHOW");
                authorLabel.setText("Creator: " + ((Serial) item).getCreator());
                extraLabel.setText("Episodes: " + ((Serial) item).getTotalEpisodes());
            } else if (item instanceof Music) {
                typeLabel.setText("🎵 TYPE: MUSIC ALBUM");
                authorLabel.setText("Artist: " + ((Music) item).getArtist());
                extraLabel.setText("Tracks: " + ((Music) item).getTotalTracks());
            }

            if (item.isFavorite()) {
                favoriteButton.setText("❤️ Favorite");
                favoriteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-background-radius: 15; -fx-font-weight: bold;");
            }

            favoriteButton.setOnAction(e -> {
                if (item.isFavorite()) {
                    item.setFavorite(false);
                    favoriteButton.setText("🖤 Add to Favorites");
                    favoriteButton.setStyle("-fx-background-color: #2a2a2a; -fx-text-fill: #ff4757; -fx-background-radius: 15; -fx-font-weight: bold;");
                } else {
                    item.setFavorite(true);
                    favoriteButton.setText("❤️ Favorite");
                    favoriteButton.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white; -fx-background-radius: 15; -fx-font-weight: bold;");
                }
                MediaLibrary.saveToFile();
                refreshGrid(MediaLibrary.getMediaList());
            });

            ratingComboBox.setItems(FXCollections.observableArrayList(
                    "Not Rated", "⭐", "⭐⭐", "⭐⭐⭐", "⭐⭐⭐⭐", "⭐⭐⭐⭐⭐"
            ));
            ratingComboBox.getSelectionModel().select(item.getRating());

            ratingComboBox.setOnAction(e -> {
                int selectedIndex = ratingComboBox.getSelectionModel().getSelectedIndex();
                item.setRating(selectedIndex);
                MediaLibrary.saveToFile();
            });

            Stage stage = new Stage();

            if (deleteButton != null) {
                deleteButton.setOnAction(e -> {
                    System.out.println("🗑️ Odstraňuji položku z knihovny: " + item.getTitle());
                    MediaLibrary.removeItem(item);
                    stage.close();
                    refreshGrid(MediaLibrary.getMediaList());
                });
            }

            stage.setTitle("Media Detail - " + item.getTitle());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.setMinWidth(720);
            stage.setMinHeight(540);
            stage.show();

        } catch (IOException e) {
            System.out.println("🚨 Chyba při otevírání detailního okna: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Spawns a synchronized blocking dialog layer containing the smart AI asset submission form fields.
     */
    @FXML
    private void onAddWindowClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/add_window.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Media Item");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            System.out.println("🔄 Formulář zavřen, překresluji hlavní mřížku...");
            refreshGrid(MediaLibrary.getMediaList());
        } catch (IOException e) {
            System.out.println("🚨 Nelze otevřít okno pro přidání: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Spawns a floating auxiliary data metrics interface highlighting compiled statistical visual graphics.
     */
    @FXML
    private void onStatsWindowClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/stats_window.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Library Statistics");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.out.println("🚨 Nelze otevřít okno statistik: " + e.getMessage());
        }
    }
}