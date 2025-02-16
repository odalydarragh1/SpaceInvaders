import java.awt.*;
import java.util.Random;

public class Alien extends Sprite2D {

    private double xLocation, yLocation;
    private double xVelocity;
    private Image myImage;
    private final int TOP_BORDER = 30;

    public Alien(Image i, boolean player, int SPRITE_WIDTH, int SPRITE_HEIGHT) {
        this.myImage = i;
        if (player) {
            setPosition(300, 560);
            return;
        }

        setPosition(Math.random() * (600 - SPRITE_WIDTH), (Math.random() * (600 - SPRITE_HEIGHT) + (TOP_BORDER)));
    }

    public void move(int width, int height) {
        Random random = new Random();

        int xMove = random.nextBoolean() ? 1 : -1; // 50/50 chance of positive or negative
        int yMove = random.nextBoolean() ? 1 : -1;

        if (xLocation + xMove >= 0 && xLocation + xMove < width) {
            xLocation += xMove;
        }
        if (yLocation + yMove >= TOP_BORDER && yLocation + yMove < height) {
            yLocation += yMove;
        }
    }


    public void playerMove(int width) {
        if (xLocation + xVelocity >= 0 && xLocation + xVelocity < width) {
            xLocation += xVelocity;
        }
    }

    public void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void paint(Graphics g) {
        g.drawImage(myImage, (int) xLocation, (int) yLocation, null);
    }

    public void setPosition(double xLocation, double yLocation) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }
}