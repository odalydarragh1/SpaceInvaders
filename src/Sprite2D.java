import java.awt.*;

public class Sprite2D {
    public boolean destroyed;
    protected double xLocation;
    protected double yLocation;
    protected double xVelocity;
    protected double yVelocity;
    protected static int SPRITE_WIDTH = 54;
    protected static int SPRITE_HEIGHT = 54;
    protected static int BULLET_HEIGHT = 26;
    protected static int BULLET_WIDTH = 12;

    public Sprite2D() {
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setPosition(double xLocation, double yLocation) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    public void paint(Graphics g, Image image) {
        if (destroyed) {
            return;
        }
        g.drawImage(image,(int) xLocation,(int) yLocation,null);
    }

    public void destroy() {
        destroyed = true;
    }
}
