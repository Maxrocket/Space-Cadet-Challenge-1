package barebonesinterpreter.codelogic;

import barebonesinterpreter.codelogic.code.Block;
import barebonesinterpreter.codelogic.commands.Assign;
import barebonesinterpreter.codelogic.commands.Create;
import barebonesinterpreter.codelogic.commands.If;
import barebonesinterpreter.codelogic.commands.Print;
import barebonesinterpreter.codelogic.commands.While;
import barebonesinterpreter.ui.UIConsole;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {

    String[] code;
    public HashMap variables;
    int indent;
    UIConsole uiConsole;

    public Interpreter(UIConsole uiConsole) {
        this.uiConsole = uiConsole;
    }

    public void executeCode(String rawCode) {
        variables = new HashMap();
        indent = 0;
        code = rawCode.split(";");
        for (int i = 0; i < code.length; i++) {
            while (code[i].charAt(0) == ' ') {
                code[i] = code[i].substring(3, code[i].length());
            }
        }
        ArrayList<Block> codeBlocks = new ArrayList();
        Block main = new Block();
        codeBlocks.add(main);
        for (String line : code) {
            if (line.split(" ")[0].equals("end")) {
                codeBlocks.remove(indent);
                indent--;
            } else if (line.split(" ")[1].equals("=")) {
                String[] args = line.split(" =");
                codeBlocks.get(indent).add(new Assign(args[0], args[1]));
            } else if (line.split(" ")[0].equals("print")) {
                codeBlocks.get(indent).add(new Print(line.substring(6, line.length()), uiConsole));
            } else if (line.split(" ")[0].equals("if")) {
                Block ifBody = new Block();
                codeBlocks.get(indent).add(new If(line.substring(3, line.length() - 1), ifBody));
                indent++;
                codeBlocks.add(ifBody);
            } else if (line.split(" ")[0].equals("ifelse")) {
                Block ifBody = new Block();
                Block elseBody = new Block();
                codeBlocks.get(indent).add(new If(line.substring(7, line.length() - 1), ifBody, elseBody));
                indent+= 2;
                codeBlocks.add(elseBody);
                codeBlocks.add(ifBody);
            } else if (line.split(" ")[0].equals("while")) {
                Block ifBody = new Block();
                codeBlocks.get(indent).add(new While(line.substring(6, line.length() - 1), ifBody));
                indent++;
                codeBlocks.add(ifBody);
            } else if (line.split(" ")[0].equals("create")) {
                String[] args = line.split(" ");
                if (args.length == 2) {
                    codeBlocks.get(indent).add(new Create(args[1]));
                } else {
                    codeBlocks.get(indent).add(new Create(args[1], Integer.parseInt(args[2])));
                }
            }
        }
        main.executeBlock(variables);
    }

}
