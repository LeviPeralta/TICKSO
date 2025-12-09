package com.ticketsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.ticketsystem.database.DB;
import com.ticketsystem.model.User;

public class UserDAO {

    /**
     * Iniciar sesión y devolver un objeto User con su rol
     */
    public static User login(String usuario, String password) {

        String sql = "SELECT * FROM usuarios WHERE usuario = ?";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String passBD = rs.getString("password");

                    // Validación simple
                    if (password.equals(passBD)) {

                        return new User(
                                rs.getInt("id"),
                                rs.getString("usuario"),
                                rs.getString("password"),
                                rs.getString("rol")  // ← IMPORTANTE
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error en login: " + e.getMessage());
        }

        return null; // Usuario no encontrado o password incorrecto
    }


    /**
     * Crear usuario con rol
     */
    public static boolean crearUsuario(String usuario, String password, String rol) {

        String sql = "INSERT INTO usuarios (usuario, password, rol) VALUES (?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);
            stmt.setString(3, rol);

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }

        return false;
    }

    public static java.util.List<User> getTecnicos() {

        java.util.List<User> lista = new java.util.ArrayList<>();

        String sql = "SELECT id, usuario, password, rol FROM usuarios WHERE LOWER(rol) = 'tecnico'";

        try (Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new User(
                    rs.getInt("id"),
                    rs.getString("usuario"),
                    rs.getString("password"),
                    rs.getString("rol")
                ));
            }

        } catch (Exception e) {
            System.out.println("Error al obtener técnicos: " + e.getMessage());
        }

        return lista;
    }

    /**
 * Obtener usuarios según su rol (admin, tecnico, usuario)
 */
    public static java.util.List<User> obtenerPorRol(String rol) {

        java.util.List<User> lista = new java.util.ArrayList<>();

        String sql = "SELECT id, usuario, password, rol FROM usuarios WHERE LOWER(rol) = LOWER(?)";

        try (Connection conn = DB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rol);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    lista.add(new User(
                        rs.getInt("id"),
                        rs.getString("usuario"),
                        rs.getString("password"),
                        rs.getString("rol")
                    ));
                }

            }

        } catch (Exception e) {
            System.out.println("Error al obtener usuarios por rol: " + e.getMessage());
        }

        return lista;
    }


}
