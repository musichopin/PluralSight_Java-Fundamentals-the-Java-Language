package com.pluralsight.calcengine;

import org.omg.CORBA.DynAnyPackage.Invalid;

/**
 * Created by Jim on 10/18/2015.
 */
public class CalculateHelper {
    private static final char ADD_SYMBOL = '+';
    private static final char SUBTRACT_SYMBOL = '-';
    private static final char MULTIPLY_SYMBOL = '*';
    private static final char DIVIDE_SYMBOL = '/';

    MathCommand command;
    double leftValue;
    double rightValue;
    double result;

    public void process(String statement) throws InvalidStatementException {
        // we want the caller to get the exceptions with throws kw (instead of try-catch block)

        // add 1.0 2.0
        String[] parts = statement.split(" ");
        if(parts.length != 3)
            // sonradan hata vermesine karşın önceden önlem alınıyor if ve throw ile
            throw new InvalidStatementException("Incorrect number of fields", statement);
//        alt:
//        InvalidStatementException x = new InvalidStatementException("Incorrect number of fields", statement);
//        throw x;

        String commandString = parts[0]; // add

        try {
            leftValue = Double.parseDouble(parts[1]); // 1.0
            rightValue = Double.parseDouble(parts[2]); // 2.0
            // exception otomatik fırlatılır: "throw new NumberFormatException (e)" olarak düşün
        } catch (NumberFormatException e) {
            throw new InvalidStatementException("Non-numeric data", statement, e);
            // with "e" we do not wanna lose the fact that this was triggered by another exception
//            throw kw sayesinde ve throw ile fırlatılan exceptionın catch ile
//            yakalanmamış olması sayesinde ve onun yerine throws kw sayesinde
//            exception class dışına fırlatılır
        }
        setCommandFromString(commandString);
        if(command == null)
            // sonradan hata vermesine karşın önceden önlem alınıyor if ve throw ile
            throw new InvalidStatementException("Invalid command", statement);

        CalculateBase calculator = null;
        switch (command) {
            case Add:
                calculator = new Adder(leftValue, rightValue);
                break;
            case Subtract:
                calculator = new Subtracter(leftValue, rightValue);
                break;
            case Multiply:
                calculator = new Multiplier(leftValue, rightValue);
                break;
            case Divide:
                calculator = new Divider(leftValue, rightValue);
                break;
        }

        calculator.calculate();
        result = calculator.getResult();


    }

    private void setCommandFromString(String commandString) {
        // add -> MathCommand.Add

        if(commandString.equalsIgnoreCase(MathCommand.Add.toString()))
            command = MathCommand.Add;
        else if(commandString.equalsIgnoreCase(MathCommand.Subtract.toString()))
            command = MathCommand.Subtract;
        else if(commandString.equalsIgnoreCase(MathCommand.Multiply.toString()))
            command = MathCommand.Multiply;
        else if(commandString.equalsIgnoreCase(MathCommand.Divide.toString()))
            command = MathCommand.Divide;
    }

    @Override
    public String toString() {
        // 1.0 + 2.0 = 3.0
        char symbol = ' ';
        switch(command) {
            case Add:
                symbol = ADD_SYMBOL;
                break;
            case Subtract:
                symbol = SUBTRACT_SYMBOL;
                break;
            case Multiply:
                symbol = MULTIPLY_SYMBOL;
                break;
            case Divide:
                symbol = DIVIDE_SYMBOL;
                break;
        }

        StringBuilder sb = new StringBuilder(20);
        sb.append(leftValue);
        sb.append(' ');
        sb.append(symbol);
        sb.append(' ');
        sb.append(rightValue);
        sb.append(" = ");
        sb.append(result);

        return sb.toString();
    }



}
