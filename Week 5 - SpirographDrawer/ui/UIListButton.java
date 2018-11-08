package spirographs.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class UIListButton extends UIObject {

    public String text;
    CurrentCursor cc;
    public double value;
    public double cursorX, cursorY;

    public Point lastPoint;
    public ArrayList<Line2D> curve;
    

    public UIListButton(float x, float y, int width, int height, String text, double value, CurrentCursor cc) {
        super(x, y, width, height);
        this.text = text;
        this.value = value;
        this.cc = cc;
        curve = new ArrayList();
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        if (cc.getCurrentCursor() == this) {
            g.setColor(Color.lightGray);
        }
        g.fillRect((int) x, (int) y, width, height);
        g.setFont(new Font("monospaced", Font.PLAIN, 30));
        g.setColor(Color.gray);
        if (cc.getCurrentCursor() == this) {
            g.setColor(Color.black);
        }
        g.drawRect((int) x, (int) y, width, height);
        g.setColor(Color.black);
        g.drawString(text, (int) x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), (int) y + (height / 2) + 10);
    }

    public void onClick() {
        cc.setCurrentCursor(this);
        cc.setJustChanged(true);
    }

    @Override
    public void onRClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
