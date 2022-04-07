package eliasoving4.demo;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Service
public class CalcService {

    private Calculator calc;
    private String resultString ="";

    public CalcService() {
        calc = new Calculator();
    }

    public double buttonPressed(String button, int userId){
        switch (button){
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
                resultString += button;
                return calc.pressNumber(Integer.parseInt(button));
            case "ans":
                double d = calc.pressAns();
                resultString += d;
                return d;
            case "c":
                resultString = "";
                return calc.pressC();
            case "del":
                resultString = resultString.substring(0, resultString.length()-1);
                return calc.pressDel();
            case "equals":
                double d1 = calc.pressEquals();
                resultString += "=" + d1;
                addEquationToDatabase(resultString, userId);
                resultString = "";
                return d1;
            case "dot":
                resultString+= ".";
                return calc.pressDecimal();
            default:
                resultString += button;
                return calc.pressOperator(button);
        }

    }

    public boolean addEquationToDatabase(String equation, int userId){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            try {
                PreparedStatement s = connection.prepareStatement(
                        "INSERT INTO question (text, owner) VALUES(?, ?)");
                s.setString(1, equation);
                s.setInt(2, userId);
                System.out.println(s.toString());
                int result = s.executeUpdate();
                s.close();
                return result == 1;
            } finally {
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public ArrayList<Question> getEquationsOfUser(int userId){
        Connection connection = null;
        try {
            connection = DatabaseService.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM question WHERE  owner = ? ORDER BY id DESC");
            statement.setInt(1, userId);
            ResultSet results = statement.executeQuery();
            ArrayList<Question> questions = new ArrayList<Question>();

            while (results.next()) {
                Question question = new Question();
                question.setId(results.getInt("id"));
                question.setText(results.getString("text"));
                question.setOwner(results.getInt("owner"));
                questions.add(question);
            }
            return questions;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
