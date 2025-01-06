package org.example.render;

import org.example.model.Rotation;
import org.example.shader.ShaderProgram;
import org.example.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL30C.glUniformMatrix4fv;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class RotationUniformRenderAction extends RenderAction {

    public static final String UNIFORM_NAME = "rotation";
    private int uniformLocation;
    private final Rotation rotation;

    public RotationUniformRenderAction(Rotation rotation) {
        this.rotation = rotation;
    }

    @Override
    void setup(ShaderProgram shaderProgram) {
        uniformLocation = createUniformLocation(shaderProgram.getProgramId(), UNIFORM_NAME);
    }

    @Override
    void action(Window window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.translate(new Vector3f(0.0f, 0.0f, -1.8f), rotationMatrix);
            rotationMatrix.rotate((float) (glfwGetTime() * rotation.getAngleRadians()),
                    new Vector3f(rotation.getRotXAxis(), rotation.getRotYAxis(), rotation.getRotZAxis()).normalize());
            glUniformMatrix4fv(uniformLocation, false,
                    rotationMatrix.get(stack.mallocFloat(16)));
        }
    }
}
