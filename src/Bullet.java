import javax.swing.*;
import java.awt.*;

public class Bullet extends Sprite2D{
    private final Image imageBullet = new ImageIcon("images/bullet.png").getImage();


    public Bullet(double xLocation) {
        setPosition(xLocation+((double) Sprite2D.SPRITE_WIDTH /2), 560);
        this.yVelocity = 10;
    }

    public void move(){
        if (yLocation <= 0) {
            destroyed = true;
            return;
        }
        this.yLocation -= this.yVelocity;
    }

    public Image getImage() {
        return imageBullet;
    }
}
