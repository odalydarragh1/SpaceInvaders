import java.awt.*;

public class Sprite2D {
    protected double xLocation;
    protected double yLocation;
    protected double xVelocity;
    protected final static int SPRITE_WIDTH = 54;
    protected final static int SPRITE_HEIGHT = 54;

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
        g.drawImage(image,(int) xLocation,(int) yLocation,null);
    }
}
