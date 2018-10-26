package barebonesinterpreter.codelogic.logic;

import java.util.HashMap;
import java.util.Map;

public class Equality extends Condition {

    Expression expression1, expression2;
    String operator;
    Map<String, OperatorMethod> opMap;

    public Equality(Expression expression1, Expression expression2, String operator) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.operator = operator;
        
        opMap = new HashMap<String, OperatorMethod>();
        opMap.put(">", new OperatorMethod() {
            @Override
            public boolean getValue(int a, int b) {
                return a > b;
            }
        });
        opMap.put("<", new OperatorMethod() {
            @Override
            public boolean getValue(int a, int b) {
                return a < b;
            }
        });
        opMap.put(">=", new OperatorMethod() {
            @Override
            public boolean getValue(int a, int b) {
                return a >= b;
            }
        });
        opMap.put("<=", new OperatorMethod() {
            @Override
            public boolean getValue(int a, int b) {
                return a <= b;
            }
        });
        opMap.put("==", new OperatorMethod() {
            @Override
            public boolean getValue(int a, int b) {
                return a == b;
            }
        });
        opMap.put("!=", new OperatorMethod() {
            @Override
            public boolean getValue(int a, int b) {
                return a != b;
            }
        });
    }

    public boolean getValue(HashMap vars) {
        return opMap.get(operator).getValue(expression1.getValue(vars), expression2.getValue(vars));
    }

    abstract class OperatorMethod {
        abstract boolean getValue(int a, int b);
    }

}
