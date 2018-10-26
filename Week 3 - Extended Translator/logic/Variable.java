package barebonesinterpreter.codelogic.logic;

import java.util.HashMap;

public class Variable extends Expression {

    String variable;
    
    public Variable(String variable) {
        this.variable = variable;
    }
    
    public int getValue(HashMap vars) {
        return (int) vars.get(variable);
    }

    public String getString() {
        return variable;
    }
    
}
