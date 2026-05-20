package module;

import java.time.LocalDateTime;

public class Serial extends Module {
    private String creator;
    private int totalSeasons;
    private int totalEpisodes;
    private String status;

    public Serial(int id, String title, String genre, int year, int rating, String imageUrl, String description, boolean favorite, LocalDateTime dateAdded, String creator, int totalSeasons, int totalEpisodes, String status) {
        super(id, title, genre, year, rating, imageUrl, description);
        this.creator = creator;
        this.totalSeasons = totalSeasons;
        this.totalEpisodes = totalEpisodes;
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(int totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public int getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(int totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
