package HUD;

import data.TextureLoader;
import draw.OpenGLRenderer;
import map.Entity;
import map.Map;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.ArrayList;

public class RemainingEnemy {
    private ArrayList<Entity> remainingEnemies;
    private int EnemyNumber;
    private int fontTexture;  // This will hold the texture ID for the font

    public RemainingEnemy() {
        this.remainingEnemies = new ArrayList<>();
        // Load the font texture (you'll need to provide the path to your font texture)
        this.fontTexture = TextureLoader.loadTexture("src" + File.separator + "assets" + File.separator + "font.bmp");
        if (this.fontTexture != 0){
            System.out.println("nigga");
        }
    }

    // Update the remaining enemy count, usually after an entity's health changes
    public void updateRemainingEnemies(Map map) {
        // Create a list of only alive enemies
        this.remainingEnemies.clear();
        for (Entity entity : map.getListEntities()) {
            if (entity.getHealth() > 0) {
                remainingEnemies.add(entity);  // Add only the alive entities
            }
        }

        // Alternatively, just update the EnemyNumber if you don't need the list of enemies themselves
        this.EnemyNumber = remainingEnemies.size();
    }

    // Render the remaining enemies (this could be displayed on the HUD)
    public void render() {
        OpenGLRenderer renderer = new OpenGLRenderer();
        renderer.setup2DRendering(); // Set up 2D rendering state

        // Now you can render your text or other 2D objects
        String text = "Remaining enemies: " + EnemyNumber;
        float x = 10;  // Starting X position for the text
        float y = 550; // Starting Y position for the text

        // Bind the font texture (this is the font texture atlas you've loaded)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontTexture);

        // Render the text
        for (char c : text.toCharArray()) {
            // Texture coordinates for the character
            float tx = (c % 16) / 16.0f;  // Texture coordinates based on character position
            float ty = (c / 16) / 16.0f;  // Assuming 16x16 grid of characters in the font texture

            float charWidth = 0.1f;  // Width of each character
            float charHeight = 0.1f; // Height of each character

            renderCharacter(x, y, tx, ty, charWidth, charHeight);
            x += charWidth;  // Move to the next character's position
        }
    }


    private void renderCharacter(float x, float y, float tx, float ty, float width, float height) {
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glTexCoord2f(tx, ty); GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(tx + width, ty); GL11.glVertex2f(x + width, y);
        GL11.glTexCoord2f(tx + width, ty + height); GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(tx, ty + height); GL11.glVertex2f(x, y + height);

        GL11.glEnd();
    }
}
