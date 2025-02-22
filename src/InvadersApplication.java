import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    // Defining my constants
    private static final int NUM_ALIENS = 30;
    private static final int ALIEN_COLUMNS = 10;
    private static final int HORIZONTAL_SPACING = 15;
    private static final int VERTICAL_SPACING = 30;
    private static final int TOP_BORDER = 50;
    private static final int PLAYER_START_Y = 560;

    // Creating necessary instances
    private final Alien[] aliens = new Alien[NUM_ALIENS];
    private final Spaceship player = new Spaceship();
    private final Canvas canvas = new Canvas();
    private final PauseScreen pauseScreen = new PauseScreen();

    // Gamestate trackers
    private boolean movingLeft = false;
    private boolean paused = false;
    private boolean gameOver = false;
    private int score = 0;
    private int frameCount = 0;

    public InvadersApplication() {
        configureWindow();
        initialiseAliens();
        startGameLoop();
    }

    // Start thread
    private void startGameLoop() {
        new Thread(this).start();
    }

    // Setup j-frame canvas
    private void configureWindow() {
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        canvas.setPreferredSize(new Dimension(800, 650));
        canvas.setBackground(Color.BLACK);
        canvas.addKeyListener(this);
        getContentPane().add(canvas);
        pack();
        centerWindow();
        setVisible(true);
    }
    
    // Simply center the window on the screen
    private void centerWindow() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth())/2, (screen.height - getHeight())/2);
    }
    
    // Initialise the aliens in an array
    private void initialiseAliens() {
        for (int i = 0; i < NUM_ALIENS; i++) {
            aliens[i] = new Alien();
            resetAlienPosition(i);
        }
    }

    // Set alien position into rows and columns by index and set all aliens to alive(destroyed == false)
    private void resetAlienPosition(int index) {
        int row = index / ALIEN_COLUMNS;
        int col = index % ALIEN_COLUMNS;
        double x = col * (aliens[0].getWidth() + HORIZONTAL_SPACING);
        double y = TOP_BORDER + row * (aliens[0].getHeight() + VERTICAL_SPACING);
        aliens[index].setPosition(x, y);
        aliens[index].destroyed = false;
    }

    // Run my thread
    @Override
    public void run() {
        canvas.createBufferStrategy(2); // Double buffering
        BufferStrategy strategy = canvas.getBufferStrategy();

        while (true) {
            if (!paused && !gameOver) {
                updateGameState();
            }
            renderFrame(strategy);
            sleep(20);
        }
    }

    // A method that can be called to update all elements of the game
    private void updateGameState() {
        updatePlayer();
        updateBullets();
        updateAliens();
        checkCollisions();
        checkGameOver();
        checkWinCondition();
    }

    // Update the player ship
    private void updatePlayer() {
        player.move(canvas.getWidth());
    }

    // Update the bullets/arraylist of bullets
    private void updateBullets() {
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : player.getBullets()) {
            bullet.move();
            if (bullet.destroyed) {
                bulletsToRemove.add(bullet);
            }
        }
        player.getBullets().removeAll(bulletsToRemove);
    }

    // Update the Aliens
    private void updateAliens() {
        boolean edgeReached = checkAliensEdges();
        handleAlienMovement(edgeReached);
    }

    // Handles the alien movement of hitting the edge and changing direction while getting closer to the player ship
    private void handleAlienMovement(boolean edgeReached) {
        if (edgeReached) {
            movingLeft = !movingLeft;
            for (Alien alien : aliens) {
                if (!alien.destroyed) alien.moveDown();
            }
        } else {
            for (Alien alien : aliens) {
                if (!alien.destroyed) alien.moveHorizontal(movingLeft);
            }
        }
    }

    // Returns whether the aliens have reached an edge or not
    private boolean checkAliensEdges() {
        for (Alien alien : aliens) {
            if (!alien.destroyed && alien.isAtEdge(movingLeft, canvas.getWidth())) {
                return true;
            }
        }
        return false;
    }

    // Checks for collisions between the bullets
    private void checkCollisions() {
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : player.getBullets()) {
            if (bullet.destroyed) continue;

            for (Alien alien : aliens) {
                if (alien.destroyed) continue;

                if (checkCollision(alien, bullet)) {
                    alien.destroy();
                    bullet.destroy();
                    score += 10;
                    bulletsToRemove.add(bullet);
                }
            }
        }
        player.getBullets().removeAll(bulletsToRemove);
    }

    // Checks collisions between two sprites
    private boolean checkCollision(Sprite2D a, Sprite2D b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    // Checks if the aliens have collided with the player
    private void checkGameOver() {
        for (Alien alien : aliens) {
            if (!alien.destroyed && checkCollision(player, alien)) {
                gameOver = true;
                return;
            }
            if (alien.isAtBottom(canvas.getHeight())) {
                gameOver = true;
                return;
            }
        }
    }

    // Checks the win condition of if all aliens are destroyed
    private void checkWinCondition() {
        boolean allDestroyed = true;
        for (Alien alien : aliens) {
            if (!alien.destroyed) {
                allDestroyed = false;
                break;
            }
        }
        if (allDestroyed) {
            resetGame(true);
            score += 1000;
        }
    }

    private void resetGame(boolean increaseSpeed) {
        for (int i = 0; i < NUM_ALIENS; i++) {
            resetAlienPosition(i);
            if (increaseSpeed) {
                aliens[i].increaseSpeed(2);
            } else {
                aliens[i].setSpeed(1);
            }
        }
        player.setPosition(300, PLAYER_START_Y);
        player.clearBullets();
        gameOver = false;
    }

    private void renderFrame(BufferStrategy strategy) {
        do {
            Graphics g = strategy.getDrawGraphics();
            try {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                if (paused || gameOver) {
                    pauseScreen.paint(g, score, gameOver);
                } else {
                    player.paint(g, player.getImage());

                    for (Alien alien : aliens) {
                        if (!alien.destroyed) {
                            alien.paint(g, alien.getImage(frameCount));
                        }
                    }

                    for (Bullet bullet : player.getBullets()) {
                        if (!bullet.destroyed) {
                            bullet.paint(g, bullet.getImage());
                        }
                    }

                    g.setColor(Color.WHITE);
                    g.drawString("Score: " + score, 20, 30);

                    frameCount++;
                }
            } finally {
                g.dispose(); // garbage collection
            }
        } while (strategy.contentsRestored());

        strategy.show();
    }

    // Controls the framerate
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Main keyboard controls
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                player.setXVelocity(-5);
                break;
            case KeyEvent.VK_RIGHT:
                player.setXVelocity(5);
                break;
            case KeyEvent.VK_SPACE:
                if (!gameOver && !paused) player.shoot();
                break;
            case KeyEvent.VK_ESCAPE:
                if (gameOver) resetGame(false);
                else paused = !paused;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setXVelocity(0);
        }
    }

    public static void main(String[] args) {
        new InvadersApplication();
    }
}
