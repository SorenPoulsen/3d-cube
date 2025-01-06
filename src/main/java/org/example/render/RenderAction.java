package org.example.render;

import org.example.shader.ShaderProgram;
import org.example.window.Window;

import static org.lwjgl.opengl.GL30C.glGetUniformLocation;

/**
 * RenderActions is a way of packing render loop code with its related model and shader program.
 * <p>
 * RenderActions are invoked in the render loop, after the shader program is bound but before shaders are run.
 * This allows RenderActions to update attributes and uniforms used by the shader program.
 *
 * @author Søren Thalbitzer Poulsen
 */
public abstract class RenderAction {

    /**
     * Is invoked during render setup, after the shader program is created.
     *
     * @param shaderProgram
     */
    abstract void setup(ShaderProgram shaderProgram);

    /**
     * Is invoked in the render loop, after the shader program is bound but before shaders are run.
     *
     * @param window
     */
    abstract void action(Window window);

    /**
     * Close any resources set up by the RenderAction.
     */
    public void close() {
    }

    protected int createUniformLocation(int programId, String uniformName) {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new RuntimeException("Failed to create uniform with name " + uniformName + " in program " +
                    programId);
        }
        return uniformLocation;
    }
}
