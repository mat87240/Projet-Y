package HUD;

import org.lwjgl.opengl.GL11;
import player.Inventory;
import player.Player;
import player.items.Armor;
import player.items.Weapon;

import static main.Main.screenHeight;
import static main.Main.screenWidth;
import static org.lwjgl.opengl.GL11.*;

public class DisplayInventory {

    private Inventory inventory;
    private Player player;
    private int x, y, width, height;
    private int slotSpacing = 5;  // Space between slots
    private int paddingTopBottom = 10;  // Padding for top and bottom of the inventory

    public DisplayInventory(Player player) {
        this.inventory = player.getInventory();
        this.player = player;
        this.width = screenWidth / 2;  // 50% of the screen width
        this.height = 128;  // Fixed height (128px as per your requirement)
        this.x = (screenWidth - width) / 2;  // Centered horizontally
        this.y = screenHeight - height;  // Positioned exactly at the bottom of the screen
    }

    public void render() {
        GL11.glPushMatrix();

        // Draw the background for the inventory (the large rectangle)
        drawInventoryRectangle(x, y, width, height);

        // Calculate slot size considering padding and spacing
        int availableWidth = width - (9 * slotSpacing);  // Remove space for 9 slots between the 10 total
        int slotWidth = availableWidth / 10;  // Divide the available width by the number of slots

        // Calculate the slot height based on available space after padding
        int availableHeight = height - (2 * paddingTopBottom);  // Available space after top and bottom padding
        int slotHeight = slotWidth;  // Since we want square slots, set height to slotWidth

        // Draw 10 square placeholders inside the inventory with spacing
        for (int i = 0; i < 10; i++) {
            int slotX = x + i * (slotWidth + slotSpacing);  // Calculate position for each slot with spacing
            int slotY = y + paddingTopBottom;  // Apply top padding
            drawSlot(slotX, slotY, slotWidth, slotHeight);  // Draw individual square slot

            // If it's the first slot (index 0), render the weapon
            if (i == 0) {
                renderWeaponInSlot(slotX, slotY, slotWidth, slotHeight);
            }

            // If it's the armor slot (index 2-5), render the corresponding armor item
            if (i >= 1 && i <= 4) {
                renderArmorInSlot(i, slotX, slotY, slotWidth, slotHeight);
            }
        }

        GL11.glPopMatrix();
    }


    private void drawInventoryRectangle(int x, int y, int width, int height) {
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);  // Gray color with 50% opacity
        glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
    }

    private void drawSlot(int x, int y, int width, int height) {
        GL11.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);  // Solid gray color
        glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);  // Top-left corner
        GL11.glVertex2f(x + width, y);  // Top-right corner
        GL11.glVertex2f(x + width, y + height);  // Bottom-right corner
        GL11.glVertex2f(x, y + height);  // Bottom-left corner
        GL11.glEnd();
    }

    // Render weapon in the first inventory slot
    // Render weapon in the first inventory slot
    private void renderWeaponInSlot(int slotX, int slotY, int slotWidth, int slotHeight) {
        // Get the weapon from the player's inventory (assuming the first item is the weapon)
        Weapon weapon = player.getInventory().getWeapon();

        // If the player has a weapon and it's the first slot, render it
        if (weapon != null) {
            int weaponX = slotX;  // Align the weapon with the slot (no padding)
            int weaponY = slotY;  // Align the weapon with the slot (no padding)

            // Enable texture rendering
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, weapon.getTextureID()); // Bind the weapon texture

            glColor3f(1.0f, 1.0f, 1.0f); // White color for the texture

            // Draw the weapon texture to fit the entire slot
            glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 0.0f); glVertex2f(weaponX, weaponY);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(weaponX + slotWidth, weaponY);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(weaponX + slotWidth, weaponY + slotHeight);
            glTexCoord2f(0.0f, 1.0f); glVertex2f(weaponX, weaponY + slotHeight);
            glEnd();

            // Disable texture rendering
            glDisable(GL_TEXTURE_2D);
        }
    }

    private void renderArmorInSlot(int slotIndex, int slotX, int slotY, int slotWidth, int slotHeight) {
        // Get the player's armor
        Armor armor = player.getInventory().getArmor();

        // Directly get the specific armor texture for the current slot
        int textureID = -1;  // Default value for textureID, in case it's not found

        switch (slotIndex) {
            case 1: // Helmet (Slot 2)
                textureID = armor.getHelmetTextureID();
                break;
            case 2: // Chestplate (Slot 3)
                textureID = armor.getChestplateTextureID();
                break;
            case 3: // Leggings (Slot 4)
                textureID = armor.getLeggingsTextureID();
                break;
            case 4: // Boots (Slot 5)
                textureID = armor.getBootsTextureID();
                break;
        }

        // If a valid textureID is found, render it
        if (textureID != -1) {
            // Align the armor texture with the slot (no padding)
            int armorX = slotX;  // Align with the left of the slot
            int armorY = slotY;  // Align with the top of the slot

            // Enable texture rendering
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, textureID); // Bind the armor texture

            glColor3f(1.0f, 1.0f, 1.0f); // White color for the texture

            // Draw the armor texture to fit the entire slot
            glBegin(GL_QUADS);
            glTexCoord2f(0.0f, 0.0f); glVertex2f(armorX, armorY);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(armorX + slotWidth, armorY);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(armorX + slotWidth, armorY + slotHeight);
            glTexCoord2f(0.0f, 1.0f); glVertex2f(armorX, armorY + slotHeight);
            glEnd();

            // Disable texture rendering
            glDisable(GL_TEXTURE_2D);
        }
    }
}
