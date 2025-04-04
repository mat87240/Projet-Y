package map;

import static org.lwjgl.opengl.GL11.*;
import data.TextureLoader;
import draw.DrawMain;

public class Room {
    private int ID;
    private int type; // 0 = Classic room, 1 = Pathway
    private int x, y;
    private int sizeX, sizeY;
    private int floorTexture;

    public Room(int ID, int type, int x, int y, int sizeX, int sizeY) {
        this.ID = ID;
        this.type = type;
        this.x = x >> 6 << 6;
        this.y = y >> 6 << 6;
        this.sizeX = sizeX >> 6 << 6;
        this.sizeY = sizeY >> 6 << 6;
        this.floorTexture = TextureLoader.loadTexture("src/assets/floor/floor.png");
    }

    public void draw() {
        glBindTexture(GL_TEXTURE_2D, floorTexture);
        glEnable(GL_TEXTURE_2D);

        int tilesX = (int) Math.ceil(sizeX / 64f);
        int tilesY = (int) Math.ceil(sizeY / 64f);

        for (int i = 0; i < tilesX; i++) {
            for (int j = 0; j < tilesY; j++) {
                float tileX = x + i * 64;
                float tileY = y + j * 64;
                float[] glCoords1 = DrawMain.convertCartesianToGL(tileX, tileY);
                float[] glCoords2 = DrawMain.convertCartesianToGL(tileX + 64, tileY + 64);

                glBegin(GL_QUADS);
                glTexCoord2f(0.0f, 0.0f);
                glVertex2f(glCoords1[0], glCoords1[1]);
                glTexCoord2f(1.0f, 0.0f);
                glVertex2f(glCoords2[0], glCoords1[1]);
                glTexCoord2f(1.0f, 1.0f);
                glVertex2f(glCoords2[0], glCoords2[1]);
                glTexCoord2f(0.0f, 1.0f);
                glVertex2f(glCoords1[0], glCoords2[1]);
                glEnd();
            }
        }

        glDisable(GL_TEXTURE_2D);
    }

    public int getID() {
        return ID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getType() {
        return type;
    }

    public int getFloorTexture() {
        return floorTexture;
    }

    public void clear() {
        glDeleteTextures(this.getFloorTexture());
    }
}
