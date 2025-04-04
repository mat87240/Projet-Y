package HUD;

import map.Entity;
import map.Map;
import org.lwjgl.opengl.GL11;
import player.Player;
import player.Inventory;


public class DisplayHUD {
    private Stat healthBar;
    private Stat manaBar;
    private DisplayInventory inventoryDisplay;
    private int currentFPS;
    private RemainingEnemy remainingEnemyDisplay;

    public DisplayHUD(Player player, Map map) {
        healthBar = new StatsBar(player, "Health", 100, 50, 50, 200, 25);
        manaBar = new StatsBar(player, "Mana", 100, 50, 100, 200, 25);
        this.inventoryDisplay = new DisplayInventory(player);
        this.remainingEnemyDisplay = new RemainingEnemy();  // Initialize remaining enemy display
    }

    public void renderHUD(Map map) {
        GL11.glPushMatrix();

        // Render health bar for Player
        if (healthBar instanceof StatsBar) {
            healthBar.render();
        }

        // Render health bars for Entities
        for (Entity entity : map.getListEntities()) {
            entity.getEntityHealthBar().render();
        }

        // Render mana bar for Player
        if (manaBar != null && manaBar instanceof StatsBar) {
            manaBar.render();
        }

        // Render inventory display
        inventoryDisplay.render();

        // Render remaining enemies
        remainingEnemyDisplay.updateRemainingEnemies(map);
        remainingEnemyDisplay.render();

        GL11.glPopMatrix();
    }

    public void updateRemainingEnemies(Map map) {
        remainingEnemyDisplay.updateRemainingEnemies(map); // Update the remaining enemies count
    }
}
