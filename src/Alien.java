import javax.swing.*;
import java.awt.*;

public class Alien extends Sprite2D {
    private final Image imageInvader = new ImageIcon("images/alien_ship_1.png").getImage();

    // set x and y position initially
    public Alien(int row, int column) {
        super();
        int x = column * (SPRITE_WIDTH);
        int y = InvadersApplication.TOP_BORDER + row * (SPRITE_HEIGHT+20);
        setPosition(x, y);
    }

    // check if it has hit an edge
    public boolean isAtEdge(boolean movingLeft, int screenWidth) {
        if (movingLeft) {
            return (xLocation <= 0);
        } else {
            return (xLocation + SPRITE_WIDTH >= screenWidth);
        }
    }

    public void moveDown() {
        yLocation += SPRITE_HEIGHT / 2;
    }

    // moves the aliens horizontally
    public void moveHorizontal(boolean movingLeft) {
        int ALIEN_SPEED = 1;
        if (movingLeft) {
            xLocation -= ALIEN_SPEED;
        } else {
            xLocation += ALIEN_SPEED;
        }
    }

    public Image getImage() {
        return imageInvader;
    }
}