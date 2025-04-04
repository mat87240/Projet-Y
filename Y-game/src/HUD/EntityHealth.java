package HUD;

import draw.DrawMain;
import map.Entity;
import org.lwjgl.opengl.GL11;

public class EntityHealth implements Stat {

    private Entity entity;

    public EntityHealth(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void setStat(int value) {
        // Set health value for entity
        entity.setHealth(value);
    }

    @Override
    public int getCurrentStat() {
        // Return current health of the entity
        return entity.getHealth();
    }

    public void render() {
        // Get the entity's position directly from the entity object
        int entityX = entity.getX();
        int entityY = entity.getY();

        // Get the entity's size for the width of the health bar
        int entityWidth = entity.getSizeX();  // Assuming getSizeX() returns the width of the entity

        // Calculate where the health bar should be rendered relative to the entity
        int healthBarX = entityX;
        int healthBarY = entityY - 20; // 20 pixels above the entity for the health bar

        // Convert the entity's position to OpenGL coordinates
        float[] glCoords1 = DrawMain.convertCartesianToGL(healthBarX, healthBarY);
        float[] glCoords2 = DrawMain.convertCartesianToGL(healthBarX + entityWidth, healthBarY + 5); // Width is based on entity's size

        // Calculate health percentage
        float healthPercentage = (float) entity.getHealth() / entity.getMaxHealth();
        float greenBarWidth = entityWidth * healthPercentage;  // Green part width (current health)
        float redBarWidth = entityWidth - greenBarWidth;  // Red part width (empty health)

        // Convert the health bar's width to OpenGL coordinates
        float[] glCoordsGreenEnd = DrawMain.convertCartesianToGL(healthBarX + greenBarWidth, healthBarY);
        float[] glCoordsRedEnd = DrawMain.convertCartesianToGL(healthBarX + redBarWidth, healthBarY);

        // Render the health bar using OpenGL
        GL11.glPushMatrix();

        // Render the red part (empty health)
        GL11.glColor3f(1.0f, 0.0f, 0.0f); // Red color
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(glCoords1[0], glCoords1[1]); // Bottom-left corner of the red part
        GL11.glVertex2f(glCoordsRedEnd[0], glCoords1[1]); // Bottom-right corner of the red part
        GL11.glVertex2f(glCoordsRedEnd[0], glCoords2[1]); // Top-right corner of the red part
        GL11.glVertex2f(glCoords1[0], glCoords2[1]); // Top-left corner of the red part
        GL11.glEnd();

        // Render the green part (current health)
        GL11.glColor3f(0.0f, 1.0f, 0.0f); // Green color
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(glCoordsRedEnd[0], glCoords1[1]); // Bottom-left corner of the green part
        GL11.glVertex2f(glCoordsGreenEnd[0], glCoords1[1]); // Bottom-right corner of the green part
        GL11.glVertex2f(glCoordsGreenEnd[0], glCoords2[1]); // Top-right corner of the green part
        GL11.glVertex2f(glCoordsRedEnd[0], glCoords2[1]); // Top-left corner of the green part
        GL11.glEnd();

        GL11.glPopMatrix();
    }
}
