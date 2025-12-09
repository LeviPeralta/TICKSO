package com.ticketsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private static final String DB_URL = "jdbc:sqlite:ticketsystem.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            System.out.println("ERROR cargando driver SQLite: " + e.getMessage());
        }

        initDatabase();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void initDatabase() {
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            String sql = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    usuario TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    rol TEXT NOT NULL
                );

                CREATE TABLE IF NOT EXISTS incidencias (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    primer_apellido TEXT NOT NULL,
                    segundo_apellido TEXT,
                    telefono TEXT NOT NULL,
                    tipo TEXT NOT NULL,
                    descripcion TEXT NOT NULL,
                    estado TEXT NOT NULL ,
                    id_usuario_asignado INTEGER,
                    FOREIGN KEY (id_usuario_asignado) REFERENCES usuarios(id)
                );
                """;

            stmt.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
