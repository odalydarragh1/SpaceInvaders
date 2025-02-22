import java.awt.*;

public class PauseScreen {
    public void paint(Graphics g, int score) {
        g.setColor(Color.white);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        if (InvadersApplication.paused){
            g.drawString("Paused", 320, 250);
            g.drawString("Press Escape to continue", 180,  300);
            g.drawString("Score: ", 250, 350);
            g.drawString((Integer.toString(score)), 300, 350);
        }
        if (InvadersApplication.gameOver) {
            g.drawString("Game Over", 320, 250);
            g.drawString("Press Escape to restart", 180, 300);
            g.drawString("Score: ", 250, 350);
            g.drawString((Integer.toString(score)), 300, 350);
        }
    }
}
