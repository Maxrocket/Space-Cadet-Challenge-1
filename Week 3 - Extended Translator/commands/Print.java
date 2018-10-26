package barebonesinterpreter.codelogic.commands;

import barebonesinterpreter.codelogic.code.Command;
import barebonesinterpreter.ui.UIConsole;
import java.util.HashMap;

public class Print extends Command {

    UIConsole uiConsole;
    String text;

    public Print(String text, UIConsole uiConsole) {
        this.text = text;
        this.uiConsole = uiConsole;
    }

    public void execute(HashMap vars) {
        String[] parts = text.split(" & ");
        String finalOutput = "";
        for (String part : parts) {
            if (part.charAt(0) == '\"') {
                finalOutput += part.substring(1, part.length() - 1);
            } else {
                finalOutput += vars.get(part);
            }
        }
        uiConsole.add(finalOutput);
    }

}
