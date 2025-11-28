package com.ticketsystem.utils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenManager {

    private static Stage window;
    private static Scene scene;   // 👈 UNA sola escena global

    public static void setStage(Stage stage) {
        window = stage;

        // Crear una escena vacía al iniciar
        scene = new Scene(new Parent(){}, 1280, 720);

        window.setScene(scene);
        window.setResizable(true);
        window.setMaximized(true);
    }

    public static void show(Parent root) {

        if (root == null) {
            System.out.println("⚠ Error: root es null en ScreenManager.show()");
            return;
        }

        // 👈 Aquí solo actualizamos el contenido
        scene.setRoot(root);

        if (!window.isMaximized()) {
            window.setMaximized(true);
        }

        window.centerOnScreen();
    }

    public static Stage getStage() {
        return window;
    }
}
