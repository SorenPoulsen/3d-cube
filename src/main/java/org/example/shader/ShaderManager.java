package org.example.shader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.lwjgl.opengl.GL30C.*;

/**
 * @author Søren Thalbitzer Poulsen
 */
public class ShaderManager implements AutoCloseable {

    private final List<ShaderProgram> shaderPrograms = new ArrayList<>();

    /**
     * Create a program of shaders. The shader sources are compiled and linked into a new program.
     *
     * @param shaderSources List of shader sources. May contain multiple shaders of different types and also multiple
     *                      shaders of the same type.
     * @return A ShaderProgram containing the binding IDs of the program and the uniforms.
     */
    public ShaderProgram createProgram(List<ShaderSource> shaderSources) {
        int programId = glCreateProgram();
        if (programId == 0) {
            throw new RuntimeException("Failed to create shader program");
        }
        List<Integer> shaderIds = new ArrayList<>();
        for (ShaderSource shaderSource : shaderSources) {
            int shaderId = compileShader(shaderSource);
            glAttachShader(programId, shaderId);
            shaderIds.add(shaderId);
        }
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Failed to link program: " + glGetProgramInfoLog(programId, 1024));
        }
        for (Integer shaderId : shaderIds) {
            glDetachShader(programId, shaderId);
            glDeleteShader(shaderId);
        }
        ShaderProgram shaderProgram = new ShaderProgram(programId);
        shaderPrograms.add(shaderProgram);
        return shaderProgram;
    }

    public void bind(ShaderProgram shaderProgram) {
        glUseProgram(shaderProgram.getProgramId());
    }

    public void unbind() {
        glUseProgram(0);
    }

    @Override
    public void close() {
        unbind();
        for (ShaderProgram shaderProgram : shaderPrograms) {
            glDeleteProgram(shaderProgram.getProgramId());
        }
    }

    private String readShaderSource(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            return new BufferedReader(new InputStreamReader(is)).lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read shader source from " + resourcePath, e);
        }
    }

    private int compileShader(ShaderSource shaderSource) {
        String glslSourceCode = readShaderSource(shaderSource.resourcePath());
        int shaderId = glCreateShader(shaderSource.type());
        if (shaderId == 0) {
            throw new RuntimeException("Failed to create shader " + shaderSource.resourcePath() +
                    " with type " + shaderSource.type());
        }
        glShaderSource(shaderId, glslSourceCode);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Failed to compile shader " + shaderSource.resourcePath() + ". " +
                    glGetShaderInfoLog(shaderId, 1024));
        }
        return shaderId;
    }

}
