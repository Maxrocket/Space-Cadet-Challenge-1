package spirographs.states;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import spirographs.Handler;
import spirographs.ui.ClickListener;
import spirographs.ui.UIButton;
import spirographs.ui.UICheckBox;
import spirographs.ui.UIListButton;
import spirographs.ui.UIManager;
import spirographs.ui.UISlider;
import spirographs.ui.CurrentCursor;

public class CanvasState extends State {

    private UIManager uiManager;
    private int canvasWidth, canvasCenterX, canvasCenterY;
    private double circlePos, circleRot, circleX, circleY;

    private int speed, cursorCount = 1;
    private double circleSize, largeCircleSize, mod;

    private boolean running = false, viewCircles = true;

    private UISlider circleSizeSlider, cursorSizeSlider, speedSlider;
    private UICheckBox viewCirclesCheckBox, outerRunningCheckBox;

    private CurrentCursor currentSelectedCursor;
    private ArrayList<UIListButton> cursorButtons = new ArrayList();

    public CanvasState(final Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUIManager(uiManager);
        canvasWidth = handler.getHeight() - 100;
        canvasCenterX = 50 + (canvasWidth / 2);
        canvasCenterY = 50 + (canvasWidth / 2);

        currentSelectedCursor = new CurrentCursor(null);
        cursorButtons.add(new UIListButton(100 + canvasWidth, 550, handler.getWidth() - canvasWidth - 150, 40, "Cursor 1", 0.5, currentSelectedCursor));
        currentSelectedCursor.setCurrentCursor(cursorButtons.get(0));
        addUIList();

        uiManager.addUIObject(new UIButton(100 + canvasWidth, handler.getHeight() - 130, handler.getWidth() - canvasWidth - 150, 80, "START / STOP", new ClickListener() {
            public void onClick() {
                running = !running;
                if (running) {
                    for (UIListButton cursorButton : cursorButtons) {
                        cursorButton.curve.clear();
                    }
                }
            }
        }));
        circleSizeSlider = new UISlider(100 + canvasWidth, 50, handler.getWidth() - canvasWidth - 150, 80, 0.0, 1.0, 4.0 / 5.0, "0.00", handler.getMouseManager());
        cursorSizeSlider = new UISlider(100 + canvasWidth, 150, handler.getWidth() - canvasWidth - 150, 80, 0.0, 1.0, 0.5, "0.00", handler.getMouseManager());
        speedSlider = new UISlider(100 + canvasWidth, 250, handler.getWidth() - canvasWidth - 150, 80, 1.0, 1000.0, 1.0, "###0", handler.getMouseManager());
        viewCirclesCheckBox = new UICheckBox(100 + canvasWidth, 350, handler.getWidth() - canvasWidth - 150, 40, "View Circles");
        outerRunningCheckBox = new UICheckBox(100 + canvasWidth, 400, handler.getWidth() - canvasWidth - 150, 40, "Run On Outside");
        uiManager.addUIObject(circleSizeSlider);
        uiManager.addUIObject(cursorSizeSlider);
        uiManager.addUIObject(speedSlider);
        uiManager.addUIObject(viewCirclesCheckBox);
        uiManager.addUIObject(outerRunningCheckBox);
        uiManager.addUIObject(new UIButton(100 + canvasWidth, 450, handler.getWidth() - canvasWidth - 150, 40, "ADD", new ClickListener() {
            public void onClick() {
                if (cursorCount < 8) {
                    cursorCount++;
                    removeUIList();
                    cursorButtons.add(new UIListButton(100 + canvasWidth, 500 + (50 * cursorCount), handler.getWidth() - canvasWidth - 150, 40, "Cursor " + cursorCount, 0.5, currentSelectedCursor));
                    addUIList();
                }
            }
        }));
        uiManager.addUIObject(new UIButton(100 + canvasWidth, 500, handler.getWidth() - canvasWidth - 150, 40, "REMOVE", new ClickListener() {
            public void onClick() {
                if (cursorCount > 1) {
                    cursorCount--;
                    removeUIList();
                    cursorButtons.remove(cursorCount);
                    addUIList();
                }
            }
        }));
    }

    public void removeUIList() {
        for (UIListButton cursorButton : cursorButtons) {
            uiManager.removeUIObject(cursorButton);
        }
    }

    public void addUIList() {
        for (UIListButton cursorButton : cursorButtons) {
            uiManager.addUIObject(cursorButton);
        }
    }

