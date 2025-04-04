package player;

import map.Bullet;
import map.Entity;
import map.Map;
import map.Wall;
import player.items.Weapon;
import utils.hitbox.AABB;
import static main.Main.dt;

import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Player extends Entity {
    private int x, y;
    private float speed;
    private int sizeX = 64; // Adjusted size for better wall collision
    private int sizeY = 64;
    private int health;
    private int maxHealth;
    private int mana;
    private Inventory inventory;
    private long lastAttackTime = 0; // Cooldown time tracking
    private boolean isAttacking;
    private float targetSpeed = 516.0f;
    private boolean movingLeft = false;

    private long lastHealTime;  // Time of the last heal
    private static final long HEAL_INTERVAL = 5000;  // 5 seconds in milliseconds
    private static final int HEAL_AMOUNT = 10;  // Amount of HP to heal

    public Player(int id, int x, int y) {
        super(id, x, y, 64, 64, 32, 0);
        this.speed = 32;
        this.inventory = new Inventory();
        init(x, y);
    }

    public void init(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public void update(long window, Map map, Aim aim) {
        int newX = x;
        int newY = y;

        float movementSpeed = (float) (targetSpeed * dt);

        // Process movement inputs for both axes
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) newY += movementSpeed;
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) newY -= movementSpeed;
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) newX -= movementSpeed;
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) newX += movementSpeed;

        AABB hitbox = this.getHitbox();
        hitbox.setCoordinate(x, y);  // Set initial position for collision checks

        List<Wall> listWalls = map.getListWalls();

        // X collision check
        boolean left = false;
        boolean right = false;
        hitbox.setCoordinate(newX, y); // Simulate movement in X-axis
        for (Wall wall : listWalls) {
            boolean[] intersection = hitbox.intersect(wall.getHitbox());
            if (intersection[3]) { // Left collision
                left = true;
                System.out.println("Left collision");
            }
            if (intersection[1]) { // Right collision
                right = true;
                System.out.println("Right collision");
            }
            if (left || right) break;
        }

        // Y collision check
        boolean top = false;
        boolean bottom = false;
        hitbox.setCoordinate(x, newY); // Simulate movement in Y-axis
        for (Wall wall : listWalls) {
            boolean[] intersection = hitbox.intersect(wall.getHitbox());
            if (intersection[0]) { // Top collision
                top = true;
                System.out.println("Top collision");
            }
            if (intersection[2]) { // Bottom collision
                bottom = true;
                System.out.println("Bottom collision");
            }
            if (top || bottom) break;
        }

        // Apply movement only if there’s no collision in that direction
        if (!left && !right) x = newX;
        if (!top && !bottom) y = newY;

        // Rest of your update logic (attacking, healing, etc.)
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }

    public int getMana() { return this.mana; }
    public Inventory getInventory() { return inventory; }
    public boolean isMovingLeft() { return movingLeft; }
}
