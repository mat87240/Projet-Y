package draw;

import static main.Main.dt;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;

import map.Entity;
import map.Room;
import player.Player;
import player.Camera;
import map.Map;
import map.Wall;
import map.Bullet;
import map.Room;
import player.items.Weapon;

public class DrawMain {
    static float ScreenWidth;
    static float ScreenHeight;
    static Camera camera;

    public static float[] convertCartesianToGL(float x, float y) {
        float adjustedX = x - camera.getX() + ScreenWidth/2;
        float adjustedY = -(y - camera.getY()) + ScreenHeight/2;
        return new float[]{adjustedX, adjustedY};
    }

    public static void drawAll(Player player, Map map, float screenWidth, float screenHeight, Camera cam) {
        ScreenWidth = screenWidth;
        ScreenHeight = screenHeight;
        camera = cam;

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glColor3f(0.5f, 0.5f, 0.5f); // Set grid color (gray)

        // Cache the converted player coordinates
        float[] playerCoords = convertCartesianToGL(player.getX(), player.getY());

        // Draw all rooms and walls
        for (Room room : map.getRoomList()) {
            if (isVisible(room.getX(), room.getY(), room.getSizeX(), room.getSizeY())) {  // Use room's dimensions
                room.draw();  // Draw visible room immediately
            }
        }

        for (Wall wall : map.getListWalls()) {
            if (isVisible(wall.getX(), wall.getY(), wall.getSizeX(), wall.getSizeY())) {  // Use wall's dimensions
                wall.draw();  // Draw visible wall immediately
            }
        }

        for (Bullet bullet : map.getBullets()) {
            // Assuming that Bullet's radius is 5, adjust the visibility check accordingly
            if (isVisible(bullet.getX(), bullet.getY(), 5, 5)) {  // Check if bullet is visible based on its radius
                bullet.draw();  // Draw visible bullet immediately
            }
        }

        // Batch entities visibility check
        player.updateAnimation(dt);
        Weapon equippedWeapon = player.getInventory().getWeapon();
        if (equippedWeapon != null) {
            equippedWeapon.render(player);
        }
        for (Entity entity : Map.getListEntities()) {
            entity.updateAnimation(dt); // Update animation per entity

            if (isVisible(entity.getX(), entity.getY(), 64, 64)) {
                float[] entityCoords = convertCartesianToGL(entity.getX(), entity.getY());
                entity.draw(entityCoords[0], entityCoords[1]);
            }
        }

        // Draw the player
        player.draw(playerCoords[0], playerCoords[1]);
    }

    private static boolean isVisible(float x, float y, float sizeX, float sizeY) {
        float cameraX = camera.getX();
        float cameraY = camera.getY();

        float halfScreenWidth = ScreenWidth / 2;
        float halfScreenHeight = ScreenHeight / 2;

        // Calculate the visible area (the camera's viewport)
        float leftBound = cameraX - halfScreenWidth;
        float rightBound = cameraX + halfScreenWidth;
        float topBound = cameraY - halfScreenHeight;
        float bottomBound = cameraY + halfScreenHeight;

        // Check if the object is within the camera's visible area
        return x + sizeX > leftBound &&
                x < rightBound &&
                y + sizeY > topBound &&
                y < bottomBound;
    }
}
