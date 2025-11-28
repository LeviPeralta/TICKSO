package com.ticketsystem.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ConfirmDialog {

    public static void show(Parent root, String mensaje, Runnable onAccept) {

        // Buscar un StackPane en el árbol de nodos (recursivo)
        StackPane stackRoot = findStackPaneInTree(root);

        // Determinar objetivo del blur (si encontramos StackPane lo usamos, sino usamos el parent recibido)
        Parent blurTarget = (stackRoot != null) ? stackRoot : root;

        // Aplicar blur
        BoxBlur blur = new BoxBlur(10, 10, 3);
        blurTarget.setEffect(blur);

        // Crear diálogo modal (Stage)
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(getWindowFromParent(root));
        dialog.setResizable(false);

        Label lbl = new Label(mensaje);
        lbl.setStyle("-fx-font-size: 18px; -fx-text-fill: #333;");

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #d9d9d9; -fx-background-radius: 8; -fx-font-size: 14px;");

        Button btnAceptar = new Button("Aceptar");
        btnAceptar.setStyle("-fx-background-color: #344668; -fx-text-fill: white; -fx-background-radius: 8; -fx-font-size: 14px;");

        btnCancelar.setOnAction(e -> {
            blurTarget.setEffect(null);
            dialog.close();
        });

        btnAceptar.setOnAction(e -> {
            blurTarget.setEffect(null);
            dialog.close();
            try {
                if (onAccept != null) onAccept.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox box = new VBox(20, lbl, btnCancelar, btnAceptar);
        box.setAlignment(Pos.CENTER);
        box.setStyle(
            "-fx-background-color: white; -fx-padding: 40; -fx-background-radius: 18;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 18, 0, 0, 6);"
        );

        Scene s = new Scene(new StackPane(box), 380, 220);
        dialog.setScene(s);
        dialog.showAndWait();
    }

    // Busca recursivamente un StackPane entre root y sus hijos.
    private static StackPane findStackPaneInTree(Parent root) {
        if (root == null) return null;
        if (root instanceof StackPane) {
            return (StackPane) root;
        }

        for (Node child : root.getChildrenUnmodifiable()) {
            if (child instanceof Parent) {
                StackPane found = findStackPaneInTree((Parent) child);
                if (found != null) return found;
            }
        }
        return null;
    }

    // Obtener ventana propietaria del Parent (si es posible)
    private static Window getWindowFromParent(Parent p) {
        if (p == null || p.getScene() == null) return null;
        return p.getScene().getWindow();
    }
}
