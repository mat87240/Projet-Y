package draw;

import org.lwjgl.opengl.GL11;

public class OpenGLRenderer {

    public void setup2DRendering() {
        GL11.glDisable(GL11.GL_DEPTH_TEST); // Disable depth testing (not needed for 2D)
        GL11.glEnable(GL11.GL_BLEND); // Enable blending for smooth text and sprites
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // Set blending function
    }

    // You can call this method during the rendering phase to ensure 2D state is set up
}
