package map;

import HUD.EntityHealth;
import data.TextureLoader;
import AI.AIBehavior;
import player.Player;
import player.items.Weapon;
import utils.hitbox.AABB;

import java.util.*;
import java.io.File;
import java.util.Stack;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static main.Main.dt;

public class Entity {
    private int id;
    private int x, y;
    private int sizeX, sizeY;
    private float targetX, targetY;
    private String type;
    private float velocity;
    private int AggressivityType; // 0 = player, 1 = passive, 2 = aggressive
    private Stack<int[]> pathFinding = new Stack<int[]>();  // Store all the point
    private AABB hitbox;
    private AIBehavior aiBehavior;

    private int health;
    private int maxHealth;
    private int damage;
    private int range;

    private EntityHealth entityHealthBar;

    private ArrayList<ArrayList<Integer>> renderAnimations;
    private int[] AnimationTag;  // [0] -> animation type, [1] -> frame number

    private double lastFrameTime = 0;
    private long lastAttackTime = 0;

    // Last position of the entity to determine movement direction
    private float lastX, lastY;

    // Direction of movement (0 = right, 1 = left)
    private int lastDirection;

    private static final Map<Integer, ArrayList<ArrayList<Integer>>> animationCache = new HashMap<>();

    public Entity(int id, int x, int y, int sizeX, int sizeY, float velocity, int AggressivityType) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.sizeX=sizeX;
        this.sizeY=sizeY;
        this.velocity = velocity;
        this.AnimationTag = new int[]{0, 0};
        this.AggressivityType = AggressivityType;
        this.hitbox = new AABB(x,y,sizeX,sizeY);
        this.aiBehavior = new AI.Aggressive.Entity1AI();
        int[] tmp = {x,y};
        this.pathFinding.add(tmp);

        this.damage = 10;


        this.maxHealth = 100;
        this.health = maxHealth;

        this.entityHealthBar = new EntityHealth(this);

        if (animationCache.containsKey(id)) {
            this.renderAnimations = animationCache.get(id);
            System.out.println("Reusing animation set for entity ID: " + id);
        } else {
            this.renderAnimations = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                renderAnimations.add(new ArrayList<>());
            }

            String projectDir = System.getProperty("user.dir");
            String assetsPath = new File(projectDir, "src" + File.separator + "assets" + File.separator).getPath();
            loadTextures(assetsPath);

