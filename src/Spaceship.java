import javax.swing.*;
import java.awt.*;

public class Spaceship extends Sprite2D {
    private final Image imagePlayerShip = new ImageIcon("images/player_ship.png").getImage();

    public Spaceship() {
        super();
        setPosition(300, 560);
    }

    public void move(int width) {
        if (xLocation + xVelocity >= 0 && xLocation + xVelocity < width) {
            xLocation += xVelocity;
        }
    }

    public Image getImage() {
        return imagePlayerShip;
    }
}
