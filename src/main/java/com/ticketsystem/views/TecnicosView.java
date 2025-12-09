package com.ticketsystem.views;

import java.util.List;
import java.util.stream.Collectors;

import com.ticketsystem.dao.IncidenciasDAO;
import com.ticketsystem.model.Incidencia;
import com.ticketsystem.model.User;
import com.ticketsystem.utils.ScreenManager;
import com.ticketsystem.utils.Session;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TecnicosView {

    public Parent getView() {

        User tecnico = Session.getCurrentUser();  // 👈 Técnico logueado

        BorderPane root = new BorderPane();
        root.getStyleClass().add("status-root");

        root.getStylesheets().add(
                TicketStatusView.class.getResource("/css/status.css").toExternalForm()
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
        Label title = new Label("Mis Tickets Asignados");
        title.getStyleClass().add("status-title");

        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10));

        root.setCenter(titleBox);

        /* -------------------------
         TABLAS
        -------------------------- */
        TabPane tabPane = new TabPane();

        TableView<Incidencia> tableTodos = crearTabla();
        TableView<Incidencia> tableEspera = crearTabla();
        TableView<Incidencia> tableAbierto = crearTabla();
        TableView<Incidencia> tableProceso = crearTabla();
        TableView<Incidencia> tableResuelto = crearTabla();

        tabPane.getTabs().addAll(
                new Tab("Todos", tableTodos),
                new Tab("En espera", tableEspera),
                new Tab("Abierto", tableAbierto),
                new Tab("En proceso", tableProceso),
                new Tab("Resuelto", tableResuelto)
        );

        root.setBottom(tabPane);

        /* -------------------------
         CARGAR SOLO LOS TICKETS DEL TÉCNICO
        -------------------------- */

        List<Incidencia> incidencias = IncidenciasDAO.obtenerTodas()
                .stream()
                .filter(i -> tecnico.getId() == i.getIdTecnico())   // 👈 Solo los del técnico
                .collect(Collectors.toList());

        tableTodos.getItems().addAll(incidencias);
        tableEspera.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("En espera")).toList());
        tableAbierto.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("Abierto")).toList());
        tableProceso.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("En proceso")).toList());
        tableResuelto.getItems().addAll(incidencias.stream().filter(i -> i.getEstado().equals("Resuelto")).toList());

        return root;
    }

    private TableView<Incidencia> crearTabla() {
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

        table.getColumns().addAll(colId, colEstado, colTipo, colDesc);

        return table;
    }
}
