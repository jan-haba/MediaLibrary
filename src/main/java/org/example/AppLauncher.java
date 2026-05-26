package org.example;
/**
 * Launcher class for the entire application.
 * This class serves as a workaround for launching the JavaFX application.
 * It prevents runtime issues when compiling the project into an executable JAR file.
 */
public class AppLauncher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
