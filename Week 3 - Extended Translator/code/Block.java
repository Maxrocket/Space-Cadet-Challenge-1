package barebonesinterpreter.codelogic.code;

import java.util.ArrayList;
import java.util.HashMap;

public class Block {
    
    ArrayList<Command> commands;
    
    public Block() {
        commands = new ArrayList();
    }
    
    public void executeBlock(HashMap vars) {
        for (Command command : commands) {
            command.execute(vars);
        }
    }
    
    public void add(Command command) {
        commands.add(command);
    }
    
}
