import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Spaceship extends Sprite2D {

    public Spaceship(Image image) {
        super(image);
        setPosition(300, 560);
    }

    public void move(int width, int height) {
        if (xLocation + xVelocity >= 0 && xLocation + xVelocity < width) {
            xLocation += xVelocity;
        }
    }
}
