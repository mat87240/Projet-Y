package main;

import map.Entity;
import map.Map;
import player.Player;
import map.Wave;

public class GameManager {
    private Player player;
    private Map map;
    private boolean isWaveInProgress;
    private Wave wave;

    public GameManager(Player player, Map map) {
        this.player = player;
        this.map = map;
        this.wave = new Wave(map, player);
        init();
    }

    // Initialize the game, like setting up initial waves or game states
    public void init() {
        wave.startWave(); // Start the first wave
    }

    // Update the game state
    public void update() {
        wave.update();
    }
}
