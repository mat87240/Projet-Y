package player;

import map.Map;
import map.Room;
import player.Player;

public class Camera {
    private float x, y; // Camera position
    private float targetX, targetY; // Target position for the camera
    private float lastX, lastY; // Previous position of the camera for smooth transitions
    private float easingFactor = -0.15f; // Adjust the smoothness, smaller values = slower transitions
    private Player player;

    public Camera(Player player) {
        this.player = player;
        this.lastX = player.getX();
        this.lastY = player.getY();
        this.targetX = lastX;
        this.targetY = lastY;
    }

    public void update(Map map, float screenHeight, float screenWidth) {
        Room currentRoom = map.getCurrentRoom();

        if (currentRoom == null) {
            targetX = player.getX();
            targetY = player.getY();
        } else {
            if (currentRoom.getSizeX() < screenWidth && currentRoom.getSizeY() < screenHeight) {
                targetX = currentRoom.getX() + (float) currentRoom.getSizeX() / 2;
                targetY = currentRoom.getY() + (float) currentRoom.getSizeY() / 2;
            } else {
                targetX = (player.getX() - screenWidth / 2 >= currentRoom.getX() && player.getX() + screenWidth / 2 <= currentRoom.getX() + currentRoom.getSizeX())
                        ? player.getX() - 1 * 64
                        : (player.getX() - screenWidth / 2 <= currentRoom.getX())
                        ? currentRoom.getX() + screenWidth / 2 - 5 * 64
                        : currentRoom.getX() + currentRoom.getSizeX() - screenWidth / 2 + 5 * 64;

                targetY = (currentRoom.getSizeY() <= screenHeight)
                        ? currentRoom.getY() + (float) currentRoom.getSizeY() / 2
                        : (player.getY() - screenHeight / 2 >= currentRoom.getY() && player.getY() + screenHeight / 2 <= currentRoom.getY() + currentRoom.getSizeY())
                        ? player.getY() - 1 * 64
                        : (player.getY() - screenHeight / 2 <= currentRoom.getY())
                        ? currentRoom.getY() + screenHeight / 2 - 5 * 64
                        : currentRoom.getY() + currentRoom.getSizeY() - screenHeight / 2 + 5 * 64;
            }
        }

        x = easeInOutCubic(lastX, targetX, easingFactor);
        y = easeInOutCubic(lastY, targetY, easingFactor);

        lastX = x;
        lastY = y;
    }


    // Cubic easing function (EaseInOutCubic)
    private float easeInOutCubic(float start, float end, float factor) {
        float change = end - start;
        factor = factor * factor * (3 - 2 * factor); // EaseInOutCubic formula
        return start + change * factor;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}
