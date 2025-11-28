package com.ticketsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ticketsystem.database.DB;
import com.ticketsystem.model.Incidencia;

public class IncidenciasDAO {

    // Tu método guardar existente
    public static boolean guardar(
        String nombre,
        String primerApellido,
        String segundoApellido,
        String telefono,
        String tipo,
        String descripcion,
        String estado
    ) {
        String sql = """
            INSERT INTO incidencias(nombre, primer_apellido, segundo_apellido, telefono, tipo, descripcion, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, primerApellido);
            stmt.setString(3, segundoApellido);
            stmt.setString(4, telefono);
            stmt.setString(5, tipo);
            stmt.setString(6, descripcion);
            stmt.setString(7, estado);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Método para precargar 5 incidencias de prueba
    public static void precargarIncidencias() {
        guardar("Juan", "Pérez", "Gómez", "5551234567", "Software", "Falla en login", "En espera");
        guardar("María", "López", "Hernández", "5559876543", "Hardware", "Monitor no enciende", "En proceso");
        guardar("Carlos", "Ramírez", "Santos", "5557654321", "Red", "No hay conexión a internet", "Resuelto");
        guardar("Ana", "Martínez", "Ruiz", "5556781234", "Software", "Error en la actualización", "En espera");
        guardar("Luis", "García", "Vega", "5554321678", "Hardware", "Teclado con teclas pegadas", "En proceso");
    }

    public static List<Incidencia> obtenerTodas() {

    List<Incidencia> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, primer_apellido, segundo_apellido, telefono, tipo, descripcion, estado FROM incidencias";

        try (Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                lista.add(new Incidencia(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("primer_apellido"),
                    rs.getString("segundo_apellido"),
                    rs.getString("telefono"),
                    rs.getString("tipo"),
                    rs.getString("descripcion"),
                    rs.getString("estado")  // 🔥 nuevo
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static boolean eliminar(int id) {

        String sql = "DELETE FROM incidencias WHERE id = ?";

        try (Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
