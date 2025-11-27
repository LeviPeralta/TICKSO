package com.ticketsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ticketsystem.database.DB;

public class UserDAO {

    public static boolean validarLogin(String usuario, String password) {
        String sql = "SELECT password FROM usuarios WHERE usuario = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String passBD = rs.getString("password");

                    // TODO: si usas texto plano:
                    return password.equals(passBD);

                    // Si usas BCrypt:
                    // return BCrypt.checkpw(password, passBD);
                }
            }

        } catch (Exception e) {
            System.out.println("Error en validarLogin: " + e.getMessage());
        }

        return false;
    }

    public static boolean crearUsuario(String usuario, String password) {
    String sql = "INSERT INTO usuarios (usuario, password) VALUES (?, ?)";

    try (Connection conn = DB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, usuario);
        stmt.setString(2, password);

        int filas = stmt.executeUpdate();
        return filas > 0;

    } catch (Exception e) {
        System.out.println("Error al crear usuario: " + e.getMessage());
    }

    return false;
}

}
