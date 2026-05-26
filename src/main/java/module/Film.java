package module;

/**
 * Represents a feature film media item within the library database.
 * <p>
 * This class extends the core Media parent structure by appending specialized attributes
 * unique to movies, including trackable directors, runtime duration tracking in minutes,
 * and the primary production studio responsible for the distribution.
 * </p>
 */
public class Film extends Media {
    private String director;
    private int duration;
    private String mainStudio;

    public Film(int id, String title, String genre, int year, String imageUrl, String description, int duration, String director, String mainStudio) {
        super(id, title, genre, year, imageUrl, description);
        this.duration = duration;
        this.director = director;
        this.mainStudio = mainStudio;
    }

    @Override
    public String toString() {
        return "Film{" +
                super.toString() +
                "director='" + director + '\'' +
                ", duration=" + duration +
                ", mainStudio='" + mainStudio + '\'' +
                '}';
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
