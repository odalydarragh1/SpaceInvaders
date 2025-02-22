import javax.swing.*;
import java.awt.*;

public class Alien extends Sprite2D {
    private final Image imageInvader1 = new ImageIcon("images/alien_ship_1.png").getImage();
    private final Image imageInvader2 = new ImageIcon("images/alien_ship_2.png").getImage();
    private int ALIEN_SPEED = 1;

    public Alien() {
    }

    // check if it has hit an edge
    public boolean isAtEdge(boolean movingLeft, int screenWidth) {
        if (movingLeft) {
            return (xLocation <= 0);
        } else {
            return (xLocation + SPRITE_WIDTH >= screenWidth);
        }
    }

    public boolean isAtBottom(int screenHeight) {
        return (yLocation >= screenHeight);
    }

    public void moveDown() {
        yLocation += ALIEN_SPEED;
    }

    // moves the aliens horizontally
    public void moveHorizontal(boolean movingLeft) {

        if (movingLeft) {
            xLocation -= ALIEN_SPEED;
        } else {
            xLocation += ALIEN_SPEED;
        }
    }

    public Image getImage(int framesDrawn) {
        if ( framesDrawn%100<50 )
            return imageInvader1;
        else
            return imageInvader2;
    }

    public void reset(int index, int columns, int horizontalSpacing, int verticalSpacing) {
        int row = index / columns; // Calculate row from index
        int col = index % columns; // Calculate column from index

        // Calculate position with spacing
        int x = col * (SPRITE_WIDTH + horizontalSpacing);
        int y = InvadersApplication.TOP_BORDER + row * (SPRITE_HEIGHT + verticalSpacing);
        destroyed = false;
        setPosition(x, y);
        }

    public void increaseSpeed(int ALIEN_SPEED) {
        this.ALIEN_SPEED += ALIEN_SPEED;
    }

    public void setSpeed(int ALIEN_SPEED) {
        this.ALIEN_SPEED = ALIEN_SPEED;
    }
    }


