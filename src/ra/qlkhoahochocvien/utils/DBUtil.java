package ra.qlkhoahochocvien.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String url = "jdbc:postgresql://localhost:5432/qlhocvien?currentSchema=qlhocvien";
    private static final String user = "postgres";
    private static final String password = "123456";
    private static final String driver = "org.postgresql.Driver";

    public static Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
