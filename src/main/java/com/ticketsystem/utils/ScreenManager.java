package com.ticketsystem.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {

    private static Stage window;

    public static void setStage(Stage stage) {
        window = stage;

        // Configuración inicial del stage
        window.setResizable(true);
        window.setMaximized(true);     // Que arranque maximizado
    }

    public static void show(Parent root) {

        if (root == null) {
            System.out.println("⚠ Error: root es null en ScreenManager.show()");
            return;
        }

        Scene scene = new Scene(root, 1280, 720);
        window.setScene(scene);

        // Forzar que cada pantalla quede maximizada
        if (!window.isMaximized()) {
            window.setMaximized(true);
        }

        // Opcional: reposicionar en caso de glitch
        window.centerOnScreen();
    }

    public static Stage getStage() {
        return window;
    }
}
