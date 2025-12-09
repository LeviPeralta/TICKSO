package com.ticketsystem.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ticketsystem.dao.UserDAO;

public class SeedData {

    // Se ejecuta una sola vez para llenar la BD
    public static void init() {
        try {
            if (!hayUsuarios()) {
                System.out.println("No hay usuarios, generando seed data...");
                insertarUsuariosDemo();
            } else {
                System.out.println("Usuarios ya existen, no se genera seed data.");
            }
        } catch (Exception e) {
            System.out.println("Error en SeedData.init(): " + e.getMessage());
        }
    }

    private static boolean hayUsuarios() {
        String sql = "SELECT COUNT(*) AS total FROM usuarios";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                return total > 0;
            }

        } catch (Exception e) {
            System.out.println("Error verificando usuarios: " + e.getMessage());
        }

        return false;
    }

    private static void insertarUsuariosDemo() {
        UserDAO.crearUsuario("admin", "1234", "admin");
        UserDAO.crearUsuario("josseph", "1234", "usuario");
        UserDAO.crearUsuario("martin", "1234", "tecnico");
        UserDAO.crearUsuario("pablo", "1234", "tecnico");

        System.out.println("Usuarios de prueba insertados correctamente.");
    }
}
