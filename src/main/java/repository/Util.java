package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// archivo clave: guarda los datos para conectarse a la base de datos
public class Util {

    private static final String URL      = "jdbc:mysql://localhost:3306/salonDeEventos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER     = "root";
    private static final String PASSWORD = "gestor123";

    // Devuelve una nueva conexión a la BD. llave q usan los daos para entrar a la
    // bd
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
