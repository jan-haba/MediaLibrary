package module;

import java.time.LocalDateTime;

public class Module {
    private int id;
    private String title;
    private String genre;
    private int year;
    private int rating;
    private String imageUrl;
    private String description;
    private boolean favorite;
    private LocalDateTime dateAdded;

    public Module(int id, String title, String genre, int year, int rating, String imageUrl, String description) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.description = description;
        this.favorite = false;
        this.dateAdded = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
}
