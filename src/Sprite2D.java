import java.awt.*;
import java.util.Random;


public class Sprite2D {
    protected double xLocation;
    protected double yLocation;
    protected double xVelocity;
    private final Image myImage;

    public Sprite2D(Image image) {
        this.myImage = image;
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setPosition(double xLocation, double yLocation) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }

    public void paint(Graphics g){
        g.drawImage(myImage,(int) xLocation,(int) yLocation,null);
    }
}
