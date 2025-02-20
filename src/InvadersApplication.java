import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {
    private final int NUM_ALIENS = 30;
    private final Alien[] aliens = new Alien[NUM_ALIENS];
    private final Spaceship playerShip = new Spaceship();
    private boolean movingLeft = false;
    private final Canvas canvas; // Dedicated rendering component
    static final int TOP_BORDER = 30;
    private int framesDrawn = 0;

    public InvadersApplication() {
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int CANVAS_WIDTH = 800; // new height
        int CANVAS_HEIGHT = 600 + TOP_BORDER;

        // Canvas needed for double buffering
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        canvas.setBackground(Color.BLACK);
        canvas.setFocusable(true);
        canvas.addKeyListener(this);

        getContentPane().add(canvas);
        pack(); // for layout
        centerFrame();

        // Initialize aliens
        initializeAliens();
        setVisible(true);

        new Thread(this).start();
    }

    // centers
    private void centerFrame() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth())/2, (screen.height - getHeight())/2);
    }

    // initialise aliens in rows and columns
    private void initializeAliens() {
        int columns = 12;
        int rows = (NUM_ALIENS + columns - 1) / columns;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int index = row * columns + col;
                if (index >= NUM_ALIENS) break;
                aliens[index] = new Alien(row, col);
            }
        }
    }

    @Override
    public void run() {
        // double buffering
        canvas.createBufferStrategy(2);
        BufferStrategy strategy = canvas.getBufferStrategy();

        while (true) {
            updateGameState();
            renderFrame(strategy);
            try { Thread.sleep(20); }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // method that updates the locations
    private void updateGameState() {
        int width = getSize().width;
        boolean edgeReached = false;

        playerShip.move(width);
        if (playerShip.bullets != null && !playerShip.bullets.isEmpty()) {
            for (Bullet bullet : playerShip.bullets) {
                bullet.move();
            }
        }
        // check if alien is at the edge of screen
        for (Alien alien : aliens) {
            if(alien.destroyed){continue;}
            if (alien.isAtEdge(movingLeft, width)) {
                edgeReached = true;
                break;
            }
        }

        // change direction on move down if at screen edge
        if (edgeReached) {
            movingLeft = !movingLeft;
            for (Alien alien : aliens) {
                if(alien.destroyed){continue;}
                alien.moveDown();
            }
        }
        else {
            for (Alien alien : aliens) {
                if(alien.destroyed){continue;}
                alien.moveHorizontal(movingLeft);
            }
        }
        if (playerShip.bullets != null && !playerShip.bullets.isEmpty()) {
            for (Bullet bullet : playerShip.bullets) {
                if (bullet.destroyed) {
                    continue;
                }
                for (Alien alien : aliens) {
                    if (alien.destroyed) {
                        continue;
                    }
                    if (checkCollision(alien, bullet)) {
                        alien.destroy();
                        bullet.destroy();
                    }
                }
            }
        }
    }

    private void renderFrame(BufferStrategy strategy) {
        do {
            do {
                Graphics g = strategy.getDrawGraphics();
                try {
                    int width = canvas.getWidth();
                    int height = canvas.getHeight();

                    // clear screen
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, width, height);

                    // render sprites
                    playerShip.paint(g, playerShip.getImage());
                    for (Alien alien : aliens) {
                        if (alien != null) { // Add null check
                            alien.paint(g, alien.getImage(framesDrawn));
                        }
                    }

                    for (Bullet bullet : playerShip.bullets) {
                        if (bullet != null) {
                            bullet.paint(g, bullet.getImage());
                        }
                    }

                    framesDrawn++;
                } finally {
                    g.dispose(); // garbage collection
                }
            } while (strategy.contentsRestored());
            strategy.show();
        } while (strategy.contentsLost());
    }

    public boolean checkCollision(Alien alien, Bullet bullet) {
        return ((alien.xLocation < bullet.xLocation && alien.xLocation + Sprite2D.SPRITE_WIDTH > bullet.xLocation) || (bullet.xLocation < alien.xLocation && bullet.xLocation + Sprite2D.BULLET_WIDTH > alien.xLocation)) && ((alien.yLocation < bullet.yLocation && alien.yLocation + Sprite2D.SPRITE_HEIGHT > bullet.yLocation) || (bullet.yLocation < alien.yLocation && bullet.yLocation + Sprite2D.BULLET_HEIGHT > alien.yLocation));
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
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            playerShip.shoot();
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