    public void tick() {
        uiManager.tick();
        if (currentSelectedCursor.isJustChanged()) {
            cursorSizeSlider.value = currentSelectedCursor.getCurrentCursor().value;
        }
        currentSelectedCursor.setJustChanged(false);

        if (!running) {
            circleSize = circleSizeSlider.value;
            currentSelectedCursor.getCurrentCursor().value = cursorSizeSlider.value;
            circlePos = 0.0;
            circleRot = 0.0;
            viewCircles = true;
            mod = -1.0;
            largeCircleSize = canvasWidth;

            if (outerRunningCheckBox.value) {
                mod = 1.0;
                largeCircleSize = (canvasWidth + 0.0) / ((2.0 * circleSize) + 1.0);
                circleSizeSlider.max = 2.0;
            } else {
                circleSizeSlider.max = 1.0;
                if (circleSizeSlider.value > 1.0) {
                    circleSizeSlider.value = 1.0;
                }
            }
        }

        double circleCircum = (largeCircleSize * circleSize);
        circleX = canvasCenterX + (((largeCircleSize / 2.0) + (mod * (circleCircum / 2.0))) * Math.sin(circlePos));
        circleY = canvasCenterY + (((largeCircleSize / 2.0) + (mod * (circleCircum / 2.0))) * Math.cos(circlePos));
        double arcLength = (circlePos / (2.0 * Math.PI)) * largeCircleSize;
        circleRot = ((arcLength / circleCircum) * 2.0 * mod * Math.PI) + circlePos;

        for (UIListButton cursorButton : cursorButtons) {
            cursorButton.cursorX = circleX + ((circleCircum / 2.0) * cursorButton.value * Math.sin(circleRot));
            cursorButton.cursorY = circleY + ((circleCircum / 2.0) * cursorButton.value * Math.cos(circleRot));
        }

        if (running) {
            viewCircles = viewCirclesCheckBox.value;
            speed = (int) speedSlider.value;

            for (int i = 0; i < speed; i++) {
                for (UIListButton cursorButton : cursorButtons) {
                    cursorButton.lastPoint = new Point((int) cursorButton.cursorX, (int) cursorButton.cursorY);
                }

                circlePos += Math.toRadians(1);

                circleCircum = (largeCircleSize * circleSize);
                circleX = canvasCenterX + (((largeCircleSize / 2.0) + (mod * (circleCircum / 2.0))) * Math.sin(circlePos));
                circleY = canvasCenterY + (((largeCircleSize / 2.0) + (mod * (circleCircum / 2.0))) * Math.cos(circlePos));
                arcLength = (circlePos / (2.0 * Math.PI)) * largeCircleSize;
                circleRot = ((arcLength / circleCircum) * 2.0 * mod * Math.PI) + circlePos;

                for (UIListButton cursorButton : cursorButtons) {
                    cursorButton.cursorX = circleX + ((circleCircum / 2.0) * cursorButton.value * Math.sin(circleRot));
                    cursorButton.cursorY = circleY + ((circleCircum / 2.0) * cursorButton.value * Math.cos(circleRot));

                    cursorButton.curve.add(new Line2D.Double(cursorButton.lastPoint, new Point((int) cursorButton.cursorX, (int) cursorButton.cursorY)));
                }

            }
        }

    }

    public void render(Graphics g) {
        uiManager.render(g);
        g.setColor(Color.white);
        g.fillRect(50, 50, canvasWidth, canvasWidth);
        g.setColor(Color.gray);
        g.drawRect(50, 50, canvasWidth, canvasWidth);
        if (viewCircles) {
            g.setColor(Color.black);
            g.fillOval((int) canvasCenterX - 5, (int) canvasCenterY - 5, 10, 10);
            g.fillOval((int) circleX - 5, (int) circleY - 5, 10, 10);
            g.drawOval(canvasCenterX - (int) (largeCircleSize / 2.0), canvasCenterY - (int) (largeCircleSize / 2.0), (int) largeCircleSize, (int) largeCircleSize);
            g.drawOval((int) (circleX - ((largeCircleSize * circleSize) / 2.0)), (int) (circleY - ((largeCircleSize * circleSize) / 2.0)), (int) (largeCircleSize * circleSize), (int) (largeCircleSize * circleSize));
        }

        int count = 0;
        try {
            for (UIListButton cursorButton  : cursorButtons) {
                g.setColor(Color.getHSBColor((1.0f / 8.0f) * count, 1.0f, 1.0f));
                g.fillOval((int) cursorButton.cursorX - 5, (int) cursorButton.cursorY - 5, 10, 10);
                try {
                    for (Line2D line : cursorButton.curve) {
                        g.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
                    }
                } catch (ConcurrentModificationException ex) {

                }
                count++;
            }

        } catch (ConcurrentModificationException ex) {

        }
    }

    public void init() {
        handler.getMouseManager().setUIManager(uiManager);
    }
}
