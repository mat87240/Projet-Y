package map;

import static org.lwjgl.opengl.GL11.*;

import data.TextureLoader;
import utils.hitbox.AABB;
import draw.DrawMain;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

public class Wall {
    private int x, y;
    private int sizeX, sizeY;
    private AABB hitbox;
    private int type; // 0 = invisible wall, 1 == decorated one
    private int[] id = new int[2];
    private int textureID;

    private static final Map<String, Integer> textureCache = new HashMap<>();

    public Wall(int x, int y, int sizeX, int sizeY, int type, int id1, int id2) {
        this.x = x >> 6 << 6;
        this.y = y >> 6 << 6;
        this.sizeX = sizeX >> 6 << 6;
        this.sizeY = sizeY >> 6 << 6;
        this.hitbox = new AABB(x, y, sizeX, sizeY);
        this.type = type;
        this.id = new int[]{id1, id2};
        this.textureID = -1;
    }

    public void assignImage() {
        String projectDir = System.getProperty("user.dir");
        String assetsPath = new File(projectDir, "src" + File.separator + "assets" + File.separator).getPath();

        // Create a unique key for the texture cache based on the ID pair
        String textureKey = id[0] + "_" + id[1];

        // Check if the texture is already in the cache
        if (textureCache.containsKey(textureKey)) {
            this.textureID = textureCache.get(textureKey); // Reuse the texture
        } else {
            // If not in cache, load the texture
            String texturePath = assetsPath + File.separator + "wall" + File.separator + (id[0]) + ".png";
            this.textureID = TextureLoader.loadTexture(texturePath);

            // Store the loaded texture in the cache
            textureCache.put(textureKey, textureID);
        }
    }

    public void draw() {
        if (textureID == -1) return; // If no texture is assigned, do nothing

        // Coordinates of the wall in OpenGL space
        float[] glCoords1 = DrawMain.convertCartesianToGL(x, y);
        float[] glCoords2 = DrawMain.convertCartesianToGL(x + sizeX, y + sizeY);
        int rotation = id[1]; // Rotation ID

        if (type == 0) {
            // Brown background for type 0 (outside room)
            glColor3f(0.235294f, 0.176471f, 0.160784f); // RGB for brown
        } else if (type == 1) {
            // White background for type 1 (inside room)
            glColor4f(0.235294f, 0.176471f, 0.160784f, 0.0f); // Brown color with full transparency
        }

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 0.0f); glVertex2f(glCoords1[0], glCoords1[1]);
        glTexCoord2f(1.0f, 0.0f); glVertex2f(glCoords2[0], glCoords1[1]);
        glTexCoord2f(1.0f, 1.0f); glVertex2f(glCoords2[0], glCoords2[1]);
        glTexCoord2f(0.0f, 1.0f); glVertex2f(glCoords1[0], glCoords2[1]);
        glEnd();

        // Draw the wall texture after the white square
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glColor3f(1.0f, 1.0f, 1.0f); // Ensure color is white for texture rendering

        glBegin(GL_QUADS);
        switch (rotation) {
            case 0: // No rotation
                glTexCoord2f(0.0f, 0.0f); glVertex2f(glCoords1[0], glCoords1[1]);
                glTexCoord2f(1.0f, 0.0f); glVertex2f(glCoords2[0], glCoords1[1]);
                glTexCoord2f(1.0f, 1.0f); glVertex2f(glCoords2[0], glCoords2[1]);
                glTexCoord2f(0.0f, 1.0f); glVertex2f(glCoords1[0], glCoords2[1]);
                break;

            case 1: // 90 degrees
                glTexCoord2f(1.0f, 0.0f); glVertex2f(glCoords1[0], glCoords1[1]);
                glTexCoord2f(1.0f, 1.0f); glVertex2f(glCoords2[0], glCoords1[1]);
                glTexCoord2f(0.0f, 1.0f); glVertex2f(glCoords2[0], glCoords2[1]);
                glTexCoord2f(0.0f, 0.0f); glVertex2f(glCoords1[0], glCoords2[1]);
                break;

            case 2: // 180 degrees
                glTexCoord2f(1.0f, 1.0f); glVertex2f(glCoords1[0], glCoords1[1]);
                glTexCoord2f(0.0f, 1.0f); glVertex2f(glCoords2[0], glCoords1[1]);
                glTexCoord2f(0.0f, 0.0f); glVertex2f(glCoords2[0], glCoords2[1]);
                glTexCoord2f(1.0f, 0.0f); glVertex2f(glCoords1[0], glCoords2[1]);
                break;

            case 3: // 270 degrees
                glTexCoord2f(0.0f, 1.0f); glVertex2f(glCoords1[0], glCoords1[1]);
                glTexCoord2f(0.0f, 0.0f); glVertex2f(glCoords2[0], glCoords1[1]);
                glTexCoord2f(1.0f, 0.0f); glVertex2f(glCoords2[0], glCoords2[1]);
                glTexCoord2f(1.0f, 1.0f); glVertex2f(glCoords1[0], glCoords2[1]);
                break;
        }
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public int getSizeX() { return sizeX; }
    public int getSizeY() { return sizeY; }
    public AABB getHitbox() { return hitbox; }
    public int[] getId() { return id; }
    public void setId(int i) { this.id[1] = i; }
    public int getTextureID() { return this.textureID; }

    public static Map<String, Integer> getTextureCache() {
        return textureCache;
    }

    public static Collection<Integer> getTextureCacheValues() {
        return textureCache.values();
    }

    public void setX(int adjustedX) {this.x=adjustedX;}
    public void setY(int adjustedY) {this.y=adjustedY;}
    public void setSizeX(int adjustedWidth) {this.sizeX=adjustedWidth;}
    public void setSizeY(int adjustedHeight) {this.sizeY=adjustedHeight;}

    public void setType(int i) {this.type=i;}

    public int getType() {return this.type;}
}
