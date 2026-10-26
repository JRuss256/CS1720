import edu.cymbal.graphics.CanvasAPI;
import edu.cymbal.graphics.Graphics;
import edu.cymbal.graphics.PainterAPI;
import java.awt.Color;

/**
 * Initial Project 2 checkpoint program for the aquarium scene.
 * The main method makes a small test drawing; the scene methods are planning
 * placeholders that will be filled in for the final project.
 */
public class Aquarium {
    /** Opens the Painter canvas, tests three PainterAPI methods, and calls the scene method. */
    public static void main(String[] args) {
        CanvasAPI canvas = Graphics.createCanvas(800, 600, "Aquarium Project");
        PainterAPI painter = canvas.getPainter();
        painter.setSpeed(0);

        painter.forward(100);
        painter.turnRight(120);
        painter.circle(30);

        drawAquarium(painter);
        canvas.awaitClose();
    }

    /**
     * Plans the full aquarium scene.
     * @param painter the Painter used to draw the scene
     */
    private static void drawAquarium(PainterAPI painter) {
        // Calls drawSand, drawRock, drawSeaweed, drawCoral, drawFish, drawStarfish, and drawBubble.
    }

    /**
     * Plans one fish with an oval body and triangular tail.
     * @param painter the Painter used to draw the fish
     * @param bodyColor the fish body color
     * @param x the horizontal center of the fish
     * @param y the vertical center of the fish
     * @param size the overall scale of the fish
     * @param facesRight whether the fish faces right
     */
    private static void drawFish(PainterAPI painter, Color bodyColor,
                                 int x, int y, double size, boolean facesRight) {
        // Calls drawEllipse and drawPolygon.
    }

    /**
     * Plans a five-armed starfish.
     * @param painter the Painter used to draw the starfish
     * @param color the starfish color
     * @param x the horizontal center of the starfish
     * @param y the vertical center of the starfish
     * @param radius the distance from the center to each arm tip
     */
    private static void drawStarfish(PainterAPI painter, Color color,
                                     int x, int y, int radius) {
        // Calls drawPolygon and drawEllipse.
    }

    /**
     * Plans a seaweed stalk.
     * @param painter the Painter used to draw the seaweed
     * @param color the seaweed color
     * @param x the horizontal position of the stalk
     * @param bottom the vertical position of its base
     * @param height the stalk height
     */
    private static void drawSeaweed(PainterAPI painter, Color color,
                                    int x, int bottom, int height) {
        // Calls drawLine.
    }

    /**
     * Plans branching coral.
     * @param painter the Painter used to draw the coral
     * @param color the coral color
     * @param x the horizontal position of the coral base
     * @param bottom the vertical position of the coral base
     * @param size the overall scale of the coral
     */
    private static void drawCoral(PainterAPI painter, Color color,
                                  int x, int bottom, double size) {
        // Calls drawLine.
    }

    /** Plans the sandy floor and scattered stones. */
    private static void drawSand(PainterAPI painter) {
        // Calls drawPolygon and drawEllipse.
    }

    /** Plans one rising bubble. */
    private static void drawBubble(PainterAPI painter, int x, int y, int radius) {
        // Calls drawEllipse.
    }

    /**
     * Plans a reusable oval shape.
     * @param painter the Painter used to draw the oval
     * @param fillColor the interior color
     * @param outlineColor the outline color
     * @param centerX the horizontal center
     * @param centerY the vertical center
     * @param radiusX the horizontal radius
     * @param radiusY the vertical radius
     */
    private static void drawEllipse(PainterAPI painter, Color fillColor,
                                    Color outlineColor, int centerX, int centerY,
                                    int radiusX, int radiusY) {
    }

    /**
     * Plans a reusable filled polygon from matching coordinate arrays.
     * @param painter the Painter used to draw the polygon
     * @param xPoints the horizontal coordinates of the vertices
     * @param yPoints the vertical coordinates of the vertices
     * @param fillColor the polygon interior color
     * @param outlineColor the polygon outline color
     */
    private static void drawPolygon(PainterAPI painter, int[] xPoints, int[] yPoints,
                                    Color fillColor, Color outlineColor) {
    }

    /** Plans a reusable line segment. */
    private static void drawLine(PainterAPI painter, Color color,
                                 int x1, int y1, int x2, int y2, float width) {
    }

    /** Plans a rounded rock on the aquarium floor. */
    private static void drawRock(PainterAPI painter, int x, int y, int width, int height) {
        // Calls drawEllipse.
    }
}
