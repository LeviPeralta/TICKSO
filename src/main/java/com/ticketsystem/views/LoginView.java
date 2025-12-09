package com.ticketsystem.views;

import com.ticketsystem.dao.UserDAO;
import com.ticketsystem.model.User;
import com.ticketsystem.utils.ScreenManager;
import com.ticketsystem.utils.Session;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LoginView extends StackPane {

    public LoginView() {

        // === CARGAR CSS DEL LOGIN ===
        this.getStylesheets().add(
            getClass().getResource("/css/login.css").toExternalForm()
        );

        // === FONDO ===
        this.setStyle(
            "-fx-background-image: url('/images/bg_login.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center;"
        );

        // === TARJETA ===
        VBox card = new VBox(15);
        card.getStyleClass().add("card");

        Label title = new Label("¡Bienvenido!");
        title.getStyleClass().add("title");

        Label subtitle = new Label("Ingresa tus datos personales para usar todas las funciones del sitio");
        subtitle.getStyleClass().add("subtitle");

        Label iniciar = new Label("Iniciar sesión");
        iniciar.getStyleClass().add("section-title");

        TextField usuario = new TextField();
        usuario.setPromptText("Usuario");
        usuario.getStyleClass().add("input");

        PasswordField password = new PasswordField();
        password.setPromptText("Contraseña");
        password.getStyleClass().add("input");

        // === MENSAJE DE ERROR ===
        Label error = new Label("");
        error.setTextFill(Color.RED);
        error.getStyleClass().add("error-text");
        error.setVisible(false);

        Hyperlink recuperar = new Hyperlink("¿Olvidaste tu contraseña?");
        recuperar.getStyleClass().add("link");

        Button loginBtn = new Button("Iniciar Sesión");
        loginBtn.getStyleClass().add("login-btn");

        // === ACCIÓN DE LOGIN ===
        loginBtn.setOnAction(e -> {

            String user = usuario.getText().trim();
            String pass = password.getText().trim();

            // Login correcto → regresa User
            User loggedUser = UserDAO.login(user, pass);

            if (loggedUser != null) {

                error.setVisible(false);

                // Guardar el usuario en sesión 👇
                Session.setCurrentUser(loggedUser);

                // Mostrar pantalla de carga
                ScreenManager.show(LoadingView.getView());

                Timeline wait = new Timeline(new KeyFrame(Duration.seconds(2), ev -> {

                    String rol = loggedUser.getRol().toLowerCase();

                    switch (rol) {
                        case "admin":
                            ScreenManager.show(AdminHistoryView.getView());
                            break;

                        case "tecnico":
                            // Redirige a la vista correcta para técnicos
                            ScreenManager.show(new TicketStatusView().getView());
                            break;

                        case "usuario":
                            ScreenManager.show(MenuView.getView());
                            break;

                        default:
                            System.out.println("Rol desconocido: " + rol);
                            ScreenManager.show(MenuView.getView());
                            break;
                    }

                }));
                wait.setCycleCount(1);
                wait.play();

            } else {
                error.setText("Usuario o contraseña incorrectos");
                error.setVisible(true);

                Timeline hideError = new Timeline(
                        new KeyFrame(Duration.seconds(2), ev -> error.setVisible(false))
                );
                hideError.play();
            }
        });

        card.getChildren().addAll(
                title, subtitle, iniciar, usuario, password,
                error, recuperar, loginBtn
        );

        card.setAlignment(Pos.CENTER);
        this.getChildren().add(card);
    }
}
