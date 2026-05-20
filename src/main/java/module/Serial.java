package module;

public class Serial extends Media {
    private String creator;
    private int totalSeasons;
    private int totalEpisodes;
    private String status;

    public Serial(int id, String title, String genre, int year, String imageUrl, String description, String creator, int totalSeasons, int totalEpisodes, String status) {
        super(id, title, genre, year, imageUrl, description);
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
