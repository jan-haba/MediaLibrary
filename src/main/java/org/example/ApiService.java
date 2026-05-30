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


    private static final String OMDB_API_KEY = "d94efde7";


    private static final String GEMINI_API_KEY = "AIzaSyD61-BVLy0YVPoDSRCGmY7Q7Swpogjc9HY";

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

            String prompt = "You are a media database assistant. Clean up typos and output ONLY the official, exact English title of this movie, book, or show. Do not include quotes, periods, explanation text, or greetings. Input text: " + inputTitle;
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
                aiTitle = aiTitle.replace("\"", "").replace("'", "");
                if (aiTitle.endsWith(".")) {
                    aiTitle = aiTitle.substring(0, aiTitle.length() - 1).trim();
                }

                System.out.println("🤖 Gemini AI Cleanup: '" + inputTitle + "' ➔ '" + aiTitle + "'");
                return aiTitle;
            } else {
                System.out.println("⚠️ Gemini API returned HTTP error code: " + status);
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
    public static JSONObject fetchMovieOrSeries(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String urlString = "https://www.omdbapi.com/?apikey=" + OMDB_API_KEY + "&t=" + encodedTitle;

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
     * Queries a remote literary cloud repository database to harvest structural metadata for book entries.
     * <p>
     * <i>Note: This method is currently structured as a placeholder stub to be fully wired up with external
     * target configurations in subsequent lifecycle iterations.</i>
     * </p>
     *
     * @param title the standardized text title query parsed to discover matched publication items
     * @return a mapped {@link JSONObject} containing literary metadata attributes, or {@code null} if unavailable
     */
    public static JSONObject fetchBook(String title) {
        return null;
    }

    /**
     * Queries a global digital catalog index database to retrieve metadata records for music releases.
     * <p>
     * <i>Note: This method is currently structured as a placeholder stub to be fully wired up with external
     * target configurations in subsequent lifecycle iterations.</i>
     * </p>
     *
     * @param title the cleaned title name query sequence parsed to discover matching musical assets
     * @return a mapped {@link JSONObject} containing music album and recording fields, or {@code null} if unavailable
     */
    public static JSONObject fetchMusic(String title) {
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
}