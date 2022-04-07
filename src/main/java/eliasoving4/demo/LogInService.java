package eliasoving4.demo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides database service for login.
 */
public class LogInService {
    public Person findPerson(String username, String personPasswordHash) throws SQLException {
        System.out.println("Searching for person: " + username + ", password " + personPasswordHash);
        Connection connection = DatabaseService.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM person WHERE username = ? AND password_hash = ?;");
        statement.setString(1, username);
        statement.setString(2, personPasswordHash);
        ResultSet results = statement.executeQuery();

        if (results.next()) {
            Person person = new Person();
            person.setId(results.getInt("id"));
            person.setUsername(results.getString("username"));
            person.setPasswordHash(results.getString("password_hash"));
            return person;
        }
        return null;
    }
}
