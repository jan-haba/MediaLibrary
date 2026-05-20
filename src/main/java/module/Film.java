package module;

import java.time.LocalDateTime;

public class Film extends Module {
    private String director;
    private int duration;
    private String mainStudio;

    public Film(int id, String title, String genre, int year, int rating, String imageUrl, String description, boolean favorite, LocalDateTime dateAdded, String director, int duration, String mainStudio) {
        super(id, title, genre, year, rating, imageUrl, description);
        this.director = director;
        this.duration = duration;
        this.mainStudio = mainStudio;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMainStudio() {
        return mainStudio;
    }

    public void setMainStudio(String mainStudio) {
        this.mainStudio = mainStudio;
    }
}
