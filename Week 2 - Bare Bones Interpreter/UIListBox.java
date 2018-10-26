package barebonesinterpreter.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class UIListBox extends UIObject {

    public ArrayList<String> list;

    public UIListBox(float x, float y, int width, int height) {
        super(x, y, width, height);
        list = new ArrayList();
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int) x, (int) y, width, height);
        g.setFont(new Font("monospaced", Font.PLAIN, 30));
        g.setColor(Color.black);
        for (int i = 0; i < list.size(); i++) {
            g.drawString(list.get(i), (int) x + 10, (int) y + 30 * (i + 1));
        }
        g.setColor(Color.gray);
        g.drawRect((int) x, (int) y, width, height);
    }

    public void clear() {
        list.clear();
    }

    public void add(String text) {
        list.add(text);
    }

    public void onClick() {

    }

    @Override
    public void onRClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
