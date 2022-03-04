package eliasoving4.demo;

import org.springframework.cglib.core.internal.Function;

import java.beans.JavaBean;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleBiFunction;


public class Calculator {
    private double d1;
    private double d2;
    private int sinceDecimal;
    private ToDoubleBiFunction<Double, Double> func;
    private double ans;
    private int ansDecimal;
    private boolean divide;


    public Calculator() {
        resetCalculator();
    }

    private double getNumb1() {
        return d1;
    }

    private void setNumb1(double numb1) {
        this.d1 = numb1;
    }

    private double getNumb2() {
        return d2;
    }

    private void setNumb2(double numb2) {
        this.d2 = numb2;
    }

    private ToDoubleBiFunction<Double, Double> getFunction() {
        return func;
    }

    private void setAddition(){
        func = (Double double1, Double double2) -> double1 + double2;
    }

    private void setSubtraction(){
        func = (Double double1, Double double2) -> double1 - double2;
    }

    private void setMultiplication(){
        func = (Double double1, Double double2) -> double1 * double2;
    }

    private void setDivision(){
        func = (Double double1, Double double2) -> double1 / double2;
        divide = true;
    }

    private void resetFunction(){
        func = (Double double1, Double double2) -> double2;
        divide = false;
    }

    private void resetCalculator(){
        resetFunction();
        sinceDecimal = 0;
        d1 = 0;
        d2 = 0;
        divide = false;
    }

    private void completeReset(){
        resetCalculator();
        ans = 0;
        ansDecimal = 0;

    }



    private void doFunction(){
        if (d2 == 0 && divide){
            throw new IllegalArgumentException("Tried to divide by zero");
        }
        d1 = func.applyAsDouble(d1, d2);
        sinceDecimal = 0;
        d2 = 0;
    }

    public double pressNumber(double digit){
        if (sinceDecimal == 0){
            d2 *=10;
            d2 += digit;
        } else {
            digit *= Math.pow(10, -sinceDecimal);
            d2 += digit;
            sinceDecimal++;
        }
        return d2;
    }

    public double pressDecimal(){
        if (sinceDecimal == 0){
            sinceDecimal = 1;
        }

        return d2;
    }

    public double pressOperator(String funcString){
        doFunction();
        switch (funcString){
            case "+":
                setAddition();
                break;
            case "-":
                setSubtraction();
                break;
            case "*":
                setMultiplication();
                break;
            case "d":
                setDivision();
                break;
            default:
                throw new IllegalArgumentException("Unknown operator submitted: " + funcString);
        }

        return d1;
    }

    public double pressEquals(){
        doFunction();
        ans = d1;
        ansDecimal = sinceDecimal;
        resetCalculator();
        return ans;
    }

    public double pressAns(){
        d2 = ans;
        sinceDecimal = ansDecimal;
        return d2;
    }

    public double pressDel(){
        if (sinceDecimal == 0){
            if(d2 != 0){
                d2 =(d2 - (d2 % 10)) /10;
            }
        }else {
            d2 = (d2 % Math.pow(10, 1 - sinceDecimal));
            sinceDecimal--;
        }
        return d2;
    }
    public double pressC(){
        resetCalculator();
        return d1;
    }

}
