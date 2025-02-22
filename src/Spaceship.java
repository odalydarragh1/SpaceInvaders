import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Spaceship extends Sprite2D {
    private final Image imagePlayerShip = new ImageIcon("images/player_ship.png").getImage();
    protected final ArrayList<Bullet> bullets = new ArrayList<>();

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

    public void shoot(){
        bullets.add(new Bullet(this.xLocation));
    }

    public void reset(){
        setPosition(300, 560);
        bullets.clear();
    }
}
