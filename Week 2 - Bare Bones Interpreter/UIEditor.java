package barebonesinterpreter.ui;

import barebonesinterpreter.Handler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class UIEditor extends UIObject {

    public ArrayList<UILine> textLines;
    ClickListener clicker;
    UILine currentLine;
    int cursorAnim;
    Handler handler;
    UIEditor editor;

    public UIEditor(Handler handler, float x, float y, int width, int height) {
        super(x, y, width, height);
        textLines = new ArrayList();
        editor = this;
        UILine line = new UILine(editor, 0, "");
        textLines.add(line);
        currentLine = line;
        cursorAnim = 0;
        this.handler = handler;
        handler.getGame().getDisplay().getFrame().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {

            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    try {
                        currentLine.text = currentLine.text.substring(0, currentLine.cursorPos - 1) + currentLine.text.substring(currentLine.cursorPos, currentLine.text.length());
                        if (currentLine.cursorPos > 0) {
                            currentLine.cursorPos--;
                        }
                        return;
                    } catch (StringIndexOutOfBoundsException ex) {
                        return;
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_DELETE) {
                    try {
                        currentLine.text = currentLine.text.substring(0, currentLine.cursorPos) + currentLine.text.substring(currentLine.cursorPos + 1, currentLine.text.length());
                        return;
                    } catch (StringIndexOutOfBoundsException ex) {
                        return;
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                    int newIndex = currentLine.lineNumber + 1;
                    UILine newLine = new UILine(editor, newIndex, currentLine.text.substring(currentLine.cursorPos, currentLine.text.length()));
                    textLines.add(newIndex, newLine);
                    currentLine.text = currentLine.text.substring(0, currentLine.cursorPos);
                    currentLine = newLine;
                    for (int i = newIndex + 1; i < textLines.size(); i++) {
                        textLines.get(i).lineNumber = textLines.get(i).lineNumber + 1;
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (currentLine.cursorPos > 0) {
                        currentLine.cursorPos--;
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (currentLine.cursorPos < currentLine.text.length()) {
                        currentLine.cursorPos++;
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_UP) {
                    if (currentLine.lineNumber - 1 >= 0) {
                        textLines.get(currentLine.lineNumber - 1).cursorPos = currentLine.cursorPos;
                        currentLine = textLines.get(currentLine.lineNumber - 1);
                        if (currentLine.cursorPos > currentLine.text.length()) {
                            currentLine.cursorPos = currentLine.text.length();
                        }
                    }
                } else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (currentLine.lineNumber + 1 < textLines.size()) {
                        textLines.get(currentLine.lineNumber + 1).cursorPos = currentLine.cursorPos;
                        currentLine = textLines.get(currentLine.lineNumber + 1);
                        if (currentLine.cursorPos > currentLine.text.length()) {
                            currentLine.cursorPos = currentLine.text.length();
                        }
                    }
                } else {
                    if (ke.getKeyCode() >= 32) {
                        currentLine.text = currentLine.text.substring(0, currentLine.cursorPos) + ke.getKeyChar() + currentLine.text.substring(currentLine.cursorPos, currentLine.text.length());
                        currentLine.cursorPos++;
                    }
                }
                cursorAnim = 0;
            }

            @Override
            public void keyReleased(KeyEvent ke) {

            }
        }
        );
    }

    public void tick() {
        cursorAnim++;
        if (cursorAnim >= 120) {
            cursorAnim = 0;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int) x, (int) y, width, height);
        int offset = 0;
        try {
            for (UILine textLine : textLines) {
                textLine.render(g, (int) x, (int) y + offset);
                if (cursorAnim < 60 && textLine == currentLine) {
                    textLine.renderCursor(g, (int) x, (int) y + offset);
                }
                offset += 40;
            }
        } catch (java.util.ConcurrentModificationException e) {

        }
        g.setColor(Color.gray);
        g.drawRect((int) x, (int) y, width, height);
    }

    public void onClick() {
        clicker.onClick();
    }

    public void changeCurrentSelectedLine(UILine line) {
        currentLine = line;
    }

    @Override
    public void onRClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
