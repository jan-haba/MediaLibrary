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

public class ApiService {

    private static final String OMDB_API_KEY = "d94efde7";
    private static final String GEMINI_API_KEY = "AIzaSyD61-BVLy0YVPoDSRCGmY7Q7Swpogjc9HY";


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





    public static JSONObject fetchBook(String title) {
        return null;
    }


    public static JSONObject fetchMusic(String title) {
        return null;
    }

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