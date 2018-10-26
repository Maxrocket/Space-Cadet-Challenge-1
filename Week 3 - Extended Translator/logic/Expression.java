package barebonesinterpreter.codelogic.logic;

import java.util.HashMap;

public abstract class Expression {
    
    public abstract int getValue(HashMap vars);
    public abstract String getString();
    
}
