package barebonesinterpreter.codelogic.commands;

import barebonesinterpreter.codelogic.code.Command;
import barebonesinterpreter.codelogic.logic.Expression;
import barebonesinterpreter.codelogic.logic.Operator;
import barebonesinterpreter.codelogic.logic.Value;
import barebonesinterpreter.codelogic.logic.Variable;
import java.util.HashMap;

public class Assign extends Command {

    String variable;
    Expression expression;

    public Assign(String variable, String expressionString) {
        this.variable = variable;
        expression = translate(expressionString);
    }

    public Expression translate(String rawText) {
        rawText = rawText.replace(" ", "");
        if (rawText.contains("+") || rawText.contains("-") || rawText.contains("/") || rawText.contains("*")) {
            Expression exp1;
            if (rawText.charAt(0) == '(') {
                int count = 1;
                int charCounter = 0;
                while (count > 0) {
                    charCounter++;
                    if (rawText.charAt(charCounter) == '(') {
                        count++;
                    } else if (rawText.charAt(charCounter) == ')') {
                        count--;
                    }
                }
                exp1 = translate(rawText.substring(1, charCounter));
                rawText = rawText.substring(charCounter + 1);
            } else {
                int firstOpIndex = findFirstOp(rawText);
                exp1 = translate(rawText.substring(0, firstOpIndex));
                rawText = rawText.substring(firstOpIndex);
            }
            String operator = rawText.charAt(0) + "";
            rawText = rawText.substring(1);
            Expression exp2;
            if (rawText.charAt(0) == '(') {
                exp2 = translate(rawText.substring(1, rawText.length() - 1));
            } else {
                exp2 = translate(rawText);
            }
            return new Operator(exp1, exp2, operator);
        } else {
            if (isInteger(rawText)) {
                return new Value(Integer.parseInt(rawText));
            } else {
                return new Variable(rawText);
            }
        }
    }

    public void execute(HashMap vars) {
        vars.put(variable, expression.getValue(vars));
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public int findFirstOp(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '+' || c == '-' || c == '/' || c == '*') {
                return i;
            }
        }
        return -1;
    }

}
