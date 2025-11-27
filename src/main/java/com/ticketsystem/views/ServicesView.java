package com.ticketsystem.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ServicesView {

    public static Parent getView() {
        try {
            return FXMLLoader.load(
                ServicesView.class.getResource("/com/ticketsystem/views/services.fxml")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
