import java.awt.*;

public class PauseScreen {
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 50);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 30);

    public void paint(Graphics g, int score, boolean gameOver) {
        g.setColor(Color.WHITE);
        g.setFont(TITLE_FONT);

        String title = gameOver ? "Game Over" : "Paused";
        int titleWidth = g.getFontMetrics().stringWidth(title); // get the width in pixels of the title
        int x = (800 - titleWidth) / 2; // find midpoint

        // Draw title
        g.drawString(title, x, 250);

        // Draw instructions
        g.setFont(TEXT_FONT);
        String instruction = gameOver ? "Press ESC to restart" : "Press ESC to continue";
        g.drawString(instruction, 250, 300);

        // Draw score
        String scoring = gameOver ? "Final Score: " : "Current Score: ";
        g.drawString(scoring + score, 300, 350);
    }
}