package com.ticketsystem.views;

import com.ticketsystem.components.ConfirmDialog;
import com.ticketsystem.dao.IncidenciasDAO;
import com.ticketsystem.model.Incidencia;
import com.ticketsystem.utils.ScreenManager;

import javafx.geometry.Insets;
import javafx.scene.Parent;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AdminHistoryView {

    public static StackPane getView() {

        // Contenido principal (lo que era antes tu BorderPane "root")
        BorderPane content = new BorderPane();
        content.getStylesheets().add(AdminHistoryView.class.getResource("/css/adminhistory.css").toExternalForm());

        /* ---------------- LEFT SIDEBAR ---------------- */
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        Label lblHistorial = new Label("Historial");
        lblHistorial.getStyleClass().add("sidebar-title");

        Button btnAsignar = new Button("Asignar técnicos");
        btnAsignar.getStyleClass().add("sidebar-btn");
        btnAsignar.setOnAction(e -> ScreenManager.show(AsignarTecnicosView.getView()));

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

        ImageView logoutIcon = new ImageView(new Image(AdminHistoryView.class.getResource("/images/logout.png").toExternalForm()));
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

        Button tabTodos = makeTab("Todos", true);
        Button tabEspera = makeTab("En espera", false);
        Button tabAbierto = makeTab("Abierto", false);
        Button tabProceso = makeTab("En Proceso", false);
        Button tabResuelto = makeTab("Resuelto", false);

        tabs.getChildren().addAll(tabTodos, tabEspera, tabAbierto, tabProceso, tabResuelto);


        /* ---------------- TABLE ---------------- */
        TableView<Incidencia> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Incidencia, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Incidencia, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));

        TableColumn<Incidencia, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));

        TableColumn<Incidencia, String> colDesc = new TableColumn<>("Detalles");
        colDesc.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcion()));

        // Acciones (eliminar)
        TableColumn<Incidencia, Void> colAcciones = new TableColumn<>("Acciones");
        colAcciones.setCellFactory(column -> new TableCell<>() {
            private final Button btnEliminar = new Button();

            {
                ImageView trashIcon = new ImageView(new Image(
                        AdminHistoryView.class.getResource("/images/trash.png").toExternalForm()
                ));
                trashIcon.setFitHeight(18);
                trashIcon.setFitWidth(18);

                btnEliminar.setGraphic(trashIcon);
                btnEliminar.getStyleClass().add("action-btn");

                btnEliminar.setOnAction(e -> {
                    Incidencia inc = getTableView().getItems().get(getIndex());
                    Parent rootNode = table.getScene().getRoot();

                    ConfirmDialog.show(
                            rootNode,
                            "¿Está seguro de eliminar este ticket?",
                            () -> {
                                IncidenciasDAO.eliminar(inc.getId());
                                // Remover de la tabla visible
                                table.getItems().removeIf(it -> it.getId() == inc.getId());
                            }
                    );
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEliminar);
            }
        });

        table.getColumns().addAll(colId, colEstado, colTipo, colDesc, colAcciones);

        // Cargar datos iniciales (Todos)
        table.getItems().addAll(IncidenciasDAO.obtenerTodas());


        /* ---------------- TAB EVENTS (filtrado) ---------------- */
        tabTodos.setOnAction(e -> {
            resetTabs(tabs, tabTodos);
            cargarFiltrado(table, "Todos");
        });

        tabEspera.setOnAction(e -> {
            resetTabs(tabs, tabEspera);
            cargarFiltrado(table, "En espera");
        });

        tabAbierto.setOnAction(e -> {
            resetTabs(tabs, tabAbierto);
            cargarFiltrado(table, "Abierto");
        });

        tabProceso.setOnAction(e -> {
            resetTabs(tabs, tabProceso);
            cargarFiltrado(table, "En Proceso");
        });

        tabResuelto.setOnAction(e -> {
            resetTabs(tabs, tabResuelto);
            cargarFiltrado(table, "Resuelto");
        });


        /* ---------------- CENTER CARD ---------------- */
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.getChildren().addAll(tabs, table);

        content.setLeft(sidebar);
        content.setTop(header);
        content.setCenter(card);

        // Meter todo en StackPane para que ConfirmDialog pueda encontrarlo y aplicar blur
        StackPane root = new StackPane(content);
        return root;
    }


    /* ---------------- HELPERS ---------------- */

    private static Button makeTab(String text, boolean selected) {
        Button b = new Button(text);
        b.getStyleClass().add("nav-tab");
        if (selected) b.getStyleClass().add("nav-tab-selected");
        return b;
    }

    private static void resetTabs(HBox tabs, Button selectedBtn) {
        for (javafx.scene.Node n : tabs.getChildren()) {
            n.getStyleClass().remove("nav-tab-selected");
        }
        selectedBtn.getStyleClass().add("nav-tab-selected");
    }

    private static void cargarFiltrado(TableView<Incidencia> table, String estado) {
        table.getItems().clear();

        if ("Todos".equalsIgnoreCase(estado)) {
            table.getItems().addAll(IncidenciasDAO.obtenerTodas());
            return;
        }

        for (Incidencia i : IncidenciasDAO.obtenerTodas()) {
            if (i.getEstado() != null && i.getEstado().equalsIgnoreCase(estado)) {
                table.getItems().add(i);
            }
        }
    }
}
