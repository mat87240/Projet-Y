package HUD;

import org.lwjgl.opengl.GL11;
import player.Player;

public class StatsBar implements Stat {
    private int maxStat;
    private int currentStat;
    private int x, y, width, height;
    private String statName;
    private Player player;

    public StatsBar(Player player, String statName, int maxStat, int x, int y, int width, int height) {
        this.player = player;
        this.statName = statName;
        this.maxStat = maxStat;
        this.currentStat = maxStat;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void setStat(int value) {
        this.currentStat = value;
        if (currentStat > maxStat) {
            currentStat = maxStat;
        }
        if (currentStat < 0) {
            currentStat = 0;
        }
    }

    @Override
    public int getCurrentStat() {
        switch(statName) {
            case "Health":
                return player.getHealth();
            case "Mana":
                return player.getMana();
            default:
                return currentStat;
        }
    }

    @Override
    public void render() {
        int currentStat = getCurrentStat();

        GL11.glColor3f(0.5f, 0.5f, 0.5f); // Background color
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();

        float statPercentage = (float) currentStat / maxStat;
        GL11.glColor3f(1.0f - statPercentage, statPercentage, 0.0f); // Foreground color
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width * statPercentage, y);
        GL11.glVertex2f(x + width * statPercentage, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }
}
