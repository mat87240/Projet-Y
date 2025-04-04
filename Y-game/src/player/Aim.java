package player;
import main.Main;
import player.items.Weapon;

import static org.lwjgl.glfw.GLFW.*;

public class Aim {
    private double mouseX, mouseY;
    private double angle;

    public Aim(Camera camera) {
        this.mouseX = 0;
        this.mouseY = 0;
        this.angle = 0;
    }

    public void update(long window, Camera camera, Player player) {
        double[] x = new double[1];
        double[] y = new double[1];

        glfwGetCursorPos(window, x, y);

        // Adjust mouse coordinates based on camera position
        this.mouseX = (x[0] + camera.getX()) - Main.screenWidth / 2;
        this.mouseY = (-(y[0] - camera.getY())) + Main.screenHeight / 2;

        // Get the player's equipped weapon
        Weapon equippedWeapon = player.getInventory().getWeapon();

        if (equippedWeapon != null) {
            // Calculate the weapon's center coordinates
            int weaponX = equippedWeapon.getWeaponX(player) + equippedWeapon.getSizeX() / 2;  // Get the center X of the weapon
            int weaponY = equippedWeapon.getWeaponY(player) + equippedWeapon.getSizeY() / 2;  // Get the center Y of the weapon

            // Calculate the difference in X and Y between the mouse position and the weapon's center
            double deltaX = mouseX - weaponX;
            double deltaY = mouseY - weaponY;

            // Compute the angle in radians (atan2 handles both x and y signs)
            this.angle = Math.atan2(deltaY, deltaX);
        }
    }

    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }
    public double getAngle() { return angle; }
}
