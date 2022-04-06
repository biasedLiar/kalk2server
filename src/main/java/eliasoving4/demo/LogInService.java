package eliasoving4.demo;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides database service for login.
 */
public class LogInService {
    public User findUser(String userEmail, String userPasswordHash) throws SQLException {
        System.out.println("Searching for user: " + userEmail + ", password " + userPasswordHash);
        Connection connection = DatabaseService.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM \"user\" WHERE email = ? AND password_hash = ?;");
        statement.setString(1, userEmail);
        statement.setString(2, userPasswordHash);
        ResultSet results = statement.executeQuery();

        if (results.next()) {
            User user = new User();
            user.setID(results.getInt("id"));
            user.setFirstName(results.getString("first_name"));
            user.setLastName(results.getString("last_name"));
            user.setEmail(results.getString("email"));
            user.setAdmin(results.getBoolean("is_admin"));
            user.setPasswordHash(results.getString("password_hash"));
            return user;
        }
        return null;
    }
}
