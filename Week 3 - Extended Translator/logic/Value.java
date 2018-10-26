package barebonesinterpreter.codelogic.logic;

import java.util.HashMap;

public class Value extends Expression {

    int value;
    
    public Value(int value) {
        this.value = value;
    }
    
    public int getValue(HashMap vars) {
        return value;
    }

    public String getString() {
        return value + "";
    }
    
}
