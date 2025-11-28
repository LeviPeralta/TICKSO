package com.ticketsystem.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.ticketsystem.database.DB;
import com.ticketsystem.model.Incidencia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IncidenciaService {

    public static ObservableList<Incidencia> getAll() {

        ObservableList<Incidencia> list = FXCollections.observableArrayList();

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM incidencias")) {

            while (rs.next()) {
                list.add(new Incidencia(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("primer_apellido"),
                        rs.getString("segundo_apellido"),
                        rs.getString("telefono"),
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        rs.getString("estado")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
