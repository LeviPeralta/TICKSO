package com.ticketsystem;

import com.ticketsystem.dao.IncidenciasDAO;
import com.ticketsystem.database.SeedData;
import com.ticketsystem.utils.ScreenManager;
import com.ticketsystem.views.LoginView;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        // Inicializar datos de prueba
        SeedData.init();
        // Precargar incidencias de prueba
        IncidenciasDAO.precargarIncidencias();
        System.out.println("Incidencias precargadas correctamente.");

        // Registrar el Stage para poder cambiar pantallas
        ScreenManager.setStage(stage);

        // Mostrar la primera pantalla (Login)
        ScreenManager.show(new LoginView());

        // Configuración de la ventana
        stage.setTitle("Sistema de Tickets");
        stage.setResizable(true);
        stage.setMaximized(true);  // Ventana siempre maximizada
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
