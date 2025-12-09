package com.ticketsystem.views;

import com.ticketsystem.dao.IncidenciasDAO;
import com.ticketsystem.dao.UserDAO;
import com.ticketsystem.model.Incidencia;
import com.ticketsystem.model.User;
import com.ticketsystem.utils.ScreenManager;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class AsignarTecnicosView {

    public static Parent getView() {

        BorderPane content = new BorderPane();
        content.getStylesheets().add(
                AsignarTecnicosView.class.getResource("/css/asignartecnicos.css").toExternalForm()
        );

        /* ---------------- LEFT SIDEBAR ---------------- */
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        Button btnHistorial = new Button("Historial");
        btnHistorial.getStyleClass().add("sidebar-title");
        btnHistorial.setOnAction(e -> ScreenManager.show(AdminHistoryView.getView()));

        Button btnAsignar = new Button("Asignar técnicos");
        btnAsignar.getStyleClass().add("sidebar-btn-selected");

        Button btnCambiar = new Button("Cambiar estados");
        btnCambiar.getStyleClass().add("sidebar-btn");

        sidebar.getChildren().addAll(btnHistorial, btnAsignar, btnCambiar);


        /* ---------------- HEADER ---------------- */
        HBox header = new HBox();
        header.getStyleClass().add("header");

        ImageView logo = new ImageView(
                AsignarTecnicosView.class.getResource("/images/logo.png").toExternalForm()
        );
        logo.setFitWidth(150);
        logo.setPreserveRatio(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ImageView logoutIcon = new ImageView(
                new Image(AsignarTecnicosView.class.getResource("/images/logout.png").toExternalForm())
        );
        logoutIcon.setFitWidth(34);
        logoutIcon.setPreserveRatio(true);

        Button logoutBtn = new Button();
        logoutBtn.setGraphic(logoutIcon);
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setOnAction(e -> ScreenManager.show(new LoginView()));

        header.getChildren().addAll(logo, spacer, logoutBtn);


        /* ---------------- TABS ---------------- */
        HBox tabs = new HBox(15);
        tabs.getStyleClass().add("tabs");

        Button tabTodos     = makeTab("Todos", true);
        Button tabEspera    = makeTab("En espera", false);
        Button tabAbierto   = makeTab("Abierto", false);
        Button tabProceso   = makeTab("En proceso", false);
        Button tabResuelto  = makeTab("Resuelto", false);

        tabs.getChildren().addAll(tabTodos, tabEspera, tabAbierto, tabProceso, tabResuelto);


        /* ---------------- TABLE ---------------- */
        TableView<Incidencia> table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Incidencia, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId()))
        );

        TableColumn<Incidencia, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstado())
        );

        TableColumn<Incidencia, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTipo())
        );

        TableColumn<Incidencia, String> colDetalles = new TableColumn<>("Descripción");
        colDetalles.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescripcion())
        );


        /* --------- COLUMNA ComboBox para técnicos --------- */
        TableColumn<Incidencia, User> colTecnico = new TableColumn<>("Técnico");

        colTecnico.setCellFactory(column -> new TableCell<>() {

            private final ComboBox<User> combo = new ComboBox<>();

            {
                combo.setPromptText("Seleccionar técnico");
                combo.setPrefWidth(180);
            }

            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                combo.getItems().setAll(UserDAO.getTecnicos());

                Incidencia inc = getTableView().getItems().get(getIndex());

                // Seleccionar el técnico asignado
                if (inc.getIdTecnico() != null) {
                    for (User u : combo.getItems()) {
                        if (u.getId() == inc.getIdTecnico()) {
                            combo.setValue(u);
                            break;
                        }
                    }
                }

                combo.setOnAction(e -> {
                    User seleccionado = combo.getValue();
                    if (seleccionado != null) {
                        IncidenciasDAO.asignarTecnico(inc.getId(), seleccionado.getId());
                        inc.setIdTecnico(seleccionado.getId());
                    }
                });

                setGraphic(combo);
            }

        });

        table.getColumns().addAll(colId, colEstado, colTipo, colDetalles, colTecnico);

        /* Cargar datos */
        table.getItems().addAll(IncidenciasDAO.obtenerTodas());


        /* --------- TABS acciones --------- */
        tabTodos.setOnAction(e -> cargarEstado(tabs, tabTodos, table, "Todos"));
        tabEspera.setOnAction(e -> cargarEstado(tabs, tabEspera, table, "En espera"));
        tabAbierto.setOnAction(e -> cargarEstado(tabs, tabAbierto, table, "Abierto"));
        tabProceso.setOnAction(e -> cargarEstado(tabs, tabProceso, table, "En proceso"));
        tabResuelto.setOnAction(e -> cargarEstado(tabs, tabResuelto, table, "Resuelto"));


        /* --------- MAIN CARD --------- */
        VBox card = new VBox(25);
        card.getStyleClass().add("card");
        card.getChildren().addAll(tabs, table);

        content.setLeft(sidebar);
        content.setTop(header);
        content.setCenter(card);

        return content;
    }


    /* ================= HELPERS ================= */

    private static Button makeTab(String text, boolean selected) {
        Button b = new Button(text);
        b.getStyleClass().add("tab-btn");
        if (selected) b.getStyleClass().add("tab-btn-selected");
        return b;
    }

    private static void cargarEstado(HBox tabs, Button selected, TableView<Incidencia> table, String estado) {

        for (javafx.scene.Node n : tabs.getChildren())
            n.getStyleClass().remove("tab-btn-selected");

        selected.getStyleClass().add("tab-btn-selected");

        table.getItems().clear();

        if (estado.equals("Todos")) {
            table.getItems().addAll(IncidenciasDAO.obtenerTodas());
        } else {
            table.getItems().addAll(IncidenciasDAO.getByEstado(estado));
        }
    }
}
