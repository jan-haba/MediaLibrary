package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import module.Media;
import module.Film;
import module.Book;
import module.Music;
import module.Serial;
import module.MusicType;

public class MediaLibrary {
    private static final List<Media> mediaList = new ArrayList<>();
    private static final String DATA_FILE = "data.txt";

    public static void addItem(Media newMedia) {
        mediaList.add(newMedia);
        saveToFile();
    }

    public static List<Media> getMediaList() {
        return mediaList;
    }

    public static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Media m : mediaList) {
                String commonData = m.getId() + ";" + m.getTitle() + ";" + m.getGenre() + ";" +
                        m.getYear() + ";" + m.getImageUrl() + ";" + m.getDescription();

                String userData = m.getRating() + ";" + m.isFavorite() + ";" + m.getDateAdded();

                if (m instanceof Film) {
                    Film f = (Film) m;
                    writer.println("FILM;" + commonData + ";" + f.getDuration() + ";" + f.getDirector() + ";" + f.getMainStudio() + ";" + userData);
                }
                else if (m instanceof Book) {
                    Book b = (Book) m;
                    writer.println("BOOK;" + commonData + ";" + b.getAuthor() + ";" + b.getPageCount() + ";" + b.getPublisher() + ";" + b.getIsbn() + ";" + userData);
                }
                else if (m instanceof Music) {
                    Music mu = (Music) m;
                    writer.println("MUSIC;" + commonData + ";" + mu.getArtist() + ";" + mu.getRecordLabel() + ";" + mu.getTotalTracks() + ";" + mu.getDurationSeconds() + ";" + mu.getReleaseType() + ";" + userData);
                }
                else if (m instanceof Serial) {
                    Serial s = (Serial) m;
                    writer.println("SERIAL;" + commonData + ";" + s.getCreator() + ";" + s.getTotalSeasons() + ";" + s.getTotalEpisodes() + ";" + s.getStatus() + ";" + userData);
                }
            }
            System.out.println("-> Data successfully saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    public static void loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Data file does not exist yet. Starting with an empty library.");
            return;
        }

        mediaList.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 7) continue;

                String type = parts[0];
                int id = Integer.parseInt(parts[1]);
                String title = parts[2];
                String genre = parts[3];
                int year = Integer.parseInt(parts[4]);
                String imageUrl = parts[5];
                String description = parts[6];

                Media loadedMedia = null;
                int lastSpecIndex = 0;

                switch (type) {
                    case "FILM":
                        int duration = Integer.parseInt(parts[7]);
                        String director = parts[8];
                        String mainStudio = parts[9];
                        loadedMedia = new Film(id, title, genre, year, imageUrl, description, duration, director, mainStudio);
                        lastSpecIndex = 9;
                        break;

                    case "BOOK":
                        String author = parts[7];
                        int pageCount = Integer.parseInt(parts[8]);
                        String publisher = parts[9];
                        String isbn = parts[10];
                        loadedMedia = new Book(id, title, genre, year, imageUrl, description, author, pageCount, publisher, isbn);
                        lastSpecIndex = 10;
                        break;

                    case "MUSIC":
                        String artist = parts[7];
                        String recordLabel = parts[8];
                        int totalTracks = Integer.parseInt(parts[9]);
                        int durationSeconds = Integer.parseInt(parts[10]);
                        MusicType releaseType = MusicType.valueOf(parts[11]);
                        loadedMedia = new Music(id, title, genre, year, imageUrl, description, artist, recordLabel, totalTracks, durationSeconds, releaseType);
                        lastSpecIndex = 11;
                        break;

                    case "SERIAL":
                        String creator = parts[7];
                        int totalSeasons = Integer.parseInt(parts[8]);
                        int totalEpisodes = Integer.parseInt(parts[9]);
                        String status = parts[10];
                        loadedMedia = new Serial(id, title, genre, year, imageUrl, description, creator, totalSeasons, totalEpisodes, status);
                        lastSpecIndex = 10;
                        break;
                }


                if (loadedMedia != null && parts.length > lastSpecIndex + 3) {
                    loadedMedia.setRating(Integer.parseInt(parts[lastSpecIndex + 1]));
                    loadedMedia.setFavorite(Boolean.parseBoolean(parts[lastSpecIndex + 2]));
                    loadedMedia.setDateAdded(java.time.LocalDateTime.parse(parts[lastSpecIndex + 3]));
                }

                if (loadedMedia != null) {
                    mediaList.add(loadedMedia);
                }
            }
            System.out.println("-> Data successfully loaded from file (" + mediaList.size() + " items).");
        } catch (Exception e) {
            System.out.println("Error loading from file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}