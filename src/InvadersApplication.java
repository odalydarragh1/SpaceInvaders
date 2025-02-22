import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {
    private final int NUM_ALIENS = 30;
    private final Alien[] aliens = new Alien[NUM_ALIENS];
    private final Spaceship playerShip = new Spaceship();
    private boolean movingLeft = false;
    private final Canvas canvas; // Dedicated rendering component
    static final int TOP_BORDER = 30;
    private int framesDrawn = 0;
    static boolean paused = false;
    static boolean gameOver = false;
    PauseScreen pauseScreen = new PauseScreen();
    private final int columns = 12;
    private final int horizontalSpacing = 5; // Adjust spacing between columns
    private final int verticalSpacing = 10;   // Adjust spacing between rows
    private final int height = 600;
    private int score = 0;



    public InvadersApplication() {
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        int CANVAS_WIDTH = 800; // new height
        int CANVAS_HEIGHT = height + TOP_BORDER;

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
        for (int i = 0; i < NUM_ALIENS; i++) {
            aliens[i] = new Alien();
            aliens[i].reset(i, columns, horizontalSpacing, verticalSpacing);
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
            if(checkWin()){
                resetGame(true);
            }
            checkGameOver();
            if (gameOver) {
                resetGame(false);
            }
            while (paused || gameOver) {
                displayPauseScreen(strategy);
            }
        }
    }

    // method that updates the locations
    private void updateGameState() {
        int width = getSize().width;
        boolean edgeReached = false;
        List <Bullet> bulletsToRemove = new ArrayList<>();

        playerShip.move(width);
        if (playerShip.bullets != null && !playerShip.bullets.isEmpty()) {
            for (Bullet bullet : playerShip.bullets) {
                bullet.move();
            }
        }
        // check if alien is at the edge of screen
        for (Alien alien : aliens) {
            if(alien.destroyed){continue;}
            if(alien.isAtBottom(height)){
                resetGame(false);
            }
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
            List<Bullet> bulletsCopy;
            synchronized (playerShip.bullets) {
                bulletsCopy = new ArrayList<>(playerShip.bullets);
            }

            for (Bullet bullet : bulletsCopy) {
                if (bullet != null) {
                    if (bullet.destroyed) {
                        continue;
                    }
                    for (Alien alien : aliens) {
                        if (alien.destroyed) {
                            continue;
                        }
                        if (checkCollision(alien, bullet)) {
                            alien.destroy();
                            score++;
                            bullet.destroy();
                            bulletsToRemove.add(bullet);
                        }
                    }
                }
            }
        }
        assert playerShip.bullets != null;
        playerShip.bullets.removeAll(bulletsToRemove);
    }

    private void displayPauseScreen(BufferStrategy strategy) {
        do {
            do {
                Graphics g = strategy.getDrawGraphics();
                try {
                    int width = canvas.getWidth();
                    int height = canvas.getHeight();

                    // clear screen
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, width, height);

                    pauseScreen.paint(g, score);

                } finally {
                    g.dispose(); // garbage collection
                }
            } while (strategy.contentsRestored());
            strategy.show();
        } while (strategy.contentsLost());
    }

    private void checkGameOver() {
        for (Alien alien : aliens) {
            if (checkCollision(playerShip, alien)) {
                gameOver = true;
            }
        }
    }

    private boolean checkWin() {
        boolean win = true;
        for (Alien alien : aliens) {
            if (!alien.destroyed) {
                win = false;
                break;
            }
        }
        return win;
    }

    private void resetGame(boolean win) {
        for (int i = 0; i < NUM_ALIENS; i++) {
            aliens[i].reset(i, columns, horizontalSpacing, verticalSpacing);
            if (win){
                aliens[i].increaseSpeed(5);
            }
            else{
                aliens[i].setSpeed(1);
            }
        }
        playerShip.reset();
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

                    List<Bullet> bulletsCopy;
                    synchronized (playerShip.bullets) {
                        bulletsCopy = new ArrayList<>(playerShip.bullets);
                    }

                    for (Bullet bullet : bulletsCopy) {
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

    public boolean checkCollision(Sprite2D sprite1, Sprite2D sprite2) {
        return ((sprite1.xLocation < sprite2.xLocation && sprite1.xLocation + Sprite2D.SPRITE_WIDTH > sprite2.xLocation) || (sprite2.xLocation < sprite1.xLocation && sprite2.xLocation + Sprite2D.BULLET_WIDTH > sprite1.xLocation)) && ((sprite1.yLocation < sprite2.yLocation && sprite1.yLocation + Sprite2D.SPRITE_HEIGHT > sprite2.yLocation) || (sprite2.yLocation < sprite1.yLocation && sprite2.yLocation + Sprite2D.BULLET_HEIGHT > sprite1.yLocation));
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
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!gameOver) {
                paused = !paused;
            }
            if (gameOver) {
                gameOver = false;
            }
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
