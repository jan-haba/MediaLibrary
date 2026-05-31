package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import module.Book;
import module.Film;
import module.Media;
import module.Music;
import module.Serial;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class responsible for managing and visualizing library analytics.
 * This class handles the logic for the statistics window, processing the global media list
 * to compute quantitative summaries (such as total items, total book pages, and movie watch time)
 * and populating rich JavaFX chart controls (PieChart and BarChart) to visualize media types
 * and top genres in real-time.
 */
public class StatsController {


    @FXML
    private Label totalItemsLabel;
    @FXML
    private Label totalPagesLabel;

    @FXML
    private Label totalWatchTimeLabel;
    @FXML
    private PieChart typePieChart;
    @FXML
    private BarChart<String, Number> genreBarChart;
    @FXML
    private CategoryAxis xAxis;

    /**
     * Initializes the statistics controller automatically after its FXML root element has been loaded.
     * <p>
     * This method fetches the master media collection from {@link MediaLibrary}, iterates through it
     * using polymorphism to aggregate specific numeric metrics, populates information summary cards,
     * and compiles data sets required to render interactive analytics charts.
     * </p>
     */
    @FXML
    public void initialize() {
        System.out.println("📊 Generating library metrics and data visualization charts...");

        List<Media> mediaList = MediaLibrary.getMediaList();

        int totalItems = mediaList.size();
        int totalPages = 0;
        int totalWatchTime = 0;

        int bookCount = 0;
        int filmCount = 0;
        int serialCount = 0;
        int musicCount = 0;

        Map<String, Integer> genreMap = new HashMap<>();

        for (Media m : mediaList) {
            String genre = m.getGenre();
            if (genre == null || genre.isEmpty()) {
                genre = "Unknown";
            }
            genreMap.put(genre, genreMap.getOrDefault(genre, 0) + 1);

            if (m instanceof Book) {
                bookCount++;
                totalPages += ((Book) m).getPageCount();
            } else if (m instanceof Film) {
                filmCount++;
                totalWatchTime += ((Film) m).getDuration();
            } else if (m instanceof Serial) {
                serialCount++;
            } else if (m instanceof Music) {
                musicCount++;
            }
        }

        totalItemsLabel.setText(String.valueOf(totalItems));
        totalPagesLabel.setText(String.format("%,d pages", totalPages));
        totalWatchTimeLabel.setText(String.format("%,d min", totalWatchTime));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        if (bookCount > 0) pieChartData.add(new PieChart.Data("Books (" + bookCount + ")", bookCount));
        if (filmCount > 0) pieChartData.add(new PieChart.Data("Movies (" + filmCount + ")", filmCount));
        if (serialCount > 0) pieChartData.add(new PieChart.Data("TV Shows (" + serialCount + ")", serialCount));
        if (musicCount > 0) pieChartData.add(new PieChart.Data("Music (" + musicCount + ")", musicCount));

        typePieChart.setData(pieChartData);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        genreMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(6)
                .forEach(entry -> {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                });

        genreBarChart.getData().clear();
        genreBarChart.getData().add(series);
    }
}