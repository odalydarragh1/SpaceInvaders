import javax.swing.*;
import java.awt.*;

public class Alien extends Sprite2D {
    private final Image image1 = new ImageIcon("images/alien_ship_1.png").getImage();
    private final Image image2 = new ImageIcon("images/alien_ship_2.png").getImage();
    private int speed = 1;

    // Returns true or false based on whether the alien is at the edge or not based on its direction
    public boolean isAtEdge(boolean movingLeft, int screenWidth) {
        return movingLeft ? (x <= 0) : (x + getWidth() >= screenWidth);
    }

    // returns whether the alien is at the bottom
    public boolean isAtBottom(int screenHeight) {
        return y >= screenHeight;
    }

    // Simply move down
    public void moveDown() {
        y += speed;
    }

    // Move horizontal based on direction
    public void moveHorizontal(boolean movingLeft) {
        x += movingLeft ? -speed : speed;
    }

    // Return the correct image based on the modules of the framecount
    public Image getImage(int frameCount) {
        return (frameCount % 100 < 50) ? image1 : image2;
    }

    // Simply increase speed
    public void increaseSpeed(int increment) {
        speed += increment;
    }

    // Simply set speed
    public void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    // Returns the width and height of this sprite

    @Override
    public int getWidth() { return SPRITE_WIDTH; }
    @Override
    public int getHeight() { return SPRITE_HEIGHT; }
}