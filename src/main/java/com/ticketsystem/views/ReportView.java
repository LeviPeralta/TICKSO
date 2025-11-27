package com.ticketsystem.views;

import com.ticketsystem.utils.ScreenManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ReportView {

    public static Parent getView() {

        // ROOT
        BorderPane root = new BorderPane();
        root.getStyleClass().add("report-root");
        root.setPadding(new Insets(20)); // antes 40


        // =====================================================================================
        // TOP BAR
        // =====================================================================================

        ImageView logo = new ImageView(
                new Image(ReportView.class.getResource("/images/logo.png").toExternalForm())
        );
        logo.setFitWidth(130);
        logo.setPreserveRatio(true);

        ImageView homeIcon = new ImageView(
                new Image(ReportView.class.getResource("/images/home.png").toExternalForm())
        );
        homeIcon.setFitWidth(30);
        homeIcon.setPreserveRatio(true);

        Button homeBtn = new Button();
        homeBtn.setGraphic(homeIcon);
        homeBtn.getStyleClass().add("home-btn");

        homeBtn.setOnAction(e -> { ScreenManager.show(MenuView.getView()); });

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

        HBox rightBox = new HBox(15, homeBtn, logoutBtn);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(logo);
        topBar.setRight(rightBox);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        root.setTop(topBar);

        // =====================================================================================
        // FORMULARIO
        // =====================================================================================

        VBox card = new VBox(18);

        card.getStyleClass().add("report-card");

        Label titulo = new Label("Formulario");
        titulo.getStyleClass().add("report-title");

        // === SECCIÓN DE DATOS PERSONALES ===
        Label datosPersonales = new Label("Datos personales");
        datosPersonales.getStyleClass().add("section-title");

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(10);


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);

        grid.getColumnConstraints().addAll(col1, col2);

        Label lblNombre = new Label("Nombre(s):");
        TextField txtNombre = new TextField();
        txtNombre.getStyleClass().add("input-field");
        lblNombre.getStyleClass().add("field-label");

        Label lblPrimer = new Label("Primer apellido:");
        TextField txtPrimer = new TextField();
        txtPrimer.getStyleClass().add("input-field");
        lblPrimer.getStyleClass().add("field-label");

        Label lblSegundo = new Label("Segundo apellido:");
        TextField txtSegundo = new TextField();
        txtSegundo.getStyleClass().add("input-field");
        lblSegundo.getStyleClass().add("field-label");

        Label lblDir = new Label("Dirección:");
        TextField txtDir = new TextField();
        txtDir.getStyleClass().add("input-field");
        lblDir.getStyleClass().add("field-label");

        Label lblTel = new Label("Teléfono:");
        TextField txtTel = new TextField();
        txtTel.getStyleClass().add("input-field");
        lblTel.getStyleClass().add("field-label");

        grid.add(lblNombre, 0, 0);
        grid.add(txtNombre, 0, 1);

        grid.add(lblPrimer, 1, 0);
        grid.add(txtPrimer, 1, 1);

        grid.add(lblSegundo, 0, 2);
        grid.add(txtSegundo, 0, 3);

        grid.add(lblDir, 1, 2);
        grid.add(txtDir, 1, 3);

        grid.add(lblTel, 0, 4);
        grid.add(txtTel, 0, 5);

        // =====================================================================================
        // INCIDENCIA (ComboBox)
        // =====================================================================================

        Label incidencia = new Label("Incidencia");
        incidencia.getStyleClass().add("section-title");

        // ComboBox para tipo de incidencia
        Label tipoLabel = new Label("Seleccione el tipo de incidencia:");
        tipoLabel.getStyleClass().add("field-label");

        ComboBox<String> comboIncidencia = new ComboBox<>();
        comboIncidencia.getStyleClass().add("input-field");

        comboIncidencia.getItems().addAll(
                "Falla eléctrica",
                "Problemas de red o Internet",
                "Equipo no enciende",
                "Impresora sin funcionar",
                "Problemas de software",
                "Mantenimiento preventivo",
                "Error del sistema",
                "Cambio de periféricos",
                "Actualización de software",
                "Otros"
        );

        comboIncidencia.setPromptText("Seleccione una opción");

        Label descripcionLabel = new Label("Describe a detalle su situación:");
        descripcionLabel.getStyleClass().add("field-label");

        TextArea descripcion = new TextArea();
        descripcion.getStyleClass().add("textarea-field");
        descripcion.setPrefRowCount(3);


        // BOTÓN
        Button btnEnviar = new Button("Enviar");
        btnEnviar.getStyleClass().add("submit-btn");
        btnEnviar.setOnAction(e -> {
                String nombre = txtNombre.getText();
                String primer = txtPrimer.getText();
                String segundo = txtSegundo.getText();
                String telefono = txtTel.getText();
                String tipo = comboIncidencia.getValue();
                String desc = descripcion.getText();

                // Validaciones simples
                if (nombre.isEmpty() || primer.isEmpty() || telefono.isEmpty() || tipo == null || desc.isEmpty()) {
                        System.out.println("⚠️ Todos los campos necesarios no están completos.");
                        return;
                }

                boolean ok = com.ticketsystem.dao.IncidenciasDAO.guardar(
                        nombre,
                        primer,
                        segundo,
                        telefono,
                        tipo,
                        desc
                );

                if (ok) {
                        System.out.println("✅ Incidencia guardada correctamente.");
                        
                        // Limpiar formulario
                        txtNombre.clear();
                        txtPrimer.clear();
                        txtSegundo.clear();
                        txtTel.clear();
                        comboIncidencia.getSelectionModel().clearSelection();
                        descripcion.clear();

                } else {
                        System.out.println("❌ Error al guardar la incidencia.");
                }
        });


        HBox footer = new HBox(btnEnviar);
        footer.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                titulo,
                datosPersonales,
                grid,
                incidencia,
                tipoLabel,
                comboIncidencia,
                descripcionLabel,
                descripcion,
                footer
        );

        root.setCenter(card);

        root.getStylesheets().add(
                ReportView.class.getResource("/css/report.css").toExternalForm()
        );

        return root;
    }
}
