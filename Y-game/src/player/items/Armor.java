package player.items;

import data.TextureLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Armor {
    private String helmet;
    private String chestplate;
    private String leggings;
    private String boots;
    private String name;

    private int helmetTextureID;
    private int chestplateTextureID;
    private int leggingsTextureID;
    private int bootsTextureID;

    // Cache to avoid reloading textures multiple times
    private static final Map<String, Integer> textureCache = new HashMap<>();

    public Armor(String helmet, String chestplate, String leggings, String boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        loadTextures(); // Load textures for armor pieces
    }

    public Armor(String name) {
        this.name = name;
        loadTextures(); // Load textures for a named armor set
    }

    private void loadTextures() {
        // Load textures if not already cached
        this.helmetTextureID = loadTexture(helmet);
        this.chestplateTextureID = loadTexture(chestplate);
        this.leggingsTextureID = loadTexture(leggings);
        this.bootsTextureID = loadTexture(boots);
    }

    private int loadTexture(String textureName) {
        // Check if texture is already cached
        if (textureCache.containsKey(textureName)) {
            return textureCache.get(textureName);
        }

        // Load texture if not cached
        String projectDir = System.getProperty("user.dir");
        String assetsPath = new File(projectDir, "src" + File.separator + "assets" + File.separator + "armors").getPath();
        String texturePath = assetsPath + File.separator + textureName + ".png";  // Armor texture path
        System.out.println("Loading texture from path: " + texturePath);

        // Assuming TextureLoader.loadTexture is a method to load textures and return their ID
        int textureID = TextureLoader.loadTexture(texturePath);

        // Cache the texture ID for future use
        textureCache.put(textureName, textureID);

        return textureID;
    }

    // Getters for each piece of armor texture ID
    public int getHelmetTextureID() { return helmetTextureID; }
    public int getChestplateTextureID() { return chestplateTextureID; }
    public int getLeggingsTextureID() { return leggingsTextureID; }
    public int getBootsTextureID() { return bootsTextureID; }

    // Getter for the name of the armor set (if applicable)
    public String getName() {
        return name;
    }

    // Getters for individual armor pieces (if needed)
    public String getHelmet() { return helmet; }
    public String getChestplate() { return chestplate; }
    public String getLeggings() { return leggings; }
    public String getBoots() { return boots; }

    // Setters for armor pieces
    public void setHelmet(String helmet) { this.helmet = helmet; }
    public void setChestplate(String chestplate) { this.chestplate = chestplate; }
    public void setLeggings(String leggings) { this.leggings = leggings; }
    public void setBoots(String boots) { this.boots = boots; }
}
