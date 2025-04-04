package main;

import map.Entity;
import map.Room;
import map.Wall;
import org.lwjgl.opengl.GL11;
import player.Aim;
import utils.Window;
import utils.web.WebClient;

import org.lwjgl.glfw.*;
import player.Player;
import map.Map;
import player.Camera;
import draw.DrawMain;
import HUD.DisplayHUD;
import utils.web.WebClient;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main{
	private static final int TARGET_FPS = 60; // Set TARGET_FPS to -1 if you don't want to limit
	private static final double TARGET_TIME = 1/(double)TARGET_FPS;
	
	private Window window;
	private GameManager gameManager;

	public static int screenWidth;
	public static int screenHeight;

	public static double dt;
	private int currentFPS;
	// For the Game
	private Player player;
	private Map map;
	private Camera camera;
	private DisplayHUD displayHUD;
	private Aim aim;

	private void run() {
		this.init();
		this.loop();
		this.terminate();
	}
	
	private void init() {
		//Start lwjgl
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		WebClient reg = new WebClient();

		// Create Window
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		screenWidth = vidmode.width();
		screenHeight = vidmode.height();
		this.window = new Window(screenWidth, screenHeight, "Y");
		this.window.create();

		//Instance game data
		player = new Player(0, 255*64, 255*64);
		map = new Map();

		camera = new Camera(player);
		displayHUD = new DisplayHUD(player, map);
		aim = new Aim(camera);

		gameManager = new GameManager(player, map);
	}

	private void loop() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		glOrtho(0, screenWidth, screenHeight, 0, -1, 1);
		long lastTime = System.nanoTime()/1000;

		while (!window.shouldClose()) {

			long currentTime = System.nanoTime()/1000;
			dt = (currentTime - lastTime) / 1000.0;

			if (TARGET_TIME <= this.dt) {
				this.currentFPS = (int) (1 / dt);
				this.render();
				this.update();
				lastTime = System.nanoTime() / 1000;
			}
		}
	}

	private void terminate() {
		// Clean up Wall textures
		for (Integer textureID : Wall.getTextureCacheValues()) {
			glDeleteTextures(textureID);
		}

		// Clean up Entity textures
		for (Integer textureID : Entity.getTextureCacheValues()) {
			System.out.println("Deleting entity texture with ID: " + textureID);
			glDeleteTextures(textureID);
		}

		// Clean up Room textures
		for (Room room : map.getRoomList()) {
			room.clear();  // Call your clear method to delete the room texture
		}

		// Terminate window and cleanup
		this.window.delete();
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		DrawMain.drawAll(player, map, screenWidth, screenHeight, camera);
		displayHUD.renderHUD(map);

	}
	
	private void update() {
		this.player.update(window.getWindow(), map, aim);
		this.map.updateEntities(player);
		this.camera.update(map, screenHeight, screenWidth);
		this.window.update();
		this.aim.update(window.getWindow(), camera, player);

		gameManager.update();
	}


	public static void main(String[] args) {
		new Main().run();
	}
}
        