# <center>🎬 Media Library </center>

<center>Control your media</center>

Smart Media Library is a responsive desktop simulation application built in Java 25 (JavaFX) where users run their own personalized cultural database. Through a clean, dark-themed user interface, users manage books, movies, series, and music, all while the underlying code actively demonstrates core Object-Oriented Programming principles and advanced cloud AI integrations.

[![Java Version](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
---

## ✨ Key Features & Mechanics

| Feature | Description |
| :--- | :--- |
| **🖼️ Media Poster Grid** | Modern card-based user interface that automatically scrolls and dynamically wraps catalog tiles depending on window size changes. |
| **🤖 Smart AI Search** | Type any messy title (e.g. `harry potter 1`) and let Google Gemini Pro clean it into the exact, official English asset name. |
| **🌐 Cloud Metadata Lookup** | Automatically fetches high-quality artwork URLs, plot descriptions, directors, authors, and production years from global web repositories. |
| **📊 Analytics Dashboard** | Monitor your library's real-time stats (PieChart for type distribution, BarChart for top 6 genres) and absolute page/watch time tracking data counters. |
| **❤️ Custom Grading** | Rate entries using 1–5 stars, toggle favorite flags, and view the distinctive `❤ FAV` visual tag overlayed right on your main grid covers. |
| **🗑️ Asset Deletion** | Cleanly remove items from your collection via a dedicated dynamic layout workflow that updates your persistent offline database in real-time. |

---

### 🚀 Adding New Media (Smart AI / API Assistant)
The application features an automated system that fetches media metadata and cover art directly from the internet. When adding a new item, the user can proceed in two ways:

1. **Smart (Automated) Mode:**
   * The user selects the media type (e.g., MOVIE) and types a **keyword or title** into the search field (e.g., *Inception* or *Harry Potter*).
   * The system automatically queries an external API to retrieve the official poster URL, correct genre, release year, and a brief description.

2. **Fallback (Manual) Mode (Offline/Error Handling):**
   * **What happens if the internet goes down or the API fails?** The application is built with defensive programming principles and will not crash.
   * If the automated search yields no results (due to a typo or being offline), the user can **manually enter the exact title and details** into the form.
   * If a custom poster image cannot be found, the application automatically applies a beautiful, neutral fallback image from Unsplash. This ensures the visual grid remains consistent and the application stays fully functional offline.
---
## ⚙️ Installation & Setup

### Prerequisites
* **Java Development Kit (JDK) 25** or higher.
* **Maven** package tool installed.
* An IDE supporting modern Java syntax configurations (e.g., IntelliJ IDEA, Eclipse, or VS Code).

### Execution Steps
1. Clone this repository to your local machine:
   ```bash
   git clone [https://github.com/jan-haba/medialibrary](https://github.com/jan-haba/medialibrary)
2. Open the directory module inside your preferred Java IDE.

3. Locate the AppLauncher.java file in your source folder hierarchy.

4. Run the main method entry block point to load the database cache and launch the primary application frame interface.
