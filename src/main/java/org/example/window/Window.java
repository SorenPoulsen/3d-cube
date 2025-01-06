package org.example.window;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL30C.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Abstracts a GLFW window. A GLFW window encapsulates an OS window and an OPENGL context.
 *
 * @author Søren Thalbitzer Poulsen
 */
public class Window implements WindowResized {

    private final long glfwWindowHandle;
    private final WindowSize windowSize;

    public Window(String title) {

        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Get the screen size.
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        windowSize = new WindowSize(vidMode.width(), vidMode.height());

        // Create a window maximized to the screen size.
        glfwWindowHandle = glfwCreateWindow(windowSize.getWidth(), windowSize.getHeight(), title, NULL, NULL);
        if (glfwWindowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetFramebufferSizeCallback(glfwWindowHandle,
                (window, width, height) -> {
                    this.resized(width, height);
                });


        glfwSetKeyCallback(glfwWindowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true);
        });

        glfwMakeContextCurrent(glfwWindowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindowHandle);
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    public void close() {
        glfwFreeCallbacks(glfwWindowHandle);
        glfwDestroyWindow(glfwWindowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(glfwWindowHandle);
    }

    /**
     * Block thread until 1 vertical blank has completed. Then swap the front and back
     * buffers.
     */
    public void swapBuffers() {
        glfwSwapBuffers(glfwWindowHandle);
    }

    public int getWidth() {
        return windowSize.getWidth();
    }

    public int getHeight() {
        return windowSize.getHeight();
    }

    @Override
    public void resized(int width, int height) {
        windowSize.setWidth(width);
        windowSize.setHeight(height);
    }
}
