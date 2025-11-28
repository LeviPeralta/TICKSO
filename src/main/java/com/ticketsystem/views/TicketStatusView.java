package com.ticketsystem.views;

import java.util.List;

import com.ticketsystem.dao.IncidenciasDAO;
import com.ticketsystem.model.Incidencia;
import com.ticketsystem.utils.ScreenManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TicketStatusView {

    /***
     * MÉTODO PRINCIPAL → retorna el ROOT para usarlo en ScreenManager.show()
     */
    public Parent getView() {

        BorderPane root = new BorderPane();
        root.getStyleClass().add("status-root");

        root.getStylesheets().add(
            ReportView.class.getResource("/css/status.css").toExternalForm()
        );

        /* -------------------------
        TOP BAR
        -------------------------- */
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        ImageView logo = new ImageView(
                new Image(getClass().getResourceAsStream("/images/logo.png"))
        );
        logo.setFitWidth(120);
        logo.setPreserveRatio(true);

        Button homeBtn = new Button();
        homeBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/home.png"))));
        homeBtn.getStyleClass().add("home-btn");

        Button logoutBtn = new Button();
        logoutBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/logout.png"))));
        logoutBtn.getStyleClass().add("logout-btn");

        homeBtn.setOnAction(e -> ScreenManager.show(new MenuView().getView()));
        logoutBtn.setOnAction(e -> ScreenManager.show(new LoginView()));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(logo, spacer, homeBtn, logoutBtn);
        root.setTop(topBar);

        /* -------------------------
        TÍTULO
        -------------------------- */
        Label title = new Label("Estado de Tickets");
        title.getStyleClass().add("status-title");

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        root.setCenter(titleBox);

        /* -------------------------
        TABLAS
        -------------------------- */

        TabPane tabPane = new TabPane();

        // LISTA QUE GUARDA TODAS LAS TABLAS
        List<TableView<Incidencia>> allTables = new java.util.ArrayList<>();

        // Crear tablas y agregarlas a la lista
        TableView<Incidencia> tableTodos = crearTabla(allTables);
        TableView<Incidencia> tableEspera = crearTabla(allTables);
        TableView<Incidencia> tableAbierto = crearTabla(allTables);
        TableView<Incidencia> tableProceso = crearTabla(allTables);
        TableView<Incidencia> tableResuelto = crearTabla(allTables);

        allTables.add(tableTodos);
        allTables.add(tableEspera);
        allTables.add(tableAbierto);
        allTables.add(tableProceso);
        allTables.add(tableResuelto);

        Tab tabTodos = new Tab("Todos", tableTodos);
        Tab tabEspera = new Tab("En espera", tableEspera);
        Tab tabAbierto = new Tab("Abierto", tableAbierto);
        Tab tabProceso = new Tab("En proceso", tableProceso);
        Tab tabResuelto = new Tab("Resuelto", tableResuelto);

        tabPane.getTabs().addAll(tabTodos, tabEspera, tabAbierto, tabProceso, tabResuelto);

        root.setBottom(tabPane);

        /* -------------------------
        CARGAR DATOS
        -------------------------- */
        List<Incidencia> incidencias = IncidenciasDAO.obtenerTodas();

        tableTodos.getItems().addAll(incidencias);
        tableEspera.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("En espera")).toList());
        tableAbierto.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("Abierto")).toList());
        tableProceso.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("En proceso")).toList());
        tableResuelto.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("Resuelto")).toList());

        return root;
    }

    private TableView<Incidencia> crearTabla(List<TableView<Incidencia>> allTables) {

        TableView<Incidencia> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Incidencia, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Incidencia, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        TableColumn<Incidencia, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        TableColumn<Incidencia, String> colDesc = new TableColumn<>("Detalles");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        TableColumn<Incidencia, Void> colAcciones = new TableColumn<>("Acciones");

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEliminar = new Button();

            {
                ImageView trashIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/trash.png")));
                trashIcon.setFitHeight(18);
                trashIcon.setFitWidth(18);

                btnEliminar.setGraphic(trashIcon);
                btnEliminar.getStyleClass().add("action-btn");

                btnEliminar.setOnAction(e -> {
                    Incidencia inc = getTableView().getItems().get(getIndex());

                    // ELIMINAR DE LA BD
                    IncidenciasDAO.eliminar(inc.getId());

                    // ELIMINAR DE TODAS LAS TABLAS
                    for (TableView<Incidencia> t : allTables) {
                        t.getItems().removeIf(item -> item.getId() == inc.getId());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEliminar);
            }
        });

        table.getColumns().addAll(colId, colEstado, colTipo, colDesc, colAcciones);

        return table;
    }


}
