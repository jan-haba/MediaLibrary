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

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TilePane mediaTilePane;

    @FXML
    public void initialize() {
        System.out.println("🖼️ Inicializace hlavního menu s dlaždicemi plakátů...");

        mediaTilePane.prefWidthProperty().bind(scrollPane.widthProperty().subtract(25));

        refreshGrid(MediaLibrary.getMediaList());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMedia(newValue);
        });
    }

    private void refreshGrid(List<Media> items) {
        mediaTilePane.getChildren().clear();

        for (Media item : items) {
            VBox card = new VBox();
            card.setSpacing(5);
            card.setStyle("-fx-alignment: center; -fx-cursor: hand;");

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

            String shortTitle = item.getTitle();
            if (shortTitle.length() > 18) {
                shortTitle = shortTitle.substring(0, 15) + "...";
            }

            Label titleLabel = new Label(shortTitle);
            titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 11px;");

            if (item.isFavorite()) {
                titleLabel.setText("❤️ " + shortTitle);
                titleLabel.setStyle("-fx-text-fill: #ff4757; -fx-font-weight: bold; -fx-font-size: 11px;");
            }

            card.getChildren().addAll(imageView, titleLabel);
            card.setOnMouseClicked(event -> showDetailWindow(item));

            mediaTilePane.getChildren().add(card);
        }
    }

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

            // Zobrazení okna
            Stage stage = new Stage();
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