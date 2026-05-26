package module;

import java.time.LocalDateTime;

/**
 * The core base class representing a generic media item within the library.
 * <p>
 * This abstract-style parent class encapsulates shared data fields common to all media
 * types, such as titles, genres, publication dates, images, summaries, user ratings,
 * and tracking metadata like favorite flags and creation timestamps.
 * </p>
 */
public class Media {
    private int id;
    private String title;
    private String genre;
    private int year;
    private int rating;
    private String imageUrl;
    private String description;
    private boolean favorite;
    private LocalDateTime dateAdded;

    public Media(int id, String title, String genre, int year, String imageUrl, String description) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.imageUrl = imageUrl;
        this.description = description;
        this.favorite = false;
        this.dateAdded = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", favorite=" + favorite +
                ", dateAdded=" + dateAdded +
                '}';
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
