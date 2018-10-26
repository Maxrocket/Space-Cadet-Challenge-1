package barebonesinterpreter.codelogic.code;

import java.util.HashMap;

public abstract class Command {
    
    public abstract void execute(HashMap vars);
    
}
