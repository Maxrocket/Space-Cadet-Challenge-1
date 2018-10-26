package barebonesinterpreter.codelogic.logic;

import java.util.HashMap;
import java.util.Map;

public class Operator extends Expression {

    Expression expression1;
    Expression expression2;
    String operation;
    Map<String, OperatorMethod> opMap;

    public Operator(Expression expression1, Expression expression2, String operation) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operation = operation;

        opMap = new HashMap<String, OperatorMethod>();
        opMap.put("+", new OperatorMethod() {
            @Override
            public int getValue(int a, int b) {
                return a + b;
            }
        });
        opMap.put("-", new OperatorMethod() {
            @Override
            public int getValue(int a, int b) {
                return a - b;
            }
        });
        opMap.put("/", new OperatorMethod() {
            @Override
            public int getValue(int a, int b) {
                return a / b;
            }
        });
        opMap.put("*", new OperatorMethod() {
            @Override
            public int getValue(int a, int b) {
                return a * b;
            }
        });
    }

    public int getValue(HashMap vars) {
        return opMap.get(operation).getValue(expression1.getValue(vars), expression2.getValue(vars));
    }

    public String getString() {
        return "(" + expression1.getString() + " #" + operation + "# " + expression2.getString() + ")";
    }

    abstract class OperatorMethod {
        abstract int getValue(int a, int b);
    }

}
