package org.example.render;

import org.example.model.*;
import org.example.shader.ShaderManager;
import org.example.shader.ShaderProgram;
import org.example.texture.TextureManager;
import org.example.window.Window;
import org.lwjgl.opengl.GL30C;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30C.*;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class Renderer {

    private final Window window;
    private final Scene scene;
    private final ModelManager modelManager = new ModelManager();
    private final ShaderManager shaderManager = new ShaderManager();
    private final TextureManager textureManager = new TextureManager();
    private ShaderProgram shaderProgram;

    public Renderer(Window window, Scene scene) {
        this.window = window;
        this.scene = scene;
        setupScene(scene);
    }

    private void setupScene(Scene scene) {
        for (Model model : scene.getModels()) {
            modelManager.createModelBinding(model, textureManager);
        }
        shaderProgram = shaderManager.createProgram(scene.getShaderSources());
        Color background = scene.getBackgroundColor();
        glClearColor(background.getRed(), background.getGreen(), background.getBlue(), background.getAlpha());
        for (RenderAction renderAction : scene.getRenderActions()) {
            renderAction.setup(shaderProgram);
        }
    }

    public void loop() {
        while (!window.windowShouldClose()) {
            // Set the lower left corner and size of viewport in screen coordinates
            // so that normalized coordinates will be transformed correctly.
            glViewport(0, 0, window.getWidth(), window.getHeight());
            render();
            window.swapBuffers();   // block thread until vertical blank has finished, then swap buffers.
            glfwPollEvents();       // Poll events. Key callback is invoked.
        }
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        shaderManager.bind(shaderProgram);
        // Run all render actions that typically update attributes and uniforms
        // used by the Shader Program.
        for (RenderAction renderAction : scene.getRenderActions()) {
            renderAction.action(window);
        }
        // For each model bind the model vertices and the texture, and then run the shader program.
        for (ModelBinding modelBinding : modelManager.getModelBindings()) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, modelBinding.textureId());
            glBindVertexArray(modelBinding.vaoId());
            glDrawElements(GL_TRIANGLES, modelBinding.numVertices(), GL_UNSIGNED_INT, 0);
        }
        GL30C.glBindVertexArray(0);
        shaderManager.unbind();
    }

    public void close() {
        for (RenderAction renderAction : scene.getRenderActions()) {
            renderAction.close();
        }
        shaderManager.close();
        modelManager.close();
        textureManager.close();
    }
}
