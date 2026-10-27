package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Conexión única a la base de datos (patrón Singleton). */
public class DatabaseConnection {
    // Si MariaDB está en otra máquina, cambia "localhost" por su IP
    private static final String URL = "jdbc:mariadb://localhost:3306/practica";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encuentra el driver de MariaDB", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DatabaseConnection getInstance() throws SQLException {
        // isValid comprueba que el servidor sigue respondiendo (espera como mucho 2 segundos).
        // Si se cayó y ha vuelto, se crea una conexión nueva en vez de seguir con la rota.
        if (instance == null || !instance.getConnection().isValid(2)) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}