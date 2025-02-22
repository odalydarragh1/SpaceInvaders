import javax.swing.*;
import java.awt.*;

public class Bullet extends Sprite2D {
    private final Image image = new ImageIcon("images/bullet.png").getImage();
    private static final int SPEED = 10;

    // Sets location of bullet based on player ship
    public Bullet(double startX) {
        x = startX;
        y = 560;
    }

    // Move upwards based on speed and destroy itself once it hits the ceiling
    public void move() {
        y -= SPEED;
        if (y + getHeight() <= 0) {
            destroyed = true;
        }
    }

    // Return image
    public Image getImage() { return image; }

    // Returns the dimensions of this sprite
    @Override
    public int getWidth() { return BULLET_WIDTH; }
    @Override
    public int getHeight() { return BULLET_HEIGHT; }
}