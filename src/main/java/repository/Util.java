package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad de conexión a la base de datos MySQL.
 * Centraliza la configuración de conexión para todos los DAOs.
 */
public class Util {

    private static final String URL      = "jdbc:mysql://localhost:3306/salonDeEventos?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "gestor123";

    /**
     * Devuelve una nueva conexión a la BD.
     * Usar siempre dentro de un try-with-resources para cerrarla automáticamente.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
