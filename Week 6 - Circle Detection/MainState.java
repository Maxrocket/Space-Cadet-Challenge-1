package circldetection.states;

import java.awt.Graphics;
import circldetection.Handler;
import circldetection.gfx.Assets;
import circldetection.gfx.Circle;
import circldetection.ui.UIManager;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainState extends State {

    private UIManager uiManager;
    private double scale = 1.5;
    private BufferedImage originalImage, greyScaleImage, edgedImage;
    private ArrayList<Circle> circles;
    private ArrayList<Point> pointsToCheck;

    public MainState(final Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUIManager(uiManager);
    }

    public void tick() {
        uiManager.tick();
    }

    public void render(Graphics g) {
        uiManager.render(g);
        g.drawImage(originalImage, 0, 0, (int) (originalImage.getWidth() * scale), (int) (originalImage.getHeight() * scale), null);
        g.drawImage(greyScaleImage, (int) (originalImage.getWidth() * scale), 0, (int) (originalImage.getWidth() * scale), (int) (originalImage.getHeight() * scale), null);
        g.drawImage(edgedImage, 0, (int) (originalImage.getHeight() * scale), (int) (originalImage.getWidth() * scale), (int) (originalImage.getHeight() * scale), null);
        g.drawImage(edgedImage, (int) (originalImage.getWidth() * 2.0 * scale), 0, (int) (originalImage.getWidth() * scale), (int) (originalImage.getHeight() * scale), null);
        g.drawImage(originalImage, (int) (originalImage.getWidth() * scale), (int) (originalImage.getHeight() * scale), (int) (originalImage.getWidth() * scale), (int) (originalImage.getHeight() * scale), null);
        g.setColor(Color.magenta);
        for (Point point : pointsToCheck) {
            g.fillRect((int) ((point.x - 1.0) * scale), (int) ((point.y - 1.0) * scale) + (int) (originalImage.getHeight() * scale), (int) (scale *3.0), (int)(scale *3.0));
        }
        for (Circle circle : circles) {
            drawCenteredCircle(g, (int) (circle.x * scale + originalImage.getWidth() * scale), (int) (circle.y * scale + originalImage.getHeight() * scale), (int) (circle.r * scale));
        }
    }

    public void init() {
        scale = 540.0 / Assets.circleImage.getHeight();
        handler.getMouseManager().setUIManager(uiManager);
        originalImage = Assets.circleImage;
        greyScaleImage = toGreyScale(originalImage);
        edgedImage = toEdgeImage(greyScaleImage);
        circles = findCircles(edgedImage);
    }

    public BufferedImage toGreyScale(BufferedImage img) {
        BufferedImage newImage = copyImage(img);
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                Color c = new Color(newImage.getRGB(x, y));
                int avg = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                Color nc = new Color(avg, avg, avg);
                newImage.setRGB(x, y, nc.getRGB());
            }
        }

        return newImage;
    }

    public BufferedImage toEdgeImage(BufferedImage img) {
        BufferedImage newImage = copyImage(img);
        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                if (x == 0 || y == 0 || x == newImage.getWidth() - 1 || y == newImage.getHeight() - 1) {
                    Color c = new Color(0, 0, 0);
                    newImage.setRGB(x, y, c.getRGB());
                } else {
                    int c11 = new Color(img.getRGB(x - 1, y - 1)).getRed();
                    int c12 = new Color(img.getRGB(x - 1, y)).getRed();
                    int c13 = new Color(img.getRGB(x - 1, y + 1)).getRed();
                    int c21 = new Color(img.getRGB(x, y - 1)).getRed();
                    int c22 = new Color(img.getRGB(x, y)).getRed();
                    int c23 = new Color(img.getRGB(x, y + 1)).getRed();
                    int c31 = new Color(img.getRGB(x + 1, y - 1)).getRed();
                    int c32 = new Color(img.getRGB(x + 1, y)).getRed();
                    int c33 = new Color(img.getRGB(x + 1, y + 1)).getRed();
                    int[][] pixels = new int[][]{new int[]{c11, c12, c13}, new int[]{c21, c22, c23}, new int[]{c31, c32, c33}};
                    int[][] gx = new int[][]{new int[]{1, 2, 1}, new int[]{0, 0, 0}, new int[]{-1, -2, -1}};
                    int[][] gy = new int[][]{new int[]{1, 0, -1}, new int[]{2, 0, -2}, new int[]{1, 0, -1}};
                    int g = (int) Math.sqrt(Math.pow(matrixConvolution(pixels, gx) / 4.5, 2) + Math.pow(matrixConvolution(pixels, gy) / 4.5, 2));
                    Color c = new Color(g, g, g);
                    newImage.setRGB(x, y, c.getRGB());
                }
            }
        }
        BufferedImage thinImage = copyImage(newImage);
