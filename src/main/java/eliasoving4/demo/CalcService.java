package eliasoving4.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalcService {

    private Calculator calc;

    public CalcService() {
        calc = new Calculator();
    }

    public double buttonPressed(String button){
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
                return calc.pressNumber(Integer.parseInt(button));
            case "ans":
                return calc.pressAns();
            case "c":
                return calc.pressC();
            case "del":
                return calc.pressDel();
            case "equals":
                return calc.pressEquals();
            case "dot":
                return calc.pressDecimal();
            default:
                return calc.pressOperator(button);
        }
    }


}
