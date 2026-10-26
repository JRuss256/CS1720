import edu.cymbal.graphics.CanvasAPI;
import edu.cymbal.graphics.Graphics;
import edu.cymbal.graphics.PainterAPI;
import java.awt.Color;

/**
 * A colorful aquarium drawn with the class Painter library.
 * Run with painter-canvas-17.jar on the classpath.
 */
public class Aquarium {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Color WATER = new Color(8, 48, 92);
    private static final Color SAND = new Color(218, 185, 111);
    private static final Color WHITE = new Color(245, 245, 230);

    public static void main(String[] args) {
        CanvasAPI canvas = Graphics.createCanvas(WIDTH, HEIGHT, "Moonlit Aquarium");
        canvas.setBackgroundColor(WATER);
        PainterAPI painter = canvas.getPainter();
        painter.setSpeed(0);

        Thread animation = new Thread(() -> {
            int frame = 0;
            while (!Thread.currentThread().isInterrupted()) {
                painter.clear();
                drawAquarium(painter, frame++);
                try {
                    Thread.sleep(80);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "aquarium-animation");
        animation.setDaemon(true);
        animation.start();
        canvas.awaitClose();
        animation.interrupt();
    }

    /** Draws the complete scene for one animation frame. */
    private static void drawAquarium(PainterAPI p, int frame) {
        drawSand(p);
        drawRock(p, 93, 465, 1.0);
        drawSeaweed(p, 180, 500, 1.0, new Color(36, 160, 104));
        drawSeaweed(p, 222, 500, 0.72, new Color(63, 190, 122));
        drawSeaweed(p, 700, 500, 1.15, new Color(32, 142, 94));
        drawCoral(p, 580, 495, 1.0, new Color(229, 92, 118));

        // Each fish swims back and forth, turning around when it reaches a wall.
        for (int i = 0; i < 4; i++) {
            double scale = 0.72 + i * 0.09;
            double margin = 65 * scale; // tail length, the fish's farthest reach from center
            double range = WIDTH - 2 * margin;
            double travel = (frame * 2.0 + i * 175) % (2 * range);
            boolean facesRight = travel < range;
            double x = facesRight ? margin + travel : WIDTH - margin - (travel - range);
            double y = 150 + i * 52;
            drawFish(p, x, y, scale,
                    new Color(255, 170 - i * 20, 60 + i * 30), facesRight);
        }

        drawStarfish(p, 345, 475, 35, new Color(250, 205, 73));
        drawStarfish(p, 435, 500, 24, new Color(220, 103, 89));

        // Bubbles rise at different speeds and restart at the bottom.
        for (int i = 0; i < 9; i++) {
            double x = 270 + i * 58;
            double y = 535 - ((frame * (1 + i % 3) + i * 63) % 500);
            drawBubble(p, x, y, 4 + (i % 3) * 2);
        }
    }

    /** Draws a fish with an oval body and triangular tail; x and y are its center. */
    private static void drawFish(PainterAPI p, double x, double y, double scale,
                                 Color color, boolean facesRight) {
        double direction = facesRight ? 1 : -1;
        drawEllipse(p, x, y, 43 * scale, 25 * scale, color, new Color(30, 35, 47));
        drawPolygon(p, new double[]{x - direction * 35 * scale, x - direction * 65 * scale,
                        x - direction * 35 * scale},
                new double[]{y, y - 22 * scale, y + 22 * scale},
                new Color(235, 110, 65), new Color(30, 35, 47));
        drawEllipse(p, x + direction * 20 * scale, y - 4 * scale,
                3.5 * scale, 3.5 * scale, WHITE, WHITE);
        drawEllipse(p, x + direction * 21 * scale, y - 4 * scale,
                1.3 * scale, 1.3 * scale, Color.BLACK, Color.BLACK);
    }

    /** Draws a five-armed starfish as a filled polygon. */
    private static void drawStarfish(PainterAPI p, double x, double y,
                                     double radius, Color color) {
        double[] xs = new double[10];
        double[] ys = new double[10];
        for (int i = 0; i < 10; i++) {
            double angle = -Math.PI / 2 + i * Math.PI / 5;
            double r = (i % 2 == 0) ? radius : radius * 0.43;
            xs[i] = x + Math.cos(angle) * r;
            ys[i] = y + Math.sin(angle) * r;
        }
        drawPolygon(p, xs, ys, color, new Color(85, 48, 54));
        drawEllipse(p, x, y, radius * 0.17, radius * 0.17,
                new Color(255, 227, 164), new Color(255, 227, 164));
    }

    /** Draws a seaweed stalk made of curved-looking connected segments. */
    private static void drawSeaweed(PainterAPI p, double x, double bottom,
                                   double scale, Color color) {
        p.setPenColor(color);
        p.setPenWidth((float) (5 * scale));
        p.liftPen();
        moveTo(p, x, bottom);
        p.lowerPen();
        for (int i = 0; i < 7; i++) {
            double nextX = x + Math.sin(i * 0.9) * 15 * scale;
            double nextY = bottom - (i + 1) * 17 * scale;
            moveTo(p, nextX, nextY);
        }
        p.liftPen();
    }

    /** Draws branched coral using reusable line segments. */
    private static void drawCoral(PainterAPI p, double x, double bottom,
                                  double scale, Color color) {
        p.setPenColor(color);
        p.setPenWidth((float) (7 * scale));
        drawLine(p, x, bottom, x - 10 * scale, bottom - 45 * scale, color, 7 * scale);
        drawLine(p, x - 10 * scale, bottom - 45 * scale,
                x - 30 * scale, bottom - 68 * scale, color, 6 * scale);
        drawLine(p, x - 10 * scale, bottom - 45 * scale,
                x + 12 * scale, bottom - 78 * scale, color, 6 * scale);
        drawLine(p, x - 30 * scale, bottom - 68 * scale,
                x - 34 * scale, bottom - 91 * scale, color, 5 * scale);
        drawLine(p, x + 12 * scale, bottom - 78 * scale,
                x + 29 * scale, bottom - 99 * scale, color, 5 * scale);
    }

    private static void drawSand(PainterAPI p) {
        drawPolygon(p, new double[]{0, WIDTH, WIDTH, 0},
                new double[]{500, 500, HEIGHT, HEIGHT}, SAND, SAND);
        for (int i = 0; i < 16; i++) {
            drawEllipse(p, 24 + i * 51, 555 + (i % 2) * 17, 3, 2,
                    new Color(178, 143, 83), new Color(178, 143, 83));
        }
    }

    private static void drawRock(PainterAPI p, double x, double y, double scale) {
        drawEllipse(p, x, y, 48 * scale, 27 * scale,
                new Color(105, 111, 115), new Color(55, 67, 75));
    }

    private static void drawBubble(PainterAPI p, double x, double y, double radius) {
        drawEllipse(p, x, y, radius, radius,
                new Color(90, 185, 220, 100), new Color(180, 225, 240));
    }

    /** Draws an oval by joining points around an ellipse. */
    private static void drawEllipse(PainterAPI p, double centerX, double centerY,
                                    double radiusX, double radiusY,
                                    Color fill, Color outline) {
        int points = 32;
        double[] xs = new double[points];
        double[] ys = new double[points];
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points;
            xs[i] = centerX + Math.cos(angle) * radiusX;
            ys[i] = centerY + Math.sin(angle) * radiusY;
        }
        drawPolygon(p, xs, ys, fill, outline);
    }

    /** Draws and fills a closed shape from matching x and y coordinate arrays. */
    private static void drawPolygon(PainterAPI p, double[] xs, double[] ys,
                                    Color fill, Color outline) {
        p.liftPen();
        p.setPenColor(outline);
        p.setPenWidth(1.5f);
        moveTo(p, xs[0], ys[0]);
        p.startFill(fill);
        p.lowerPen();
        for (int i = 1; i < xs.length; i++) {
            moveTo(p, xs[i], ys[i]);
        }
        moveTo(p, xs[0], ys[0]);
        p.finishFill();
        p.liftPen();
    }

    /** Converts drawing coordinates (top-left origin) to Painter's centered coordinates. */
    private static void moveTo(PainterAPI p, double x, double y) {
        p.moveTo(x - WIDTH / 2.0, HEIGHT / 2.0 - y);
    }

    private static void drawLine(PainterAPI p, double x1, double y1,
                                 double x2, double y2, Color color, double width) {
        p.liftPen();
        p.setPenColor(color);
        p.setPenWidth((float) width);
        moveTo(p, x1, y1);
        p.lowerPen();
        moveTo(p, x2, y2);
        p.liftPen();
    }
}
