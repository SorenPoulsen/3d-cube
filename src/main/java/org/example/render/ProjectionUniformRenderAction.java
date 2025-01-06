package org.example.render;

import org.example.model.Projection;
import org.example.shader.ShaderProgram;
import org.example.window.Window;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL30C.glUniformMatrix4fv;

/**
 * Updates the projection matrix uniform based on the current window size.
 *
 * @author Søren Thalbitzer Poulsen
 */
public class ProjectionUniformRenderAction extends RenderAction {

    public static final String UNIFORM_NAME = "projection";

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Projection projection;
    private int uniformLocation;

    public ProjectionUniformRenderAction(Projection projection) {
        this.projection = projection;
    }

    @Override
    public void setup(ShaderProgram shaderProgram) {
        uniformLocation = createUniformLocation(shaderProgram.getProgramId(), UNIFORM_NAME);
    }

    @Override
    public void action(Window window) {
        updateProjectionUniform(window);
    }

    private void updateProjectionUniform(Window window) {
        projectionMatrix.setPerspective(projection.getFov(), (float) window.getWidth() / window.getHeight(),
                projection.getZNear(), projection.getZFar());
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniformLocation,
                    false, projectionMatrix.get(stack.mallocFloat(16)));
        }
    }
}
