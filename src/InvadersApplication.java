import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    private final int NUM_INVADERS = 30;
    private final Sprite2D[] invaders = new Sprite2D[NUM_INVADERS];
    private final Sprite2D playerShip;
    // Sprite collision dimensions
    private final int SPRITE_WIDTH = 54;
    private final int SPRITE_HEIGHT = 54;
    // MacOS border
    private final int TOP_BORDER = 30;

    public InvadersApplication() {
        // Set images
        Image imageInvader = new ImageIcon("images/alien_ship_1.png").getImage();
        Image imagePlayerShip = new ImageIcon("images/player_ship.png").getImage();

        // Initialise sprites
        playerShip = new Sprite2D(imagePlayerShip, true, SPRITE_WIDTH, SPRITE_HEIGHT);
        for (int i = 0; i < NUM_INVADERS; i++) {
            invaders[i] = new Sprite2D(imageInvader, false, SPRITE_WIDTH, SPRITE_HEIGHT);
        }

        // Add thread
        Thread t = new Thread(this);
        t.start();

        // Add listener
        addKeyListener(this);

        // Setup Jframe
        this.setTitle("Space Invaders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, (600+TOP_BORDER));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getSize().width) / 2;
        int y = (screenSize.height - getSize().height) / 2;
        setBounds(x, y, getSize().width, getSize().height);
        setBackground(Color.BLACK);
        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 50 FPS
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            int width = getSize().width -= SPRITE_WIDTH;
            int height = getSize().height -= SPRITE_HEIGHT;
            // Move Sprites
            playerShip.playerMove(width);
            for (int i = 0; i < NUM_INVADERS; i++) {
                invaders[i].invaderMove(width, height);
                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getSize().width, getSize().height);

        // Paint in sprites
        playerShip.paint(g);
        for (int i = 0; i < NUM_INVADERS; i++) {
            invaders[i].paint(g);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    // Set player sprite velocity
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerShip.setXVelocity(-3);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerShip.setXVelocity(3);
        }
    }

    // Stop moving player sprite
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerShip.setXVelocity(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerShip.setXVelocity(0);
        }    }

    public static void main(String[] args) {
        new InvadersApplication();

    }

}