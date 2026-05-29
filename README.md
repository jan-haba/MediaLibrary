# 🎬 Smart Media Library (JavaFX)

A modern desktop application for personal multimedia library management built using **JavaFX** and compiled via **Maven**. This application enables users to cleanly organize, search, filter, and rate various types of media, ranging from movies and TV shows to books and music albums. It integrates advanced cloud capabilities, including **Gemini AI** for title text cleaning and optimization, and the **OMDb API** for rich metadata and artwork retrieval.

---

## ✨ Key Features

* **🖼️ Dynamic Grid Layout:** Text-based listings are replaced by a sleek tile-based layout using a `ScrollPane` and a `TilePane`. Media posters and covers load side-by-side, creating an immersive, Netflix-like digital catalog.
* **🤖 Smart AI Cleanup (Gemini Pro):** Users do not need to worry about formatting or typos when adding new items (e.g., inputting `hARRY POTTER 1`). The embedded **Gemini 2.5 Flash** model standardizes the query to its official, exact title.
* **🌐 Automated Cloud Metadata Import:** Following AI stabilization, the backend queries global databases (such as OMDb). It automatically fetches high-quality poster URLs, release years, genres, descriptions, directors, authors, or runtimes.
* **⚡ Asynchronous Image Background Loading:** Media images stream in smoothly via daemon-backed threads. Populating dozens of media elements never blocks or freezes the application UI.
* **📱 Fully Responsive Design:** Both the primary catalog and detail view windows dynamically adapt when resized. Tiles realign seamlessly to fill out your screen real estate upon window maximization.
* **📊 Analytics Dashboard:** Real-time data statistics are visualized using rich JavaFX charting controls:
    * *PieChart:* Displays percentage allocation across media categories (Books, Movies, Series, Music).
    * *BarChart:* Visualizes the library's top 6 most prevalent genres.
    * *Metric Cards:* Highlights overall asset aggregates, total compiled book pages read, and accumulated movie watch times.
* **❤️ Custom Rating & Favorites:** Users can grade their content using 1–5 star ratings and toggle a favorite flag. Favorited items display a vibrant, overlayed `❤ FAV` label right over their cover card in the catalog grid.
* **💾 Automatic Local Storage Sync:** Database adjustments, newly saved items, modified ratings, and favorite flags are instantly written back to a flat-file database schema (`data.txt`). No database setup is required, ensuring complete offline data persistence.

---

## 🛠️ Tech Stack & Dependencies

* **Java 25** (OpenJDK)
* **JavaFX 23.0.1** (Modules: `javafx-controls`, `javafx-fxml`)
* **Maven** (Project compilation, package lifecycle management, and dependency tracking)
* **JSON.org (20240303)** (Cloud API response serialization and mapping parsing)
* **Google Gemini API** (AI-driven title string normalization)
* **OMDb API** (Global cinematic registries search engine metadata synchronization)

---

