package barebonesinterpreter.codelogic.logic;

import java.util.HashMap;
import java.util.Map;

public class Logical extends Condition {

    Condition condition1, condition2;
    String operator;
    Map<String, OperatorMethod> opMap;

    public Logical(Condition condition1, Condition condition2, String operator) {
        this.condition1 = condition1;
        this.condition2 = condition2;
        this.operator = operator;
        
        opMap = new HashMap<String, OperatorMethod>();
        opMap.put("and", new OperatorMethod() {
            @Override
            public boolean getValue(boolean a, boolean b) {
                return a && b;
            }
        });
        opMap.put("or", new OperatorMethod() {
            @Override
            public boolean getValue(boolean a, boolean b) {
                return a || b;
            }
        });
    }

    public boolean getValue(HashMap vars) {
        return opMap.get(operator).getValue(condition1.getValue(vars), condition2.getValue(vars));
    }

    abstract class OperatorMethod {
        abstract boolean getValue(boolean a, boolean b);
    }

}
