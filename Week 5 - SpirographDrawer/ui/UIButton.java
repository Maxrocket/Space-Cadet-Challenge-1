package spirographs.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class UIButton extends UIObject {

    String text;
    ClickListener clicker;

    public UIButton(float x, float y, int width, int height, String text, ClickListener clicker) {
        super(x, y, width, height);
        this.text = text;
        this.clicker = clicker;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillRect((int) x, (int) y, width, height);
        g.setFont(new Font("monospaced", Font.PLAIN, 30));
        g.setColor(Color.gray);
        g.drawRect((int) x, (int) y, width, height);
        g.setColor(Color.black);
        g.drawString(text, (int) x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), (int) y + (height / 2) + 10);
    }

    public void onClick() {
        clicker.onClick();
    }

    @Override
    public void onRClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
