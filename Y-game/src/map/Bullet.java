package map;

import draw.DrawMain;
import org.lwjgl.opengl.GL11;
import utils.hitbox.AABB;  // Use AABB instead of Circle

public class Bullet {
    private float x, y;
    private float velocityX, velocityY;
    private AABB hitbox;  // Use AABB instead of Circle for collision
    private int type; // 0==player bullet, 1==entity bullet
    private int radius = 5;  // Bullet radius (can be adjusted)
    private int damage;

    public Bullet(float startX, float startY, float angle, float size, float speed, int type, int damage) {
        this.x = startX;
        this.y = startY;
        this.type = type;

        // Calculate velocity based on angle
        this.velocityX = (float) Math.cos(angle) * speed;
        this.velocityY = (float) Math.sin(angle) * speed;

        // Create the hitbox for the bullet using AABB (width and height)
        // The hitbox will be a square (same width and height) with a size equal to 'size'
        this.hitbox = new AABB((int) x, (int) y, (int) size, (int) size);
        this.damage = damage;
    }

    public void update() {
        x += velocityX;
        y += velocityY;
        hitbox.setX((int) x);  // Update hitbox position
        hitbox.setY((int) y);  // Update hitbox position
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public AABB getHitbox() { return hitbox; }
    public int getDamage() { return damage;}

    public void draw() {
        GL11.glColor3f(1.0f, 1.0f, 1.0f);  // Bullet color (white)

        // Convert Cartesian coordinates to OpenGL screen coordinates
        float[] glCoords = DrawMain.convertCartesianToGL(x, y);

        // Draw a circle using OpenGL (but still use AABB for hitbox)
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i < 360; i++) {
            float angle = (float) Math.toRadians(i);
            float dx = radius * (float) Math.cos(angle);
            float dy = radius * (float) Math.sin(angle);
            GL11.glVertex2f(glCoords[0] + dx, glCoords[1] + dy);
        }
        GL11.glEnd();
    }
}
