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
                if (m instanceof Film) {
                    Film f = (Film) m;
                    writer.println("FILM;" + f.getId() + ";" + f.getTitle() + ";" + f.getGenre() + ";" +
                            f.getYear() + ";" + f.getImageUrl() + ";" + f.getDescription() + ";" +
                            f.getDuration() + ";" + f.getDirector() + ";" + f.getMainStudio());
                }
                else if (m instanceof Book) {
                    Book b = (Book) m;
                    writer.println("BOOK;" + b.getId() + ";" + b.getTitle() + ";" + b.getGenre() + ";" +
                            b.getYear() + ";" + b.getImageUrl() + ";" + b.getDescription() + ";" +
                            b.getAuthor() + ";" + b.getPageCount() + ";" + b.getPublisher() + ";" + b.getIsbn());
                }
                else if (m instanceof Music) {
                    Music mu = (Music) m;
                    // Formát: MUSIC;id;title;genre;year;imageUrl;description;artist;recordLabel;totalTracks;durationSeconds;releaseType
                    writer.println("MUSIC;" + mu.getId() + ";" + mu.getTitle() + ";" + mu.getGenre() + ";" +
                            mu.getYear() + ";" + mu.getImageUrl() + ";" + mu.getDescription() + ";" +
                            mu.getArtist() + ";" + mu.getRecordLabel() + ";" + mu.getTotalTracks() + ";" +
                            mu.getDurationSeconds() + ";" + mu.getReleaseType());
                }
                else if (m instanceof Serial) {
                    Serial s = (Serial) m;
                    writer.println("SERIAL;" + s.getId() + ";" + s.getTitle() + ";" + s.getGenre() + ";" +
                            s.getYear() + ";" + s.getImageUrl() + ";" + s.getDescription() + ";" +
                            s.getCreator() + ";" + s.getTotalSeasons() + ";" + s.getTotalEpisodes() + ";" + s.getStatus());
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
                String type = parts[0];

                int id = Integer.parseInt(parts[1]);
                String title = parts[2];
                String genre = parts[3];

                switch (type) {
                    case "FILM":
                        int filmYear = Integer.parseInt(parts[4]);
                        String filmImageUrl = parts[5];
                        String filmDescription = parts[6];

                        int duration = Integer.parseInt(parts[7]);
                        String director = parts[8];
                        String mainStudio = parts[9];

                        mediaList.add(new Film(id, title, genre, filmYear, filmImageUrl, filmDescription, duration, director, mainStudio));
                        break;
                    case "BOOK":
                        int bookYear = Integer.parseInt(parts[4]);
                        String bookImageUrl = parts[5];
                        String bookDescription = parts[6];

                        String author = parts[7];
                        int pageCount = Integer.parseInt(parts[8]);
                        String publisher = parts[9];
                        String isbn = parts[10];

                        mediaList.add(new Book(id, title, genre, bookYear, bookImageUrl, bookDescription, author, pageCount, publisher, isbn));
                        break;

                    case "MUSIC":
                        // Společné věci z Media (indexy 4, 5, 6)
                        int musicYear = Integer.parseInt(parts[4]);
                        String musicImageUrl = parts[5];
                        String musicDescription = parts[6];

                        // Specifické věci pro Music (indexy 7, 8, 9, 10, 11)
                        String artist = parts[7];
                        String recordLabel = parts[8];
                        int totalTracks = Integer.parseInt(parts[9]);
                        int durationSeconds = Integer.parseInt(parts[10]);
                        MusicType releaseType = MusicType.valueOf(parts[11]); // Převede uložený text zpět na tvůj Enum

                        // Zavoláme přesně tvůj konstruktor pro Music
                        mediaList.add(new Music(id, title, genre, musicYear, musicImageUrl, musicDescription, artist, recordLabel, totalTracks, durationSeconds, releaseType));
                        break;
                    case "SERIAL":
                        // Společné věci z Media (indexy 4, 5, 6)
                        int serialYear = Integer.parseInt(parts[4]);
                        String serialImageUrl = parts[5];
                        String serialDescription = parts[6];

                        // Specifické věci pro Serial (indexy 7, 8, 9, 10)
                        String creator = parts[7];
                        int totalSeasons = Integer.parseInt(parts[8]);
                        int totalEpisodes = Integer.parseInt(parts[9]);
                        String status = parts[10];

                        // Zavoláme přesně tvůj konstruktor pro Serial
                        mediaList.add(new Serial(id, title, genre, serialYear, serialImageUrl, serialDescription, creator, totalSeasons, totalEpisodes, status));
                        break;
                }
            }
            System.out.println("-> Data successfully loaded from file (" + mediaList.size() + " items).");
        } catch (Exception e) {
            System.out.println("Error loading from file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}