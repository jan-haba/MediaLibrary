package module;

import java.time.LocalDateTime;

public class Music extends Module {
    private String artist;
    private String recordLabel;
    private int totalTracks;
    private int durationSeconds;
    private MusicType releaseType;

    public Music(int id, String title, String genre, int year, int rating, String imageUrl, String description, boolean favorite, LocalDateTime dateAdded, String artist, String recordLabel, int totalTracks, int durationSeconds, MusicType releaseType) {
        super(id, title, genre, year, rating, imageUrl, description);
        this.artist = artist;
        this.recordLabel = recordLabel;
        this.totalTracks = totalTracks;
        this.durationSeconds = durationSeconds;
        this.releaseType = releaseType;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(String recordLabel) {
        this.recordLabel = recordLabel;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public MusicType getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(MusicType releaseType) {
        this.releaseType = releaseType;
    }
}
