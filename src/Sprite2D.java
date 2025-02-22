import java.awt.*;

public abstract class Sprite2D {
    protected double x;
    protected double y;
    protected boolean destroyed;
    public static final int SPRITE_WIDTH = 54;
    public static final int SPRITE_HEIGHT = 54;
    public static final int BULLET_WIDTH = 12;
    public static final int BULLET_HEIGHT = 26;

    // Abstract fields as all sprites are different
    public abstract int getWidth();
    public abstract int getHeight();

    // Sets 2d position of sprite
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // As long as its not destroyed, it will get rendered
    public void paint(Graphics g, Image image) {
        if (!destroyed) {
            g.drawImage(image, (int) x, (int) y, null);
        }
    }

    // Destroy itself
    public void destroy() {
        destroyed = true;
    }

    // Positioning getter methods
    public double getX() { return x; }
    public double getY() { return y; }
}