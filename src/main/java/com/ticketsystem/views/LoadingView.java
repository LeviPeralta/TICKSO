package com.ticketsystem.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LoadingView {

    public static Parent getView() {

        // Barra de progreso vacía
        Rectangle barBg = new Rectangle(320, 10, Color.web("#E8E8E8"));
        barBg.setArcWidth(10);
        barBg.setArcHeight(10);

        // Barra que se llena
        Rectangle barFill = new Rectangle(0, 10, Color.web("#FD6B70"));
        barFill.setArcWidth(10);
        barFill.setArcHeight(10);

        StackPane barContainer = new StackPane(barBg, barFill);

        Label lbl = new Label("Cargando...");
        lbl.getStyleClass().add("loading-text");

        VBox root = new VBox(25, lbl, barContainer);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("loading-root");

        root.getStylesheets().add(
                LoadingView.class.getResource("/css/loading.css").toExternalForm()
        );

        // ANIMACIÓN de llenado de la barra
        Timeline tl = new Timeline(
                new KeyFrame(Duration.millis(25),
                        e -> {
                            if (barFill.getWidth() < 320) {
                                barFill.setWidth(barFill.getWidth() + 8);
                            }
                        })
        );
        
        tl.setCycleCount(100); 
        tl.play();

        return root;
    }
}
