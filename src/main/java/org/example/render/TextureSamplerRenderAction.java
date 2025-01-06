package org.example.render;

import org.example.shader.ShaderProgram;
import org.example.window.Window;

import static org.lwjgl.opengl.GL30C.glUniform1i;

/**
 * Set the textureSampler uniform in the render loop.
 *
 * @author Søren Thalbitzer Poulsen
 */
public class TextureSamplerRenderAction extends RenderAction {

    public static final String UNIFORM_NAME = "textureSampler";
    private int uniformLocation;

    @Override
    void setup(ShaderProgram shaderProgram) {
        uniformLocation = createUniformLocation(shaderProgram.getProgramId(), UNIFORM_NAME);
    }

    @Override
    void action(Window window) {
        glUniform1i(uniformLocation, 0);
    }
}
