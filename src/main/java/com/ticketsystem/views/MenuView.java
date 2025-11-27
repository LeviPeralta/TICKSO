package com.ticketsystem.views;

import com.ticketsystem.utils.ScreenManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuView {

    public static Parent getView() {

        // === LOGO ===
        ImageView logo = new ImageView(
                new Image(MenuView.class.getResource("/images/logo.png").toExternalForm())
        );
        logo.setFitWidth(150);
        logo.setPreserveRatio(true);

        // === BOTÓN SALIR ===
        ImageView logoutIcon = new ImageView(
                new Image(MenuView.class.getResource("/images/logout.png").toExternalForm())
        );
        logoutIcon.setFitWidth(30);
        logoutIcon.setPreserveRatio(true);

        Button logoutBtn = new Button();
        logoutBtn.setGraphic(logoutIcon);
        logoutBtn.getStyleClass().add("logout-btn");

        logoutBtn.setOnAction(e -> {
            ScreenManager.show(new LoginView());
        });

        // === BARRA SUPERIOR ===
        BorderPane topBar = new BorderPane();
        topBar.setLeft(logo);
        topBar.setRight(logoutBtn);
        topBar.setPadding(new Insets(20, 30, 10, 30));

        // === TÍTULO ===
        Label title = new Label("Servicios");
        title.getStyleClass().add("menu-title");

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);

        // === TARJETAS ===
        StackPane c1 = createCard("/images/report.png", "Reportar\nIncidencias");
        StackPane c2 = createCard("/images/check.png", "Consultar\nestado de los\nTickets");

        // === ACCIÓN DE LA CARD REPORT ===
        c1.setOnMouseClicked(e -> {
            ScreenManager.show(ReportView.getView());
        });

        HBox cards = new HBox(90, c1, c2);
        cards.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, topBar, titleBox, cards);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(25));
        root.getStyleClass().add("menu-root");

        // === CSS ===
        root.getStylesheets().add(
                MenuView.class.getResource("/css/menu.css").toExternalForm()
        );

        return root;
    }

    private static StackPane createCard(String iconPath, String text) {

        ImageView icon = new ImageView(
                new Image(MenuView.class.getResource(iconPath).toExternalForm())
        );
        icon.setFitWidth(70);
        icon.setPreserveRatio(true);

        Label label = new Label(text);
        label.getStyleClass().add("menu-card-text");

        VBox box = new VBox(10, icon, label);
        box.setAlignment(Pos.CENTER);

        StackPane card = new StackPane(box);
        card.getStyleClass().add("menu-card");

        return card;
    }
}
