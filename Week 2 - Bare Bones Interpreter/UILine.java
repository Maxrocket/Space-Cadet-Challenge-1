package barebonesinterpreter.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class UILine {

    ClickListener clicker;
    int lineNumber;
    public String text;
    UIEditor parent;
    int cursorPos;

    public UILine(UIEditor parent, int lineNumber, String initialText) {
        this.lineNumber = lineNumber;
        this.parent = parent;
        text = initialText;
        cursorPos = 0;
    }

    public void render(Graphics g, int x, int y) {
        g.setColor(Color.lightGray);
        g.fillRect(x, y, 40, 40);
        g.setFont(new Font("monospaced", Font.PLAIN, 30));
        g.setColor(Color.black);
        g.drawString(lineNumber + "", x + 20 - (g.getFontMetrics().stringWidth(lineNumber + "") / 2), y + 30);
        g.drawString(text, x + 50, y + 30);
    }
    
    public void renderCursor(Graphics g, int x, int y) {
        g.setColor(Color.black);
        g.fillRect(x + 50 + (18 * cursorPos), y + 5, 3, 30);
    }

    public void onClick() {
        parent.changeCurrentSelectedLine(this);
    }

    public void onRClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
