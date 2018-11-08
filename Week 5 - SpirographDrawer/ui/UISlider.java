package spirographs.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.text.DecimalFormat;
import spirographs.input.MouseManager;

public class UISlider extends UIObject {

    public double min, max;
    public double value;
    int barWidth;
    String labelFormat;
    MouseManager mm;
    boolean trackMouse = false;

    public UISlider(float x, float y, int width, int height, double min, double max, double startValue, MouseManager mm) {
        super(x, y, width, height);
        this.min = min;
        this.max = max;
        this.value = startValue;
        labelFormat = max + "";
        this.mm = mm;
    }

    public UISlider(float x, float y, int width, int height, double min, double max, double startValue, String labelFormat, MouseManager mm) {
        super(x, y, width, height);
        this.min = min;
        this.max = max;
        this.value = startValue;
        this.labelFormat = labelFormat;
        this.mm = mm;
    }

    public void tick() {
        if (trackMouse) {
            if (!mm.isLeftPressed()) {
                trackMouse = false;
            } else {
                double x = mm.getMouseX() - this.x;
                value = ((x / barWidth) * (max - min)) + min;
                value = Math.min(max, value);
                value = Math.max(min, value);
            }
        } else {
            if (mm.isLeftJustPressed() && bounds.contains(new Point(mm.getMouseX(), mm.getMouseY()))) {
                trackMouse = true;
            }
        }
    }

    public void render(Graphics g) {
        barWidth = width - 20 - g.getFontMetrics(new Font("monospaced", Font.PLAIN, 30)).stringWidth(labelFormat);

        DecimalFormat df = new DecimalFormat(labelFormat);
        g.setColor(Color.white);
        g.fillRect((int) x, (int) y + (height / 2) - 10, barWidth, 20);
        g.setFont(new Font("monospaced", Font.PLAIN, 30));
        g.setColor(Color.gray);
        g.drawRect((int) x, (int) y + (height / 2) - 10, barWidth, 20);
        g.setColor(Color.black);
        g.drawString(df.format(value), (int) x + barWidth + 20, (int) y + (height / 2) + 10);

        g.setColor(Color.white);
        int offset = (int) ((value / (max - min)) * barWidth);
        g.fillRect((int) x + offset - 10, (int) y + (height / 2) - 30, 20, 60);
        g.setColor(Color.gray);
        g.drawRect((int) x + offset - 10, (int) y + (height / 2) - 30, 20, 60);
    }

    @Override
    public void onRClick() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onClick() {
        trackMouse = true;
    }

}
