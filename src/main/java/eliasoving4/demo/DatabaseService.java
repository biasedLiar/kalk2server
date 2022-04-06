package eliasoving4.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Is responsible for giving out connections to the database.
 */
public class DatabaseService {
    private static DatabaseService instance = new DatabaseService();

    /**
     * gets a instance of the class
     * @return The instance of the class
     */
    public static DatabaseService getInstance() {
        return instance;
    }

    /**
     * Gets a connection to the database
     * @return A connection to the database
     * @throws SQLException
     */
    Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");

    }
}
