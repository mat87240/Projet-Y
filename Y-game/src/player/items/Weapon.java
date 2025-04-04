package player.items;

import player.Player;
import data.TextureLoader;
import draw.DrawMain;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Weapon {
    private String name;
    private int damage;
    private long cooldown;
    private int textureID;
    private int speed;

    // Assuming default size for now (you can adjust these values depending on weapon type)
    private int sizeX = 48; // Width of the weapon
    private int sizeY = 48; // Height of the weapon

    private static final Map<String, Integer> textureCache = new HashMap<>();

    public Weapon(String name, int damage, long cooldown, int speed) {
        this.name = name;
        this.damage = damage;
        this.cooldown = cooldown;
        this.textureID = -1; // Default texture state
        this.speed = speed;
        assignImage(); // Load texture on creation
    }

    private void assignImage() {
        String projectDir = System.getProperty("user.dir");
        String assetsPath = new File(projectDir, "src" + File.separator + "assets" + File.separator + "weapons").getPath();
        String texturePath = assetsPath + File.separator + name + ".png"; // Weapon image path

        // Check cache
        if (textureCache.containsKey(name)) {
            this.textureID = textureCache.get(name);
        } else {
            this.textureID = TextureLoader.loadTexture(texturePath);
            textureCache.put(name, this.textureID);
        }
    }

    public void render(Player player) {
        if (player == null || textureID == -1) return;

        int weaponX = player.getX();
        int weaponY = player.getY() + player.getSizeY() / 2 - sizeY / 2; // Adjusted centering for weapon size

        if (player.isMovingLeft()) {
            weaponX -= sizeX; // Offset to the left
        } else {
            weaponX += player.getSizeX(); // Offset to the right
        }

        float[] weaponCoords1 = DrawMain.convertCartesianToGL(weaponX, weaponY);
        float[] weaponCoords2 = DrawMain.convertCartesianToGL(weaponX + sizeX, weaponY + sizeY);

        // Enable texture rendering
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glColor3f(1.0f, 1.0f, 1.0f); // Ensure white for proper texture display

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(weaponCoords1[0], weaponCoords1[1]);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(weaponCoords2[0], weaponCoords1[1]);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(weaponCoords2[0], weaponCoords2[1]);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(weaponCoords1[0], weaponCoords2[1]);
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    public String getName() { return name; }
    public int getDamage() { return damage; }
    public long getCooldown() { return cooldown; }

    public int getWeaponX(Player player) {
        if (player.isMovingLeft()) {
            return player.getX() - sizeX; // Adjust based on weapon's width
        } else {
            return player.getX() + player.getSizeX();
        }
    }

    public int getWeaponY(Player player) {
        return player.getY() + player.getSizeY() / 2 - sizeY / 2; // Adjust based on weapon's height
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public float getSpeed() {return this.speed;}

    public int getTextureID() {return this.textureID;}
}