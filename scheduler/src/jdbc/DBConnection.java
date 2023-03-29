package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class establishes a connection to the database
 * @author Derek Brown
 */
public class DBConnection {

    /**
     * url for the database
     */
    public static String url = "jdbc:mysql://localhost:3306/client_schedule";
    /**
     * database username to log in
     */
    public static String jdbcUsername = "sqlUser";
    /**
     * database password to log in
     */
    public static String jdbcPassword = "Passw0rd!";
    /**
     * connection object to allow us to connect and interact with database
     */
    public static Connection connection;

    /**
     * method establishes a connection using the url, username, password, and connection object in this class
     * @return connection to database
     * @throws SQLException if unable to get connection via SQL
     * @throws ClassNotFoundException if the server connection is not available
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, jdbcUsername, jdbcPassword);
        return connection;
    }

}
