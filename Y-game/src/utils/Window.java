package utils;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;

public class Window {

	private int width, height;
	private String title;
	private long window;
	private boolean fullscreen, shouldClose;
	
	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public void create() {
		if (!GLFW.glfwInit()) {
			System.out.println("/!\\ Error : GLFW not initialized");
			return;
		}
		
		//Add window property
		//GLFW.glfwWindowHint();
		
		//Create window
		this.window = glfwCreateWindow(this.width, this.height, this.title, glfwGetPrimaryMonitor(), 0);
		if (this.window==0) { throw new Error("/!\\ Error : Window wasn't created"); }
		
		//Set window in the middle
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(this.window, (videoMode.width()-this.width)>>1, (videoMode.height()-this.height)>>1);
		
		//Setup OpenGL
		glfwMakeContextCurrent(this.window);
		createCapabilities();
		
		//Set VSYNC
		glfwSwapInterval(1);
		
		// Setup key callback
		glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true);
		});
	}
	
	public void update() {
		glfwSwapBuffers(this.window);
		glfwPollEvents();	
	}
	
	public void delete() {
		glfwFreeCallbacks(this.window);
		glfwDestroyWindow(this.window);
	}
	
	public int getWidth() { return this.width; }
	public void setWidth(int width) { this.width = width>0 ? width : this.width; }
	public int getHeight() { return this.height; }
	public void setHeight(int height) { this.height = height>0 ? height : this.height; }
	public void setResolution(int width, int height) { this.setWidth(width); this.setHeight(height); }
	public boolean getFullscreen() { return this.fullscreen; }
	public void setFullscreen(boolean is_fullscreen) { this.fullscreen = is_fullscreen; }
	public long getWindow() { return this.window; }
	public boolean shouldClose() { return this.shouldClose || glfwWindowShouldClose(window);}
	protected void close() { this.shouldClose = !this.shouldClose;}
}
