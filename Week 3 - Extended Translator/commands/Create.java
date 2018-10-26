package barebonesinterpreter.codelogic.commands;

import barebonesinterpreter.codelogic.code.Command;
import java.util.HashMap;

public class Create extends Command {

    String variableName;
    int variableValue;

    public Create(String variableName) {
        this.variableName = variableName;
        variableValue = 0;
    }
    
    public Create(String variableName, int variableValue) {
        this.variableName = variableName;
        this.variableValue = variableValue;
    }

    public void execute(HashMap vars) {
        vars.put(variableName, variableValue);
    }

}
