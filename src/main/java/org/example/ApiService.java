package org.example;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Network and API service integration layer handling external REST communication.
 * <p>
 * This utility provider handles secure outbound HTTP communication pipelines targeting
 * remote cloud infrastructures. It encapsulates token parameters, maps request raw payloads,
 * interfaces with the Google Gemini Generative AI model for lookahead string preprocessing, and
 * wraps standard cinematic repositories to parse structural JSON formats into runtime memory maps.
 */
public class ApiService {

    private static final String OMDB_API_KEY = "";
    private static final String GEMINI_API_KEY = "";
    private static final String GOOGLE_BOOKS_API_KEY = "";

    /**
     * Sanitizes unstructured text inputs by processing them through Google's Gemini Generative AI model.
     * <p>
     * Connects via an outbound HTTP POST payload transmission containing specific formatting instructions (prompt engineering).
     * The model is instructed to remove typographical mistakes, ignore conversational metadata, and isolate exclusively
     * the canonical English title name of the matching cultural asset element.
     * </p>
     *
     * @param inputTitle the raw text query containing potential structural errors or slang formats
     * @return a sanitized, uniform string representing the official asset title name, or the trimmed raw input as a fallback
     */
    public static String cleanTitleWithGemini(String inputTitle) {
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isEmpty()) {
            System.out.println("ℹ️ Gemini Key missing or default placeholder left. Skipping AI cleanup.");
            return inputTitle.trim();
        }

