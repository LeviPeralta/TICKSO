package com.ticketsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.ticketsystem.database.DB;

public class IncidenciasDAO {

    // Tu método guardar existente
    public static boolean guardar(
        String nombre,
        String primerApellido,
        String segundoApellido,
        String telefono,
        String tipo,
        String descripcion
    ) {
        String sql = """
            INSERT INTO incidencias(nombre, primer_apellido, segundo_apellido, telefono, tipo, descripcion)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, primerApellido);
            stmt.setString(3, segundoApellido);
            stmt.setString(4, telefono);
            stmt.setString(5, tipo);
            stmt.setString(6, descripcion);

            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Método para precargar 5 incidencias de prueba
    public static void precargarIncidencias() {
        guardar("Juan", "Pérez", "Gómez", "5551234567", "Software", "Falla en login");
        guardar("María", "López", "Hernández", "5559876543", "Hardware", "Monitor no enciende");
        guardar("Carlos", "Ramírez", "Santos", "5557654321", "Red", "No hay conexión a internet");
        guardar("Ana", "Martínez", "Ruiz", "5556781234", "Software", "Error en la actualización");
        guardar("Luis", "García", "Vega", "5554321678", "Hardware", "Teclado con teclas pegadas");
    }
}
