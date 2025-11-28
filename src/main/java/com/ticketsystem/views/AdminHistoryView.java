package com.ticketsystem.views;

import com.ticketsystem.model.Incidencia;
import com.ticketsystem.services.IncidenciaService;
import com.ticketsystem.utils.ScreenManager;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AdminHistoryView {

    /***
     *  STATIC VIEW
     */
    public static BorderPane getView() {

        BorderPane root = new BorderPane();
        root.getStylesheets().add(AdminHistoryView.class
                .getResource("/css/adminhistory.css").toExternalForm());

        /* ---------------- LEFT SIDEBAR ---------------- */
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        Label lblHistorial = new Label("Historial");
        lblHistorial.getStyleClass().add("sidebar-title");

        Button btnAsignar = new Button("Asignar técnicos");
        btnAsignar.getStyleClass().add("sidebar-btn");

        Button btnCambiar = new Button("Cambiar estados");
        btnCambiar.getStyleClass().add("sidebar-btn");

        sidebar.getChildren().addAll(lblHistorial, btnAsignar, btnCambiar);


        /* ---------------- TOP HEADER ---------------- */
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setSpacing(20);

        ImageView logo = new ImageView(AdminHistoryView.class.getResource("/images/logo.png").toExternalForm());
        logo.setFitWidth(150);
        logo.setPreserveRatio(true);

        ImageView logoutIcon = new ImageView(
                new Image(MenuView.class.getResource("/images/logout.png").toExternalForm())
        );
        logoutIcon.setFitWidth(30);
        logoutIcon.setPreserveRatio(true);

        Button logoutBtn = new Button();
        logoutBtn.setGraphic(logoutIcon);
        logoutBtn.getStyleClass().add("logout-btn");

        logoutBtn.setOnAction(e -> ScreenManager.show(new LoginView()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(logo, spacer, logoutBtn);


        /* ---------------- TABS ---------------- */
        HBox tabs = new HBox(10);
        tabs.setPadding(new Insets(0, 0, 20, 0));

        tabs.getChildren().addAll(
                makeTab("Todos", true),
                makeTab("En espera", false),
                makeTab("Abierto", false),
                makeTab("En Proceso", false),
                makeTab("Resuelto", false)
        );


        /* ---------------- TABLE ---------------- */
        TableView<Incidencia> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Incidencia, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()));

        TableColumn<Incidencia, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));

        TableColumn<Incidencia, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));

        TableColumn<Incidencia, String> colDescripcion = new TableColumn<>("Detalles");
        colDescripcion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcion()));

        TableColumn<Incidencia, String> colAcciones = new TableColumn<>("");
        colAcciones.setCellFactory(tc -> new TableCell<>() {
            final Button btn = new Button("Ver");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                btn.setOnAction(e -> {
                    Incidencia inc = getTableView().getItems().get(getIndex());
                    System.out.println("Clic en ID: " + inc.getId());
                });

                setGraphic(btn);
            }
        });

        table.getColumns().addAll(colId, colEstado, colTipo, colDescripcion, colAcciones);


        /* ---------------- LOAD DATA ---------------- */
        table.setItems(IncidenciaService.getAll());


        /* ---------------- CENTER CARD ---------------- */
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.getChildren().addAll(tabs, table);

        root.setLeft(sidebar);
        root.setTop(header);
        root.setCenter(card);

        return root;
    }


    /* ---------------- HELPERS ---------------- */

    private static Button makeTab(String text, boolean selected) {
        Button b = new Button(text);
        b.getStyleClass().add("nav-tab");
        if (selected) b.getStyleClass().add("nav-tab-selected");
        return b;
    }
}
