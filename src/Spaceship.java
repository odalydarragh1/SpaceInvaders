import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Spaceship extends Sprite2D {
    private final Image image = new ImageIcon("images/player_ship.png").getImage();
    private final List<Bullet> bullets = new ArrayList<>();
    private int xVelocity;

    public Spaceship() {
        setPosition(300, 560);
    }

    // Move as long as it is in bounds
    public void move(int screenWidth) {
        double newX = x + xVelocity;
        if (newX >= 0 && newX + getWidth() <= screenWidth) {
            x = newX;
        }
    }

    // Create mew bullet at the centre of the spaceship
    public void shoot() {
        double bulletX = x + (getWidth() - Bullet.BULLET_WIDTH) / 2.0;
        bullets.add(new Bullet(bulletX));
    }

    // getter for bullets
    public List<Bullet> getBullets() { return new ArrayList<>(bullets); }

    // Removes all bullets
    public void clearBullets() { bullets.clear(); }

    // Returns image
    public Image getImage() { return image; }

    // Set new velocity
    public void setXVelocity(int velocity) { xVelocity = velocity; }

    // Returns the dimensions of this sprite
    @Override
    public int getWidth() { return SPRITE_WIDTH; }
    @Override
    public int getHeight() { return SPRITE_HEIGHT; }
}