package map;

import player.Player;
import utils.hitbox.Hitbox;

import static main.Main.dt;
import java.util.Random;

public class Wave {
    private Map map;
    private Player player;
    private int waveNumber;
    private boolean isWaveInProgress;
    private static final long COOLDOWN_DURATION = 5000; // Cooldown duration in milliseconds (5 seconds)
    private double waveStartTime = 0;

    // Constructor
    public Wave(Map map, Player player) {
        this.map = map;
        this.player = player;
        this.waveNumber = 0;
        this.isWaveInProgress = false;
    }

    // Start the next wave (called by GameManager)
    public void startWave() {
        if (isWaveInProgress) {
            return; // Don't start a new wave if one is already in progress
        }

        this.waveNumber++; // Increment wave number
        this.isWaveInProgress = true;
        System.out.println("Starting Wave " + waveNumber);
        spawnEnemiesForWave();
    }

    // Update wave (called every frame to check if the wave is complete or cooldown is over)
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (isWaveInProgress) {
            // If all enemies are defeated, end the wave and start cooldown
            if (map.getListEntities().isEmpty()) {
                isWaveInProgress = false;
                System.out.println("Wave " + waveNumber + " complete!");
                waveStartTime = currentTime; // Start cooldown
            }
        } else {
            // Cooldown logic
            if (currentTime - waveStartTime >= COOLDOWN_DURATION) {
                System.out.println("Cooldown over, starting next wave.");
                Entity newEntity = new Entity(1, 254*64, 254*64, 64, 64, 2.0f, 0);
                map.addEntity(newEntity); // Add enemy to the map
                startWave(); // Start a new wave
            }
        }
    }

    // Spawn enemies based on the current wave number
    private void spawnEnemiesForWave() {
        // Determine the number of entities based on the wave number
        int entityAmount = 1 + this.waveNumber * 0;

        // Iterate through the number of entities to spawn
        for (int i = 0; i < entityAmount; i++) {
            // Select a random room from the room list
            Room randomRoom = map.getRandomRoom(); // Assuming you have a method to get a random room

            // Get the room's boundaries and center position
            int roomX = randomRoom.getX();
            int roomY = randomRoom.getY();
            int roomWidth = randomRoom.getSizeX();
            int roomHeight = randomRoom.getSizeY();

            int randomX = roomX + (int) (Math.random() * roomWidth);
            int randomY = roomY + (int) (Math.random() * roomHeight);

            // Attempt to spawn the entity a limited number of times
            int maxAttempts = 5;
            boolean isValidPosition = false;

            for (int attempts = 0; attempts < maxAttempts; attempts++) {
                // Check if there is a wall at this position
                isValidPosition = true;
                for (Wall wall : map.getListWalls()) {
                    // Check if the random position collides with the wall
                    if (randomX < wall.getX() + wall.getSizeX() &&
                            randomX + 64 > wall.getX() &&
                            randomY < wall.getY() + wall.getSizeY() &&
                            randomY + 64 > wall.getY()) {
                        isValidPosition = false; // Wall detected, invalid position
                        break; // No need to check further, break the loop
                    }
                }

                if (isValidPosition) {
                    // Create a new entity and place it at the valid random position within the room
                    //Entity newEntity = new Entity(1, randomX, randomY, 64, 64, 2.0f, 0);
                    Entity newEntity = new Entity(1, 255*64, 255*64, 64, 64, 2.0f, 0);

                    map.addEntity(newEntity); // Add entity to the map
                    break; // Exit the attempts loop after successfully spawning the entity
                } else {
                    // Reattempt with a new random position if invalid
                    randomX = roomX + (int) (Math.random() * roomWidth);
                    randomY = roomY + (int) (Math.random() * roomHeight);
                }
            }

            // If we reached maxAttempts and still didn't find a valid position, log a warning
            if (!isValidPosition) {
                System.out.println("Failed to spawn enemy after " + maxAttempts + " attempts for wave " + waveNumber);
            }
        }

        System.out.println("Spawned " + entityAmount + " enemies for wave " + waveNumber);
    }

    // Getter for wave status (can be used to display wave state on HUD)
    public boolean isWaveInProgress() {return isWaveInProgress;}

    // Getter for current wave number (for UI or debugging)
    public int getWaveNumber() {return this.waveNumber;}
}
