package org.example.shader;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class ShaderProgram {

    private final int programId;

    public ShaderProgram(int programId) {
        this.programId = programId;
    }

    public int getProgramId() {
        return programId;
    }
}
