package barebonesinterpreter;

import java.util.HashMap;

public class Interpreter {

    String[] code;
    public HashMap variables;
    int programCounter;
    public String variableList;

    public Interpreter() {

    }

    public void executeCode(String rawCode) {
        variables = new HashMap();
        variableList = "";
        programCounter = 0;
        code = rawCode.split(";");
        for (int i = 0; i < code.length; i++) {
            while (code[i].charAt(0) == ' ') {
                code[i] = code[i].substring(3, code[i].length());
            }
        }
        while (programCounter < code.length) {
            String[] param = code[programCounter].split(" ");
            switch (param[0]) {
                case "clear":
                    variables.put(param[1], 0);
                    variableList += param[1] + ",";
                    break;
                case "incr":
                    variables.put(param[1], (int) variables.get(param[1]) + 1);
                    break;
                case "decr":
                    variables.put(param[1], (int) variables.get(param[1]) - 1);
                    break;
                case "end":
                    int tempCounter = programCounter;
                    int whileCount = 1;
                    do {
                        tempCounter--;
                        param = code[tempCounter].split(" ");
                        if (param[0].equals("end")) {
                            whileCount++;
                        } else if (param[0].equals("while")) {
                            whileCount--;
                        }
                    } while (whileCount > 0);
                    int value = (int) variables.get(param[1]);
                    if (value != 0) {
                        programCounter = tempCounter;
                    }
                    break;
            }
            programCounter++;
        }
    }

}
