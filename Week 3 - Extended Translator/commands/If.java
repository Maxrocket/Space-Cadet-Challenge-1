package barebonesinterpreter.codelogic.commands;

import barebonesinterpreter.codelogic.code.Block;
import barebonesinterpreter.codelogic.code.Command;
import barebonesinterpreter.codelogic.logic.Condition;
import barebonesinterpreter.codelogic.logic.Equality;
import barebonesinterpreter.codelogic.logic.Expression;
import barebonesinterpreter.codelogic.logic.Logical;
import barebonesinterpreter.codelogic.logic.Operator;
import barebonesinterpreter.codelogic.logic.Value;
import barebonesinterpreter.codelogic.logic.Variable;
import java.util.HashMap;

public class If extends Command {

    Block body;
    Block elseBody;
    Condition condition;

    public If(String conditionString, Block body) {
        this.body = body;
        condition = translateCondition(conditionString.substring(1, conditionString.length()));
        elseBody = new Block();
    }
    
    public If(String conditionString, Block body, Block elseBody) {
        this.body = body;
        condition = translateCondition(conditionString.substring(1, conditionString.length()));
        this.elseBody = elseBody;
    }

    public Condition translateCondition(String rawText) {
        rawText = rawText.replace(" ", "");
        if (rawText.contains("and") || rawText.contains("or")) {
            Condition exp1;
            String operator;
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
                exp1 = translateCondition(rawText.substring(1, charCounter));
                rawText = rawText.substring(charCounter + 1);
                operator = findFirstLogOp(rawText);
            } else {
                operator = findFirstLogOp(rawText);
                exp1 = translateCondition(rawText.split(operator)[0]);
            }
            Condition exp2;
            rawText = rawText.substring(operator.length());
            if (rawText.charAt(0) == '(') {
                exp2 = translateCondition(rawText.substring(1, rawText.length() - 1));
            } else {
                exp2 = translateCondition(rawText);
            }
            return new Logical(exp1, exp2, operator);
        } else {
            String operator = findFirstEqOp(rawText);
            String[] parts = rawText.split(operator);
            Expression exp1;
            Expression exp2;
            if (parts[0].charAt(0) == '(') {
                exp1 = translateExpression(parts[0].substring(1, parts[0].length() - 1));
            } else {
                exp1 = translateExpression(parts[0]);
            }
            if (parts[1].charAt(0) == '(') {
                exp2 = translateExpression(parts[1].substring(1, parts[0].length() - 1));
            } else {
                exp2 = translateExpression(parts[1]);
            }
            return new Equality(exp1, exp2, operator);
        }
    }

    public Expression translateExpression(String rawText) {
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
                exp1 = translateExpression(rawText.substring(1, charCounter));
                rawText = rawText.substring(charCounter + 1);
            } else {
                int firstOpIndex = findFirstOp(rawText);
                exp1 = translateExpression(rawText.substring(0, firstOpIndex));
                rawText = rawText.substring(firstOpIndex);
            }
            String operator = rawText.charAt(0) + "";
            rawText = rawText.substring(1);
            Expression exp2;
            if (rawText.charAt(0) == '(') {
                exp2 = translateExpression(rawText.substring(1, rawText.length() - 1));
            } else {
                exp2 = translateExpression(rawText);
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

    public String findFirstLogOp(String str) {
        for (int i = 0; i < str.length() - 2; i++) {
            if (str.substring(i, i + 3).equals("and")) {
                return str.substring(i, i + 3);
            } else if (str.substring(i, i + 2).equals("or")) {
                return str.substring(i, i + 2);
            }
        }
        return null;
    }

    public String findFirstEqOp(String str) {
        for (int i = 0; i < str.length() - 1; i++) {
            if (str.substring(i, i + 2).equals(">=") || str.substring(i, i + 2).equals("<=") || str.substring(i, i + 2).equals("==") || str.substring(i, i + 2).equals("!=")) {
                return str.substring(i, i + 2);
            } else if (str.substring(i, i + 1).equals(">") || str.substring(i, i + 1).equals("<")) {
                return str.substring(i, i + 1);
            }
        }
        return null;
    }

    public void execute(HashMap vars) {
        if (condition.getValue(vars)) {
            body.executeBlock(vars);
        } else {
            elseBody.executeBlock(vars);
        }
    }

}