//        for (int x = 1; x < thinImage.getWidth() - 1; x++) {
//            for (int y = 1; y < thinImage.getHeight() - 1; y++) {
//                int c = new Color(newImage.getRGB(x, y)).getRed();
//                if (c == 0) {
//                    thinImage.setRGB(x - 1, y - 1, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x - 1, y, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x - 1, y + 1, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x, y - 1, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x, y, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x, y + 1, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x + 1, y - 1, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x + 1, y, new Color(0, 0, 0).getRGB());
//                    thinImage.setRGB(x + 1, y + 1, new Color(0, 0, 0).getRGB());
//                }
//            }
//        }
        return thinImage;
    }

    public ArrayList<Circle> findCircles(BufferedImage img) {
        int threshold = 100;
        ArrayList<Circle> circles = new ArrayList();
        pointsToCheck = new ArrayList();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int c = new Color(img.getRGB(x, y)).getRed();
                if (c >= threshold) {
                    pointsToCheck.add(new Point(x, y));
                }
            }
        }

        System.out.println("Points To Check: " + pointsToCheck.size());

        int totalChecks = 0;
        for (int j = 0; j < pointsToCheck.size(); j++) {
            for (int i = 1 + j; i < pointsToCheck.size(); i++) {
                totalChecks++;
            }
        }
        System.out.println(totalChecks);

        double lastPercent = 0.0;
        int pointsChecked = 0;
        int checks = 0;
        long lastTime = System.currentTimeMillis();
        long speed = 0;
        for (Point point : pointsToCheck) {
            int x = point.x;
            int y = point.y;
            for (int i = 1 + pointsChecked; i < pointsToCheck.size(); i++) {
                int x2 = pointsToCheck.get(i).x;
                int y2 = pointsToCheck.get(i).y;
                double d = Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2));
                if (d >= Math.min(img.getWidth(), img.getHeight()) / 10.0) {
                    double mx = (x + x2) / 2.0;
                    double my = (y + y2) / 2.0;
                    double mr = d / 2.0;
                    Circle circle = new Circle(mx, my, d);
                    if (!contains(circles, circle)) {
                        if (mx >= mr && mx <= img.getWidth() - 1 - mr && my >= mr && my <= img.getHeight() - 1 - mr) {
                            double value = 0.0;
                            int count = 0;
                            int nx = (int) mr - 1;
                            int ny = 0;
                            int dx = 1, dy = 1;
                            int err = dx - (int) mr * 2;
                            while (nx > ny) {
                                count++;
                                value += new Color(img.getRGB((int) mx + nx, (int) my + ny)).getRed();
                                value += new Color(img.getRGB((int) mx + nx, (int) my - ny)).getRed();
                                value += new Color(img.getRGB((int) mx - nx, (int) my + ny)).getRed();
                                value += new Color(img.getRGB((int) mx - nx, (int) my - ny)).getRed();
                                value += new Color(img.getRGB((int) mx + ny, (int) my + nx)).getRed();
                                value += new Color(img.getRGB((int) mx + ny, (int) my - nx)).getRed();
                                value += new Color(img.getRGB((int) mx - ny, (int) my + nx)).getRed();
                                value += new Color(img.getRGB((int) mx - ny, (int) my - nx)).getRed();
                                if (err <= 0) {
                                    ny++;
                                    err += dy;
                                    dy += 2;
                                }
                                if (err > 0) {
                                    nx--;
                                    dx += 2;
                                    err += dx - (int) mr * 2;
                                }
                            }
                            value /= count;
                            value /= 4.0;
                            if (value > 150) {
                                circles.add(circle);
                            }
                        }
                    }
                }

                checks++;
            }

            speed = System.currentTimeMillis() - lastTime;
            double percent = ((checks + 0.0) / (totalChecks + 0.0)) * 100.0;
            if (percent - lastPercent > 1.0) {
                lastPercent = percent;
                int pointsRemaining = totalChecks - checks;
                long remainingTime = (long) ((pointsRemaining + 0.0) * ((speed + 0.0) / (checks + 0.0)));
                Date timeRemaining = new Date(remainingTime);
                DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                String timeRemainingFormatted = formatter.format(timeRemaining);
                System.out.println(percent + "% - Time Remaining: " + timeRemainingFormatted);
            }

            pointsChecked++;

        }

        speed = System.currentTimeMillis() - lastTime;
        Date timeTaken = new Date(speed);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timeTakenFormatted = formatter.format(timeTaken);
        System.out.println("Time Taken: " + timeTakenFormatted);

        return circles;
    }

    public void drawCenteredCircle(Graphics g, int x, int y, int r) {
        x = x - (r / 2);
        y = y - (r / 2);
        g.drawOval(x, y, r, r);
    }

    public int matrixConvolution(int[][] matrix, int[][] matrix2) {
        int value = 0;
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                value += matrix[x][y] * matrix2[x][y];
            }
        }
        return value;
    }

    public BufferedImage copyImage(BufferedImage source) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public boolean contains(ArrayList<Circle> circles, Circle circle) {
        for (Circle circle1 : circles) {
            if (circle.x == circle1.x && circle.y == circle1.y && circle.r == circle1.r) {
                return true;
            }
        }
        return false;
    }
}
