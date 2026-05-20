import module.*;

import java.util.ArrayList;
import java.util.List;

public class MediaLibrary {
    private ArrayList<Media> items;
    private int nextID;

    public MediaLibrary() {
        this.items = new ArrayList<>();
        this.nextID = 1;
    }

    public void addFilm(String title, String genre, int year, String imageUrl, String description, String director, int duration, String mainStudio){
        Film film = new Film(nextID++, title, genre, year, imageUrl, description, duration, director,mainStudio);
        items.add(film);
    }

    public void addSerial(String title, String genre, int year, String imageUrl, String description, String language, int seasons, int episodes, String status){
        Serial serial = new Serial(nextID++, title, genre, year, imageUrl, description, language, seasons, episodes, status);
        items.add(serial);
    }

    public void addMusic(String title, String genre, int year, String imageUrl, String description, String artist, String recordLabel, int totalTracks, int durationSeconds, MusicType releaseType) {
        Music newMusic = new Music(nextID++, title, genre, year, imageUrl, description, artist, recordLabel, totalTracks, durationSeconds, releaseType);
        items.add(newMusic);
    }

    public void addBook(String title, String genre, int year, String imageUrl, String description, String author, int pageCount, String publisher, String isbn) {
        Book newBook = new Book(nextID++, title, genre, year, imageUrl, description, author, pageCount, publisher, isbn);
        items.add(newBook);
    }

    public ArrayList<Media> getItems() {
        return items;
    }
    public void rateItem(int id, int rating) {
        for (Media item : items) {
            if (item.getId() == id) {
                item.setRating(rating);
                break;
            }
        }
    }

    public void setFavoriteItem(int id, boolean isFavorite) {
        for (Media item : items) {
            if (item.getId() == id) {
                item.setFavorite(isFavorite);
                break;
            }
        }
    }

    public List<Media> search(String query) {
        List<Media> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Media item : items) {
            if (item.getTitle().toLowerCase().contains(lowerQuery) ||
                    item.getGenre().toLowerCase().contains(lowerQuery) ||
                    item.getDescription().toLowerCase().contains(lowerQuery)) {
                results.add(item);
                continue;
            }

            if (item instanceof Book && ((Book) item).getAuthor().toLowerCase().contains(lowerQuery)) {
                results.add(item);
            } else if (item instanceof Music && ((Music) item).getArtist().toLowerCase().contains(lowerQuery)) {
                results.add(item);
            } else if (item instanceof Film && ((Film) item).getDirector().toLowerCase().contains(lowerQuery)) {
                results.add(item);
            }
        }
        return results;
    }
}