        try {
            String urlString = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String prompt = "You are an expert media database assistant. Clean up typos and output ONLY the official, exact canonical English title of this movie, book, or show. " +
                    "Crucially, if the item has different British and American titles, ALWAYS output the official US release title (e.g., return 'Harry Potter and the Sorcerer's Stone' instead of 'Philosopher's Stone'). " +
                    "Do not include quotes, periods, explanation text, or greetings. Input text: " + inputTitle;

            String jsonPayload = "{\"contents\": [{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            if (status == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(content.toString());
                String aiTitle = jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text").trim();

                aiTitle = aiTitle.replace("\"", "");
                if (aiTitle.endsWith(".")) {
                    aiTitle = aiTitle.substring(0, aiTitle.length() - 1).trim();
                }

                System.out.println("🤖 Gemini AI Cleanup: '" + inputTitle + "' ➔ '" + aiTitle + "'");
                return aiTitle;
            } else {
                System.out.println("⚠️ Gemini API returned HTTP error code: " + status + ". Using raw title fallback.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ Gemini AI Connection failed: " + e.getMessage());
        }
        return inputTitle.trim();
    }

    /**
     * Queries the global OMDb registry to fetch exhaustive metadata for a specific movie or series title.
     * <p>
     * Encodes the string criteria symmetrically into UTF-8 formats and fires a standard HTTP GET lookup stream.
     * If the remote platform flags the lookup sequence execution status as valid, it deserializes the response
     * body mapping into a unified JSON schema map wrapper container.
     * </p>
     *
     * @param title the normalized exact title string criteria of the cinematic item being sought
     * @return a {@link JSONObject} mapping response attributes (Artwork URL, Plot, Year, Cast), or {@code null} if mapping fails
     */
    public static JSONObject fetchMovieOrSeries(String title, String mediaType) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);

            String urlString = "https://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&t=" + encodedTitle + "&type=" + mediaType;

            String response = makeHttpRequest(urlString);
            if (response != null) {
                JSONObject json = new JSONObject(response);
                if (json.has("Response") && json.getString("Response").equals("True")) {
                    return json;
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ OMDb API Request failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Queries the public Google Books API to fetch rich metadata for literary items.
     * <p>
     * Encodes the title parameter inside strict quotation brackets under an exclusive title-only
     * search constraint. Extracts critical parameters from the nested JSON response under the volumeInfo
     * layout wrapper (authors, categories, pageCount, description, and high-quality image thumbnails).
     * </p>
     *
     * @param title the standardized text title query parsed to discover matched publication items
     * @return a mapped {@link JSONObject} containing literary metadata attributes, or {@code null} if unavailable
     */
    /**
     * Queries the public Google Books API to fetch rich metadata for literary items.
     * Evaluates multiple results dynamically to filter out modern booklets and validates genres.
     *
     * @param title the standardized text title query (potentially including author text)
     * @return a mapped {@link JSONObject} containing literary metadata attributes, or {@code null} if unavailable
     */
    public static JSONObject fetchBook(String title) {
        try {
            String query = "";
            String cleanTitleOnly = title;
            String cleanAuthorOnly = "";

            if (title.contains(" by ")) {
                String[] parts = title.split(" by ");
                cleanTitleOnly = parts[0].trim();
                cleanAuthorOnly = parts[1].trim();
                query = "intitle:\"" + cleanTitleOnly + "\" inauthor:\"" + cleanAuthorOnly + "\"";
            } else {
                query = "intitle:\"" + title + "\"";
            }

            String encodedTitle = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + encodedTitle + "&maxResults=3&printType=books&key=" + GOOGLE_BOOKS_API_KEY;

            System.out.println("🌐 Sending stable URL request to Google Books (Smart Validation): " + urlString);

            String response = makeHttpRequest(urlString);
            if (response != null) {
                JSONObject json = new JSONObject(response);
                if (json.has("items") && !json.getJSONArray("items").isEmpty()) {
                    JSONArray items = json.getJSONArray("items");

                    JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject candidateInfo = items.getJSONObject(i).getJSONObject("volumeInfo");
                        int pages = candidateInfo.optInt("pageCount", 0);
                        String desc = candidateInfo.optString("description", "");

                        if (pages > 80 && desc.length() > 30) {
                            volumeInfo = candidateInfo;
                            break;
                        }
                    }

                    JSONObject parsedBook = new JSONObject();
                    String finalTitle = volumeInfo.optString("title", cleanTitleOnly);
                    parsedBook.put("title", finalTitle);
                    parsedBook.put("description", volumeInfo.optString("description", "No description available."));
                    parsedBook.put("pageCount", volumeInfo.optInt("pageCount", 250));
                    parsedBook.put("publisher", volumeInfo.optString("publisher", "Global Publisher"));
                    parsedBook.put("publishedDate", volumeInfo.optString("publishedDate", "2026"));

                    String detectedGenre = "Literature & Fiction";

                    if (volumeInfo.has("categories") && !volumeInfo.getJSONArray("categories").isEmpty()) {
                        String rawGenre = volumeInfo.getJSONArray("categories").getString(0);
                        String rawGenreLower = rawGenre.toLowerCase();
                        String titleLower = finalTitle.toLowerCase();
                        String authorLower = cleanAuthorOnly.toLowerCase();

                        if (rawGenreLower.contains(authorLower) && !authorLower.isEmpty() ||
                                titleLower.contains(rawGenreLower) ||
                                rawGenreLower.contains("potter") ||
                                rawGenreLower.contains("tolkien") ||
                                rawGenreLower.contains("frodo") ||
                                rawGenreLower.contains("character")) {

                            detectedGenre = "Literature & Fiction";
                        } else {
                            detectedGenre = rawGenre;
                        }
                    }

                    JSONArray genreArray = new JSONArray();
                    genreArray.put(detectedGenre);
                    parsedBook.put("categories", genreArray);

                    // Autoři
                    if (volumeInfo.has("authors")) {
                        parsedBook.put("authors", volumeInfo.getJSONArray("authors"));
                    } else if (!cleanAuthorOnly.isEmpty()) {
                        JSONArray fallbackAuthors = new JSONArray();
                        fallbackAuthors.put(cleanAuthorOnly);
                        parsedBook.put("authors", fallbackAuthors);
                    }

                    if (volumeInfo.has("industryIdentifiers")) {
                        parsedBook.put("industryIdentifiers", volumeInfo.getJSONArray("industryIdentifiers"));
                    }

                    if (volumeInfo.has("imageLinks")) {
                        String rawUrl = volumeInfo.getJSONObject("imageLinks").optString("thumbnail", "");
                        if (rawUrl.startsWith("http://")) {
                            rawUrl = rawUrl.replace("http://", "https://");
                        }
                        parsedBook.put("imageUrl", rawUrl);
                    } else {
                        parsedBook.put("imageUrl", "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=300");
                    }

                    return parsedBook;
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Google Books API Request failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Queries the public Apple iTunes Search API to retrieve structural metadata for main musical albums.
     * Enforces strict album validation matching constraints to prioritize the album title over the artist's name.
     * Dynamically verifies track counts and artist names to filter out singles, EPs, and unrelated band names.
     *
     * @param title the cleaned album title name query sequence parsed to discover matching musical assets
     * @return a mapped {@link JSONObject} containing music album fields, or {@code null} if unavailable
     */
    public static JSONObject fetchMusic(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);

            String urlString = "https://itunes.apple.com/search?term=" + encodedTitle + "&media=music&entity=album&attribute=albumTerm&limit=10";
            String response = makeHttpRequest(urlString);


            if (response == null || !new JSONObject(response).has("results") || new JSONObject(response).getJSONArray("results").isEmpty()) {
                System.out.println("ℹ️ Primary lookup yielded zero results. Retrying with focused album-term constraint...");
                urlString = "https://itunes.apple.com/search?term=" + encodedTitle + "&media=music&entity=album&attribute=albumTerm&limit=10";
                response = makeHttpRequest(urlString);
            }

            if (response != null) {
                JSONObject json = new JSONObject(response);
                if (json.has("results") && !json.getJSONArray("results").isEmpty()) {
                    JSONArray results = json.getJSONArray("results");

                    JSONObject rawMusic = null;
                    String lowerTitle = title.toLowerCase();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject potentialAlbum = results.getJSONObject(i);
                        String currentCollectionName = potentialAlbum.optString("collectionName", "").toLowerCase();
                        String currentArtistName = potentialAlbum.optString("artistName", "").toLowerCase();
                        int trackCount = potentialAlbum.optInt("trackCount", 0);

                        if (trackCount > 5 &&
                                !currentCollectionName.contains("remix") &&
                                !currentCollectionName.contains("- single") &&
                                !currentCollectionName.contains("b-sides") &&
                                !currentCollectionName.contains("- ep") &&
                                !currentCollectionName.contains("live at") &&
                                !currentCollectionName.contains("edition")) {


                            if (lowerTitle.contains(currentArtistName) || rawMusic == null) {
                                rawMusic = potentialAlbum;

                                if (lowerTitle.contains(currentArtistName)) {
                                    break;
                                }
                            }
                        }
                    }

                    if (rawMusic == null) {
                        rawMusic = results.getJSONObject(0);
                    }

                    JSONObject parsedMusic = new JSONObject();
                    String albumTitle = rawMusic.optString("collectionName", title);

                    if (albumTitle.toLowerCase().endsWith(" - album")) {
                        albumTitle = albumTitle.substring(0, albumTitle.length() - 8).trim();
                    }

                    parsedMusic.put("title", albumTitle);
                    parsedMusic.put("artist", rawMusic.optString("artistName", "Unknown Artist"));
                    parsedMusic.put("genre", rawMusic.optString("primaryGenreName", "Music"));
                    parsedMusic.put("publisher", rawMusic.optString("copyright", "Record Label"));

                    String releaseDate = rawMusic.optString("releaseDate", "2026");
                    parsedMusic.put("year", releaseDate.length() >= 4 ? Integer.parseInt(releaseDate.substring(0, 4)) : 2026);

                    parsedMusic.put("totalTracks", rawMusic.optInt("trackCount", 12));
                    parsedMusic.put("durationSeconds", 2700);
                    parsedMusic.put("releaseType", "ALBUM");
                    parsedMusic.put("description", "Studio Album release by " + parsedMusic.getString("artist"));

                    String artworkUrl = rawMusic.optString("artworkUrl100", "");
                    if (artworkUrl.contains("100x100bb.jpg")) {
                        artworkUrl = artworkUrl.replace("100x100bb.jpg", "600x600bb.jpg");
                    }
                    parsedMusic.put("imageUrl", artworkUrl);

                    return parsedMusic;
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ iTunes Music API Request failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * Executes synchronous outbound raw network connections and streams the fetched text content back.
     * <p>
     * Initializes fundamental connection connection timeout parameters, forces proper text stream encoding schemas,
     * inspects connection status responses, and accumulates sequential text reader columns utilizing a standard
     * buffered stream block arrangement.
     * </p>
     *
     * @param urlString the precise string representation mapping the outbound network target address path
     * @return a plain text representation containing raw structural payload responses, or {@code null} if streaming fails
     * @throws Exception if a physical low-level read/write transport or handshake exception condition presents itself
     */
    private static String makeHttpRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int status = conn.getResponseCode();
        if (status == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
            return content.toString();
        }
        conn.disconnect();
        return null;
    }

    /**
     * Queries the OMDb registry for a specific season of a television series to extract
     * the exact number of episodes broadcasted within that validation cycle.
     * <p>
     * Encodes the query parameters into uniform UTF-8 text, appends the specific target
     * season index array parameter, and parses the root "Episodes" structural JSON array
     * length to compute runtime metadata statistics dynamically.
     * </p>
     *
     * @param title  the normalized canonical title string of the television show to query
     * @param season the incremental integer index of the specific season being scanned
     * @return an integer indicating the total count of serialized episodes parsed inside
     * the target season matrix, or {@code 0} if transport handshake fails or metadata is missing
     */
    public static int fetchEpisodeCountForSeason(String title, int season) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String urlString = "https://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&t=" + encodedTitle + "&Season=" + season;

            String response = makeHttpRequest(urlString);
            if (response != null) {
                JSONObject json = new JSONObject(response);
                if (json.has("Episodes")) {
                    return json.getJSONArray("Episodes").length();
                }
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to load episodes for season " + season + ": " + e.getMessage());
        }
        return 0;
    }
}