            animationCache.put(id, renderAnimations);
        }
    }

    private void loadTextures(String basePath) {
        String renderPath = basePath + File.separator + "render" + File.separator + id + File.separator;

        loadTextureSet(renderAnimations.get(0), renderPath + "movement" + File.separator);
        loadTextureSet(renderAnimations.get(1), renderPath + "attack" + File.separator);
        loadTextureSet(renderAnimations.get(2), renderPath + "damage" + File.separator);
        loadTextureSet(renderAnimations.get(3), renderPath + "item" + File.separator);
        loadTextureSet(renderAnimations.get(4), renderPath + "idle" + File.separator);
    }

    private void loadTextureSet(ArrayList<Integer> textureList, String path) {
        File folder = new File(path);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null && files.length > 0) {
            for (File file : files) {
                int textureID = TextureLoader.loadTexture(file.getAbsolutePath());
                textureList.add(textureID);
            }
        } else {
        }
    }

    public void update(ArrayList<Wall> listWalls, int x, int y) {
        // If there are waypoints in the pathfinding stack
        if (!this.pathFinding.isEmpty()) {
            // Get the next target position (peek at the top of the stack)
            int[] nextWaypoint = this.pathFinding.peek(); // Peek the next waypoint
            this.targetX = nextWaypoint[0];
            this.targetY = nextWaypoint[1];

            // Set x and y to the target position
            this.x = (int) this.targetX;
            this.y = (int) this.targetY;

            // After moving to the target, pop the waypoint from the stack
            this.pathFinding.pop();
        } else {
            // If the path is empty, reset target to current position
            this.targetX = x;
            this.targetY = y;
            this.x = (int) this.targetX;
            this.y = (int) this.targetY;
        }
    }


    public void draw(float xGL, float yGL) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        int textureID = getCurrentTexture(AnimationTag[0], AnimationTag[1]);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glColor3f(1.0f, 1.0f, 1.0f);

        glBegin(GL_QUADS);
        if (lastDirection == 0) {
            glTexCoord2f(0.0f, 0.0f); glVertex2f(xGL, yGL - 64);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(xGL + 64, yGL - 64);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(xGL + 64, yGL);
            glTexCoord2f(0.0f, 1.0f); glVertex2f(xGL, yGL);
        } else {
            glTexCoord2f(0.0f, 0.0f); glVertex2f(xGL + 64, yGL - 64);
            glTexCoord2f(1.0f, 0.0f); glVertex2f(xGL, yGL - 64);
            glTexCoord2f(1.0f, 1.0f); glVertex2f(xGL, yGL);
            glTexCoord2f(0.0f, 1.0f); glVertex2f(xGL + 64, yGL);
        }
        glEnd();

        glDisable(GL_TEXTURE_2D);
    }

    public void attack(Player player) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime >= 1000) {
            player.takeDamage(this.damage);
            System.out.println(player.getHealth());
            lastAttackTime = currentTime;
        }
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public ArrayList<Integer> getTextures(int i) { return renderAnimations.get(i); }

    public int[] getAnimationTag() { return AnimationTag; }
    public void setAnimationTag(int value, int index) { AnimationTag[index] = value; }
    public int getCurrentTexture(int index1, int index2) { return renderAnimations.get(index1).get(index2); }
    public int getLastDirection() { return lastDirection; }
    public int getAggressivityType() { return AggressivityType; }
    public void setPath(Stack<int[]> path) {
        this.pathFinding = path;  // Store the new path (single point in this case)
    }
    public Stack<int[]> getPath() {return this.pathFinding;}

    public void clearPath() {this.pathFinding.clear();}

    public void setAI(AIBehavior aiBehavior) {this.aiBehavior = aiBehavior;}

    public float getVelocity() {return this.velocity;}
    public AABB getHitbox() {return this.hitbox;}

    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        if (this.health > maxHealth) this.health = maxHealth;
    }

    public void setHealth(int health) {this.health = Math.max(0, Math.min(health, maxHealth));}
    public void takeDamage(int damage) {setHealth(this.health - damage);
    System.out.println(this.health);}
    public void heal(int amount) {setHealth(this.health + amount);}

    public static Collection<Integer> getTextureCacheValues() {
        Collection<Integer> allTextures = new ArrayList<>();
        for (ArrayList<ArrayList<Integer>> animations : animationCache.values()) {
            for (ArrayList<Integer> animation : animations) {
                allTextures.addAll(animation);
            }
        }
        return allTextures;
    }

    public void updateAnimation(double dt) {
        lastFrameTime += dt;

        boolean isMoving = (x != lastX || y != lastY);

        // Set the animation tag based on whether the character is moving
        AnimationTag[0] = isMoving ? 0 : 4;

        // Reset the animation frame to the first frame when not moving, but still animate idle
        if (!isMoving) {
            AnimationTag[1] = AnimationTag[1] % getTextures(AnimationTag[0]).size();  // Ensure it loops
        } else {
            // Set the animation tag [1] to 0 when starting to move
            AnimationTag[1] = 0;
        }

        // Update frame if enough time has passed for the current animation set
        if (lastFrameTime > 0.450) { // Adjust this duration to suit your animation timing
            int currentFrame = AnimationTag[1];
            List<Integer> textures = getTextures(AnimationTag[0]);
            if (textures != null && !textures.isEmpty()) {
                currentFrame = (currentFrame + 1) % textures.size(); // Loop animation
                setAnimationTag(currentFrame, 1);
            }
            lastFrameTime = 0;
        }

        // Update last known position to check for future movement
        lastX = x;
        lastY = y;
    }

    public EntityHealth getEntityHealthBar() {return this.entityHealthBar;}

    public int getSizeX() {return this.sizeX;}
}
